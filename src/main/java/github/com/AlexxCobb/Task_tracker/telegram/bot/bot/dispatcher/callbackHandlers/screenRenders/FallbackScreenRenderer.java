package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.screenRenders;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
@RequiredArgsConstructor
public class FallbackScreenRenderer {

    private final KeyboardService keyboardService;

    public EditMessageText fallback(UpdateContext context) {
        return EditMessageText.builder()
                .chatId(context.chatId())
                .messageId(context.update().getCallbackQuery().getMessage().getMessageId())
                .text("Выбери, что хочешь сделать:")
                .replyMarkup(keyboardService.getStartKeyboard())
                .build();
    }
}
