package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.NavigationService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskCompleteCallbackHandler implements UpdateHandler {

    private final TaskService taskService;
    private final NavigationService navigationService;

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() && (
                context.dto().getType().equals(CallbackType.TASK_COMPLETE) ||
                        context.dto().getType().equals(CallbackType.SUBTASK_COMPLETE));
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var isTask = context.dto().getType() == CallbackType.TASK_COMPLETE;

        if (isTask) {
            taskService.completeTask(chatId, context.dto().getEntityId());
        } else {
            taskService.completeSubtask(chatId, context.dto().getEntityId());
        }

        return List.of(navigationService.returnAfterMutation(context));
    }
}
