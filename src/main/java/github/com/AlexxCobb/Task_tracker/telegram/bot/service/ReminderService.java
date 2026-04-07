package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.config.properties.ReminderProperties;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Reminder;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.ReminderStatus;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.mappers.ReminderMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.ReminderRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.ForbiddenException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.ReminderAlreadyExistsException;
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
    private final ReminderProperties reminderProperties;

    @Transactional
    public void createTaskRemind(Long chatId, Task task, OffsetDateTime dateTime) {
        var alreadyExists =
                reminderRepository.existsByTaskIdAndRemindAtAndStatus(task.getId(), dateTime, ReminderStatus.SCHEDULED);
        if (alreadyExists) {
            throw new ReminderAlreadyExistsException();
        }

        var taskReminder = Reminder.builder()
                .chatId(chatId)
                .remindAt(dateTime)
                .status(ReminderStatus.SCHEDULED)
                .task(task)
                .build();
        reminderRepository.save(taskReminder);
    }

    @Transactional
    public List<ReminderDetails> lockDetails() {
        var scheduledBatch = reminderProperties.batch().scheduledSize();
        var reminderIds = reminderRepository.selectForUpdate(scheduledBatch);

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

    public List<ReminderDetails> findReminderDetailsList(Long chatId) {
        return reminderRepository.findRemindersWithTasks(chatId)
                .stream()
                .map(reminderMapper::toReminderDetails)
                .toList();
    }

    public ReminderDetails findReminderDetail(Long reminderId, Long chatId) {
        var reminder = reminderRepository.findUserReminderWithTasks(reminderId, chatId).orElseThrow(
                ForbiddenException::new);
        return reminderMapper.toReminderDetails(reminder);
    }

    @Transactional
    public void recoverStuckReminderDetails() {
        var batch = reminderProperties.batch().processingRetrySize();
        var timeOut = reminderProperties.batch().processingTimeoutMinutes();

        var reminderIds = reminderRepository.selectForUpdateStuckReminders(
                OffsetDateTime.now().minusMinutes(timeOut), batch);
        if (reminderIds.isEmpty()) {
            return;
        }
        reminderRepository.resetProcessingToScheduled(reminderIds);
    }
}
