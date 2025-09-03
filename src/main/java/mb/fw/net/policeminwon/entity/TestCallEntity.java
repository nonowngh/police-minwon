package mb.fw.net.policeminwon.entity;

import java.io.ByteArrayOutputStream;

import lombok.Data;

/**
 * 경찰청 범칙금 - TestCall
 */
@Data
public class TestCallEntity {
	private String msgLength;           // 전문 길이
    private String jobType;             // 업무 구분
    private String orgCode;             // 기관 코드
    private String msgType;             // 전문 종별 코드
    private String trCode;              // 거래 구분 코드
    private String statusCode;          // 상태 코드
    private String flag;                // 송수신 FLAG
    private String respCode;            // 응답 코드
    private String sendTime;            // 전송 일시
    private String centerMsgNo;         // 센터 전문 관리 번호
    private String orgMsgNo;            // 이용기관 전문 관리 번호
    private String orgTypeCode;         // 이용기관 발행기관 분류코드
    private String orgGiroNo;           // 이용기관 지로번호
    private String filler;              // 여분 필드

    public String toFixedLengthString(TestCallEntity req) {
        StringBuilder sb = new StringBuilder();
//        sb.append(padLeft(req.getMsgLength(), 4, '0'));             // 전문 길이
        sb.append(padRight(req.getJobType(), 3, ' '));              // 업무 구분
        sb.append(padLeft(req.getOrgCode(), 3, '0'));               // 기관 코드
        sb.append(padLeft(req.getMsgType(), 4, '0'));               // 전문 종별 코드
        sb.append(padLeft(req.getTrCode(), 6, '0'));                // 거래 구분 코드
        sb.append(padLeft(req.getStatusCode(), 3, '0'));            // 상태 코드
        sb.append(padRight(req.getFlag(), 1, ' '));                 // 송수신 FLAG
        sb.append(padRight(req.getRespCode(), 3, ' '));             // 응답 코드
        sb.append(padLeft(req.getSendTime(), 12, '0'));             // 전송 일시
        sb.append(padRight(req.getCenterMsgNo(), 12, ' '));         // 센터 전문 관리 번호
        sb.append(padRight(req.getOrgMsgNo(), 12, ' '));            // 이용기관 전문 관리 번호
        sb.append(padLeft(req.getOrgTypeCode(), 2, '0'));           // 이용기관 발행기관 분류코드
        sb.append(padLeft(req.getOrgGiroNo(), 7, '0'));             // 이용기관 지로번호
        sb.append(padLeft(req.getFiller(), 2, '0'));                // 여분 필드
        return sb.toString();
    }
    private static String padLeft(String s, int n, char c) {
        if (s == null) s = "";
        return String.format("%1$" + n + "s", s).replace(' ', c);
    }
    private static String padRight(String s, int n, char c) {
        if (s == null) s = "";
        return String.format("%1$-" + n + "s", s).replace(' ', c);
    }
    
    public byte[] spaceToEucKrDoubleSpace(String str) throws Exception {
	    if (str == null) return new byte[0];
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    for (char c : str.toCharArray()) {
	        if (c == ' ') {
	            baos.write(0xA1);
	            baos.write(0xA1);
	        } else {
	            byte[] chBytes = String.valueOf(c).getBytes("EUC-KR");
	            baos.write(chBytes);
	        }
	    }
	    return baos.toByteArray();
	}
}
