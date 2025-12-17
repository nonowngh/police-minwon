package mb.fw.policeminwon.configuration;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@Configuration
public class NettyLoggerConfiguration {

    private static final String TESTCALL_PREFIX = "0070IGN0990800000301";

	@PostConstruct
	public void init() {
		InternalLoggerFactory.setDefaultFactory(new ChannelEventsLoggerFactory());
	}

	static class ChannelEventsLoggerFactory extends InternalLoggerFactory {

		@Override
		protected InternalLogger newInstance(String name) {
			org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(name);

			return new InternalLogger() {

				private boolean isChannelEvent(String msg) {
					return msg != null
							&& (msg.contains("REGISTERED") || msg.contains("ACTIVE") || msg.contains("READ COMPLETE") || msg.contains(TESTCALL_PREFIX));
				}

				private InternalLogLevel adjustLevel(InternalLogLevel level, String msg) {
					if (level == InternalLogLevel.INFO && isChannelEvent(msg)) {
						return InternalLogLevel.DEBUG;
					}
					return level;
				}

				@Override
				public void log(InternalLogLevel level, String msg) {
					if (adjustLevel(level, msg) == InternalLogLevel.DEBUG) {
						slf4jLogger.debug(msg);
					} else {
						slf4jLogger.info(msg);
					}
				}

//				@Override
//				public void log(InternalLogLevel level, String msg, Object param1) {
//					log(level, msg);
//				}

//				@Override
//				public void log(InternalLogLevel level, String msg, Object param1, Object param2) {
//					log(level, msg);
//				}

//				@Override
//				public void log(InternalLogLevel level, String msg, Object[] params) {
//					log(level, msg);
//				}

//				@Override
//				public void log(InternalLogLevel level, String msg, Throwable t) {
//					slf4jLogger.info(msg, t);
//				}

				@Override
				public void log(InternalLogLevel level, Throwable t) {
					slf4jLogger.info("Exception", t);
				}
				
				@Override
				public void log(InternalLogLevel level, String format, Object... arguments) {
				    String msg = org.slf4j.helpers.MessageFormatter
				            .arrayFormat(format, arguments)
				            .getMessage();
				    log(level, msg);
				}
				
				@Override
				public void log(InternalLogLevel level, String msg, Throwable t) {
				    if (adjustLevel(level, msg) == InternalLogLevel.DEBUG) {
				        slf4jLogger.debug(msg, t);
				    } else {
				        slf4jLogger.info(msg, t);
				    }
				}
				
				@Override
				public void log(InternalLogLevel level, String format, Object arg) {
				    log(level, format, new Object[]{arg});
				}
				
				@Override
				public void log(InternalLogLevel level, String format, Object arg1, Object arg2) {
				    log(level, format, new Object[]{arg1, arg2});
				}

//				@Override
//				public boolean isEnabled(InternalLogLevel level) {
//					return true;
//				}
				
				@Override
				public boolean isEnabled(InternalLogLevel level) {
				    switch (level) {
				        case TRACE:
				            return slf4jLogger.isTraceEnabled();
				        case DEBUG:
				            return slf4jLogger.isDebugEnabled();
				        case INFO:
				            return slf4jLogger.isInfoEnabled();
				        case WARN:
				            return slf4jLogger.isWarnEnabled();
				        case ERROR:
				            return slf4jLogger.isErrorEnabled();
				        default:
				            return true;
				    }
				}

				@Override
				public String name() {
					return name;
				}

				// trace / debug / info / warn / error 등 모두 slf4j 기본 전달
				@Override
				public void trace(String msg) {
					slf4jLogger.trace(msg);
				}

				@Override
				public void debug(String msg) {
					slf4jLogger.debug(msg);
				}

				@Override
				public void info(String msg) {
					slf4jLogger.info(msg);
				}

				@Override
				public void warn(String msg) {
					slf4jLogger.warn(msg);
				}

				@Override
				public void error(String msg) {
					slf4jLogger.error(msg);
				}

				@Override
				public void trace(String msg, Throwable t) {
					slf4jLogger.trace(msg, t);
				}

				@Override
				public void debug(String msg, Throwable t) {
					slf4jLogger.debug(msg, t);
				}

				@Override
				public void info(String msg, Throwable t) {
					slf4jLogger.info(msg, t);
				}

				@Override
				public void warn(String msg, Throwable t) {
					slf4jLogger.warn(msg, t);
				}

				@Override
				public void error(String msg, Throwable t) {
					slf4jLogger.error(msg, t);
				}

				@Override
				public boolean isTraceEnabled() {
					return slf4jLogger.isTraceEnabled();
				}

				@Override
				public boolean isDebugEnabled() {
					return slf4jLogger.isDebugEnabled();
				}

				@Override
				public boolean isInfoEnabled() {
					return slf4jLogger.isInfoEnabled();
				}

				@Override
				public boolean isWarnEnabled() {
					return slf4jLogger.isWarnEnabled();
				}

				@Override
				public boolean isErrorEnabled() {
					return slf4jLogger.isErrorEnabled();
				}

				@Override
				public void trace(String format, Object arg) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void trace(String format, Object argA, Object argB) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void trace(String format, Object... arguments) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void trace(Throwable t) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void debug(String format, Object arg) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void debug(String format, Object argA, Object argB) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void debug(String format, Object... arguments) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void debug(Throwable t) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void info(String format, Object arg) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void info(String format, Object argA, Object argB) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void info(String format, Object... arguments) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void info(Throwable t) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void warn(String format, Object arg) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void warn(String format, Object... arguments) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void warn(String format, Object argA, Object argB) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void warn(Throwable t) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void error(String format, Object arg) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void error(String format, Object argA, Object argB) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void error(String format, Object... arguments) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void error(Throwable t) {
					// TODO Auto-generated method stub
					
				}
			};
		}
	}
}
