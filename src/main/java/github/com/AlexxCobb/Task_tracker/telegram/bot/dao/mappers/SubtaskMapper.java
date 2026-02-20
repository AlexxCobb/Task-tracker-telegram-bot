package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.mappers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.SubtaskDetails;
import org.springframework.stereotype.Component;

@Component
public class SubtaskMapper {

    public SubtaskDetails toSubtaskDetails(Subtask subtask) {
        if (subtask == null) {
            return null;
        }

        return SubtaskDetails.builder()
                .id(subtask.getId())
                .title(subtask.getTitle())
                .status(subtask.getStatus())
                .build();
    }
}
