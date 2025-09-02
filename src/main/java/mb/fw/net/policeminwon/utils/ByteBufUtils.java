package mb.fw.net.policeminwon.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

public class ByteBufUtils {

	static Charset charset = StandardCharsets.UTF_8;

	public static String getStringfromBytebuf(ByteBuf buf, int statrtIdx, int length) {
		return buf.toString(statrtIdx, length, charset);
	}
}
