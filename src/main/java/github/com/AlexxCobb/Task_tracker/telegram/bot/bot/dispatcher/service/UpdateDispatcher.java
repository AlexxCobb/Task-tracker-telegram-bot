package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.exceptionHandlers.ExceptionHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final List<UpdateHandler> handlers;
    private final UserService userService;
    private final ExceptionHandler exceptionHandler;

    public SendMessage dispatch(Update update) {
        try {
            userService.ensureUser(update);

            return handlers.stream()
                    .filter(h -> h.canHandle(update))
                    .findFirst()
                    .map(h -> h.handle(update))
                    .orElse(null);

        } catch (Exception e) {
            return exceptionHandler.handleException(update, e);
        }

    }
}
