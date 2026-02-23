package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
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
public class DeleteCompletedTasksHandler implements UpdateHandler {

    private final TaskService taskService;
    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.DELETE_COMPLETED_TASKS);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {

        var chatId = context.chatId();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        taskService.removeCompletedTasks(chatId);

        return List.of(EditMessageText.builder()
                               .chatId(chatId)
                               .messageId(messageId)
                               .text("Выбери, что хочешь сделать:")
                               .replyMarkup(keyboardService.getStartKeyboard())
                               .build());
    }
}
