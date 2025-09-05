package mb.fw.policeminwon.parser;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import mb.fw.policeminwon.entity.PaymentResultNotificationEntity;

/**
 * 경찰청 범칙금 - 과태료 납부결과 통지
 */
public class PaymentResultParser {
	
	public static PaymentResultNotificationEntity toEntity(String data) {
		PaymentResultNotificationEntity msg = new PaymentResultNotificationEntity();
        int pos = 0;
        msg.setMsgLength(data.substring(pos, pos += 4));             // 전문 길이
        msg.setJobType(data.substring(pos, pos += 3));               // 업무 구분
        msg.setOrgCode(data.substring(pos, pos += 3));               // 기관 코드
        msg.setMsgType(data.substring(pos, pos += 4));               // 전문 종별 코드
        msg.setTrCode(data.substring(pos, pos += 6));                // 거래 구분 코드
        msg.setStatusCode(data.substring(pos, pos += 3));            // 상태 코드
        msg.setFlag(data.substring(pos, pos += 1));                  // 송수신 FLAG
        msg.setRespCode(data.substring(pos, pos += 3));              // 응답 코드
        msg.setSendTime(data.substring(pos, pos += 12));             // 전송 일시
        msg.setCenterMsgNo(data.substring(pos, pos += 12));          // 센터 전문 관리 번호
        msg.setOrgMsgNo(data.substring(pos, pos += 12));             // 이용기관 전문 관리 번호
        msg.setOrgTypeCode(data.substring(pos, pos += 2));           // 이용기관 발행기관 분류코드
        msg.setOrgGiroNo(data.substring(pos, pos += 7));             // 이용기관 지로번호
        msg.setFiller(data.substring(pos, pos += 2));                // 여분 필드
        msg.setObligorRegNo(data.substring(pos, pos += 13));         // 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
        msg.setCollectorAccountNo(data.substring(pos, pos += 6));    // 징수관 계좌번호 (AN, 6)
        msg.setElecPayNo(data.substring(pos, pos += 19));            // 전자납부번호 (AN, 19)
        msg.setReserveField1(data.substring(pos, pos += 3));         // 예비정보 FIELD 1 (AN, 3)
        msg.setReserveField2(data.substring(pos, pos += 7));         // 예비정보 FIELD 2 (AN, 7)
        msg.setPayAmount(data.substring(pos, pos += 15));            // 납부 금액 (N, 15)
        msg.setPayDate(data.substring(pos, pos += 8));               // 납부 일자 (N, 8)
        msg.setBankBranchCode(data.substring(pos, pos += 7));        // 출금 금융회사 점별 코드 (N, 7)
        msg.setReserveField3(data.substring(pos, pos += 16));        // 예비정보 FIELD 3 (AN, 16)
        msg.setReserveField4(data.substring(pos, pos += 14));        // 예비정보 FIELD 4 (ANS, 14)
        msg.setPayerRegNo(data.substring(pos, pos += 13));           // 납부자 주민(사업자) 등록 번호 (AN, 13)
        msg.setReserveField5(data.substring(pos, pos += 10));        // 예비정보 FIELD 5 (AHNS, 10)
        msg.setReserveField6(data.substring(pos, pos += 10));        // 예비정보 FIELD 6 (AHNS, 10)
        msg.setPaySystem(data.substring(pos, pos += 1));             // 납부 이용 시스템 (AN, 1)
        msg.setPrePaySystem(data.substring(pos, pos += 1));          // 기 납부 이용 시스템 (AN, 1)
        msg.setPayType(data.substring(pos, pos += 1));               // 납부 형태 구분 (AN, 1)
        msg.setReserveField7(data.substring(pos, pos += 9));         // 예비정보 FIELD 7 (AN, 9)
        return msg;
    }
	
    public static String makeResponeMessage(PaymentResultNotificationEntity req, String resultCode) {
    	//TODO 응답 메시지 생성 하는 부분 - request 에 추가 하는 개념
    	req.setMsgType("0210");
    	req.setRespCode(resultCode);
    	req.setFlag("G");  
    	req.setSendTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmm"));
    	req.setOrgTypeCode(req.getOrgTypeCode());   //TODO 이용기관 발행기관 분류코드 생성규칙 알아야함. 
    	String responseMsg = req.toFixedLengthString(req);
    	return responseMsg;
    }
}