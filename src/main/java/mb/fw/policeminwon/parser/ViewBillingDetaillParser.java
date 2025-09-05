package mb.fw.policeminwon.parser;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mb.fw.policeminwon.entity.ViewBillingDetaillEntity;
import mb.fw.policeminwon.utils.ByteBufUtils;

/**
 * 경찰청 범칙금 - 과태료 고지내역 상세 조회
 */
public class ViewBillingDetaillParser {
	public static ViewBillingDetaillEntity toEntity(String data) {
		ByteBuf buf = Unpooled.copiedBuffer(data, StandardCharsets.UTF_8);
		ViewBillingDetaillEntity entity = new ViewBillingDetaillEntity();
		int offset = 0;
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setElecPayNo, buf, offset, 19); // 전자납부번호 (AN, 19)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setReserveField1, buf, offset, 20); // 예비 정보 FIELD 1 (AN, 20)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setRandomVal, buf, offset, 32); // 난수 (AN, 32)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setReserveField2, buf, offset, 32); // 예비 정보 FIELD 2 (AN, 32)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setMemberType, buf, offset, 1); // (회원정보연계) 회원 유형 (AN, 1)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setMemberRegNo, buf, offset, 13); // (회원정보연계) 회원 주민등록번호 (AN, 13)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setMemberBizNo, buf, offset, 10); // (회원정보연계) 회원 사업자등록번호 (AN, 10)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setReserveField3, buf, offset, 3); // 예비 정보 FIELD 3 (AN, 3)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setMemberName, buf, offset, 40); // (회원정보연계) 회원명 (AHNS, 40)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setReserveField4, buf, offset, 10); // 예비 정보 FIELD 4 (AN, 10)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setObligorRegNo, buf, offset, 13);// 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayerNo, buf, offset, 15); // 납부자(고지서) 번호 (AN, 15)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setFeeType, buf, offset, 1); // 과금 종류 (N, 1)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setCollectorName, buf, offset, 20);// 징수 기관명 (AHN, 20)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setCollectorAccountNo, buf, offset, 6); // 징수관 계좌번호 (AN, 6)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setSubAccount, buf, offset, 1); // 소계정 (N, 1)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayAmountInDue, buf, offset, 15); // 납기내 금액 (N, 15)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayAmountAfterDue, buf, offset, 15); // 납기후 금액 (N, 15)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setItemCode, buf, offset, 7); // 징수 과목 코드(세목 코드) (N, 7)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setFiscalYear, buf, offset, 4); // 징수 결의 회계 년도 (N, 4)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayDueDateIn, buf, offset, 8); // 납기일(납기내) (N, 8)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayDueDateAfter, buf, offset, 8); // 납기일(납기후) (N, 8)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setTaxReasonDate, buf, offset, 14); // 과세 원인 일시 (N, 14)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setViolationDate, buf, offset, 14); // 위반 일시 (N, 14)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setViolationLocation, buf, offset, 40); // 위반 장소 (AHNS, 40)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setViolationContent, buf, offset, 100); // 위반 내용 (AHNS, 100)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setViolationCarNo, buf, offset, 20); // 위반차량 번호 (AHNS, 20)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setLawBasis, buf, offset, 100); // 법령 근거 (AHNS, 100)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setReserveField5, buf, offset, 7); // 예비 정보 FIELD 5 (AN, 7)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setPayDate, buf, offset, 14); // 납부 일시 (N, 14)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setAfterDueType, buf, offset, 1); // 납기 내후 구분 (AN, 1)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setObligorName, buf, offset, 8); // 납부의무자 성명 (AN, 8)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setCardPayYn, buf, offset, 1); // 신용카드 납부 제하 여부 (AN, 1)
		offset = ByteBufUtils.setStringAndMoveOffset(entity::setReserveField6, buf, offset, 18); // 예비 정보 FIELD 6 (AN, 18)
		return entity;
	}

//	public static String makeResponeMessage(ViewBillingDetaillEntity_bak req, ViewBillingDetaillEntity_bak res,
//			String resultCode) throws Exception {
//		// 응답 메시지 생성 하는 부분 - res(조회 결과) 헤더 정보 추가 하는 개념
//		res.setentityLength(req.getentityLength()); // 전문 길이
//		res.setJobType(req.getJobType()); // 업무 구분
//		res.setOrgCode(req.getOrgCode()); // 기관 코드
//		res.setentityType("0210"); // 전문 종별 코드
//		res.setTrCode(req.getTrCode()); // 거래 구분 코드
//		res.setStatusCode(req.getStatusCode()); // 상태 코드
//		res.setFlag("G"); // 송수신 FLAG
//		res.setRespCode(resultCode); // 응답 코드
//		res.setSendTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmm")); // 전송 일시
//		res.setCenterentityNo(req.getCenterentityNo()); // 센터 전문 관리 번호
//		res.setOrgentityNo(req.getOrgentityNo()); // 이용기관 전문 관리 번호
//		res.setOrgTypeCode(req.getOrgTypeCode()); // TODO 이용기관 발행기관 분류코드 생성규칙 알아야함.
//		res.setOrgGiroNo(req.getOrgGiroNo()); // 이용기관 지로번호
//		res.setFiller(req.getFiller()); // 여분 필드
//		String responseentity = req.toFixedLengthString(res);
//		return responseentity;
//	}

	public static String toMessage(ViewBillingDetaillEntity entity) {
		ByteBuf buf = Unpooled.buffer();		
		ByteBufUtils.writeRightPaddingString(buf, entity.getElecPayNo(), 19);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getReserveField1(), 20);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getRandomVal(), 32);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getReserveField2(), 32);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getMemberType(), 1);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getMemberRegNo(), 13);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getMemberBizNo(), 10);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getReserveField3(), 3);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getMemberName(), 40);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getReserveField4(), 10);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getObligorRegNo(), 13);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getPayerNo(), 15);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getFeeType(), 1);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getCollectorName(), 20);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getCollectorAccountNo(), 6);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getSubAccount(), 1);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getPayAmountInDue(), 15);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getPayAmountAfterDue(), 15);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getItemCode(), 7);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getFiscalYear(), 4);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getPayDueDateIn(), 8);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getPayDueDateAfter(), 8);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getTaxReasonDate(), 14);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getViolationDate(), 14);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getViolationLocation(), 40);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getViolationContent(), 100);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getViolationCarNo(), 20);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getLawBasis(), 100);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getReserveField5(), 7);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getPayDate(), 14);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getAfterDueType(), 1);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getObligorName(), 8);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getCardPayYn(), 1);
	    ByteBufUtils.writeRightPaddingString(buf, entity.getReserveField6(), 18);
	    return buf.toString(StandardCharsets.UTF_8);
	}
}