package mb.fw.policeminwon.parser.slice;

import io.netty.buffer.ByteBuf;
import mb.fw.policeminwon.utils.ByteBufUtils;

public class HeaderSlice {

	public static String getTransactionCode(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 14, 6);
	}

	public static String getSrFlag(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 23, 1);
	}
}
