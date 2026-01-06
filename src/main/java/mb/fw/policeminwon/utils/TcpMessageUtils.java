package mb.fw.policeminwon.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.constants.TcpCommonSettingConstants;

@Slf4j
public class TcpMessageUtils {

	static final int HEADER_LENGTH = 70;
	static final int VIEW_BILLING_DETAIL_BODY_LENGTH = 630;
	static final int PAYMENT_RESULT_NOTIFICATION_BODY_LENGTH = 153;
	static final int CANCEL_PAYMENT_BODY_LENGTH = 86;

	public static final Map<String, Integer> HEADER_MAP = new LinkedHashMap<>();
	public static final Map<String, Integer> VIEW_BILLING_DETAIL_BODY_MAP = new LinkedHashMap<>();
	public static final Map<String, Integer> PAYMENT_RESULT_NOTIFICATION_BODY_MAP = new LinkedHashMap<>();
	public static final Map<String, Integer> CANCEL_PAYMENT_BODY_MAP = new LinkedHashMap<>();
	static {
//		HEADER_MAP.put("전문길이", 4);
		HEADER_MAP.put("업무구분", 3);
		HEADER_MAP.put("기관코드", 3);
		HEADER_MAP.put("전문 종별코드", 4);
		HEADER_MAP.put("거래 구분코드", 6);
		HEADER_MAP.put("상태코드", 3);
		HEADER_MAP.put("송수신 FLAG", 1);
		HEADER_MAP.put("응답코드", 3);
		HEADER_MAP.put("전송일시", 12);
		HEADER_MAP.put("센터 전문관리번호", 12);
		HEADER_MAP.put("이용기관 전문관리번호", 12);
		HEADER_MAP.put("이용기관 발행기관분류코드", 2);
		HEADER_MAP.put("이용기관 지로번호", 7);
		HEADER_MAP.put("FILLER", 2);

		VIEW_BILLING_DETAIL_BODY_MAP.put("전자납부번호", 19);
		VIEW_BILLING_DETAIL_BODY_MAP.put("예비정보 FIELD1", 20);
		VIEW_BILLING_DETAIL_BODY_MAP.put("난수", 32);
		VIEW_BILLING_DETAIL_BODY_MAP.put("예비정보 FIELD2", 32);
		VIEW_BILLING_DETAIL_BODY_MAP.put("(회원정보연계)회원 유형", 1);
		VIEW_BILLING_DETAIL_BODY_MAP.put("(회원정보연계)회원 주민등록번호", 13);
		VIEW_BILLING_DETAIL_BODY_MAP.put("(회원정보연계)회원 사업자등록번호", 10);
		VIEW_BILLING_DETAIL_BODY_MAP.put("예비정보 FIELD3", 3);
		VIEW_BILLING_DETAIL_BODY_MAP.put("(회원정보연계)회원명", 40);
		VIEW_BILLING_DETAIL_BODY_MAP.put("예비정보 FIELD4", 10);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납부의무자 주민(사업자,법인)등록번호", 13);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납부자(고지서) 번호", 15);
		VIEW_BILLING_DETAIL_BODY_MAP.put("과금종류", 1);
		VIEW_BILLING_DETAIL_BODY_MAP.put("징수 기관명", 20);
		VIEW_BILLING_DETAIL_BODY_MAP.put("징수관 계좌번호", 6);
		VIEW_BILLING_DETAIL_BODY_MAP.put("소계정", 1);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납기내 금액", 15);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납기후 금액", 15);
		VIEW_BILLING_DETAIL_BODY_MAP.put("징수과목 코드(세목코드)", 7);
		VIEW_BILLING_DETAIL_BODY_MAP.put("징수결의 회계년도", 4);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납기일(납기내)", 8);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납기일(납기후)", 8);
		VIEW_BILLING_DETAIL_BODY_MAP.put("과세원인 일시", 14);
		VIEW_BILLING_DETAIL_BODY_MAP.put("위반일시", 14);
		VIEW_BILLING_DETAIL_BODY_MAP.put("위반장소", 40);
		VIEW_BILLING_DETAIL_BODY_MAP.put("위반내용", 100);
		VIEW_BILLING_DETAIL_BODY_MAP.put("위반차량 번호", 20);
		VIEW_BILLING_DETAIL_BODY_MAP.put("법령 근거", 100);
		VIEW_BILLING_DETAIL_BODY_MAP.put("예비정보 FIELD5", 7);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납부일시", 14);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납기내후 구분", 1);
		VIEW_BILLING_DETAIL_BODY_MAP.put("납부의무자 성명", 8);
		VIEW_BILLING_DETAIL_BODY_MAP.put("신용카드 납부제하 여부", 1);
		VIEW_BILLING_DETAIL_BODY_MAP.put("예비정보 FIELD6", 18);

		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("납부의무자 주민(사업자,법인)등록번호", 13);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("징수관 계좌번호", 6);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("전자납부번호", 19);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("예비정보 FIELD1", 3);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("예비정보 FIELD2", 7);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("납부금액", 15);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("납부일자", 8);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("출금 금융회사 점별코드", 7);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("예비정보 FIELD3", 16);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("예비정보 FIELD4", 14);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("납부자 주민(사업자) 등록번호", 13);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("예비정보 FIELD5", 10);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("예비정보 FIELD6", 10);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("납부 이용시스템", 1);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("기납부 이용시스템", 1);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("납부형태 구분", 1);
		PAYMENT_RESULT_NOTIFICATION_BODY_MAP.put("예비정보 FIELD7", 9);

		CANCEL_PAYMENT_BODY_MAP.put("출금 금융회사 점별코드", 7);
		CANCEL_PAYMENT_BODY_MAP.put("납부자 주민(사업자)등록번호", 13);
		CANCEL_PAYMENT_BODY_MAP.put("원거래 센터 전문관리번호", 12);
		CANCEL_PAYMENT_BODY_MAP.put("원거래 전송일시", 12);
		CANCEL_PAYMENT_BODY_MAP.put("예비정보 FIELD1", 16);
		CANCEL_PAYMENT_BODY_MAP.put("원거래 납부금액", 15);
		CANCEL_PAYMENT_BODY_MAP.put("취소사유", 1);
		CANCEL_PAYMENT_BODY_MAP.put("원거래 납부형태 구분", 1);
		CANCEL_PAYMENT_BODY_MAP.put("예비정보 FIELD2", 9);
	}

	public static String saveHistoryMessage(ByteBuf buf) {
		int mark = buf.readerIndex(); // ⭐ 시작 위치 저장
		try {
			if (!TcpCommonSettingConstants.SAVE_MESSAGE_PRETTY)
				return buf.toString(TcpCommonSettingConstants.MESSAGE_CHARSET);
			StringBuilder sb = new StringBuilder();
			int totalLength = buf.readableBytes();
			if (totalLength < HEADER_LENGTH) {
				log.warn("전문 길이 부족");
				return buf.toString(TcpCommonSettingConstants.MESSAGE_CHARSET);
			}
			sb.append("---헤더---").append(System.lineSeparator());
			for (Map.Entry<String, Integer> entry : HEADER_MAP.entrySet()) {
				appendField(buf, sb, entry);
			}
			int remaining = buf.readableBytes();
			if (remaining > 0) {
				sb.append("---바디---").append(System.lineSeparator());
				if (remaining == VIEW_BILLING_DETAIL_BODY_LENGTH) {
					for (Map.Entry<String, Integer> entry : VIEW_BILLING_DETAIL_BODY_MAP.entrySet()) {
						appendField(buf, sb, entry);
					}
				} else if (remaining == PAYMENT_RESULT_NOTIFICATION_BODY_LENGTH) {
					for (Map.Entry<String, Integer> entry : PAYMENT_RESULT_NOTIFICATION_BODY_MAP.entrySet()) {
						appendField(buf, sb, entry);
					}
				} else if (remaining == CANCEL_PAYMENT_BODY_LENGTH) {
					for (Map.Entry<String, Integer> entry : CANCEL_PAYMENT_BODY_MAP.entrySet()) {
						appendField(buf, sb, entry);
					}
				} else {
					log.debug("남은 body 데이터에 해당하는 메시지 타입이 없음. 바디 길이 : {}", remaining);
					byte[] bytes = new byte[remaining];
					buf.readBytes(bytes);
					String value = new String(bytes, TcpCommonSettingConstants.MESSAGE_CHARSET);
					sb.append("body").append("=[").append(value).append("](").append(remaining).append(")")
							.append(System.lineSeparator());
				}
			}
			return sb.toString();
		} finally {
			buf.readerIndex(mark); // ⭐ 반드시 원복
		}
	}

	private static void appendField(ByteBuf buf, StringBuilder sb, Map.Entry<String, Integer> entry) {
		String fieldName = entry.getKey();
		int length = entry.getValue();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes);
		String value = new String(bytes, TcpCommonSettingConstants.MESSAGE_CHARSET);
		sb.append(fieldName).append("=[").append(value).append("](").append(length).append(")")
				.append(System.lineSeparator());
	}
}
