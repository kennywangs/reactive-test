package com.xxb.reactive.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

public class LogAppender extends ConsoleAppender<LoggingEvent> {

	@Override
	protected void append(LoggingEvent eventObject) {
		super.append(eventObject);
//		System.out.println("wtf:"+eventObject.getMessage());
	}

}
