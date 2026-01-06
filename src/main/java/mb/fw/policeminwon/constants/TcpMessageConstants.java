package mb.fw.policeminwon.constants;

public class TcpMessageConstants {

	private TcpMessageConstants() {
	}

	private static final String PREFIX = "0137";
	private static final String SUFFIX = "3";

	// 즉심 전자납부번호 형태 '013720253 + 일련번호'
//	public static String getSJSElecNumType() {
//		int currentYear = LocalDate.now().getYear();
//		return PREFIX + currentYear + SUFFIX;
//	}

	// 수정!! 전자납부번호 형태 'xxxxxxxx3 + 일련번호'
	public static boolean isSummary(String elecNum) {
		if (elecNum.length() > 8 && elecNum.charAt(8) == '3')
			return true;
		return false;
	}

	// 송수신 플래그 금결원
	public static final String SRFLAG_KFTC = "C";

	// 송수신 플래그 경찰청
	public static final String SRFLAG_POLICE = "G";

}
