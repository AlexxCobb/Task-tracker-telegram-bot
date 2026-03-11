package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.SubtaskDetails;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.TaskDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMessageFormatter {

    public String formatTask(List<TaskDetails> tasks) {

        if (tasks.isEmpty()) {
            return "📭 У вас пока нет задач";
        }

        var sb = new StringBuilder("📋 Ваши задачи:\n\n");

        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            var status = task.status() == Status.DONE ? "✅" : "⏳";

            sb.append(i + 1)
                    .append(". ")
                    .append(task.title())
                    .append(" ")
                    .append(status)
                    .append("\n");
            if (task.subtasks() != null && !task.subtasks().isEmpty()) {
                for (var sub : task.subtasks()) {
                    var subStatus = sub.status() == Status.DONE ? "   └ ✅ " : "   └ ⏳ ";
                    sb.append(subStatus)
                            .append(sub.title())
                            .append("\n");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public String formatTaskDetails(TaskDetails taskDetails) {

        var sb = new StringBuilder();
        var status = taskDetails.status() == Status.DONE ? "✅" : "⏳";

        sb.append("📌 ")
                .append(taskDetails.title())
                .append(" ")
                .append(status)
                .append("\n\n");

        if (taskDetails.subtasks() != null && !taskDetails.subtasks().isEmpty()) {

            sb.append("Подзадачи:\n\n");

            for (int i = 0; i < taskDetails.subtasks().size(); i++) {
                var sub = taskDetails.subtasks().get(i);
                var subStatus = sub.status() == Status.DONE ? "✅" : "⏳";

                sb.append(i + 1)
                        .append(". ")
                        .append(sub.title())
                        .append(" ")
                        .append(subStatus)
                        .append("\n");
            }
        }

        return sb.toString();
    }

    public String formatSubtaskDetails(SubtaskDetails subtaskDetails) {

        var sb = new StringBuilder();
        var status = subtaskDetails.status() == Status.DONE ? "✅" : "⏳";

        sb.append("📌 ")
                .append(subtaskDetails.title())
                .append(" ")
                .append(status)
                .append("\n\n");

        return sb.toString();
    }

    public String formatShoppingList(List<TaskDetails> lists) {
        if (lists.isEmpty()) {
            return "📭 У вас пока нет списков покупок";
        }

        var sb = new StringBuilder("🛒 Списки покупок:\n\n");

        for (int i = 0; i < lists.size(); i++) {
            var list = lists.get(i);

            sb.append(i + 1)
                    .append(". ")
                    .append(list.title())
                    .append("\n");

            if (list.subtasks() != null && !list.subtasks().isEmpty()) {
                for (var item : list.subtasks()) {
                    var checkbox = item.status() == Status.DONE ? "   └ ☑️ " : "   └ ☐ ";
                    sb.append(checkbox)
                            .append(item.status() == Status.DONE
                                            ? "~" + item.title() + "~"
                                            : item.title())
                            .append("\n");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
