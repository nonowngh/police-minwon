package mb.fw.atb.tcp.server;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.annotation.Validated;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;
import lombok.Setter;
import mb.fw.net.proxy.tcp.NewTCPProxyInitializer;

@ConfigurationProperties(prefix = "network.adaptor.server.tcp", ignoreUnknownFields = true)
@Validated
@Setter
@Getter
public class TcpServerAdaptor {
	
	private static final Logger log = LoggerFactory.getLogger(TcpServerAdaptor.class);
	
	boolean enabled;
	@NotNull(message = "properties 파일에 network.adaptor.server.tcp.bind-port 등록하여주세요")
    int bindPort;
	String remoteHost;
	int remotePort;
	
	@Autowired(required = false)
	JmsTemplate jmstemplate;

    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start() throws Exception {
    	log.info("▶ TCP Server Start");
    	(new Thread(() -> {
			try {
				ServerBootstrap b = new ServerBootstrap();
				((ServerBootstrap) ((ServerBootstrap) b.group(this.bossGroup, this.workerGroup)
						.channel(NioServerSocketChannel.class)).handler(new LoggingHandler(LogLevel.INFO)))
						.childHandler(new TcpServerInitializer(this.remoteHost, this.remotePort, this.jmstemplate))
						.childOption(ChannelOption.AUTO_READ, false).bind(this.bindPort).sync().channel().closeFuture()
						.sync();
			} catch (InterruptedException var2) {
				log.error("ERROR :{}", var2);
			}
		})).start();
        log.info("TCP Server started on port :{}", bindPort);
    }

	public void stop() {
		if (!this.bossGroup.isShutdown()) {
			this.bossGroup.shutdownGracefully();
		}

		if (!this.workerGroup.isShutdown()) {
			this.workerGroup.shutdownGracefully();
		}

	}
}
