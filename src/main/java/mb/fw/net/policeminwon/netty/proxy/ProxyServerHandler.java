package mb.fw.net.policeminwon.netty.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import mb.fw.net.policeminwon.constants.TcpMessageTransactionCode;
import mb.fw.net.policeminwon.netty.proxy.client.AsyncConnectionClient;
import mb.fw.net.policeminwon.utils.ByteBufUtils;

@Slf4j
public class ProxyServerHandler extends ChannelInboundHandlerAdapter {

	private AsyncConnectionClient client;

	public ProxyServerHandler(AsyncConnectionClient client) {
		this.client = client;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf inBuf = (ByteBuf) msg;
		String transactionCode = ByteBufUtils.getStringfromBytebuf(inBuf, 10, 6);

		if (TcpMessageTransactionCode.testCallCode.equals(transactionCode)) {
			log.debug("Test Call...");
			try {
				Channel asyncChannel = client.getChannel();
				if (asyncChannel != null && asyncChannel.isActive()) {
					asyncChannel.writeAndFlush(inBuf);
				} else {
					client.reconnectOnInactive(ctx);
					asyncChannel.writeAndFlush(inBuf);
				}
			} finally {
				inBuf.release();
			}
		} else {

		}
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
