package com.simple.log4j2.demo.demo;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class Log4j2TestApplication {

	public static void main(String[] args) {

		String loggerName = "testLogger";

		final ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

		final LoggerComponentBuilder loggerComp = builder.newLogger(loggerName, Level.ALL).addAttribute("additivity",
				false);

		builder.add(loggerComp);
		Configuration config = builder.build();
		LoggerContext ctx = Configurator.initialize(config);
		ctx.start(config);
		ctx.updateLoggers(config);

		// Just to show that no logger of name testLogger is initially present in log4j2
		// context.
		System.out.println(ctx.hasLogger(loggerName));

		// To get the logger of the configuration specified above.
		Logger logger = ctx.getLogger(loggerName);

		// To validate if the logger created is of the configuration specified by us:
		// We check whether the "additivity" attribute is the same as provided by us in
		// the Builder configuration above.
		System.out.println(logger.isAdditive());
		System.out.println(ctx.getLogger(loggerName).getLevel());

		// Add an appender to the logger
		logger.addAppender(attachConsoleAppender(config, "consoleAppender"));
		// Finally printing some log messages.
		logger.error("ERROR Message: You successfully created a loggger");
		logger.info("INFO Message: You successfully created a loggger");
		logger.warn("WARN Message: You successfully created a loggger");
	}

	private static Appender attachConsoleAppender(Configuration config, String appenderName) {
		Appender consoleAppender = ConsoleAppender.newBuilder().setConfiguration(config).setName(appenderName)
				.withImmediateFlush(true).build();
		consoleAppender.start();
		return consoleAppender;
	}
}
