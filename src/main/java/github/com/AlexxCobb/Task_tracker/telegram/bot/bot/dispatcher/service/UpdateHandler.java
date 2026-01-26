package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    Boolean canHandle(Update update);
    SendMessage handle(Update update);
}
