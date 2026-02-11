package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.messageHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TaskWithSubtaskMessageHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final TaskService taskService;

    @Override
    public Boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }

        var chatId = update.getMessage().getChatId();
        return dialogService.getStateOrDefault(chatId).equals(DialogState.AWAITING_TASK_WITH_SUBTASK_TITLE)
                || dialogService.getStateOrDefault(chatId)
                .equals(DialogState.AWAITING_TASK_WITH_SHOPPING_ITEMS_TITLE);
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getMessage().getChatId();

        if (dialogService.getStateOrDefault(chatId).equals(DialogState.AWAITING_TASK_WITH_SUBTASK_TITLE)) {
            var taskId = taskService.createEpicTask(chatId, update.getMessage().getText(), false);
            dialogService.setDialogState(chatId, DialogState.AWAITING_SUBTASK, taskId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("📝 Название основной задачи сохранено!\n\nНапиши подзадачу:")
                    .build();
        } else {
            var taskId = taskService.createEpicTask(chatId, update.getMessage().getText(), true);
            dialogService.setDialogState(chatId, DialogState.AWAITING_SHOPPING_ITEM, taskId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Название списка сохранено!\n\nНапиши, что нужно купить (каждую позицию новым сообщением):")
                    .build();
        }
    }
}
