package mb.fw.policeminwon.configuration;

import java.time.Duration;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.api.job.KftcApiJob;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "kftc.api", ignoreUnknownFields = true)
@ConditionalOnProperty(name = "kftc.api.enabled", havingValue = "true")
public class KftcFileApiConfiguration {

	private String clientId;

	private String clientSecret;

	private String orgCode;

	private String oAuthUrl;

	private String transferReceiveUrl;

	private String transferCloseUrl;

	private String transferResultUrl;
	
	private String receiveFileName;

	private String receiveLocalDirectory;

	private String sftpIp;

	private int sftpPort;

	private int requestTimeoutSeconds = 30;

	@Bean
	KftcApiJob kftcFileApiJob(@Qualifier("kftcAuthRequest") WebClient.RequestHeadersSpec<?> authRequest,
			@Qualifier("transferReceiveWebClient") WebClient receiveClient,
			@Qualifier("transferCloseWebClient") WebClient closeClient,
			@Qualifier("transferResultWebClient") WebClient resultClient, DefaultSftpSessionFactory sftpSessionFactory) {

		return new KftcApiJob(authRequest, receiveClient, closeClient, resultClient, sftpIp, sftpPort, orgCode, sftpSessionFactory, receiveFileName, receiveLocalDirectory);
	}

	@Bean(name = "kftcAuthRequest")
	WebClient.RequestHeadersSpec<?> kftcAuthRequest(@Qualifier("oAuthWebClient") WebClient oAuthWebClient) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("client_id", clientId);
		formData.add("client_secret", clientSecret);
		formData.add("grant_type", "client_credentials");
		return oAuthWebClient.post().body(BodyInserters.fromFormData(formData));
	}

	@Bean(name = "oAuthWebClient")
	WebClient oAuthWebClient() {
		return createBaseWebClient(oAuthUrl, MediaType.APPLICATION_FORM_URLENCODED_VALUE + "; charset=utf-8");
	}

	@Bean(name = "transferReceiveWebClient")
	WebClient transferReceiveWebClient() {
		return createBaseWebClient(transferReceiveUrl, MediaType.APPLICATION_JSON_VALUE);
	}

	@Bean(name = "transferCloseWebClient")
	WebClient transferCloseWebClient() {
		return createBaseWebClient(transferCloseUrl, MediaType.APPLICATION_JSON_VALUE);
	}

	@Bean(name = "transferResultWebClient")
	WebClient transferResultWebClient() {
		return createBaseWebClient(transferResultUrl, MediaType.APPLICATION_JSON_VALUE);
	}

	private WebClient createBaseWebClient(String baseUrl, String contentType) {
		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(createHttpClient())).baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, contentType).filter(retryFilter()) // 재시도 필터 적용
				.build();
	}

	private HttpClient createHttpClient() {
		return HttpClient.create().responseTimeout(Duration.ofSeconds(requestTimeoutSeconds));
	}

	/**
	 * 공통 재시도 로직 필터 1분 간격으로 최대 3번 재시도
	 */
	private ExchangeFilterFunction retryFilter() {
		return (request, next) -> next.exchange(request).flatMap(response -> {
			if (response.statusCode().is5xxServerError()) {
				return response.createException().flatMap(Mono::error);
			}
			return Mono.just(response);
		}).retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(30)).doBeforeRetry(
				retrySignal -> log.warn("API 호출 실패로 인해 재시도 중... (시도 횟수: {})", retrySignal.totalRetries() + 1)));
	}
	
	@Bean
    DefaultSftpSessionFactory sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost(sftpIp);
        factory.setPort(sftpPort);
//        factory.setUser(sftpId);
//        factory.setPassword(sftpPw);
        factory.setAllowUnknownKeys(true); // 내부망 접속 시 보안 정책 완화
        return factory;
    }

}