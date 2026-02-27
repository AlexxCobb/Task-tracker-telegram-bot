package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EditCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.TASK_EDIT);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        dialogService.setDialogState(chatId, DialogState.EDIT_TASK, context.dto().getEntityId());

        return List.of(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text("✍️ Напиши новое название:")
                        .build()
        );
    }
}
