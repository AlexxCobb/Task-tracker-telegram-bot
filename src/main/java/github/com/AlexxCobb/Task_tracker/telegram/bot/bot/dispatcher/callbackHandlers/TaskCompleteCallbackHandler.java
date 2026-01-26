package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TaskCompleteCallbackHandler implements UpdateHandler {

    private final TaskService taskService;
    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery()
                .getData()
                .equals(CallbackType.TASK_COMPLETE.name());
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();

        taskService.removeTask(chatId); // сделать логику чтобы задача отмечалась галочкой, флаг у сущности

        return SendMessage.builder()
                .chatId(chatId)
                .text("""
                              ✍️ Задача выполнена!
                              
                              Выбери, что хочешь сделать:
                              """)
                .replyMarkup(keyboardService.getStartKeyboard())
                .build();
    }
}
