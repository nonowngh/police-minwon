package mb.fw.policeminwon.entity;

import lombok.Data;

/**
 * 경찰청 범칙금 - 과태료 고지내역 상세 조회
 */
@Data
public class ViewBillingDetaillEntity {

	private String elecPayNo; // 전자납부번호 (AN, 19)
	private String reserveField1; // 예비 정보 FIELD 1 (AN, 20)
	private String randomVal; // 난수 (AN, 32)
	private String reserveField2; // 예비 정보 FIELD 2 (AN, 32)
	private String memberType; // (회원정보연계) 회원 유형 (AN, 1)
	private String memberRegNo; // (회원정보연계) 회원 주민등록번호 (AN, 13)
	private String memberBizNo; // (회원정보연계) 회원 사업자등록번호 (AN, 10)
	private String reserveField3; // 예비 정보 FIELD 3 (AN, 3)
	private String memberName; // (회원정보연계) 회원명 (AHNS, 40)
	private String reserveField4; // 예비 정보 FIELD 4 (AN, 10)
	private String obligorRegNo; // 납부의무자 주민(사업자, 법인) 등록번호 (AN, 13)
	private String payerNo; // 납부자(고지서) 번호 (AN, 15)
	private String feeType; // 과금 종류 (N, 1)
	private String collectorName; // 징수 기관명 (AHN, 20)
	private String collectorAccountNo; // 징수관 계좌번호 (AN, 6)
	private String subAccount; // 소계정 (N, 1)
	private String payAmountInDue; // 납기내 금액 (N, 15)
	private String payAmountAfterDue; // 납기후 금액 (N, 15)
	private String itemCode; // 징수 과목 코드(세목 코드) (N, 7)
	private String fiscalYear; // 징수 결의 회계 년도 (N, 4)
	private String payDueDateIn; // 납기일(납기내) (N, 8)
	private String payDueDateAfter; // 납기일(납기후) (N, 8)
	private String taxReasonDate; // 과세 원인 일시 (N, 14)
	private String violationDate; // 위반 일시 (N, 14)
	private String violationLocation; // 위반 장소 (AHNS, 40)
	private String violationContent; // 위반 내용 (AHNS, 100)
	private String violationCarNo; // 위반차량 번호 (AHNS, 20)
	private String lawBasis; // 법령 근거 (AHNS, 100)
	private String reserveField5; // 예비 정보 FIELD 5 (AN, 7)
	private String payDate; // 납부 일시 (N, 14)
	private String afterDueType; // 납기 내후 구분 (AN, 1)
	private String obligorName; // 납부의무자 성명 (AN, 8)
	private String cardPayYn; // 신용카드 납부 제하 여부 (AN, 1)
	private String reserveField6; // 예비 정보 FIELD 6 (AN, 18)
}
