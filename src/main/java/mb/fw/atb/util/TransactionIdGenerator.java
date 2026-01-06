package mb.fw.atb.util;

import org.apache.commons.lang3.RandomStringUtils;

public class TransactionIdGenerator {
	public static String generate(String interfaceId, String alias, String msgCreDt) {
		return interfaceId + "_" + msgCreDt + "_" + alias + RandomStringUtils.randomAlphanumeric(3).toUpperCase();
	}

	public static String generate(String interfaceId, String alias, String msgSendTime, String msgCreDt) {
		if (msgSendTime.trim().isEmpty() || msgSendTime == "000000000000")
//			return interfaceId + "_" + msgCreDt + "_" + alias + RandomStringUtils.randomAlphanumeric(3).toUpperCase();
			return interfaceId + "_" + msgCreDt + alias + RandomStringUtils.randomNumeric(5);
		else
//			return interfaceId + "_" + msgSendTime + "_" + alias + RandomStringUtils.randomAlphanumeric(3).toUpperCase();
			return interfaceId + "_" + msgSendTime + alias + RandomStringUtils.randomNumeric(5);
	}

}
