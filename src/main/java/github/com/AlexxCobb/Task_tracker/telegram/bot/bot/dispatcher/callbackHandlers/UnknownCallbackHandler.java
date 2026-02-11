package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class UnknownCallbackHandler implements UpdateHandler {

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback();
    }

    @Override
    public SendMessage handle(UpdateContext context) {
        var chatId = context.chatId();

        return SendMessage.builder()
                .chatId(chatId)
                .text("Произошла ошибка")
                .build();
    }
}
