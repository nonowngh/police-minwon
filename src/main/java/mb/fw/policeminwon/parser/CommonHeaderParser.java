package mb.fw.policeminwon.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.constants.TcpCommonSettingConstants;
import mb.fw.policeminwon.utils.ByteBufUtils;

@Slf4j
public class CommonHeaderParser {

	final static int HEADER_LENGTH = 74;

	// String 사용, 전체 헤더 길이 70 bytes(길이 필드 제외)
	public static ByteBuf responseHeader(String headerMessage, String messageTypeCode, String statusCode,
			String policeTransactionId) {
		ByteBuf reqBuf = Unpooled.copiedBuffer(headerMessage, TcpCommonSettingConstants.MESSAGE_CHARSET);
		ByteBuf resBuf = Unpooled.buffer(HEADER_LENGTH - 4);
		try {
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(reqBuf, 0, 3).getBytes()); // 업무구분(3)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(reqBuf, 3, 3).getBytes()); // 기관코드(3)
			ByteBufUtils.writeRightPaddingString(resBuf, messageTypeCode, 4); // 전문종별코드(4)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(reqBuf, 10, 6).getBytes()); // 거래구분코드(6)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(reqBuf, 16, 3).getBytes()); // 상태코드(3)
			resBuf.writeBytes("G".getBytes()); // 송수신FLAG(1)
			ByteBufUtils.writeRightPaddingString(resBuf, statusCode, 3); // 응답코드(3)
			String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
			ByteBufUtils.writeRightPaddingString(resBuf, formattedTime, 12); // 전송일시(12)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(reqBuf, 35, 12).getBytes()); // 센터전문관리번호(12)
			ByteBufUtils.writeRightPaddingString(resBuf, policeTransactionId, 12); // 이용기관전문관리번호(12)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(reqBuf, 59, 2).getBytes()); // 이용기관발행기관분류코드(2)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(reqBuf, 61, 7).getBytes()); // 이용기관지로번호(7)
			ByteBufUtils.writeRightPaddingString(resBuf, "", 2); // 필러(2)
		} catch (Exception e) {
			log.warn("make response bytebuf error -> [{}]", e.getMessage());
			ByteBufUtils.writeRightPaddingString(resBuf, "", (HEADER_LENGTH - 4 - resBuf.readableBytes()));
		}

		return resBuf;
	}

	// byteBuf 사용, 전체 헤더 길이 74 bytes
	public static ByteBuf responseHeader(ByteBuf headerBuf, String messageTypeCode, String statusCode,
			String policeTransactionId) {
		ByteBuf resBuf = Unpooled.buffer(HEADER_LENGTH - 4);
		try {
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(headerBuf, 0, 3).getBytes()); // 업무구분(3)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(headerBuf, 3, 3).getBytes()); // 기관코드(3)
			ByteBufUtils.writeRightPaddingString(resBuf, messageTypeCode, 4); // 전문종별코드(4)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(headerBuf, 10, 6).getBytes()); // 거래구분코드(6)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(headerBuf, 16, 3).getBytes()); // 상태코드(3)
			resBuf.writeBytes("G".getBytes()); // 송수신FLAG(1)
			ByteBufUtils.writeRightPaddingString(resBuf, statusCode, 3); // 응답코드(3)
//			String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
//			ByteBufUtils.writeRightPaddingString(resBuf, formattedTime, 12); // 전송일시(12)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(headerBuf, 23, 12).getBytes()); // 전송일시(12)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(headerBuf, 35, 12).getBytes()); // 센터전문관리번호(12)
			ByteBufUtils.writeRightPaddingString(resBuf, policeTransactionId, 12); // 이용기관전문관리번호(12)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(headerBuf, 59, 2).getBytes()); // 이용기관발행기관분류코드(2)
			resBuf.writeBytes(ByteBufUtils.getStringfromBytebuf(headerBuf, 61, 7).getBytes()); // 이용기관지로번호(7)
			ByteBufUtils.writeRightPaddingString(resBuf, "", 2); // 필러(2)
		} catch (Exception e) {
			log.warn("make response bytebuf error -> [{}]", e.getMessage(), e);
			ByteBufUtils.writeRightPaddingString(resBuf, "", (HEADER_LENGTH - 4 - resBuf.readableBytes()));
		}
		return resBuf;
	}
}
