package mb.fw.net.policeminwon.entity;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.client.utils.DateUtils;

import lombok.Data;

/**
 * 경찰청 범칙금 - 과태료 고지내역 상세 조회
 */
@Data
public class ViewBillingDetaillEntity {
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
    private String elecPayNo;            // 전자납부번호 (AN, 19)
    private String reserveField1;        // 예비 정보 FIELD 1 (AN, 20)
    private String randomVal;            // 난수 (AN, 32)
    private String reserveField2;        // 예비 정보 FIELD 2 (AN, 32)
    private String memberType;           // (회원정보연계) 회원 유형 (AN, 1)
    private String memberRegNo;          // (회원정보연계) 회원 주민등록번호 (AN, 13)
    private String memberBizNo;          // (회원정보연계) 회원 사업자등록번호 (AN, 10)
    private String reserveField3;        // 예비 정보 FIELD 3 (AN, 3)
    private String memberName;           // (회원정보연계) 회원명 (AHNS, 40)
    private String reserveField4;        // 예비 정보 FIELD 4 (AN, 10)
    private String obligorRegNo;         // 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
    private String payerNo;              // 납부자(고지서) 번호 (AN, 15)
    private String feeType;              // 과금 종류 (N, 1)
    private String collectorName;        // 징수 기관명 (AHN, 20)
    private String collectorAccountNo;   // 징수관 계좌번호 (AN, 6)
    private String subAccount;           // 소계정 (N, 1)
    private String payAmountInDue;       // 납기내 금액 (N, 15)
    private String payAmountAfterDue;    // 납기후 금액 (N, 15)
    private String itemCode;             // 징수 과목 코드(세목 코드) (N, 7)
    private String fiscalYear;           // 징수 결의 회계 년도 (N, 4)
    private String payDueDateIn;         // 납기일(납기내) (N, 8)
    private String payDueDateAfter;      // 납기일(납기후) (N, 8)
    private String taxReasonDate;        // 과세 원인 일시 (N, 14)
    private String violationDate;        // 위반 일시 (N, 14)
    private String violationLocation;    // 위반 장소 (AHNS, 40)
    private String violationContent;     // 위반 내용 (AHNS, 100)
    private String violationCarNo;       // 위반차량 번호 (AHNS, 20)
    private String lawBasis;             // 법령 근거 (AHNS, 100)
    private String reserveField5;        // 예비 정보 FIELD 5 (AN, 7)
    private String payDate;              // 납부 일시 (N, 14)
    private String afterDueType;         // 납기 내후 구분 (AN, 1)
    private String obligorName;          // 납부의무자 성명 (AN, 8)
    private String cardPayYn;            // 신용카드 납부 제하 여부 (AN, 1)
    private String reserveField6;        // 예비 정보 FIELD 6 (AN, 18)

