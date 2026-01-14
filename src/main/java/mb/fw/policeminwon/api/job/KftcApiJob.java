package mb.fw.policeminwon.api.job;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.api.dto.KftcFileReceiveRequest;
import mb.fw.policeminwon.api.dto.KftcFileReceiveResponse;
import mb.fw.policeminwon.api.dto.KftcOAuthResponse;
import mb.fw.policeminwon.api.dto.KftcTransferCloseRequest;
import mb.fw.policeminwon.api.dto.KftcTransferCloseResponse;

@Slf4j
public class KftcApiJob {

	private final WebClient.RequestHeadersSpec<?> authRequest;
	private final WebClient receiveWebClient;
	private final WebClient closeWebClient;
	private final WebClient resultWebClient;
	private final DefaultSftpSessionFactory sftpSessionFactory;
	String sftpIp;
	int sftpPort;
	String orgCode;

	public KftcApiJob(WebClient.RequestHeadersSpec<?> authRequest, WebClient receiveWebClient, WebClient closeWebClient,
			WebClient resultWebClient, String sftpIp, int sftpPort, String orgCode,
			DefaultSftpSessionFactory sftpSessionFactory) {
		this.authRequest = authRequest;
		this.receiveWebClient = receiveWebClient;
		this.closeWebClient = closeWebClient;
		this.resultWebClient = resultWebClient;
		this.sftpIp = sftpIp;
		this.sftpPort = sftpPort;
		this.orgCode = orgCode;
		this.sftpSessionFactory = sftpSessionFactory;
	}

	@Scheduled(cron = "${kftc.api.job-cron:0 0 0 * *}")
	public void executeTask() {
		try {
			// 1. 토큰 발급
			String accessToken = executeOAuthApi();
			// 2. 파일 수신 개시 API 호출
			String apiOpenTrxDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssFFF"));
			String openRandomStr = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
			String apiOpenTrxNo = orgCode + apiOpenTrxDtm.substring(8, 14) + openRandomStr;
			KftcFileReceiveResponse openResponse = executeReceiveApi(accessToken, apiOpenTrxDtm, apiOpenTrxNo);
			String sptfId = openResponse.getSftpOneTimeId();
			String sptfPasswd = openResponse.getSftpOneTimePasswd();
			log.info("SFTP 계정(id,passwd) 발급 성공");
			// 3.sftp 접속 및 파일 수신
			String downloadPath = executeFileDownload(sptfId, sptfPasswd);
			log.info("파일 다운로드 성공 : {}", downloadPath);
			// 4. 수신 종료
			KftcTransferCloseResponse closeResponse = executeCloseApi(accessToken, apiOpenTrxNo, apiOpenTrxDtm, "R000");
		} catch (Exception e) {
			log.error("KFTC 작업 중 예외 발생: {}", e.getMessage(), e);
		}
	}

	private String executeFileDownload(String sptfId, String sptfPasswd) {
		sftpSessionFactory.setUser(sptfId);
		sftpSessionFactory.setPassword(sptfPasswd);
		SftpRemoteFileTemplate dynamicTemplate = new SftpRemoteFileTemplate(sftpSessionFactory);
		String remotePath = "/kftc/data/test.zip";
		String localPath = "/app/down/test.zip";
		dynamicTemplate.execute(session -> {
			try (FileOutputStream fos = new FileOutputStream(new File(localPath))) {
				session.read(remotePath, fos);
				return true;
			}
		});
		return localPath;
	}

	private KftcTransferCloseResponse executeCloseApi(String accessToken, String apiOpenTrxNo, String apiOpenTrxDtm,
			String resultCode) throws Exception {
		String apiCloseTrxDtm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssFFF"));
		String closeRandomStr = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
		String apiCloseTrxNo = orgCode + apiCloseTrxDtm.substring(8, 14) + closeRandomStr;
		KftcTransferCloseRequest request = KftcTransferCloseRequest.builder().orgApiTrxNo(apiOpenTrxNo)
				.orgApiTrxDtm(apiOpenTrxDtm).resultCode(resultCode).build();
		log.info("KFTC [파일 수신]업무 종료 요청 [TrxNo: {}]", apiCloseTrxNo);
		KftcTransferCloseResponse response = closeWebClient.post().headers(h -> {
			h.setBearerAuth(accessToken); // Bearer 토큰
			h.set("api_trx_no", apiCloseTrxNo); // 거래번호
			h.set("api_trx_dtm", apiCloseTrxDtm); // 거래일시
		}).bodyValue(request) // JSON Body
				.retrieve().bodyToMono(KftcTransferCloseResponse.class).block();
		log.info("업무 종료 응답 완료: {}", response);
		if (response == null || response.getRspCode().isEmpty()) {
			throw new Exception("업무 종료 실패 : 응답이 비어있습니다.");
		}
		return response;
	}

	private KftcFileReceiveResponse executeReceiveApi(String accessToken, String apiOpenTrxDtm, String apiOpenTrxNo)
			throws Exception {
		KftcFileReceiveRequest request = KftcFileReceiveRequest.builder().fileName("test_file.dat")
				.compressedFileName("test_file.zip").build();
		log.info("KFTC [파일 수신]업무 개시 요청 [TrxNo: {}]", apiOpenTrxNo);
		KftcFileReceiveResponse response = receiveWebClient.post().headers(h -> {
			h.setBearerAuth(accessToken); // Bearer 토큰
			h.set("api_trx_no", apiOpenTrxNo); // 거래번호
			h.set("api_trx_dtm", apiOpenTrxDtm); // 거래일시
		}).bodyValue(request) // JSON Body
				.retrieve().bodyToMono(KftcFileReceiveResponse.class).block();
		log.info("파일 수신 개시 응답 완료: {}", response);
		if (response == null || response.getSftpOneTimeId().isEmpty() || response.getSftpOneTimePasswd().isEmpty()) {
			throw new Exception("SFTP 계정 발급 실패 : 응답이 비어있습니다.");
		}
		return response;
	}

	private String executeOAuthApi() throws Exception {
		log.info("KFTC OAuth 토큰 발급 요청");
		KftcOAuthResponse tokenResponse = authRequest.retrieve().bodyToMono(KftcOAuthResponse.class).block();
		if (tokenResponse == null || tokenResponse.getAccessToken().isEmpty()) {
			throw new Exception("토큰 발급 실패 : 응답이 비어있습니다.");
		}
		String accessToken = tokenResponse.getAccessToken();
		log.info("토큰 발급 성공");
		return accessToken;
	}
}
