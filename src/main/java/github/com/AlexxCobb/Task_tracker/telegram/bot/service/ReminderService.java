package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Reminder;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.ReminderStatus;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.mappers.ReminderMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.ReminderRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.ReminderDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;

    @Transactional
    public void createTaskRemind(Long chatId, Task task, OffsetDateTime dateTime) {
        var taskReminder = Reminder.builder()
                .chatId(chatId)
                .remindAt(dateTime)
                .status(ReminderStatus.SCHEDULED)
                .task(task)
                .build();
        reminderRepository.save(taskReminder);
    }

    @Transactional
    public List<ReminderDetails> lockDetails(int batchSize) {
        var reminderIds = reminderRepository.selectForUpdate(batchSize);
        if (reminderIds.isEmpty()) {
            return Collections.emptyList();
        }

        reminderRepository.updateStatusAtByIds(ReminderStatus.PROCESSING, OffsetDateTime.now(), reminderIds);

        var reminders = reminderRepository.findPendingWithTasks(reminderIds);
        return reminders.stream()
                .map(reminderMapper::toReminderDetails)
                .toList();
    }

    @Transactional
    public void markSent(List<Long> ids) {
        reminderRepository.updateStatusAtByIds(ReminderStatus.SENT, OffsetDateTime.now(), ids);
    }

    @Transactional
    public void cancelRemind(Long remindId) {
        reminderRepository.deleteById(remindId);
    }
}
