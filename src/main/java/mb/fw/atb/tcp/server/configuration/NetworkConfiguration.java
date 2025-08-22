package mb.fw.atb.tcp.server.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mb.indigo2.springsupport.AdaptorConfigBean;

import mb.fw.atb.tcp.server.TcpServerAdaptor;
import springfox.documentation.service.ApiInfo;

@Configuration
public class NetworkConfiguration {
	private static final Logger log = LoggerFactory.getLogger(NetworkConfiguration.class);
	@Autowired
	AdaptorConfigBean bean;
	String basePackage = null;
	ApiInfo apiinfo = null;
	String remoteHost;
	String remotePort;

	@ConditionalOnProperty(name = {"network.adaptor.server.tcp.enabled"}, matchIfMissing = false)
	@Bean(initMethod = "start", destroyMethod = "stop", name = {"adaptor"})
	public TcpServerAdaptor tcpServerAdaptor() {
		log.info("▶ TcpServerAdaptor start");
		return new TcpServerAdaptor();
	}
}
