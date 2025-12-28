package mb.fw.policeminwon.history.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mb.fw.policeminwon.configuration.KftcHistoryProperties;
import mb.fw.policeminwon.history.entity.KftcNotificationHistory;

@Repository
public class KftcNotificationHistoryDao {
	private final JdbcTemplate jdbcTemplate;
	private final KftcHistoryProperties historyProperties;

	public KftcNotificationHistoryDao(@Autowired(required = false) JdbcTemplate jdbcTemplate,
			@Autowired(required = false) KftcHistoryProperties historyProperties) {
		this.jdbcTemplate = jdbcTemplate;
		this.historyProperties = historyProperties;
	}

	/**
	 * 통지 이력 등록
	 */
	public int insert(KftcNotificationHistory entity) {

//		String sql = "INSERT INTO KFTC_NOTIFICATION_HISTORY (PROCESS_DATE, KFTC_MESSAGE_NO, ELECTRONIC_PAYMENT_NO, ESB_TX_ID) VALUES (TO_CHAR(SYSDATE, 'YYYYMMDD'), ?, ?, ?)";

		return jdbcTemplate.update(historyProperties.getInsertSql(), entity.getKftcMessageNo(),
				entity.getElectronicPaymentNo(), entity.getEsbTxId());
	}

	/**
	 * 처리일자 + 센터번호 단건 조회
	 */
	public KftcNotificationHistory findOne(String kftcMessageNo) {

//		String sql = "SELECT PROCESS_DATE, KFTC_MESSAGE_NO, ELECTRONIC_PAYMENT_NO, ESB_TX_ID FROM KFTC_NOTIFICATION_HISTORY WHERE PROCESS_DATE = TO_CHAR(SYSDATE, 'YYYYMMDD') AND KFTC_MESSAGE_NO = ?";

		List<KftcNotificationHistory> list = jdbcTemplate.query(historyProperties.getSelectSql(),
				(rs, rowNum) -> new KftcNotificationHistory(rs.getString("PROCESS_DATE"),
						rs.getString("KFTC_MESSAGE_NO"), rs.getString("ELECTRONIC_PAYMENT_NO"),
						rs.getString("ESB_TX_ID")),
				kftcMessageNo);

		return list.isEmpty() ? null : list.get(0); // 여러 건 있어도 첫 건만
	}
}
