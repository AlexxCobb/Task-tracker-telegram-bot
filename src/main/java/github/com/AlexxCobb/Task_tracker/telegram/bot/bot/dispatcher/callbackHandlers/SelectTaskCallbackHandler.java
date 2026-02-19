package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
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
public class SelectTaskCallbackHandler implements UpdateHandler {

    private final TaskService taskService;
    private final KeyboardService keyboardService;
    private final TaskMessageFormatter formatter;

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() &&
                context.dto().getType() == CallbackType.SELECT_TASK;
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {

        var chatId = context.chatId();
        var taskId = context.dto().getEntityId();
        var source = context.dto().getSource();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        var task = taskService.getTaskForUser(chatId, taskId);
        var text = formatter.formatTaskDetails(task);

        var keyboard = keyboardService.getTaskActionsKeyboard(task, source);

        return List.of(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text(text)
                        .replyMarkup(keyboard)
                        .build()
        );
    }
}
