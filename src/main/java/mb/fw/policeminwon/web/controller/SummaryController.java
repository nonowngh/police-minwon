package mb.fw.policeminwon.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mb.fw.policeminwon.web.dto.SummaryAPIRequest;
import mb.fw.policeminwon.web.service.SummaryService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/esb/api")
public class SummaryController {

	private final SummaryService summaryService;

	public SummaryController(SummaryService summaryService) {
		this.summaryService = summaryService;
	}

	@PostMapping("/summary")
	public Mono<ResponseEntity<String>> summaryCall(SummaryAPIRequest request) {
		summaryService.doAsyncProcess(request);
		return Mono.just(ResponseEntity.accepted().body("Accept summary service call"));
	}

}
