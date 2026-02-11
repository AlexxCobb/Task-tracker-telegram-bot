package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.messageHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
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
public class SubtaskMessageHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final TaskService taskService;
    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }

        var chatId = update.getMessage().getChatId();
        return dialogService.getStateOrDefault(chatId).equals(
                DialogState.AWAITING_SUBTASK) || dialogService.getStateOrDefault(chatId)
                .equals(DialogState.AWAITING_SHOPPING_ITEM);
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getMessage().getChatId();
        var taskId = dialogService.getTaskId(chatId);

        taskService.createSubtaskWithEpic(taskId, update.getMessage().getText());

        var currentState = dialogService.getStateOrDefault(chatId);
        var isShoppingList = currentState.equals(DialogState.AWAITING_SHOPPING_ITEM);
        var responseText = isShoppingList ?
                "✅ Элемент списка добавлен!\n\nДобавь ещё или заверши:" :
                "✅ Подзадача добавлена!\n\nДобавь ещё или заверши:";

        return SendMessage.builder()
                .chatId(chatId)
                .text(responseText)
                .replyMarkup(keyboardService.getListDoneKeyboard())
                .build();
    }
}
