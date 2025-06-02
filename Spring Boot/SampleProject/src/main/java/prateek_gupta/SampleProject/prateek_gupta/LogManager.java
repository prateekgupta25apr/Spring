package prateek_gupta.SampleProject.prateek_gupta;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class LogManager {

    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE_PREFIX = "logs.";
    private static final String LOG_FILE_SUFFIX = ".log";
    private static final int DAYS_GAP_FOR_ROTATION = 30;

    public static void initializeLogger() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel(org.apache.logging.log4j.Level.WARN);
        builder.setConfigurationName("RollingBuilder");

        // Pattern layout
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss.SSS} %M(){%F} : %msg%n");

        // Triggering Policies
        ComponentBuilder<?> triggeringPolicies = builder.newComponent("Policies")
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                        .addAttribute("size", "10MB"))
                .addComponent(builder.newComponent("TimeBasedTriggeringPolicy")
                        .addAttribute("interval", "1"));

        // RollingFile Appender
        AppenderComponentBuilder rollingFileAppender = builder.newAppender("RollingFile", "RollingFile")
                .addAttribute("fileName", LOG_DIR + "/logs.log")
                .addAttribute("filePattern", LOG_DIR + "/logs.%d{yyyy-MM-dd}.%i.log")
                .add(layoutBuilder)
                .addComponent(triggeringPolicies)
                .addComponent(builder.newComponent("DefaultRolloverStrategy")
                        .addAttribute("max", "30"));

        builder.add(rollingFileAppender);

        // Console Appender
        AppenderComponentBuilder consoleAppender = builder.newAppender("Console", "Console")
                .addAttribute("target", "SYSTEM_OUT")
                .add(layoutBuilder);

        builder.add(consoleAppender);

        // Root Logger
        builder.add(builder.newRootLogger(org.apache.logging.log4j.Level.INFO)
                .add(builder.newAppenderRef("RollingFile"))
                .add(builder.newAppenderRef("Console")));

        LoggerContext ctx = Configurator.initialize(builder.build());
        ctx.updateLoggers();
    }

    public static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(LogManager.class);

    public static void rotateLogFiles() {
        logger.info("Log files rotation begins");
        try {
            File logDirectory = new File(LOG_DIR);
            if (!logDirectory.exists()) {
                logger.warn("Log directory not found.");
                return;
            }

            File[] files = logDirectory.listFiles((dir, name) -> name.startsWith(LOG_FILE_PREFIX) && name.endsWith(LOG_FILE_SUFFIX));
            if (files == null || files.length == 0) return;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Arrays.stream(files).forEach(file -> {
                try {
                    // logs.2024-05-01.1.log → extract 2024-05-01
                    String name = file.getName();
                    String[] parts = name.split("\\.");
                    if (parts.length >= 3) {
                        LocalDate fileDate = LocalDate.parse(parts[1], formatter);
                        if (fileDate.plusDays(DAYS_GAP_FOR_ROTATION).isBefore(LocalDate.now())) {
                            if (file.delete()) {
                                logger.info("Deleted log file: {}", name);
                            } else {
                                logger.warn("Failed to delete log file: {}", name);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error parsing log file name: {}", file.getName(), e);
                }
            });
        } catch (Exception e) {
            logger.error("Error while rotating log files", e);
        }
        logger.info("Log files rotation ends");
    }

    public static void main(String[] args) {
        initializeLogger();
        logger.info("Logger initialized.");
        rotateLogFiles(); // Call this during startup or via a scheduled job
    }
}
