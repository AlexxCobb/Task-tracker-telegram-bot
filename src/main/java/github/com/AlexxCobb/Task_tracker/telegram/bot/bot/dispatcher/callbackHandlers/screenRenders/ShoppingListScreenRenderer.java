package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.screenRenders;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter.TaskMessageFormatter;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
@RequiredArgsConstructor
public class ShoppingListScreenRenderer {

    private final TaskService taskService;
    private final KeyboardService keyboardService;
    private final TaskMessageFormatter formatter;

    public EditMessageText renderList(UpdateContext context) {
        var chatId = context.chatId();
        var messageId = context.update().getCallbackQuery().getMessage().getMessageId();
        var lists = taskService.getShoppingLists(chatId);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(formatter.formatShoppingList(lists))
                .replyMarkup(keyboardService.getShoppingListSelectionKeyboard(lists))
                .build();
    }
}