package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.mappers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.SubtaskDetails;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.TaskDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TaskMapper {

    public TaskDetails toTaskDetails(Task task) {
        if (task == null) {
            return null;
        }

        return TaskDetails.builder()
                .id(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .type(task.getType())
                .subtasks(toSubtaskDetailsList(task.getSubtasks()))
                .build();
    }

    public List<TaskDetails> toTaskDetailsList(List<Task> tasks) {
        if (tasks == null) {
            return Collections.emptyList();
        }
        return tasks.stream()
                .map(this::toTaskDetails)
                .toList();
    }

    private List<SubtaskDetails> toSubtaskDetailsList(List<Subtask> subtasks) {
        if (subtasks == null || subtasks.isEmpty()) {
            return Collections.emptyList();
        }
        return subtasks.stream()
                .map(this::toSubtaskDetails)
                .toList();
    }

    private SubtaskDetails toSubtaskDetails(Subtask subtask) {
        return SubtaskDetails.builder()
                .id(subtask.getId())
                .title(subtask.getTitle())
                .status(subtask.getStatus())
                .build();
    }
}
