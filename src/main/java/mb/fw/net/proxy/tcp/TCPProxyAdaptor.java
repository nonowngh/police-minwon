package mb.fw.net.proxy.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "network.adaptor.proxy.tcp", ignoreUnknownFields = true)
@Validated
@Setter
@Getter
public class TCPProxyAdaptor {
	private static final Logger log = LoggerFactory.getLogger(TCPProxyAdaptor.class);
	boolean enabled;
	@NotNull(message = "properties 파일에 network.adaptor.proxy.tcp.local-port 등록하여주세요")
	int localPort;
	@NotNull(message = "properties 파일에 network.adaptor.proxy.tcp.remote-host 등록하여주세요")
	String remoteHost;
	@NotNull(message = "properties 파일에 network.adaptor.proxy.tcp.remote-port 등록하여주세요")
	String remotePort;
	//즉심, 교통 구분 값 전자납부번호 subString(gbnStartlength, gbnEndlength)
	int gbnStartlength; //시작 위치
	int gbnEndlength; //끝나는 위치 예 : 130720253 -> 타겟 20253 일시 startLength : 4 , endLength : 9
	
	@Autowired(required = false)
	JmsTemplate jmstemplate;
	
	EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	EventLoopGroup workerGroup = new NioEventLoopGroup();

	public void start() {
		log.info("New Proxying *:" + this.localPort + " to " + this.remoteHost + ':' + this.remotePort + " ...");
		(new Thread(() -> {
			try {
				ServerBootstrap b = new ServerBootstrap();
				((ServerBootstrap) ((ServerBootstrap) b.group(this.bossGroup, this.workerGroup)
						.channel(NioServerSocketChannel.class)).handler(new LoggingHandler(LogLevel.INFO)))
						.childHandler(new NewTCPProxyInitializer(this.remoteHost, this.remotePort, this.gbnStartlength, this.gbnEndlength))
						.childOption(ChannelOption.AUTO_READ, false).bind(this.localPort).sync().channel().closeFuture()
						.sync();
			} catch (InterruptedException var2) {
				log.error("shit happens", var2);
			}

		})).start();
	}

	public void stop() {
		if (!this.bossGroup.isShutdown()) {
			this.bossGroup.shutdownGracefully();
		}

		if (!this.workerGroup.isShutdown()) {
			this.workerGroup.shutdownGracefully();
		}

	}

//	public boolean isEnabled() {
//		return this.enabled;
//	}
//
//	public void setEnabled(final boolean enabled) {
//		this.enabled = enabled;
//	}
//
//	public int getLocalPort() {
//		return this.localPort;
//	}
//
//	public void setLocalPort(final int localPort) {
//		this.localPort = localPort;
//	}
//
//	public String getRemoteHost() {
//		return this.remoteHost;
//	}
//
//	public void setRemoteHost(final String remoteHost) {
//		this.remoteHost = remoteHost;
//	}
//
//	public String getRemotePort() {
//		return remotePort;
//	}
//
//	public void setRemotePort(String remotePort) {
//		this.remotePort = remotePort;
//	}

}