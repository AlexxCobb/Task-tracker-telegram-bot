package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ShowTasksCallbackHandler implements UpdateHandler {

    private final TaskService taskService;

    @Override
    public Boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery()
                .getData()
                .equals(CallbackType.SHOW_TASKS.name());
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();

        var task = taskService.getTasks(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Список ЗАДАЧ:")
                .text(task)
                .build();
    }
}
