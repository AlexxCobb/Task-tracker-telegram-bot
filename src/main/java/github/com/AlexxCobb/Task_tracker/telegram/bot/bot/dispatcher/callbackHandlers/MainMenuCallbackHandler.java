package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class MainMenuCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery()
                .getData()
                .equals(CallbackType.MAIN_MENU.name());
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();

        dialogService.clearState(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("""
                              Выбери, что хочешь сделать:
                              """)
                .replyMarkup(keyboardService.getStartKeyboard())
                .build();
    }
}
