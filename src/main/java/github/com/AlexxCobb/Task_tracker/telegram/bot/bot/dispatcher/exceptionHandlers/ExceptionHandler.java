package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.exceptionHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.BotException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ExceptionHandler {

    private final KeyboardService keyboardService;

    public PartialBotApiMethod<?> handleException(Update update, Exception e) {
        if (update.hasCallbackQuery()) {
            return handleCallbackException(update, e);
        }

        if (update.hasMessage()) {
            return handleMessageException(update, e);
        }

        throw new IllegalStateException("Unknown update type");
    }

    private EditMessageText handleCallbackException(Update update, Exception e) {

        var callback = update.getCallbackQuery();
        var chatId = callback.getMessage().getChatId();
        var messageId = callback.getMessage().getMessageId();

        var errorText = resolveMessage(e);
        var dto = CallbackDataMapper.toDtoFromData(callback.getData());

        var keyboard = keyboardService.buildBackKeyboard(
                dto.getSource(),
                dto.getParentId()
        );

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("⚠️ " + errorText)
                .replyMarkup(keyboard)
                .build();
    }

    private SendMessage handleMessageException(Update update, Exception e) {

        var chatId = update.getMessage().getChatId();
        var errorText = resolveMessage(e);

        return new SendMessage(chatId.toString(), "⚠️ " + errorText);
    }

    private String resolveMessage(Exception e) {
        if (e instanceof BotException botException) {
            return botException.getUserMessage();
        }
        return "Произошла ошибка. Попробуйте снова.";
    }
}
