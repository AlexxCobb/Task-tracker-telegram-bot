package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class CreateShoppingListCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.CREATE_SHOPPING_LIST);
    }

    @Override
    public SendMessage handle(UpdateContext context) {
        var chatId = context.chatId();

        dialogService.setDialogState(chatId, DialogState.AWAITING_TASK_WITH_SHOPPING_ITEMS_TITLE, null);

        return SendMessage.builder()
                .chatId(chatId)
                .text("✍️ Введи название списка:")
                .build();
    }
}
