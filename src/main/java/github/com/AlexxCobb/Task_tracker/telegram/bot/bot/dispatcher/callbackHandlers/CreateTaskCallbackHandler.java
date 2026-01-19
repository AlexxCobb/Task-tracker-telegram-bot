package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CreateTaskCallbackHandler implements UpdateHandler {

    @Override
    public Boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery()
                .getData()
                .equals(CallbackType.CREATE_TASK.name());
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId().toString();

        return SendMessage.builder()
                .chatId(chatId)
                .text("✍️ Введите название задачи:")
                .build();
    }
}
