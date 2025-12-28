	package mb.fw.policeminwon.netty.proxy.logging;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import mb.fw.policeminwon.constants.TcpCommonSettingConstants;

public class PrettyLoggingHandler extends LoggingHandler {

	public PrettyLoggingHandler(LogLevel level) {
		super(level);
	}

	@Override
	protected String format(ChannelHandlerContext ctx, String eventName, Object arg) {
		if (arg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) arg;
			int length = buf.readableBytes(); 
			String content = buf.toString(TcpCommonSettingConstants.MESSAGE_CHARSET);
			return String.format("[%s] %s: %s [%d]", ctx.channel().id(), eventName, content, length);
		}
		return super.format(ctx, eventName, arg);
	}
}
