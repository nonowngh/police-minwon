package mb.fw.policeminwon.parser;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import mb.fw.policeminwon.entity.TestCallEntity;

/**
 * 경찰청 범칙금 - TestCall
 */
public class TestCallParser {
    public static TestCallEntity toEntity(String data) {
    	TestCallEntity msg = new TestCallEntity();
        int pos = 0;
//        msg.setMsgLength(data.substring(pos, pos += 4));              // 전문 길이
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
        return msg;
    }
    
    public static String makeResponeMessage(TestCallEntity req, String resultCode) {
    	req.setMsgType("0810");
    	req.setRespCode(resultCode);
    	req.setFlag("G");  
    	req.setSendTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmm"));
    	req.setOrgTypeCode(req.getOrgTypeCode());   //TODO 이용기관 발행기관 분류코드 생성규칙 알아야함.
    	String responseMsg = req.toFixedLengthString(req);
    	return responseMsg;
    }
}