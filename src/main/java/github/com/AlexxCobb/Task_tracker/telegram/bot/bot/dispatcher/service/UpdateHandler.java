package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface UpdateHandler {
    Boolean canHandle(UpdateContext context);

    SendMessage handle(UpdateContext context);
}
