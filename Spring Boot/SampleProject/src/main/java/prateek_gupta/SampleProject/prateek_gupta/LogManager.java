package prateek_gupta.SampleProject.prateek_gupta;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Plugin(name = "LogManager",
        category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LogManager extends ConfigurationFactory{

    private static final Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(LogManager.class);

    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE_PREFIX = "logs.";
    private static final String LOG_FILE_SUFFIX = ".log";
    public static final int DAYS_GAP_FOR_ROTATION = 30;

    @Override
    public String[] getSupportedTypes() {
        return new String[] { "*" };
    }

    @Override
    public Configuration getConfiguration(
            LoggerContext loggerContext, ConfigurationSource source) {
        ConfigurationBuilder<BuiltConfiguration> configurationBuilder =
                ConfigurationBuilderFactory.newConfigurationBuilder();

        configurationBuilder.setStatusLevel(org.apache.logging.log4j.Level.WARN);
        configurationBuilder.setConfigurationName("RollingBuilder");

        // Pattern layout
        LayoutComponentBuilder layoutBuilder =
                configurationBuilder.newLayout("PatternLayout")
                        .addAttribute("pattern",
                                "%d{yyyy-MM-dd HH:mm:ss.SSS} %M(){%F} : %msg%n");

        // Log file rollover triggering Policies
        ComponentBuilder<?> triggeringPolicies =
                configurationBuilder.newComponent("Policies")
                        .addComponent(configurationBuilder.newComponent(
                                        "SizeBasedTriggeringPolicy")
                                .addAttribute("size", "10MB"));

        RootLoggerComponentBuilder rootLoggerComponentBuilder =
                configurationBuilder.newRootLogger(org.apache.logging.log4j.Level.INFO);

        if (ProjectSettings.console_logs) {
            // Console Appender
            AppenderComponentBuilder consoleAppender =
                    configurationBuilder.newAppender(
                                    "LogConsole", "Console")
                            .addAttribute("target", "SYSTEM_OUT")
                            .add(layoutBuilder);

            configurationBuilder.add(consoleAppender);
            rootLoggerComponentBuilder.add(
                    configurationBuilder.newAppenderRef("LogConsole"));
        } else {
            // RollingFile Appender
            AppenderComponentBuilder rollingFileAppender =
                    configurationBuilder.newAppender("LogFiles",
                                    "RollingFile")
                            .addAttribute("fileName", LOG_DIR + "/logs.log")
                            .addAttribute("filePattern", LOG_DIR +
                                    "/logs.%d{yyyy-MM-dd}.%i.log")
                            .add(layoutBuilder)
                            .addComponent(triggeringPolicies);

            configurationBuilder.add(rollingFileAppender);
            rootLoggerComponentBuilder.add(configurationBuilder.newAppenderRef("LogFiles"));
        }

        // Root Logger
        configurationBuilder.add(rootLoggerComponentBuilder);
        return configurationBuilder.build();
    }

    public static void rotateLogFiles(Integer daysGap)
            throws ServiceException {
        logger.info("Log files rotation begins");
        try {
            File logDirectory = new File(LOG_DIR);
            if (!logDirectory.exists())
                throw new ServiceException("Log directory not found");

            File[] files = logDirectory.listFiles(
                    (file) -> file.getName().startsWith(LOG_FILE_PREFIX) &&
                            file.getName().endsWith(LOG_FILE_SUFFIX));
            if (files == null || files.length == 0) return;

            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar=Calendar.getInstance();


            Arrays.asList(files).forEach(file -> {
                try {
                    String name = file.getName();
                    String[] parts = name.split("\\.");
                    if (parts.length >= 3) {
                        Date date=dateFormat.parse(parts[1]);
                        calendar.setTime(date);
                        calendar.add(Calendar.DAY_OF_MONTH,daysGap);

                        if (calendar.before(Calendar.getInstance())) {
                            if (file.delete()) {
                                logger.info("Deleted log file: {}", name);
                            } else {
                                logger.warn("Failed to delete log file: {}", name);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error parsing log file name: {}",
                            file.getName(), e);
                }
            });
        } catch (Exception e) {
            throw new ServiceException("Error while rotating log files");
        }
        logger.info("Log files rotation ends");
    }
}
