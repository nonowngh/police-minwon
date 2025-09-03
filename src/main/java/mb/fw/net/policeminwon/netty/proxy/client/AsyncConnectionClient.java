package mb.fw.net.policeminwon.netty.proxy.client;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncConnectionClient {

	private String host;
	private int port;
	private int reconnectDelaySec;


	public AsyncConnectionClient(String host, int port, int reconnectDelaySec) {
		this.host = host;
		this.port = port;
		this.reconnectDelaySec = reconnectDelaySec;
	}

	private static EventLoopGroup group;
	private static volatile Channel channel;
	private static Bootstrap bootstrap;

	public void start() {
		group = new NioEventLoopGroup();

		bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new LoggingHandler(LogLevel.INFO), new SimpleChannelInboundHandler<Object>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                		super.channelRead(ctx, msg);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) {
                        scheduleReconnect();
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                		super.exceptionCaught(ctx, cause);
                    }
                });
			}
		});

		doConnect();
	}

	public void shutdown() {
		if (channel != null && channel.isActive())
			channel.close();
		group.shutdownGracefully();
	}

	private void doConnect() {
		if (bootstrap == null)
			return;

		bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
			if (future.isSuccess()) {
				channel = future.channel();
				log.info("Connected to [{}:{}] server", host, port);
			} else {
				log.error("Failed to connect. Retrying in " + reconnectDelaySec + " seconds...");
				scheduleReconnect();
			}
		});
	}

	private void scheduleReconnect() {
		bootstrap.config().group().schedule(() -> {
			doConnect();
		}, reconnectDelaySec, TimeUnit.SECONDS);
	}

	public Channel getChannel() {
		return (channel != null && channel.isActive()) ? channel : null;
	}

	public void reconnectOnInactive(ChannelHandlerContext ctx) {
		log.error("Disconnected from [{}:{}] server. Will attempt reconnect...", host, port);
		channel = null;
		scheduleReconnect();
	}
}
