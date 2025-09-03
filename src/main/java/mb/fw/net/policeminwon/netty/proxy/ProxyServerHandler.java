package mb.fw.net.policeminwon.netty.proxy;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import mb.fw.net.policeminwon.constants.TcpMessageTransactionCode;
import mb.fw.net.policeminwon.netty.proxy.client.AsyncConnectionClient;
import mb.fw.net.policeminwon.parser.TestCallParser;
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
			String transactionCode = ByteBufUtils.getStringfromBytebuf(inBuf, 14, 6);
			log.info("transactionCode -> " + transactionCode);
			if (TcpMessageTransactionCode.testCall.equals(transactionCode)) {
				log.info("Test Call...");
				testCall(ctx, inBuf);
			} else {

			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	private void testCall(ChannelHandlerContext ctx, ByteBuf inBuf) throws InterruptedException {
		Channel asyncChannel = client.getChannel();
		String resStr = TestCallParser.makeResponeMessage(
				TestCallParser.toEntity(inBuf.toString(StandardCharsets.UTF_8)),
				ByteBufUtils.getStringfromBytebuf(inBuf, 16, 3));
		ByteBuf outBuf = ByteBufUtils.addMessageLength(Unpooled.copiedBuffer(resStr, StandardCharsets.UTF_8));

		try {
			if (asyncChannel != null && asyncChannel.isActive()) {
				asyncChannel.writeAndFlush(outBuf).awaitUninterruptibly();
			} else {
				ChannelFuture future = client.reconnectOnInactive(ctx);
				future.awaitUninterruptibly();
				asyncChannel = future.channel();
				if (asyncChannel != null && asyncChannel.isActive()) {
				    asyncChannel.writeAndFlush(outBuf).awaitUninterruptibly();
				}
			}
		} finally {
			if (outBuf.refCnt() > 0) {
				outBuf.release();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
