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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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

        var task = taskService.getTaskForUser(chatId, taskId);

        return List.of(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(formatter.formatTaskDetails(task))
                        .replyMarkup(keyboardService.getTasksActionsKeyboard(task))
                        .build()
        );
    }
}
