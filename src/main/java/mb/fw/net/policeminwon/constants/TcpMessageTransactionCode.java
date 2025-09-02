package mb.fw.net.policeminwon.constants;

public final class TcpMessageTransactionCode {
	private TcpMessageTransactionCode() {
    }
	
	//테스트 콜
	public static final String testCallCode = "000301";
	
	//고지내역 상세조회
	public static final String viewBillingDetails = "121002";
	
	//납부결과 통지
	public static final String paymentResultNotification = "122001";
	
	//납부 (재)취소
	public static final String cancelPayment = "992001";
}
