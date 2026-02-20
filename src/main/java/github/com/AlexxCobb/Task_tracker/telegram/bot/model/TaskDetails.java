package github.com.AlexxCobb.Task_tracker.telegram.bot.model;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.TypeOfTask;
import lombok.Builder;

import java.util.List;

@Builder
public record TaskDetails(
        Long id,
        String title,
        Status status,
        TypeOfTask type,
        List<SubtaskDetails> subtasks
) {
}
