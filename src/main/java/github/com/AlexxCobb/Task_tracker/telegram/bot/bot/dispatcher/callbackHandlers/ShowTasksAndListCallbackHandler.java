package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.TaskStatusFilter;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter.TaskMessageFormatter;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowTasksAndListCallbackHandler implements UpdateHandler {

    private final TaskService taskService;
    private final TaskMessageFormatter formatter;
    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(UpdateContext context) {
        if (!context.isCallback()) return false;

        var type = context.dto().getType();

        return type == CallbackType.SHOW_ALL_TASKS
                || type == CallbackType.SHOW_ACTIVE_TASKS
                || type == CallbackType.SHOW_COMPLETED_TASKS
                || type == CallbackType.SHOW_SHOPPING_LIST;
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var callbackType = context.dto().getType();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        var filter = callbackType.toFilter();
        if (filter == null) {
            filter = TaskStatusFilter.ALL;
        }

        var taskDetailsList = taskService.getTasks(chatId, filter);
        var text = formatter.formatTask(taskDetailsList);
        var keyboard = keyboardService.getTasksSelectionKeyboard(taskDetailsList, filter);

        return List.of(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text(text)
                        .parseMode("Markdown")
                        .replyMarkup(keyboard)
                        .build()
        );
    }
}
