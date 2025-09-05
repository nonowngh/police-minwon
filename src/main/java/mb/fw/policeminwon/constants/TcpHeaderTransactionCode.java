package mb.fw.policeminwon.constants;

public final class TcpHeaderTransactionCode {
	private TcpHeaderTransactionCode() {
    }
	
	//테스트 콜
	public static final String TEST_CALL = "000301";
	
	//고지내역 상세조회
	public static final String VIEW_BILLING_DETAIL = "121002";
	
	//납부결과 통지
	public static final String PAYMENT_RESULT_NOTIFICATION = "122001";
	
	//납부 (재)취소
	public static final String CANCEL_PAYMENT = "992001";
}