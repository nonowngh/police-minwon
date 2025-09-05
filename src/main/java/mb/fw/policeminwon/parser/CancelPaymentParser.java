package mb.fw.policeminwon.parser;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import mb.fw.policeminwon.entity.CancelPaymentEntity;

public class CancelPaymentParser {
    public static CancelPaymentEntity toEntity(String data) {
    	CancelPaymentEntity msg = new CancelPaymentEntity();
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
        msg.setBankBranchCode(data.substring(pos, pos += 7));         // 출금 금융회사 점별 코드 (N, 7)
        msg.setPayerRegNo(data.substring(pos, pos += 13));            // 납부자 주민(사업자) 등록번호 (AN, 13)
        msg.setOriginCenterMsgNo(data.substring(pos, pos += 12));     // 원거래 센터 전문 관리 번호 (AN, 12)
        msg.setOriginSendDateTime(data.substring(pos, pos += 12));    // 원거래 전송 일시 (N, 12)
        msg.setReserveField1(data.substring(pos, pos += 16));         // 예비 정보 FIELD 1 (AN, 16)
        msg.setOriginPayAmount(data.substring(pos, pos += 15));       // 원거래 납부 금액 (N, 15)
        msg.setCancelReason(data.substring(pos, pos += 1));           // 취소 사유 (AN, 1)
        msg.setOriginPayType(data.substring(pos, pos += 1));          // 원거래 납부 형태 구분 (AN, 1)
        msg.setReserveField2(data.substring(pos, pos += 9));          // 예비 정보 FIELD 2 (AN, 9)
        return msg;
    }
    
    public static String makeResponeMessage(CancelPaymentEntity req, String resultCode) {
    	//TODO 응답 메시지 생성 하는 부분 - request 에 추가 하는 개념
    	req.setMsgType("0430");
    	req.setRespCode(resultCode);
    	req.setFlag("G");                           // 송수신 FLAG
    	req.setSendTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmm"));
    	req.setOrgTypeCode(req.getOrgTypeCode());   //TODO 이용기관 발행기관 분류코드 생성규칙 알아야함. 
    	String responseMsg = req.toFixedLengthString(req);
    	return responseMsg;
    }
}