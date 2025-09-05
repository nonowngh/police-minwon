package mb.fw.policeminwon.web.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.netty.buffer.Unpooled;
import mb.fw.policeminwon.netty.proxy.client.AsyncConnectionClient;
import mb.fw.policeminwon.web.dto.SummaryAPIRequest;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/esb/api")
public class ProxyController {

	private final AsyncConnectionClient client;
	
	public ProxyController(AsyncConnectionClient client) {
		this.client = client;
	}

	@PostMapping("/proxy")
	public Mono<ResponseEntity<String>> summaryCall(SummaryAPIRequest request) {
		return Mono.just(ResponseEntity.accepted().body("Accept summary service call")).doOnSuccess(response -> {
			client.callAsync(Unpooled.copiedBuffer(request.getBodyData(), StandardCharsets.UTF_8));
		});
	}

}
