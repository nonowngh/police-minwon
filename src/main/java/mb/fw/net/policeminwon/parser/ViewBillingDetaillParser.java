package mb.fw.net.policeminwon.parser;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import mb.fw.net.policeminwon.entity.ViewBillingDetaillEntity;

/**
 * 경찰청 범칙금 - 과태료 고지내역 상세 조회
 */
public class ViewBillingDetaillParser {
	public static ViewBillingDetaillEntity toEntity(String data) {
		ViewBillingDetaillEntity msg = new ViewBillingDetaillEntity();
        int pos = 0;
        msg.setMsgLength(data.substring(pos, pos += 4));              // 전문 길이
        msg.setJobType(data.substring(pos, pos += 3));                // 업무 구분
        msg.setOrgCode(data.substring(pos, pos += 3));                // 기관 코드
        msg.setMsgType(data.substring(pos, pos += 4));                // 전문 종별 코드
        msg.setTrCode(data.substring(pos, pos += 6));                 // 거래 구분 코드
        msg.setStatusCode(data.substring(pos, pos += 3));             // 상태 코드
        msg.setFlag(data.substring(pos, pos += 1));                   // 송수신 FLAG
        msg.setRespCode(data.substring(pos, pos += 3));               // 응답 코드
        msg.setSendTime(data.substring(pos, pos += 12));              // 전송 일시
        msg.setCenterMsgNo(data.substring(pos, pos += 12));           // 센터 전문 관리 번호
        msg.setOrgMsgNo(data.substring(pos, pos += 12));              // 이용기관 전문 관리 번호
        msg.setOrgTypeCode(data.substring(pos, pos += 2));            // 이용기관 발행기관 분류코드
        msg.setOrgGiroNo(data.substring(pos, pos += 7));              // 이용기관 지로번호
        msg.setFiller(data.substring(pos, pos += 2));                 // 여분 필드
        msg.setElecPayNo(data.substring(pos, pos += 19));             // 전자납부번호 (AN, 19)
        msg.setReserveField1(data.substring(pos, pos += 20));         // 예비 정보 FIELD 1 (AN, 20)
        msg.setRandomVal(data.substring(pos, pos += 32));             // 난수 (AN, 32)
        msg.setReserveField2(data.substring(pos, pos += 32));         // 예비 정보 FIELD 2 (AN, 32)
        msg.setMemberType(data.substring(pos, pos += 1));             // (회원정보연계) 회원 유형 (AN, 1)
        msg.setMemberRegNo(data.substring(pos, pos += 13));           // (회원정보연계) 회원 주민등록번호 (AN, 13)
        msg.setMemberBizNo(data.substring(pos, pos += 10));           // (회원정보연계) 회원 사업자등록번호 (AN, 10)
        msg.setReserveField3(data.substring(pos, pos += 3));          // 예비 정보 FIELD 3 (AN, 3)
        msg.setMemberName(data.substring(pos, pos += 40));            // (회원정보연계) 회원명 (AHNS, 40)
        msg.setReserveField4(data.substring(pos, pos += 10));         // 예비 정보 FIELD 4 (AN, 10)
        msg.setObligorRegNo(data.substring(pos, pos += 13));          // 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
        msg.setPayerNo(data.substring(pos, pos += 15));               // 납부자(고지서) 번호 (AN, 15)
        msg.setFeeType(data.substring(pos, pos += 1));                // 과금 종류 (N, 1)
        msg.setCollectorName(data.substring(pos, pos += 20));         // 징수 기관명 (AHN, 20)
        msg.setCollectorAccountNo(data.substring(pos, pos += 6));     // 징수관 계좌번호 (AN, 6)
        msg.setSubAccount(data.substring(pos, pos += 1));             // 소계정 (N, 1)
        msg.setPayAmountInDue(data.substring(pos, pos += 15));        // 납기내 금액 (N, 15)
        msg.setPayAmountAfterDue(data.substring(pos, pos += 15));     // 납기후 금액 (N, 15)
        msg.setItemCode(data.substring(pos, pos += 7));               // 징수 과목 코드(세목 코드) (N, 7)
        msg.setFiscalYear(data.substring(pos, pos += 4));             // 징수 결의 회계 년도 (N, 4)
        msg.setPayDueDateIn(data.substring(pos, pos += 8));           // 납기일(납기내) (N, 8)
        msg.setPayDueDateAfter(data.substring(pos, pos += 8));        // 납기일(납기후) (N, 8)
        msg.setTaxReasonDate(data.substring(pos, pos += 14));         // 과세 원인 일시 (N, 14)
        msg.setViolationDate(data.substring(pos, pos += 14));         // 위반 일시 (N, 14)
        msg.setViolationLocation(data.substring(pos, pos += 40));     // 위반 장소 (AHNS, 40)
        msg.setViolationContent(data.substring(pos, pos += 100));     // 위반 내용 (AHNS, 100)
        msg.setViolationCarNo(data.substring(pos, pos += 20));        // 위반차량 번호 (AHNS, 20)
        msg.setLawBasis(data.substring(pos, pos += 100));             // 법령 근거 (AHNS, 100)
        msg.setReserveField5(data.substring(pos, pos += 7));          // 예비 정보 FIELD 5 (AN, 7)
        msg.setPayDate(data.substring(pos, pos += 14));               // 납부 일시 (N, 14)
        msg.setAfterDueType(data.substring(pos, pos += 1));           // 납기 내후 구분 (AN, 1)
        msg.setObligorName(data.substring(pos, pos += 8));            // 납부의무자 성명 (AN, 8)
        msg.setCardPayYn(data.substring(pos, pos += 1));              // 신용카드 납부 제하 여부 (AN, 1)
        msg.setReserveField6(data.substring(pos, pos += 18));         // 예비 정보 FIELD 6 (AN, 18)
        return msg;
    }
	
    public static String makeResponeMessage(ViewBillingDetaillEntity req, ViewBillingDetaillEntity res, String resultCode)  throws Exception {
    	//응답 메시지 생성 하는 부분 - res(조회 결과) 헤더 정보 추가 하는 개념
    	res.setMsgLength(req.getMsgLength());       // 전문 길이
    	res.setJobType(req.getJobType());           // 업무 구분
    	res.setOrgCode(req.getOrgCode());           // 기관 코드
    	res.setMsgType("0210");                     // 전문 종별 코드
    	res.setTrCode(req.getTrCode());             // 거래 구분 코드
    	res.setStatusCode(req.getStatusCode());     // 상태 코드
    	res.setFlag("G");                           // 송수신 FLAG
    	res.setRespCode(resultCode);                // 응답 코드
    	res.setSendTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmm")); // 전송 일시
    	res.setCenterMsgNo(req.getCenterMsgNo());   // 센터 전문 관리 번호
    	res.setOrgMsgNo(req.getOrgMsgNo());         // 이용기관 전문 관리 번호
    	res.setOrgTypeCode(req.getOrgTypeCode());   //TODO 이용기관 발행기관 분류코드 생성규칙 알아야함. 
    	res.setOrgGiroNo(req.getOrgGiroNo());       // 이용기관 지로번호
    	res.setFiller(req.getFiller());             // 여분 필드
    	String responseMsg = req.toFixedLengthString(res);
    	return responseMsg;
    }
}