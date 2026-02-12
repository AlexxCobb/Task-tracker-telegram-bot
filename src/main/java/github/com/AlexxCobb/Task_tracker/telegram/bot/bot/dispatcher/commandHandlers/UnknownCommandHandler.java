package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.commandHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
public class UnknownCommandHandler implements UpdateHandler {
    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isTextMessage();
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();

        return List.of(SendMessage.builder()
                               .chatId(chatId)
                               .text("Такой команды нет")
                               .build());
    }
}
