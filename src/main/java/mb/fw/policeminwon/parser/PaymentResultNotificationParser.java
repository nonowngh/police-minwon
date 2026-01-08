package mb.fw.policeminwon.parser;

import org.apache.commons.lang3.math.NumberUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mb.fw.policeminwon.constants.TcpCommonSettingConstants;
import mb.fw.policeminwon.dto.PaymentResultNotificationBody;
import mb.fw.policeminwon.utils.ByteBufUtils;

/**
 * 경찰청 범칙금 - 과태료 납부결과 통지
 */
public class PaymentResultNotificationParser {
	public static PaymentResultNotificationBody toEntity(String data) {
		ByteBuf buf = Unpooled.copiedBuffer(data, TcpCommonSettingConstants.MESSAGE_CHARSET);
		PaymentResultNotificationBody entity = new PaymentResultNotificationBody();
		int offset = 0;
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayerRrno, buf, offset, 13); // 납부의무자 주민(사업자, 법인) 등록번호
																								// (AN, 13)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPcptaxColctrAcno, buf, offset, 6); // 징수관 계좌번호 (AN, 6)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setEltrPymNo, buf, offset, 19); // 전자납부번호 (AN, 19)
		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 3); // 예비정보 FIELD 1 (AN, 3)
		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 7); // 예비정보 FIELD 2 (AN, 7)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayAmt, buf, offset, 15); // 납부 금액 (N, 15)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayYmd, buf, offset, 8); // 납부 일자 (N, 8)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setRoffFncInstCd, buf, offset, 7); // 출금 금융회사 점별 코드 (N, 7)
		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 16); // 예비정보 FIELD 3 (AN, 16)
		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 14); // 예비정보 FIELD 4 (ANS, 14)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setRealPayerRrno, buf, offset, 13);// 납부자 주민(사업자) 등록 번호
																								// (AN, 13)
		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 10); // 예비정보 FIELD 5 (AHNS, 10)
		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 10); // 예비정보 FIELD 6 (AHNS, 10)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPaySysCd, buf, offset, 1); // 납부 이용 시스템 (AN, 1)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setApaySysCd, buf, offset, 1); // 기 납부 이용 시스템 (AN, 1)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayCd, buf, offset, 1); // 납부 형태 구분 (AN, 1)
		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 9); // 예비정보 FIELD 7 (AN, 9)
		return entity;
	}

	public static String toMessage(PaymentResultNotificationBody entity) {
		ByteBuf buf = Unpooled.buffer();
		ByteBufUtils.writeRightPaddingString(buf, entity.getPayerRrno(), 13, true);
		ByteBufUtils.writeRightPaddingString(buf, entity.getPcptaxColctrAcno(), 6, true);
		ByteBufUtils.writeRightPaddingString(buf, entity.getEltrPymNo(), 19, true);
		ByteBufUtils.writeRightPaddingString(buf, "", 3, true);
		ByteBufUtils.writeRightPaddingString(buf, "", 7, true);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toLong(entity.getPayAmt(), 0), 15, true);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toInt(entity.getPayYmd(), 0), 8, true);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toInt(entity.getRoffFncInstCd(), 0), 7, true);
		ByteBufUtils.writeRightPaddingString(buf, "", 16, true);
		ByteBufUtils.writeRightPaddingString(buf, "", 14, true);
		ByteBufUtils.writeRightPaddingString(buf, entity.getRealPayerRrno(), 13, true);
		ByteBufUtils.writeRightPaddingString(buf, "", 10, true);
		ByteBufUtils.writeRightPaddingString(buf, "", 10, true);
		ByteBufUtils.writeRightPaddingString(buf, entity.getPaySysCd(), 1, true);
		ByteBufUtils.writeRightPaddingString(buf, entity.getApaySysCd(), 1, true);
		ByteBufUtils.writeRightPaddingString(buf, entity.getPayCd(), 1, true);
		ByteBufUtils.writeRightPaddingString(buf, "", 9, true);
		return buf.toString(TcpCommonSettingConstants.MESSAGE_CHARSET);
	}
}