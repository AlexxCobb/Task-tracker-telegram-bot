package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReturnToMenuCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() && (context.dto().getType().equals(CallbackType.LIST_DONE)
                || context.dto().getType().equals(CallbackType.MAIN_MENU));
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        dialogService.clearState(chatId);

        var text =
                context.dto().getType() == CallbackType.LIST_DONE ? "✅ Список сохранен!\n\nВыбери, что хочешь сделать:"
                        : "\nВыбери, что хочешь сделать:";

        return List.of(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text(text)
                        .replyMarkup(keyboardService.getStartKeyboard())
                        .build()
        );
    }
}
