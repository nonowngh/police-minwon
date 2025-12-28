package mb.fw.policeminwon.history.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.constants.SystemCodeConstants;
import mb.fw.policeminwon.constants.TcpMessageConstants;
import mb.fw.policeminwon.history.entity.KftcNotificationHistory;
import mb.fw.policeminwon.history.repository.KftcNotificationHistoryDao;

@Slf4j
@Service
public class KftcNotificationHistoryService {

	private final KftcNotificationHistoryDao dao;

	public KftcNotificationHistoryService(KftcNotificationHistoryDao dao) {
		this.dao = dao;
	}

	@Async("historyExecutor")
	public void save(KftcNotificationHistory dto) {
		dao.insert(dto);
	}

	public String get(String kftcMessageNo) {
		KftcNotificationHistory result = dao.findOne(kftcMessageNo);
		log.debug("KFTC-NOTIFICATION-HISTORY : {}", result);
		if (result == null) {
			log.debug("history result null. request to traffic system.");
			return SystemCodeConstants.TRAFFIC;
		}
		String eltrPymNo = result.getElectronicPaymentNo();
		if (eltrPymNo.startsWith(TcpMessageConstants.getSJSElecNumType())) {
			return SystemCodeConstants.SUMMRAY;
		}
		return SystemCodeConstants.TRAFFIC;
	}
}
