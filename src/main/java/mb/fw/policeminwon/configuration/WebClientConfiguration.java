package mb.fw.policeminwon.configuration;

import java.time.Duration;

import javax.net.ssl.SSLException;

import org.apache.http.HttpHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "web.client", ignoreUnknownFields = true)
@ConditionalOnProperty(name = "web.client.enabled", havingValue = "true")
public class WebClientConfiguration {

	private String targetUrl;

	private String callBackUrl;

	private int responseTimeoutSeconds = 30;

	@Bean(name = "webClient")
	WebClient webClient() {

		HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(responseTimeoutSeconds));

		if (targetUrl.startsWith("https://")) {
			httpClient = httpClient.secure(ssl -> {
				try {
					ssl.sslContext(
							SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build());
				} catch (SSLException e) {
					log.error("sslContext error!");
				}
			});
		}
		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).baseUrl(targetUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@Bean(name = "callBackWebClient")
	WebClient callBackWebClient() {
		return WebClient.builder().baseUrl(callBackUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
}
