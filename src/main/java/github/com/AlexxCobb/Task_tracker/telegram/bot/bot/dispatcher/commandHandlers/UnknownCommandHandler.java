package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.commandHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UnknownCommandHandler implements UpdateHandler {
    @Override
    public Boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getMessage().getChatId().toString();

        return SendMessage.builder()
                .chatId(chatId)
                .text("Такой команды нет")
                .build();
    }
}
