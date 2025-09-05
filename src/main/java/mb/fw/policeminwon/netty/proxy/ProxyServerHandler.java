package mb.fw.policeminwon.netty.proxy;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.reactive.function.client.WebClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.constants.TcpHeaderSrFlag;
import mb.fw.policeminwon.constants.TcpHeaderTransactionCode;
import mb.fw.policeminwon.netty.proxy.client.AsyncConnectionClient;
import mb.fw.policeminwon.parser.BodyCompareParser;
import mb.fw.policeminwon.parser.TestCallParser;
import mb.fw.policeminwon.parser.slice.HeaderSlice;
import mb.fw.policeminwon.parser.slice.VeiwBillingDetailBodySlice;
import mb.fw.policeminwon.utils.ByteBufUtils;

@Slf4j
public class ProxyServerHandler extends ChannelInboundHandlerAdapter {

	private final AsyncConnectionClient client;
	private final WebClient webClient;

	public ProxyServerHandler(AsyncConnectionClient client, WebClient webClient) {
		this.client = client;
		this.webClient = webClient;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf inBuf = (ByteBuf) msg;
		try {
			String transactionCode = HeaderSlice.getTransactionCode(inBuf);
			log.info("transactionCode -> [{}]", transactionCode);
			String srFlag = HeaderSlice.getSrFlag(inBuf);

			Map<String, Runnable> actions = new HashMap<>();
			// 테스트 콜
			actions.put(TcpHeaderTransactionCode.TEST_CALL, () -> testCall(inBuf));
			// 고지내역 상세조회
			actions.put(TcpHeaderTransactionCode.VIEW_BILLING_DETAIL, () -> veiwBillingDetail(inBuf, srFlag));
			// 납부결과 통지
			actions.put(TcpHeaderTransactionCode.PAYMENT_RESULT_NOTIFICATION, () -> testCall(inBuf));
			// 납부 (재)취소
			actions.put(TcpHeaderTransactionCode.CANCEL_PAYMENT, () -> testCall(inBuf));

			actions.getOrDefault(transactionCode, () -> {
				throw new IllegalArgumentException("Invalid transaction-code -> " + transactionCode);
			}).run();

		} finally {
			if (((ReferenceCounted) msg).refCnt() > 0)
				ReferenceCountUtil.release(msg);
		}
	}

	private void veiwBillingDetail(ByteBuf inBuf, String srFlag) {
		String policeSystemCode = VeiwBillingDetailBodySlice.getElecPayNo(inBuf);
		if (policeSystemCode.startsWith(BodyCompareParser.getSJSElctNum())) {
			if (TcpHeaderSrFlag.KFTC.equalsIgnoreCase(srFlag)) {
				log.info("고지내역 상세조회...[{}] -> [{}]", "금결원", "즉심(SJS)");
				restApiCall(inBuf);
			} else {
				log.info("고지내역 상세조회...[{}] -> [{}]", "즉심(SJS)", "금결원");
				client.callAsync(inBuf);
			}
		} else {
			if (TcpHeaderSrFlag.KFTC.equalsIgnoreCase(srFlag)) {
				log.info("고지내역 상세조회 - BYPASS...[{}] -> [{}]", "금결원", "교통(TCS)");
			} else {
				log.info("고지내역 상세조회 - BYPASS...[{}] -> [{}]", "교통(TCS)", "금결원");
			}
			client.callAsync(inBuf);
		}
	}

	private void testCall(ByteBuf inBuf) {
		log.info("테스트 콜...[{}] -> [{}]", "금결원", "프록시");
		String resStr = TestCallParser.makeResponeMessage(
				TestCallParser.toEntity(inBuf.toString(StandardCharsets.UTF_8)),
				ByteBufUtils.getStringfromBytebuf(inBuf, 16, 3));
		ByteBuf outBuf = ByteBufUtils.addMessageLength(Unpooled.copiedBuffer(resStr, StandardCharsets.UTF_8));
		client.callAsync(outBuf);
	}

	private void restApiCall(ByteBuf inBuf) {
		if (webClient != null) {
			webClient.post().bodyValue(inBuf.toString(StandardCharsets.UTF_8)).retrieve().bodyToMono(String.class)
					.doOnNext(response -> {
						System.out.println("API 응답: " + response);
					}).doOnError(error -> {
						System.err.println("API 오류: " + error.getMessage());
					}).subscribe();
		} else {
			log.error("WebClient is NULL. check yaml file.");
		}
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
