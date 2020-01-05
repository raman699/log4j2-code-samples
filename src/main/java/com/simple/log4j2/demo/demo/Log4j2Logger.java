package com.simple.log4j2.demo.demo;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class Log4j2Logger {

	int counter = 0;

	LoggerContext ctx;

	Configuration config;

	Logger logger;

	String loggerName = "testLogger";

	String appenderName = "myAppender";

	static String testMessage = "This is a Test Message";

	public void log() {

		final ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
		config = builder.build();
		ctx = Configurator.initialize(config);
		config = ctx.getConfiguration();
		ctx.start(config);
		ctx.updateLoggers(config);

		// To create/add the logger of the configuration specified above we can use the
		// getLogger(..) method
		logger = ctx.getLogger(loggerName);

		// Now we need to attach an appender to the logger so that our messages could be
		// logged
		logger.addAppender(addConsoleAppender(ctx.getConfiguration(), appenderName));
		while (counter < 10) {
			logger.error(testMessage + counter);
			counter++;
		}

		// We can remove the logger in the context also after use.
		removeLogger();
	}

	private Appender addConsoleAppender(Configuration config, String appenderName) {
		Appender consoleAppender = ConsoleAppender.newBuilder().setConfiguration(config).setName(appenderName)
				.withImmediateFlush(true).build();
		consoleAppender.start();
		return consoleAppender;
	}

	public void removeLogger() {

		config.getLoggerConfig(loggerName).getAppenders().get(appenderName).stop();
		config.getLoggerConfig(loggerName).removeAppender(appenderName);
		config.removeLogger(loggerName);
		ctx.updateLoggers(config);
	}
}
