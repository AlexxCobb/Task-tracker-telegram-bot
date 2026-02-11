package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.exceptionHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.BotException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ExceptionHandler {

    public SendMessage handleException(Update update, Exception e) {
        var chatId = extractChatId(update);

        if (e instanceof BotException botException) {
            return new SendMessage(chatId.toString(), botException.getUserMessage());
        }

        return new SendMessage(chatId.toString(), "⚠️ Произошла ошибка. Попробуйте снова.");
    }

    private Long extractChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        throw new IllegalStateException("Unknown update type");
    }

}
