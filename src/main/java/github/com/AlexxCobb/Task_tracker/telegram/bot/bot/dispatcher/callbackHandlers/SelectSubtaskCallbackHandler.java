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
public class SelectSubtaskCallbackHandler implements UpdateHandler {

    private final TaskService taskService;
    private final KeyboardService keyboardService;
    private final TaskMessageFormatter formatter;

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType() == CallbackType.SELECT_SUBTASK;
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {

        var chatId = context.chatId();
        var subtaskId = context.dto().getEntityId();
        var taskId = context.dto().getParentId();
        var source = context.dto().getSource();

        var subtask = taskService.getSubtaskForUser(chatId, subtaskId);
        var text = formatter.formatSubtaskDetails(subtask);
        var keyboard = keyboardService.getSubtaskActionsKeyboard(subtaskId, taskId, source);

        return List.of(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(context.update()
                                           .getCallbackQuery()
                                           .getMessage()
                                           .getMessageId())
                        .text(text)
                        .replyMarkup(keyboard)
                        .build()
        );
    }
}
