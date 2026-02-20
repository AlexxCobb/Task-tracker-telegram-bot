package github.com.AlexxCobb.Task_tracker.telegram.bot.model;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import lombok.Builder;

@Builder
public record SubtaskDetails(
        Long id,
        String title,
        Status status
) {
}
