package mb.fw.net.proxy.tcp;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewTCPProxyInitializer extends ChannelInitializer<SocketChannel> {
	private final String remoteHost;
	private final String remotePort;
	private final int gbnStartlength;
	private final int gbnEndlength;

	public NewTCPProxyInitializer(String remoteHost, String remotePort, int gbnStartlength, int gbnEndlength ) {
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.gbnStartlength = gbnStartlength;
		this.gbnEndlength = gbnStartlength;
	}

	public void initChannel(SocketChannel ch) {
		ch.pipeline().addLast(new ChannelHandler[]{new LoggingHandler(LogLevel.INFO),
				new TCPProxyFrontendHandler(this.remoteHost, this.remotePort, this.gbnStartlength, this.gbnEndlength)});
	}
}