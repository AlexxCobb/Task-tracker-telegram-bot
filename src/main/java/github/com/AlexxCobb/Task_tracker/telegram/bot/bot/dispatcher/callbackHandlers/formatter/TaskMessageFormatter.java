package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMessageFormatter {

    public String formatTask(List<Task> tasks) {

        if (tasks.isEmpty()) {
            return "📭 У вас пока нет задач";
        }

        var sb = new StringBuilder("📋 Ваши задачи:\n\n");

        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            var status = task.getStatus() == Status.DONE ? "✅" : "⏳";

            sb.append(i + 1)
                    .append(". ")
                    .append(task.getTitle())
                    .append(" ")
                    .append(status)
                    .append("\n");
            if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
                for (var sub : task.getSubtasks()) {
                    var subStatus = sub.getStatus() == Status.DONE ? "   └ ✅ " : "   └ ⏳ ";
                    sb.append(subStatus)
                            .append(sub.getTitle())
                            .append("\n");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public String formatTaskDetails(Task task) {

        var sb = new StringBuilder();
        var status = task.getStatus() == Status.DONE ? "✅" : "⏳";

        sb.append("📌 ")
                .append(task.getTitle())
                .append(" ")
                .append(status)
                .append("\n\n");

        if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {

            sb.append("Подзадачи:\n\n");

            for (int i = 0; i < task.getSubtasks().size(); i++) {
                var sub = task.getSubtasks().get(i);
                var subStatus = sub.getStatus() == Status.DONE ? "✅" : "⏳";

                sb.append(i + 1)
                        .append(". ")
                        .append(sub.getTitle())
                        .append(" ")
                        .append(subStatus)
                        .append("\n");
            }
        }

        return sb.toString();
    }

    public String formatSubtaskDetails(Subtask subtask) {

        var sb = new StringBuilder();
        var status = subtask.getStatus() == Status.DONE ? "✅" : "⏳";

        sb.append("📌 ")
                .append(subtask.getTitle())
                .append(" ")
                .append(status)
                .append("\n\n");

        return sb.toString();
    }
}
