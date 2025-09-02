package mb.fw.net.policeminwon.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import mb.fw.net.policeminwon.netty.proxy.ProxyServer;
import mb.fw.net.policeminwon.netty.proxy.client.AsyncConnectionClient;
import mb.fw.net.policeminwon.netty.summary.SummaryServer;

@Data
@Configuration
public class NettyServerConfiguration {

	@Bean(initMethod = "start", destroyMethod = "shutdown")
	@ConditionalOnProperty(prefix = "tcp.server.proxy", name = "enabled", havingValue = "true")
	ProxyServer proxyServer(@Value("${tcp.server.proxy.bind-port}") int bindPort, AsyncConnectionClient client) {
		return new ProxyServer(bindPort, client);
	}

	@Bean(initMethod = "start", destroyMethod = "shutdown")
	@ConditionalOnProperty(prefix = "tcp.server.summary", name = "enabled", havingValue = "true")
	SummaryServer summaryServer(@Value("${tcp.server.summary.bind-port}") int bindPort) {
		return new SummaryServer(bindPort);
	}
}
