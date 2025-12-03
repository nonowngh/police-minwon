package mb.fw.policeminwon.constants;

import java.util.Arrays;

import lombok.Getter;

public enum TcpStatusCode {

	/* 공통 오류 */
	SUCCESS("000", "정상"), 
	FORMAT_ERROR("030", "전문 오류"), 
	SEND_DATE_ERROR("031", "전문 전송 일자 오류"),
	OUT_OF_SERVICE_HOURS("092", "서비스 시간 아님"),
	SYSTEM_ERROR("093", "시스템 오류"),
	RNG_ERROR("119", "난수 오류"),
	CONTACT_USE_ORG("191", "이용기관 문의"),
	SERVICE_UNAVAILABLE("201", "서비스 불가"),
	
	/* 고지내역 조회 */
	NO_BILLING_RECORDS("311", "고지내역 없음"),
	ACCESS_PERIOD_EXPIRED("321", "조회 가능 기한 경과"),
	DESIGNATED_NUMBER_ERROR("323", "지정 번호 오류"),
	GIRO_NUMBER_ERROR("324", "지로번호 오류"),
	ISSUER_CLASSIFICATION_CODE_ERROR("339", "발행기관 분류코드 오류"),
	ELECTRONIC_PAYMENT_NUMBER_ERROR("341", "전자납부번호 오류"),
	CUSTOMER_ID_NUMBER_ERROR("342", "고객 관리번호 오류"),
	
	/* 납부 */
	ALREADY_PAID_RECORDS("331", "기 납부 내역  (납부 불가)"),
	NO_EXISTING_TRANSACTIONS("332", "(조회) 원거래 없음"),
	TRANSACTION_DATE_ERROR("337", "거래 일자(납부 일자) 오류"),
	WITHDRAWAL_BANK_BRANCH_CODE_ERROR("338", "출금 금융회사 점별 코드 오류"),
	UNIQUE_REGISTRATION_NUMBER("340", "주민(사업자) 등록번호 오류"),
	PAYMENT_AMOUNT_ERROR("343", "납부 금액 오류"),
	COLLECTOR_ACCOUNT_NUMBER_ERROR("361", "징수관 계좌번호 오류"),
	UNAVAILABLE_PAYMENT_DEADLINE_EXPIRED("364", "납부 기한 경과로 납부 불가"),
	
	/* 그외 오류 */
	RESPONSE_SYSTEM_CONNECTION_ERROR("990", "타겟시스템 통신 오류"),
	UNKNOWN_ERROR("999", "그외 오류");

	@Getter
	private final String code;
	@Getter
	private final String description;

	TcpStatusCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public static TcpStatusCode fromCode(String code) {
		return Arrays.stream(TcpStatusCode.values()).filter(value -> value.getCode().equals(code)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Invalid transaction-code: " + code));
	}
}