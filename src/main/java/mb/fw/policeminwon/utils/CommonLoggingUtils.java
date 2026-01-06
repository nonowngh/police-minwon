package mb.fw.policeminwon.utils;

import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.constants.SystemCodeConstants;
import mb.fw.policeminwon.constants.TcpStatusCode;

@Slf4j
public class CommonLoggingUtils {

	// logging tcp header response code
	public static void loggingTcpResponse(String responseCode, String esbTxId) {
		if (responseCode.trim().isEmpty())
			return;
		TcpStatusCode tcpStatusCode = TcpStatusCode.fromCode(responseCode);
		String errorMsg = "'" + esbTxId + "' tcp response code : [" + tcpStatusCode.getCode() + "]"
				+ tcpStatusCode.getDescription();
		if(esbTxId.contains("_00_")) log.debug(errorMsg);
		else log.info(errorMsg);
	}

	// logging routing and interface info
	public static void logTransaction(String description, String from, String to, String esbTxId,
			boolean isLogLevelDebug) {
		if (SystemCodeConstants.SUMMRAY.equals(to)) {
			log.info("{} - api call...[{}] -> [{}] | esb-tran-id({})", description, from, to, esbTxId);
		} else if (SystemCodeConstants.SUMMRAY.equals(from)) {
			log.info("{} - tcp send...[{}] -> [{}] | esb-tran-id({})", description, from, to, esbTxId);
		} else {
			if (isLogLevelDebug)
				log.debug("{} - bypass...[{}] -> [{}] | esb-tran-id({})", description, from, to, esbTxId);
			else
				log.info("{} - bypass...[{}] -> [{}] | esb-tran-id({})", description, from, to, esbTxId);
		}
	}

	public static void printFieldInfo(String message, int length) {
		log.debug("message : [{}], length : {}", message, length);
	}
}
