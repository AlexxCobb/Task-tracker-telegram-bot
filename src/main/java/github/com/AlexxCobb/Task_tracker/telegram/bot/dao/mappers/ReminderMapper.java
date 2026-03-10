package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.mappers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Reminder;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.ReminderDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReminderMapper {

    private final TaskMapper taskMapper;

    public ReminderDetails toReminderDetails(Reminder reminder) {
        if (reminder == null) {
            return null;
        }

        return ReminderDetails.builder()
                .id(reminder.getId())
                .chatId(reminder.getChatId())
                .status(reminder.getStatus())
                .remindAt(reminder.getRemindAt())
                .taskDetails(taskMapper.toTaskDetails(reminder.getTask()))
                .build();
    }
}
