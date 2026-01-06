package mb.fw.policeminwon.netty.proxy.logging;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.constants.TcpCommonSettingConstants;
import mb.fw.policeminwon.constants.TcpHeaderTransactionCode;
import mb.fw.policeminwon.constants.TcpMessageConstants;
import mb.fw.policeminwon.parser.slice.MessageSlice;

@Slf4j
public class CustomLoggingHandler extends LoggingHandler {

	private final Charset charset = TcpCommonSettingConstants.MESSAGE_CHARSET;
	private static final Set<TcpHeaderTransactionCode> panaltyTransactionCode = Collections.unmodifiableSet(
			new HashSet<TcpHeaderTransactionCode>(Arrays.asList(TcpHeaderTransactionCode.VIEW_BILLING_DETAIL,
					TcpHeaderTransactionCode.PAYMENT_RESULT_NOTIFICATION)));
	
	@Override
	protected String format(ChannelHandlerContext ctx, String eventName, Object arg) {
		if (arg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) arg;
			String content = buf.toString(TcpCommonSettingConstants.MESSAGE_CHARSET);
			return String.format("[%s] %s: %s", ctx.channel().id(), eventName, content);
		}
		return super.format(ctx, eventName, arg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			String sndSysCode = MessageSlice.getSrFlagWrite(buf);
			String systemName = "";
			if (TcpMessageConstants.SRFLAG_KFTC.equals(sndSysCode)) {
				systemName = "금융결제원(KFT)";
			} else {
				systemName = "교통시스템(TCS)";
				TcpHeaderTransactionCode tcpHeaderTransactionCode = TcpHeaderTransactionCode
						.fromCode(MessageSlice.getTransactionCodeWrite(buf));
				if (panaltyTransactionCode.contains(tcpHeaderTransactionCode)) {
					String policeSystemCode = TcpHeaderTransactionCode.VIEW_BILLING_DETAIL
							.equals(tcpHeaderTransactionCode) ? MessageSlice.getElecPayNoViewBillingDetailWrite(buf)
									: MessageSlice.getElecPayNoPaymentResultNotificationWrite(buf);
					if (TcpMessageConstants.isSummary(policeSystemCode)) {
						systemName = "ESB PROXY";
					}
				}
			}
			String data = buf.toString(charset);
			log.info("RECEIVE <<< from  [{}]({}|{}) : [{}]", systemName, ctx.channel().remoteAddress(),
					ctx.channel().id(), data);
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			String data = buf.toString(charset);
			String sndSysCode = MessageSlice.getSrFlagWrite(buf);
			String systemName = "";
			if (TcpMessageConstants.SRFLAG_POLICE.equals(sndSysCode)) {
				systemName = "금융결제원(KFT)";
			} else {
				systemName = "교통시스템(TCS)";
				TcpHeaderTransactionCode tcpHeaderTransactionCode = TcpHeaderTransactionCode
						.fromCode(MessageSlice.getTransactionCodeWrite(buf));
				if (panaltyTransactionCode.contains(tcpHeaderTransactionCode)) {
					String policeSystemCode = TcpHeaderTransactionCode.VIEW_BILLING_DETAIL
							.equals(tcpHeaderTransactionCode) ? MessageSlice.getElecPayNoViewBillingDetailWrite(buf)
									: MessageSlice.getElecPayNoPaymentResultNotificationWrite(buf);
					if (TcpMessageConstants.isSummary(policeSystemCode)) {
						systemName = "ESB PROXY";
					}
				}
			}
			log.info("SEND >>> to [{}]({}|{}) : [{}]", systemName, ctx.channel().remoteAddress(), ctx.channel().id(),
					data);
		}
		super.write(ctx, msg, promise);
	}
}