    public String toFixedLengthString(ViewBillingDetaillEntity req) throws Exception{
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
        sb.append(padRight(req.getElecPayNo(), 19, ' '));           // 전자납부번호 (AN, 19)
        sb.append(padRight(req.getReserveField1(), 20, ' '));       // 예비 정보 FIELD 1 (AN, 20)
        sb.append(padRight(req.getRandomVal(), 32, ' '));           // 난수 (AN, 32)
        sb.append(padRight(req.getReserveField2(), 32, ' '));       // 예비 정보 FIELD 2 (AN, 32)
        sb.append(padRight(req.getMemberType(), 1, ' '));           // (회원정보연계) 회원 유형 (AN, 1)
        sb.append(padRight(req.getMemberRegNo(), 13, ' '));         // (회원정보연계) 회원 주민등록번호 (AN, 13)
        sb.append(padRight(req.getMemberBizNo(), 10, ' '));         // (회원정보연계) 회원 사업자등록번호 (AN, 10)
        sb.append(padRight(req.getReserveField3(), 3, ' '));        // 예비 정보 FIELD 3 (AN, 3)
        sb.append(new String(padRightEucKrSpace(req.getMemberName(), 40), "EUC-KR"));          // (회원정보연계) 회원명 (AHNS, 40)
        sb.append(padRight(req.getReserveField4(), 10, ' '));       // 예비 정보 FIELD 4 (AN, 10)
        sb.append(padRight(req.getObligorRegNo(), 13, ' '));        // 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
        sb.append(padRight(req.getPayerNo(), 15, ' '));             // 납부자(고지서) 번호 (AN, 15)
        sb.append(padLeft(req.getFeeType(), 1, '0'));               // 과금 종류 (N, 1)
        sb.append(new String(padRightEucKrSpace(req.getCollectorName(), 20), "EUC-KR"));       // 징수 기관명 (AHN, 20)
        sb.append(padRight(req.getCollectorAccountNo(), 6, ' '));   // 징수관 계좌번호 (AN, 6)
        sb.append(padLeft(req.getSubAccount(), 1, '0'));            // 소계정 (N, 1)
        sb.append(padLeft(req.getPayAmountInDue(), 15, '0'));       // 납기내 금액 (N, 15)
        sb.append(padLeft(req.getPayAmountAfterDue(), 15, '0'));    // 납기후 금액 (N, 15)
        sb.append(padLeft(req.getItemCode(), 7, '0'));              // 징수 과목 코드(세목 코드) (N, 7)
        sb.append(padLeft(req.getFiscalYear(), 4, '0'));            // 징수 결의 회계 년도 (N, 4)
        sb.append(padLeft(req.getPayDueDateIn(), 8, '0'));          // 납기일(납기내) (N, 8)
        sb.append(padLeft(req.getPayDueDateAfter(), 8, '0'));       // 납기일(납기후) (N, 8)
        sb.append(padLeft(req.getTaxReasonDate(), 14, '0'));        // 과세 원인 일시 (N, 14)
        sb.append(padLeft(req.getViolationDate(), 14, '0'));        // 위반 일시 (N, 14)
        sb.append(new String(padRightEucKrSpace(req.getViolationLocation(), 40), "EUC-KR"));   // 위반 장소 (AHNS, 40)
        sb.append(new String(padRightEucKrSpace(req.getViolationContent(), 100), "EUC-KR"));   // 위반 내용 (AHNS, 100)
        sb.append(new String(padRightEucKrSpace(req.getViolationCarNo(), 20), "EUC-KR"));      // 위반차량 번호 (AHNS, 20)
        sb.append(new String(padRightEucKrSpace(req.getLawBasis(), 100), "EUC-KR"));           // 법령 근거 (AHNS, 100)
        sb.append(padRight(req.getReserveField5(), 7, ' '));        // 예비 정보 FIELD 5 (AN, 7)
        sb.append(padLeft(req.getPayDate(), 14, '0'));              // 납부 일시 (N, 14)
        sb.append(padRight(req.getAfterDueType(), 1, ' '));         // 납기 내후 구분 (AN, 1)
        sb.append(padRight(req.getObligorName(), 8, ' '));          // 납부의무자 성명 (AN, 8)
        sb.append(padRight(req.getCardPayYn(), 1, ' '));            // 신용카드 납부 제하 여부 (AN, 1)
        sb.append(padRight(req.getReserveField6(), 18, ' '));       // 예비 정보 FIELD 6 (AN, 18)
        return sb.toString();
    }

    private static String padLeft(String s, int n, char c) {
        if (s == null) s = "";
        if (s.length() > n) s = s.substring(0, n);
        return String.format("%1$" + n + "s", s).replace(' ', c);
    }
    private static String padRight(String s, int n, char c) {
        if (s == null) s = "";
        if (s.getBytes().length > n) s = s.substring(0, n);
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
    
    public static void main(String[] args) throws Exception {
    	ViewBillingDetaillEntity test = new ViewBillingDetaillEntity();
    	String result = new String(padRightEucKrSpace("김민수", 20), "EUC-KR");
    	System.out.println(result);
    	System.out.println(result.getBytes("EUC-KR").length);
    	System.out.println(DateFormatUtils.format(new Date(), "yyyyMMddHHmm"));
	}
}
