package prateek_gupta.SampleProject.prateek_gupta;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Plugin(name = "CustomConfigurationFactory",
        category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LogManager extends ConfigurationFactory{

    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE_PREFIX = "logs.";
    private static final String LOG_FILE_SUFFIX = ".log";
    private static final int DAYS_GAP_FOR_ROTATION = 30;

    @Override
    public String[] getSupportedTypes() {
        return new String[] { "*" };
    }

    @Override
    public Configuration getConfiguration(
            LoggerContext loggerContext, ConfigurationSource source) {
        return LogManager.getBuiltConfiguration(true);
    }

    public static BuiltConfiguration getBuiltConfiguration(
            boolean console_logs) {
        ConfigurationBuilder<BuiltConfiguration> configurationBuilder =
                ConfigurationBuilderFactory.newConfigurationBuilder();

        configurationBuilder.setStatusLevel(org.apache.logging.log4j.Level.WARN);
        configurationBuilder.setConfigurationName("RollingBuilder");

        // Pattern layout
        LayoutComponentBuilder layoutBuilder =
                configurationBuilder.newLayout("PatternLayout")
                        .addAttribute("pattern",
                                "%d{yyyy-MM-dd HH:mm:ss.SSS} %M(){%F} :: %msg%n");

        // Log file rollover triggering Policies
        ComponentBuilder<?> triggeringPolicies =
                configurationBuilder.newComponent("Policies")
                        .addComponent(configurationBuilder.newComponent(
                                        "SizeBasedTriggeringPolicy")
                                .addAttribute("size", "10MB"));

        RootLoggerComponentBuilder rootLoggerComponentBuilder =
                configurationBuilder.newRootLogger(org.apache.logging.log4j.Level.INFO);

        if (console_logs) {
            // Console Appender
            AppenderComponentBuilder consoleAppender =
                    configurationBuilder.newAppender("LogConsole", "Console")
                            .addAttribute("target", "SYSTEM_OUT")
                            .add(layoutBuilder);

            configurationBuilder.add(consoleAppender);
            rootLoggerComponentBuilder.add(configurationBuilder.newAppenderRef("LogConsole"));
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

    public static void rotateLogFiles() {
        //logger.info("Log files rotation begins");
        try {
            File logDirectory = new File(LOG_DIR);
            if (!logDirectory.exists()) {
                //logger.warn("Log directory not found.");
                return;
            }

            File[] files = logDirectory.listFiles(
                    (dir, name) -> name.startsWith(LOG_FILE_PREFIX) &&
                            name.endsWith(LOG_FILE_SUFFIX));
            if (files == null || files.length == 0) return;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Arrays.stream(files).forEach(file -> {
                try {
                    // logs.2024-05-01.1.log → extract 2024-05-01
                    String name = file.getName();
                    String[] parts = name.split("\\.");
                    if (parts.length >= 3) {
                        LocalDate fileDate = LocalDate.parse(parts[1], formatter);
                        if (fileDate.plusDays(DAYS_GAP_FOR_ROTATION).
                                isBefore(LocalDate.now())) {
                            if (file.delete()) {
                                //logger.info("Deleted log file: {}", name);
                            } else {
                                //logger.warn("Failed to delete log file: {}", name);
                            }
                        }
                    }
                } catch (Exception e) {
//                    logger.error("Error parsing log file name: {}",
//                            file.getName(), e);
                }
            });
        } catch (Exception e) {
            //logger.error("Error while rotating log files", e);
        }
        //logger.info("Log files rotation ends");
    }

    public static void main(String[] args) {
        System.out.println("Starts");
        LoggerContext ctx = Configurator.initialize(getBuiltConfiguration(true));
        ctx.updateLoggers();
        Logger logger =
                org.apache.logging.log4j.LogManager.getLogger(LogManager.class);
        logger.info("Logger initialized.");
        rotateLogFiles();
    }
}
