package mb.fw.atb.tcp.server;

import org.springframework.jms.core.JmsTemplate;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TcpServerInitializer  extends ChannelInitializer<SocketChannel>{
	private final String remoteHost;
	private final int remotePort;
	private JmsTemplate jmsTemplate;

	public TcpServerInitializer(String remoteHost, int remotePort, JmsTemplate jmsTemplate) {
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.jmsTemplate = jmsTemplate;
	}

	public void initChannel(SocketChannel ch) {
		ch.pipeline().addLast(new ChannelHandler[]{new LoggingHandler(LogLevel.INFO),
				new TcpServerHandler(this.remoteHost, this.remotePort, this.jmsTemplate)});
	}
}
