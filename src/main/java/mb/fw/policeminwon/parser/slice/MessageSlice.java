package mb.fw.policeminwon.parser.slice;

import io.netty.buffer.ByteBuf;
import mb.fw.policeminwon.utils.ByteBufUtils;

public class MessageSlice {

//	final static int HEADER_LENGTH = 74;
	final static int HEADER_LENGTH = 70;

//	public static String getTransactionCode(ByteBuf buf) {
//		return ByteBufUtils.getStringfromBytebuf(buf, 14, 6);
//	}
	public static String getTransactionCode(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 10, 6);
	}
	public static String getTransactionCodeWrite(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 14, 6);
	}
	
	public static String getSendTime(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 23, 12);
	}

	public static String getSrFlag(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 19, 1);
	}

	public static String getSrFlagWrite(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 23, 1);
	}

	public static String getCenterTxId(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 35, 12);
	}

	public static String getEsbTxiTail(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 53, 6);
	}
//	public static String getSrFlag(ByteBuf buf) {
//		return ByteBufUtils.getStringfromBytebuf(buf, 23, 1);
//	}

//	public static String getHeaderMessage(ByteBuf buf) {
//		int totalBufSize = buf.readableBytes();
//		if (totalBufSize < HEADER_LENGTH)
//			return ByteBufUtils.getStringfromBytebuf(buf, 4, totalBufSize);
//		else
//			return ByteBufUtils.getStringfromBytebuf(buf, 4, 70);
//	}
	public static String getHeaderMessage(ByteBuf buf) {
		int totalBufSize = buf.readableBytes();
		if (totalBufSize < HEADER_LENGTH)
			return ByteBufUtils.getStringfromBytebuf(buf, 0, totalBufSize);
		else
			return ByteBufUtils.getStringfromBytebuf(buf, 0, 70);
	}

	// 응답코드
	public static String getResponseCode(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, 20, 3);
	}
//	public static String getResponseCode(ByteBuf buf) {
//		return ByteBufUtils.getStringfromBytebuf(buf, 24, 3);
//	}

	// 전자납부번호(body index 0 ~ 19) - 고지내역 조회
	public static String getElecPayNoViewBillingDetail(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, HEADER_LENGTH, 19);
	}

	// 전자납부번호(body index 0 ~ 19) - 고지내역 조회(쓰기용)
	public static String getElecPayNoViewBillingDetailWrite(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, HEADER_LENGTH + 4, 19);
	}

	// 전자납부번호(body index 19 ~ 38) - 통지
	public static String getElecPayNoPaymentResultNotification(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, HEADER_LENGTH + 19, 19);
	}

	// 전자납부번호(body index 19 ~ 38) - 통지(쓰기용)
	public static String getElecPayNoPaymentResultNotificationWrite(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, (HEADER_LENGTH + 4) + 19, 19);
	}
	
	// 취소전문 원거래 센터번호
	public static String getOriginalCenterTxId(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, (HEADER_LENGTH) + 20, 12);
	}

	// 바디 정보(body length 630)
	public static String getVeiwBillingDetailTotalBody(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, HEADER_LENGTH, buf.readableBytes() - HEADER_LENGTH);
	}

	// 바디 정보(body length 153)
	public static String getPaymentResultNotificationTotalBody(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, HEADER_LENGTH, buf.readableBytes() - HEADER_LENGTH);
	}

	// 바디 정보(body length 86)
	public static String getCancelPaymentTotalBody(ByteBuf buf) {
		return ByteBufUtils.getStringfromBytebuf(buf, HEADER_LENGTH, buf.readableBytes() - HEADER_LENGTH);
	}
}
