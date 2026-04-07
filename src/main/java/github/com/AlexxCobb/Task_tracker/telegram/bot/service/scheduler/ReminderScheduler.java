package github.com.AlexxCobb.Task_tracker.telegram.bot.service.scheduler;

import github.com.AlexxCobb.Task_tracker.telegram.bot.service.ReminderUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final ReminderUseCaseService reminderUseCaseService;

    @Scheduled(fixedDelayString = "${reminder.scheduler.fixed-delay}")
    public void processReminders() {
        try {
            log.debug("Starting scheduled reminder processing");
            reminderUseCaseService.sendReminderUseCase();
        } catch (Exception e) {
            log.error("Scheduled reminder processing failed", e);
        }
    }

    @Scheduled(fixedDelayString = "${reminder.scheduler.recovery-fixed-delay}")
    public void retryReminders() {
        try {
            log.debug("Starting stuck reminders recovery");
            reminderUseCaseService.recoverStuckRemindersUseCase();
        } catch (Exception e) {
            log.error("Stuck reminders recovery failed", e);
        }
    }
}
