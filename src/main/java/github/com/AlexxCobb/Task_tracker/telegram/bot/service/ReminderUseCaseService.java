package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ReminderUseCaseService {

    private final ReminderService reminderService;
    private final ReminderNotificationService notificationService;
    private final TaskService taskService;

    public void createReminderUseCase(Long chatId, Long taskId, OffsetDateTime dateTime) {
        var task = taskService.getTaskEntityForUser(chatId, taskId);
        reminderService.createTaskRemind(chatId, task, dateTime);
    }

    public void sendReminderUseCase() {
        var reminders = reminderService.lockDetails();
        if (reminders.isEmpty()) {
            return;
        }
        var sentReminderIds = notificationService.sendAll(reminders);
        if (!sentReminderIds.isEmpty()) {
            reminderService.markSent(sentReminderIds);
        }
    }

    public void recoverStuckRemindersUseCase() {
        reminderService.recoverStuckReminderDetails();
    }

    public void cancelReminderUseCase(Long remindId) {
        reminderService.cancelRemind(remindId);
    }
}
