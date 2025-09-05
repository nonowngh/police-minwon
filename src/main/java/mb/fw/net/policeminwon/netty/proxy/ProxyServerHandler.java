package mb.fw.net.policeminwon.netty.proxy;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import lombok.extern.slf4j.Slf4j;
import mb.fw.net.policeminwon.constants.TcpHeaderTransactionCode;
import mb.fw.net.policeminwon.netty.proxy.client.AsyncConnectionClient;
import mb.fw.net.policeminwon.parser.BodyCompareParser;
import mb.fw.net.policeminwon.parser.TestCallParser;
import mb.fw.net.policeminwon.parser.slice.HeaderSlice;
import mb.fw.net.policeminwon.parser.slice.VeiwBillingDetailBodySlice;
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
			String transactionCode = HeaderSlice.getTransactionCode(inBuf);
			log.info("transactionCode -> [{}]", transactionCode);		
			String srFlag =  HeaderSlice.getSrFlag(inBuf);
			
			Map<String, Runnable> actions = new HashMap<>();
			// 테스트 콜
			actions.put(TcpHeaderTransactionCode.TEST_CALL, () -> testCall(ctx, inBuf));
			// 고지내역 상세조회
			actions.put(TcpHeaderTransactionCode.VIEW_BILLING_DETAIL, () -> veiwBillingDetail(ctx, inBuf));
			// 납부결과 통지
			actions.put(TcpHeaderTransactionCode.PAYMENT_RESULT_NOTIFICATION, () -> testCall(ctx, inBuf));
			// 납부 (재)취소
			actions.put(TcpHeaderTransactionCode.CANCEL_PAYMENT, () -> testCall(ctx, inBuf));

			actions.getOrDefault(transactionCode, () -> {
				throw new IllegalArgumentException("Invalid transaction-code -> " + transactionCode);	
			}).run();
			
		} finally {
			if (((ReferenceCounted) msg).refCnt() > 0) ReferenceCountUtil.release(msg);
		}
	}

	private void veiwBillingDetail(ChannelHandlerContext ctx, ByteBuf inBuf) {
		String policeSystemCode = VeiwBillingDetailBodySlice.getElecPayNo(inBuf);
		// 즉심 호출
		if(policeSystemCode.startsWith(BodyCompareParser.getSJSElctNum())){
			log.info("고지내역 상세조회...[{}] -> [{}]", "금결원", "즉심(SJS)");
		}
		// 교통 호출
		else {
			log.info("고지내역 상세조회 - BYPASS...[{}] -> [{}]", "금결원", "교통(TCS)");
			client.callAsync(ctx, inBuf);
		}
	}

	private void testCall(ChannelHandlerContext ctx, ByteBuf inBuf){
		log.info("테스트 콜...[{}] -> [{}]", "금결원", "프록시");
		String resStr = TestCallParser.makeResponeMessage(
				TestCallParser.toEntity(inBuf.toString(StandardCharsets.UTF_8)),
				ByteBufUtils.getStringfromBytebuf(inBuf, 16, 3));
		ByteBuf outBuf = ByteBufUtils.addMessageLength(Unpooled.copiedBuffer(resStr, StandardCharsets.UTF_8));

		client.callAsync(ctx, outBuf);
	}




	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
