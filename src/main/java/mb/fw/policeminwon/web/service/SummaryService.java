package mb.fw.policeminwon.web.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import mb.fw.policeminwon.entity.ViewBillingDetaillEntity;
import mb.fw.policeminwon.parser.ViewBillingDetaillParser;
import mb.fw.policeminwon.web.dto.SummaryAPIRequest;
import mb.fw.policeminwon.web.mapper.ViewBillingDetaillMapper;

@lombok.extern.slf4j.Slf4j
@Service
public class SummaryService {

	private final WebClient callBackWebClient;

	private final ViewBillingDetaillMapper viewBillingDetaillMapper;

	public SummaryService(ViewBillingDetaillMapper viewBillingDetaillMapper, @Qualifier("callBackWebClient") WebClient callBackWebClient) {
		this.viewBillingDetaillMapper = viewBillingDetaillMapper;
		this.callBackWebClient = callBackWebClient;
	}

	public void doAsyncProcess(SummaryAPIRequest request) {
		ViewBillingDetaillEntity resultEntity = viewBillingDetaillMapper.selectBillingDetaillByElecPayNo(
				ViewBillingDetaillParser.toEntity(request.getBodyData()).getElecPayNo());
		String returnMessage = ViewBillingDetaillParser.toMessage(resultEntity);

		CompletableFuture.runAsync(() -> {
			request.setBodyData(returnMessage);

			callBackWebClient.post().contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve()
					.toBodilessEntity().doOnSuccess(r -> log.info("Callback success"))
					.doOnError(e -> log.error("Callback failed", e)).subscribe();
		});
	}

}
