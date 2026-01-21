package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class UpdateDispatcher {

    private final List<UpdateHandler> handlers;

    public UpdateDispatcher(List<UpdateHandler> handlers) {
        this.handlers = handlers;
    }

    public SendMessage dispatch(Update update) {
        return handlers.stream()
                .filter(h -> h.canHandle(update))
                .findFirst()
                .map(h -> h.handle(update))
                .orElse(null);
    }
}
