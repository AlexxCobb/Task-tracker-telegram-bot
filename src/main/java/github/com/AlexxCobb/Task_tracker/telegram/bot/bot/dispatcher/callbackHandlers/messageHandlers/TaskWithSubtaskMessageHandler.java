package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.messageHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskWithSubtaskMessageHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final TaskService taskService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isTextMessage() && (context.dialogState().equals(DialogState.AWAITING_TASK_WITH_SUBTASK_TITLE)
                || context.dialogState().equals(DialogState.AWAITING_TASK_WITH_SHOPPING_ITEMS_TITLE));
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();

        if (context.dialogState().equals(DialogState.AWAITING_TASK_WITH_SUBTASK_TITLE)) {
            var taskId = taskService.createEpicTask(chatId, context.getText(), false);
            dialogService.setDialogState(chatId, DialogState.AWAITING_SUBTASK, taskId);

            return List.of(SendMessage.builder()
                                   .chatId(chatId)
                                   .text("📝 Название основной задачи сохранено!\n\nНапиши подзадачу:")
                                   .build());
        } else {
            var taskId = taskService.createEpicTask(chatId, context.getText(), true);
            dialogService.setDialogState(chatId, DialogState.AWAITING_SHOPPING_ITEM, taskId);

            return List.of(SendMessage.builder()
                                   .chatId(chatId)
                                   .text("Название списка сохранено!\n\nНапиши, что нужно купить (каждую позицию новым сообщением):")
                                   .build());
        }
    }
}
