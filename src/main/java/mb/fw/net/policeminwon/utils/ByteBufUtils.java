package mb.fw.net.policeminwon.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufUtils {

	static Charset charset = StandardCharsets.UTF_8;

	public static String getStringfromBytebuf(ByteBuf buf, int statrtIdx, int length) {
		return buf.toString(statrtIdx, length, charset);
	}
	
	public static void writeRightPaddingString(ByteBuf buf, String value, int fixedLength) {
	    byte[] rawBytes = value.getBytes(charset);
	    int paddingLength = fixedLength - rawBytes.length;

	    if (paddingLength < 0) {
	        buf.writeBytes(Arrays.copyOf(rawBytes, fixedLength));
	    } else {
	        buf.writeBytes(rawBytes);
	        for (int i = 0; i < paddingLength; i++) {
	            buf.writeByte(' '); // ASCII space padding
	        }
	    }
	}
	
	public static ByteBuf addMessageLength(ByteBuf messageBuf) {
		ByteBuf lengthBuf = Unpooled.buffer(4);
		writeLeftPaddingNumber(lengthBuf, messageBuf.readableBytes(), 4);
		return Unpooled.wrappedBuffer(lengthBuf, messageBuf);
	}
	
	public static void writeLeftPaddingNumber(ByteBuf buf, int value, int fixedLength) {
	    String numberStr = String.format("%0" + fixedLength + "d", value);
	    buf.writeBytes(numberStr.getBytes(StandardCharsets.US_ASCII));
	}
}
