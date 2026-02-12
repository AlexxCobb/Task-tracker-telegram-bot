package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskListMessageBuilder {

    private final TaskMessageFormatter formatter;
    private final KeyboardService keyboardService;

    public List<PartialBotApiMethod<?>> buildTaskList(Long chatId, List<Task> tasks, boolean isShoppingList) {

        List<PartialBotApiMethod<?>> messages = new ArrayList<>();
        messages.add(buildHeader(chatId, tasks, isShoppingList));

        if (tasks.isEmpty()) {
            messages.add(buildBackButton(chatId));
        }

        for (Task task : tasks) {
            messages.add(buildTaskButton(chatId, task));
            messages.addAll(buildSubtaskMessages(chatId, task));
        }

        return messages;
    }

    private SendMessage buildHeader(Long chatId, List<Task> tasks, boolean isShoppingList) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(formatter.formatHeader(isShoppingList, tasks.isEmpty()))
                .build();
    }

    private SendMessage buildBackButton(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Выбери действие:")
                .replyMarkup(keyboardService.getStartKeyboard())
                .build();
    }

    private SendMessage buildTaskButton(Long chatId, Task task) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(formatter.formatTask(task))
                .parseMode("Markdown")
                .replyMarkup(keyboardService.getTasksActionsKeyboard(task.getId()))
                .build();
    }

    private List<PartialBotApiMethod<?>> buildSubtaskMessages(Long chatId, Task task) {
        if (task.getSubtasks() == null) {
            return Collections.emptyList();
        }
        return task.getSubtasks().stream()
                .filter(s -> s.getStatus() != Status.DONE)
                .map(subtask -> SendMessage.builder()
                        .chatId(chatId)
                        .text("   └ ⏳ " + subtask.getTitle())
                        .replyMarkup(keyboardService.getSubtaskActionsKeyboard(subtask.getId()))
                        .build())
                .<PartialBotApiMethod<?>>map(msg -> msg)
                .toList();
    }
}
