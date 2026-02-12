package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final TaskService taskService;
    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() && (
                context.dto().getType().equals(CallbackType.TASK_DELETE) ||
                        context.dto().getType().equals(CallbackType.SUBTASK_DELETE));
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();

        var isTask = context.dto().getType() == CallbackType.TASK_DELETE;

        if (isTask) {
            taskService.removeTask(chatId, context.dto().getEntityId());
        } else {
            taskService.removeSubtask(context.dto().getEntityId());
        }
        dialogService.clearState(chatId);

        return List.of(SendMessage.builder()
                               .chatId(chatId)
                               .text("Задача удалена!\n\nВыбери, что хочешь сделать:")
                               .replyMarkup(keyboardService.getStartKeyboard())
                               .build());
    }
}
