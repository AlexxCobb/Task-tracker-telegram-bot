package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.commandHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {

    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start");
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        return SendMessage.builder()
                .chatId(chatId)
                .text("""
                              Привет! 👋
                              Я помогу тебе:
                              • создавать задачи
                              • вести списки покупок
                              • отмечать выполненное
                              
                              Выбери, что хочешь сделать:
                              """)
                .replyMarkup(keyboardService.getStartKeyboard()).build();
    }
}
