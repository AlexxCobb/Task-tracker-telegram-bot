package github.com.AlexxCobb.Task_tracker.telegram.bot.model;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.ReminderStatus;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder(toBuilder = true)
public record ReminderDetails(
        Long id,
        Long chatId,
        TaskDetails taskDetails,
        OffsetDateTime remindAt,
        ReminderStatus status
) {
}
