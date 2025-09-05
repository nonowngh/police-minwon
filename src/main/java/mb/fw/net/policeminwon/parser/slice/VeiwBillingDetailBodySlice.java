package mb.fw.net.policeminwon.parser.slice;

import io.netty.buffer.ByteBuf;
import mb.fw.net.policeminwon.utils.ByteBufUtils;

public class VeiwBillingDetailBodySlice {
	
	final static int HEADER_LENGTH = 74;

	//전자납부번호(body index 0 ~ 19)
	public static String getElecPayNo(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, HEADER_LENGTH, 19);
	}
}
