package mb.fw.policeminwon.history.scheduler;

import java.util.function.Supplier;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import mb.fw.policeminwon.configuration.KftcHistoryProperties;

@Component("kftcHistoryCronResolver")
@RequiredArgsConstructor
public class KftcHistoryCronResolver implements Supplier<String> {

	private final ObjectProvider<KftcHistoryProperties> propertiesProvider;

	@Override
	public String get() {
		KftcHistoryProperties props = propertiesProvider.getIfAvailable();
		if (props == null || !StringUtils.hasText(props.getDeleteCronSchedule())) {
			// 절대 실행 안 되는 cron
			return "0 0 0 31 2 ?";
		}
		return props.getDeleteCronSchedule();
	}
}
