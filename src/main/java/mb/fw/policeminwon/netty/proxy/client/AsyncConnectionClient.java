package mb.fw.policeminwon.netty.proxy.client;

import java.net.ConnectException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.constants.TcpCommonSettingConstants;
import mb.fw.policeminwon.netty.proxy.logging.PrettyLoggingHandler;

@Slf4j
public class AsyncConnectionClient {

	private String host;
	private int port;
	private int reconnectDelaySec;
	@Getter
	private String systemCode;

	public AsyncConnectionClient(String systemCode, String host, int port, int reconnectDelaySec) {
		this.systemCode = systemCode;
		this.host = host;
		this.port = port;
		this.reconnectDelaySec = reconnectDelaySec;
	}

	private final EventLoopGroup group = new NioEventLoopGroup();
	private volatile Channel channel;
	private final Bootstrap bootstrap = new Bootstrap();

	private final Semaphore connectionLimiter = new Semaphore(1);

	public void start() {
		log.info("Loading async connection client [{} -> {}:{}, {} seconds(reconnect-delay-sec)]", systemCode, host,
				port, reconnectDelaySec);

		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				ChannelPipeline p = ch.pipeline();
				p.addFirst(TcpCommonSettingConstants.PRETTY_LOGGING ? new PrettyLoggingHandler(LogLevel.INFO)
						: new LoggingHandler(LogLevel.INFO));
//           	 	p.addLast(new mb.fw.net.common.codec.LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4, true));
				p.addLast(
//						TcpCommonSettingConstants.PRETTY_LOGGING ? new PrettyLoggingHandler(LogLevel.INFO)
//						: new LoggingHandler(LogLevel.INFO), 
						new SimpleChannelInboundHandler<Object>() {
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
								super.channelRead(ctx, msg);
							}

							@Override
							public void channelInactive(ChannelHandlerContext ctx) {
								log.info("Disconnected [{}] async-connection-client. channel-id : {}", systemCode,
										channel.id());
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

	@PreDestroy
	public void shutdown() {
		log.info("Shutdown AsyncConnectionClient...");
		if (channel != null)
			group.shutdownGracefully();
	}

	private void doConnect() {
		if (bootstrap == null)
			return;

		if (connectionLimiter.tryAcquire()) {
			bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
				connectionLimiter.release();
				if (future.isSuccess()) {
					channel = future.channel();
					log.info("Connected to [{}:{}] ({})server ", host, port, systemCode);
				} else {
					log.error("Failed to connect [{}:{}]. Retrying in {} seconds...", host, port, reconnectDelaySec);
					scheduleReconnect();
				}
			});
		}
	}

	private void scheduleReconnect() {
		bootstrap.config().group().schedule(() -> {
			if (channel == null || !channel.isActive()) {
				doConnect();
			}
		}, reconnectDelaySec, TimeUnit.SECONDS);
	}

	public Channel getChannel() {
		return (channel != null && channel.isActive()) ? channel : null;
	}

	public ChannelFuture reconnectOnInactive() {
		log.error("Disconnected from [{}:{}] server. Will attempt reconnect...", host, port);
		return bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
			if (future.isSuccess()) {
				channel = future.channel();
				log.info("Connected to [{}:{}] ({})server ", host, port, systemCode);
			} else {
				log.error("Reconnection failed [{}:{}].", host, port);
			}
		});
	}

	public void callAsync(ByteBuf outBuf) throws Exception {
		try {
			if (channel != null && channel.isActive()) {
				channel.writeAndFlush(outBuf).awaitUninterruptibly();
			} else {
				ChannelFuture future = reconnectOnInactive();
				future.awaitUninterruptibly();
				if (!future.isSuccess()) {
					throw new ConnectException("Connection failed to [" + host + ":" + port + "]");
				}
				channel = future.channel();
				if (channel != null && channel.isActive()) {
					channel.writeAndFlush(outBuf).awaitUninterruptibly();
				} else {
					throw new Exception("Channel is not active after reconnect");
				}
			}
		} finally {
			if (((ReferenceCounted) outBuf).refCnt() > 0)
				ReferenceCountUtil.release(outBuf);
		}
	}
}
