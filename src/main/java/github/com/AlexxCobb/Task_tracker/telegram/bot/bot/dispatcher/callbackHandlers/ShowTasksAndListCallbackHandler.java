package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter.TaskListMessageBuilder;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowTasksAndListCallbackHandler implements UpdateHandler {

    private final TaskService taskService;
    private final TaskListMessageBuilder messageBuilder;

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() && (
                context.dto().getType().equals(CallbackType.SHOW_SHOPPING_LIST) ||
                        context.dto().getType().equals(CallbackType.SHOW_ALL_TASKS) ||
                        context.dto().getType().equals(CallbackType.SHOW_ACTIVE_TASKS) ||
                        context.dto().getType().equals(CallbackType.SHOW_COMPLETED_TASKS));
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();

        var isShoppingList = context.dto().getType() == CallbackType.SHOW_SHOPPING_LIST;
        var tasks = isShoppingList ? taskService.getShoppingList(chatId) : taskService.getTasks(chatId);

        return messageBuilder.buildTaskList(chatId, tasks, isShoppingList);
    }
}
