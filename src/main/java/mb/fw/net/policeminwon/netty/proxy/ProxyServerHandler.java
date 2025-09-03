package mb.fw.net.policeminwon.netty.proxy;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import mb.fw.atb.tcp.server.entity.PenaltyTestCallParser;
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
		try {
			String transactionCode = ByteBufUtils.getStringfromBytebuf(inBuf, 10, 6);
			log.info("transactionCode -> " + transactionCode);
			if (TcpMessageTransactionCode.testCallCode.equals(transactionCode)) {
				log.info("Test Call...");
				testCall(ctx, inBuf);
			} else {

			}
		} finally {
			ReferenceCountUtil.release(inBuf);
		}
	}

	private void testCall(ChannelHandlerContext ctx, ByteBuf inBuf) {
		Channel asyncChannel = client.getChannel();
		String resStr = PenaltyTestCallParser.makeResponeMessage(PenaltyTestCallParser.toEntity(inBuf.toString(StandardCharsets.UTF_8)),
				ByteBufUtils.getStringfromBytebuf(inBuf, 16, 3));
		ByteBuf outBuf = Unpooled.copiedBuffer(resStr, StandardCharsets.UTF_8);
		if (asyncChannel != null && asyncChannel.isActive()) {
			asyncChannel.writeAndFlush(outBuf).awaitUninterruptibly();
		} else {
			client.reconnectOnInactive(ctx);
			asyncChannel.writeAndFlush(outBuf);
		}
		outBuf.release();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
