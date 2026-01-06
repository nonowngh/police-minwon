package mb.fw.policeminwon.constants;

import java.nio.charset.Charset;

public class TcpCommonSettingConstants {
	private TcpCommonSettingConstants() {
	}

	// tcp socket 통신을 위한 byte encoding 설정
	public static Charset MESSAGE_CHARSET;

	// tcp socket 통신 logging pretty 설정
	public static boolean PRETTY_LOGGING;

	// tcp 메시지 전문 각 필드별로 잘라서 저장할지 설정
	public static boolean SAVE_MESSAGE_PRETTY;
}
