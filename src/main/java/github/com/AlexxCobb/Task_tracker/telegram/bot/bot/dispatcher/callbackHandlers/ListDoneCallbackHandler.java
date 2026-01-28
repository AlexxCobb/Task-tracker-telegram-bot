package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ListDoneCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final TaskService taskService;
    private final KeyboardService keyboardService;
    private final CallbackDataMapper dataMapper;

    @Override
    public Boolean canHandle(Update update) {
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var dto = dataMapper.toDtoFromData(data);
            return dto.getType().equals(CallbackType.LIST_DONE);
        }
        return false;
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();

        dialogService.clearState(chatId);
        taskService.finishingCreateEpicTask(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("✅ Список сохранен!\n\nВыбери, что хочешь сделать:")
                .replyMarkup(keyboardService.getStartKeyboard())
                .build();
    }
}
