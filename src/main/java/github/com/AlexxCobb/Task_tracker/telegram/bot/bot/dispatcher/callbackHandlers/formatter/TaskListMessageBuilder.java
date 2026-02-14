package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskListMessageBuilder {

    private final TaskMessageFormatter formatter;
    private final KeyboardService keyboardService;

    public List<PartialBotApiMethod<?>> buildTaskList(Long chatId, List<Task> tasks, boolean isShoppingList) {
        return List.of(buildTaskButton(chatId, tasks));
    }

    private SendMessage buildTaskButton(Long chatId, List<Task> tasks) {
        return
                SendMessage.builder()
                        .chatId(chatId)
                        .text(formatter.formatTask(tasks))
                        .replyMarkup(keyboardService.getTaskSelectionKeyboard(tasks))
                        .build();
    }
}
