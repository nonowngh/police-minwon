package mb.fw.net.policeminwon.entity;

import java.io.ByteArrayOutputStream;

import lombok.Data;

@Data
public class CancelPaymentEntity {
	//헤더
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
    //데이터
	private String bankBranchCode;      // 출금 금융회사 점별 코드 (N, 7)
    private String payerRegNo;          // 납부자 주민(사업자) 등록번호 (AN, 13)
    private String originCenterMsgNo;   // 원거래 센터 전문 관리 번호 (AN, 12)
    private String originSendDateTime;  // 원거래 전송 일시 (N, 12)
    private String reserveField1;       // 예비 정보 FIELD 1 (AN, 16)
    private String originPayAmount;     // 원거래 납부 금액 (N, 15)
    private String cancelReason;        // 취소 사유 (AN, 1)
    private String originPayType;       // 원거래 납부 형태 구분 (AN, 1)
    private String reserveField2;       // 예비 정보 FIELD 2 (AN, 9)

    public String toFixedLengthString(CancelPaymentEntity req) {
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
        sb.append(padLeft(req.getBankBranchCode(), 7, '0'));        // 출금 금융회사 점별 코드 (N, 7)
        sb.append(padRight(req.getPayerRegNo(), 13, ' '));          // 납부자 주민(사업자) 등록번호 (AN, 13)
        sb.append(padRight(req.getOriginCenterMsgNo(), 12, ' '));   // 원거래 센터 전문 관리 번호 (AN, 12)
        sb.append(padLeft(req.getOriginSendDateTime(), 12, '0'));   // 원거래 전송 일시 (N, 12)
        sb.append(padRight(req.getReserveField1(), 16, ' '));       // 예비 정보 FIELD 1 (AN, 16)
        sb.append(padLeft(req.getOriginPayAmount(), 15, '0'));      // 원거래 납부 금액 (N, 15)
        sb.append(padRight(req.getCancelReason(), 1, ' '));         // 취소 사유 (AN, 1)
        sb.append(padRight(req.getOriginPayType(), 1, ' '));        // 원거래 납부 형태 구분 (AN, 1)
        sb.append(padRight(req.getReserveField2(), 9, ' '));        // 예비 정보 FIELD 2 (AN, 9)
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
    
    public static byte[] padRightEucKrSpace(String str, int totalBytes) throws Exception {
        if (str == null) str = "";
        byte[] src = str.getBytes("EUC-KR");
        if (src.length > totalBytes) {
            // 자를 때 한글 깨짐 방지
            int cut = totalBytes;
            while (cut > 0 && (src[cut - 1] & 0x80) != 0) cut--;
            byte[] result = new byte[totalBytes];
            System.arraycopy(src, 0, result, 0, cut);
            // 패딩
            for (int i = cut; i < totalBytes; i += 2) {
                result[i] = (byte)0xA1;
                if (i + 1 < totalBytes) result[i + 1] = (byte)0xA1;
            }
            return result;
        } else if (src.length == totalBytes) {
            return src;
        } else {
            byte[] result = new byte[totalBytes];
            System.arraycopy(src, 0, result, 0, src.length);
            // 패딩
            for (int i = src.length; i < totalBytes; i += 2) {
                result[i] = (byte)0xA1;
                if (i + 1 < totalBytes) result[i + 1] = (byte)0xA1;
            }
            return result;
        }
    }
}
