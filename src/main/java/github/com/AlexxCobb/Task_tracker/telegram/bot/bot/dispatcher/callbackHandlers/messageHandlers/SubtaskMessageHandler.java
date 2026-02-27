package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.messageHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
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
public class SubtaskMessageHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final TaskService taskService;
    private final KeyboardService keyboardService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isTextMessage() && (
                context.dialogState() == DialogState.AWAITING_SUBTASK
                        || context.dialogState() == DialogState.AWAITING_SHOPPING_ITEM
        );
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {

        var chatId = context.chatId();
        var taskId = dialogService.getTaskId(chatId);

        taskService.createSubtaskWithEpic(taskId, context.getText());

        var isShoppingList = context.dialogState().equals(DialogState.AWAITING_SHOPPING_ITEM);
        var responseText = isShoppingList ?
                "✅ Элемент списка добавлен!\n\nДобавь ещё или заверши:" :
                "✅ Подзадача добавлена!\n\nДобавь ещё или заверши:";

        return List.of(SendMessage.builder()
                               .chatId(chatId)
                               .text(responseText)
                               .replyMarkup(keyboardService.getListDoneKeyboard())
                               .build());
    }
}
