package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import org.telegram.telegrambots.meta.api.objects.Update;


public record UpdateContext(
        Update update,
        Long chatId,
        CallbackDto dto,
        DialogState dialogState
) {

    public String getText() {
        return update.getMessage().getText();
    }

    public boolean isTextMessage() {
        return update.hasMessage() && update.getMessage().hasText();
    }

    public boolean isCallback() {
        return update.hasCallbackQuery();
    }
}
