package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

public interface UpdateHandler {
    boolean canHandle(UpdateContext context);

    List<PartialBotApiMethod<?>> handle(UpdateContext context);
}
