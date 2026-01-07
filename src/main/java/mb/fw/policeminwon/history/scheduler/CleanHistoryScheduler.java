package mb.fw.policeminwon.history.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mb.fw.policeminwon.configuration.KftcHistoryProperties;
import mb.fw.policeminwon.history.repository.KftcNotificationHistoryDao;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(KftcHistoryProperties.class)
public class CleanHistoryScheduler {

	private final KftcNotificationHistoryDao dao;

	@Transactional
	@Scheduled(cron = "#{@kftcHistoryCronResolver.get()}")
	public void deleteHistory() {
		int deletedCount = dao.deleteHistory();
		log.info("History delete execute. delete-count : {}", deletedCount);
	}
}
