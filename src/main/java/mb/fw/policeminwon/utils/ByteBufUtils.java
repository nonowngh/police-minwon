package mb.fw.policeminwon.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mb.fw.policeminwon.constants.TcpCommonSettingConstants;

public class ByteBufUtils {

	public static String getStringfromBytebuf(ByteBuf buf, int statrtIdx, int length) {
		return buf.toString(statrtIdx, length, TcpCommonSettingConstants.MESSAGE_CHARSET);
	}
	
	public static String getStringValuefromByteBuf(ByteBuf buf, int statrtIdx, int length) {
		return buf.slice(statrtIdx, length).toString(TcpCommonSettingConstants.MESSAGE_CHARSET).trim();
	}
	
	public static Integer getIntegerValuefromByteBuf(ByteBuf buf, int statrtIdx, int length) {
		return Integer.valueOf(buf.slice(statrtIdx, length).toString(TcpCommonSettingConstants.MESSAGE_CHARSET));
	}
	
	public static int setStringAndMoveOffset(Consumer<String> setter, ByteBuf buf, int start, int length) {
	    int end = start + length;
	    setter.accept(ByteBufUtils.getStringValuefromByteBuf(buf, start, length));
	    return end;
	}
	
	public static int skipAndMoveOffset(ByteBuf buf, int start, int length) {
	    // offset 구간 내용만 읽고 사용하지 않음
	    return start + length;
	}
	
	public static int setIntegerAndMoveOffset(Consumer<Integer> setter, ByteBuf buf, int start, int length) {
	    int end = start + length;
	    setter.accept(ByteBufUtils.getIntegerValuefromByteBuf(buf, start, length));
	    return end;
	}
	
	public static void writeRightPaddingString(ByteBuf buf, String value, int fixedLength) {
	    byte[] rawBytes = value.getBytes(TcpCommonSettingConstants.MESSAGE_CHARSET);
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
	public static void writeRightPaddingString(ByteBuf buf, String value, int fixedLength, boolean isPrint) {
		byte[] rawBytes = value.getBytes(TcpCommonSettingConstants.MESSAGE_CHARSET);
		int paddingLength = fixedLength - rawBytes.length;
		int initBufLength = buf.readableBytes();
		if (paddingLength < 0) {
			buf.writeBytes(Arrays.copyOf(rawBytes, fixedLength));
		} else {
			buf.writeBytes(rawBytes);
			for (int i = 0; i < paddingLength; i++) {
				buf.writeByte(' '); // ASCII space padding
			}
		}
		if(isPrint) CommonLoggingUtils.printFieldInfo(buf.toString(initBufLength, fixedLength, TcpCommonSettingConstants.MESSAGE_CHARSET), fixedLength);
	}
	
	public static void writeLeftPaddingNumber(ByteBuf buf, int value, int fixedLength) {
	    String numberStr = String.format("%0" + fixedLength + "d", value);
	    buf.writeBytes(numberStr.getBytes(StandardCharsets.US_ASCII));
	}
	public static void writeLeftPaddingNumber(ByteBuf buf, int value, int fixedLength, boolean isPrint) {
		int initBufLength = buf.readableBytes();
		String numberStr = String.format("%0" + fixedLength + "d", value);
		buf.writeBytes(numberStr.getBytes(StandardCharsets.US_ASCII));
		if(isPrint) CommonLoggingUtils.printFieldInfo(buf.toString(initBufLength, fixedLength, TcpCommonSettingConstants.MESSAGE_CHARSET), fixedLength);

	}
	
	public static ByteBuf addMessageLength(ByteBuf messageBuf) {
		ByteBuf lengthBuf = Unpooled.buffer(4);
		writeLeftPaddingNumber(lengthBuf, messageBuf.readableBytes(), 4);
	    messageBuf.retain();
		return Unpooled.wrappedBuffer(lengthBuf, messageBuf);
	}
}
