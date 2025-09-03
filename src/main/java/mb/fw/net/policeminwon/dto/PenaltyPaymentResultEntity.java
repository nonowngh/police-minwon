package mb.fw.net.policeminwon.dto;

import java.io.ByteArrayOutputStream;

import lombok.Data;

/**
 * 경찰청 범칙금 - 과태료 납부결과 통지
 */
@Data
public class PenaltyPaymentResultEntity {
	//헤더
	private String msgLength;            // 전문 길이
    private String jobType;              // 업무 구분
    private String orgCode;              // 기관 코드
    private String msgType;              // 전문 종별 코드
    private String trCode;               // 거래 구분 코드
    private String statusCode;           // 상태 코드
    private String flag;                 // 송수신 FLAG
    private String respCode;             // 응답 코드
    private String sendTime;             // 전송 일시
    private String centerMsgNo;          // 센터 전문 관리 번호
    private String orgMsgNo;             // 이용기관 전문 관리 번호
    private String orgTypeCode;          // 이용기관 발행기관 분류코드
    private String orgGiroNo;            // 이용기관 지로번호
    private String filler;               // 여분 필드
    //데이터
    private String obligorRegNo;         // 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
    private String collectorAccountNo;   // 징수관 계좌번호 (AN, 6)
    private String elecPayNo;            // 전자납부번호 (AN, 19)
    private String reserveField1;        // 예비정보 FIELD 1 (AN, 3)
    private String reserveField2;        // 예비정보 FIELD 2 (AN, 7)
    private String payAmount;            // 납부 금액 (N, 15)
    private String payDate;              // 납부 일자 (N, 8)
    private String bankBranchCode;       // 출금 금융회사 점별 코드 (N, 7)
    private String reserveField3;        // 예비정보 FIELD 3 (AN, 16)
    private String reserveField4;        // 예비정보 FIELD 4 (ANS, 14)
    private String payerRegNo;           // 납부자 주민(사업자) 등록 번호 (AN, 13)
    private String reserveField5;        // 예비정보 FIELD 5 (AHNS, 10)
    private String reserveField6;        // 예비정보 FIELD 6 (AHNS, 10)
    private String paySystem;            // 납부 이용 시스템 (AN, 1)
    private String prePaySystem;         // 기 납부 이용 시스템 (AN, 1)
    private String payType;              // 납부 형태 구분 (AN, 1)
    private String reserveField7;        // 예비정보 FIELD 7 (AN, 9)

    public String toFixedLengthString(PenaltyPaymentResultEntity req) {
    	StringBuilder sb = new StringBuilder();
        sb.append(padLeft(req.getMsgLength(), 4, '0'));             // 전문 길이
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
        sb.append(padRight(req.getObligorRegNo(), 13, ' '));        // 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
        sb.append(padRight(req.getCollectorAccountNo(), 6, ' '));   // 징수관 계좌번호 (AN, 6)
        sb.append(padRight(req.getElecPayNo(), 19, ' '));           // 전자납부번호 (AN, 19)
        sb.append(padRight(req.getReserveField1(), 3, ' '));        // 예비정보 FIELD 1 (AN, 3)
        sb.append(padRight(req.getReserveField2(), 7, ' '));        // 예비정보 FIELD 2 (AN, 7)
        sb.append(padLeft(req.getPayAmount(), 15, '0'));            // 납부 금액 (N, 15)
        sb.append(padLeft(req.getPayDate(), 8, '0'));               // 납부 일자 (N, 8)
        sb.append(padLeft(req.getBankBranchCode(), 7, '0'));        // 출금 금융회사 점별 코드 (N, 7)
        sb.append(padRight(req.getReserveField3(), 16, ' '));       // 예비정보 FIELD 3 (AN, 16)
        sb.append(padRight(req.getReserveField4(), 14, ' '));       // 예비정보 FIELD 4 (ANS, 14)
        sb.append(padRight(req.getPayerRegNo(), 13, ' '));          // 납부자 주민(사업자) 등록 번호 (AN, 13)
        sb.append(padRight(req.getReserveField5(), 10, ' '));       // 예비정보 FIELD 5 (AHNS, 10)
        sb.append(padRight(req.getReserveField6(), 10, ' '));       // 예비정보 FIELD 6 (AHNS, 10)
        sb.append(padRight(req.getPaySystem(), 1, ' '));            // 납부 이용 시스템 (AN, 1)
        sb.append(padRight(req.getPrePaySystem(), 1, ' '));         // 기 납부 이용 시스템 (AN, 1)
        sb.append(padRight(req.getPayType(), 1, ' '));              // 납부 형태 구분 (AN, 1)
        sb.append(padRight(req.getReserveField7(), 9, ' '));        // 예비정보 FIELD 7 (AN, 9)
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
