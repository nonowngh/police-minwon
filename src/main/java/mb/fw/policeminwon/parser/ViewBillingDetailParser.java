package mb.fw.policeminwon.parser;

import org.apache.commons.lang3.math.NumberUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.constants.TcpCommonSettingConstants;
import mb.fw.policeminwon.dto.ViewBillingDetailBody;
import mb.fw.policeminwon.utils.ByteBufUtils;

/**
 * 경찰청 범칙금 - 과태료 고지내역 상세 조회
 */
@Slf4j
public class ViewBillingDetailParser {
	public static ViewBillingDetailBody toEntity(String data) {
		ByteBuf buf = Unpooled.copiedBuffer(data, TcpCommonSettingConstants.MESSAGE_CHARSET);
		ViewBillingDetailBody entity = new ViewBillingDetailBody();
		int offset = 0;
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setEltrPymNo, buf, offset, 19); // 전자납부번호 (AN, 19)
		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 20); // 예비 정보 FIELD 1 (AN, 20)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setRandNo, buf, offset, 32); // 난수 (AN, 32)
//		try {
//		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 32); // 예비 정보 FIELD 2 (AN, 32)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setUserType, buf, offset, 1); // (회원정보연계) 회원 유형 (AN, 1)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setUserRrno, buf, offset, 13); // (회원정보연계) 회원 주민등록번호 (AN, 13)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setUserBznNo, buf, offset, 10); // (회원정보연계) 회원 사업자등록번호 (AN, 10)
//		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 3); // 예비 정보 FIELD 3 (AN, 3)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setUserFulnm, buf, offset, 40); // (회원정보연계) 회원명 (AHNS, 40)
//		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 10); // 예비 정보 FIELD 4 (AN, 10)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setUserRrno2, buf, offset, 13);// 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setWrintNo, buf, offset, 15); // 납부자(고지서) 번호 (AN, 15)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setFineType, buf, offset, 1); // 과금 종류 (N, 1)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setJrdtPlstNm, buf, offset, 20);// 징수 기관명 (AHN, 20)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPcptaxColctrAcno, buf, offset, 6); // 징수관 계좌번호 (AN, 6)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPcptaxColctrSmallAcnutCd, buf, offset, 1); // 소계정 (N, 1)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setBtddtAmt, buf, offset, 15); // 납기내 금액 (N, 15)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setAftdteAmt, buf, offset, 15); // 납기후 금액 (N, 15)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setIncmeMokCd, buf, offset, 7); // 징수 과목 코드(세목 코드) (N, 7)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setAccnutYr, buf, offset, 4); // 징수 결의 회계 년도 (N, 4)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setBtddtPymTmlmtDt, buf, offset, 8); // 납기일(납기내) (N, 8)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setAftdtePymYmlmtDt, buf, offset, 8); // 납기일(납기후) (N, 8)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setFineCauseDate, buf, offset, 14); // 과세 원인 일시 (N, 14)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setViolDate, buf, offset, 14); // 위반 일시 (N, 14)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setViolPlace, buf, offset, 40); // 위반 장소 (AHNS, 40)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setViolDtl, buf, offset, 100); // 위반 내용 (AHNS, 100)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setViolCarNo, buf, offset, 20); // 위반차량 번호 (AHNS, 20)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setLawNm, buf, offset, 100); // 법령 근거 (AHNS, 100)
//		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 7); // 예비 정보 FIELD 5 (AN, 7)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayDt, buf, offset, 14); // 납부 일시 (N, 14)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setDedtClCd, buf, offset, 1); // 납기 내후 구분 (AN, 1)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayerRealFulnm, buf, offset, 8); // 납부의무자 성명 (AN, 8)
//		offset = ByteBufUtils.setStringAndMoveOffset(entity::setCardPymPsbyYn, buf, offset, 1); // 신용카드 납부 제하 여부 (AN, 1)
//		offset = ByteBufUtils.skipAndMoveOffset(buf, offset, 18); // 예비 정보 FIELD 6 (AN, 18)
//		}catch(IndexOutOfBoundsException e) {
//			log.warn("byte index error...rest field skip -> " + e.getMessage());
//		}
		return entity;
	}

	public static String toMessage(ViewBillingDetailBody entity) {
		ByteBuf buf = Unpooled.buffer();
		ByteBufUtils.writeRightPaddingString(buf, entity.getEltrPymNo(), 19, false);
		ByteBufUtils.writeRightPaddingString(buf, "", 20, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getRandNo(), 32, false);
		ByteBufUtils.writeRightPaddingString(buf, "", 32, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getUserType(), 1, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getUserRrno(), 13, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getUserBznNo(), 10, false);
		ByteBufUtils.writeRightPaddingString(buf, "", 3, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getUserFulnm(), 40, false);
		ByteBufUtils.writeRightPaddingString(buf, "", 10, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getUserRrno2(), 13, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getWrintNo(), 15, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toInt(entity.getFineType(), 0), 1, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getJrdtPlstNm(), 20, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getPcptaxColctrAcno(), 6, false);
		ByteBufUtils.writeLeftPaddingNumber(buf,  NumberUtils.toInt(entity.getPcptaxColctrSmallAcnutCd(), 0), 1, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toLong(entity.getBtddtAmt(), 0), 15, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toLong(entity.getAftdteAmt(), 0), 15, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toInt(entity.getIncmeMokCd(), 0), 7, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toInt(entity.getAccnutYr(), 0), 4, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toInt(entity.getBtddtPymTmlmtDt(), 0), 8, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toInt(entity.getAftdtePymYmlmtDt(), 0), 8, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toLong(entity.getFineCauseDate(), 0), 14, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toLong(entity.getViolDate(), 0), 14, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getViolPlace(), 40, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getViolDtl(), 100, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getViolCarNo(), 20, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getLawNm(), 100, false);
		ByteBufUtils.writeRightPaddingString(buf, "", 7, false);
		ByteBufUtils.writeLeftPaddingNumber(buf, NumberUtils.toLong(entity.getPayDt(), 0), 14, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getDedtClCd(), 1, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getPayerRealFulnm(), 8, false);
		ByteBufUtils.writeRightPaddingString(buf, entity.getCardPymPsbyYn(), 1, false);
		ByteBufUtils.writeRightPaddingString(buf, "", 18, false);
		return buf.toString(TcpCommonSettingConstants.MESSAGE_CHARSET);
	}
}