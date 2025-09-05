package mb.fw.policeminwon.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import mb.fw.policeminwon.netty.proxy.client.AsyncConnectionClient;

@Data
@Configuration
@ConfigurationProperties(prefix = "tcp.client.async-connction", ignoreUnknownFields = true)
@ConditionalOnProperty(name = "tcp.client.async-connction.enabled", havingValue = "true")
public class AsyncConnectionConfiguration {

	private String host;
	
	private int port;
	
	private int reconnectDelaySec;
	
	@Bean(initMethod = "start", destroyMethod = "shutdown")
    AsyncConnectionClient client() {
    	return new AsyncConnectionClient(host, port, reconnectDelaySec);
    }
}
