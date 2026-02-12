package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class TaskMessageFormatter {

    public String formatTask(Task task) {

        var sb = new StringBuilder();
        sb.append("**  ").append(task.getTitle()).append("  **\n");

        if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
            sb.append("\n");
            for (var subtask : task.getSubtasks()) {
                var statusEmoji = subtask.getStatus() == Status.DONE ? "✅" : "⏳";
                sb.append(statusEmoji).append(" ").append(subtask.getTitle()).append("\n");
            }
        }
        return sb.toString();
    }

    public String formatHeader(boolean isShoppingList, boolean isEmpty) {
        if (isEmpty) {
            return isShoppingList ? "\uD83D\uDED2 Списков покупок пока нет" : "📭 У вас пока нет задач";
        }
        return isShoppingList ? "\uD83D\uDED2 ** Ваш список покупок: **\n\n" : "📋 ** Ваши задачи: **\n\n";
    }
}
