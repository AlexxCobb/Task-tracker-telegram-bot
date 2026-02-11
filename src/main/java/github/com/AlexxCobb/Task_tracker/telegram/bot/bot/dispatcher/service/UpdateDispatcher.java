package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.CallbackDto;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.exceptionHandlers.ExceptionHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
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
    private final DialogService dialogService;

    public SendMessage dispatch(Update update) {
        try {
            var chatId = extractChatId(update);
            userService.ensureUser(update);

            var dto = parseDto(update);
            var dialogState = dialogService.getStateOrDefault(chatId);
            var context = new UpdateContext(update, chatId, dto, dialogState);

            return handlers.stream()
                    .filter(h -> h.canHandle(context))
                    .findFirst()
                    .map(h -> h.handle(context))
                    .orElse(null);

        } catch (Exception e) {
            return exceptionHandler.handleException(update, e);
        }
    }

    private Long extractChatId(Update update) {
        if (update.hasMessage())
            return update.getMessage().getChatId();
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChatId();
        throw new IllegalStateException("Unknown update type");
    }

    private CallbackDto parseDto(Update update) {
        if (update.hasCallbackQuery()) {
            return CallbackDataMapper.toDtoFromData(update.getCallbackQuery().getData());
        }
        return null;
    }
}
