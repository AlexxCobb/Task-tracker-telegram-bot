package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "reminder")
public record ReminderProperties(
        Scheduler scheduler,
        Batch batch
) {

    public record Scheduler(
            int fixedDelay,
            int recoveryFixedDelay
    ) {
    }

    public record Batch(
            int scheduledSize,
            int processingRetrySize,
            int processingTimeoutMinutes
    ) {
    }
}