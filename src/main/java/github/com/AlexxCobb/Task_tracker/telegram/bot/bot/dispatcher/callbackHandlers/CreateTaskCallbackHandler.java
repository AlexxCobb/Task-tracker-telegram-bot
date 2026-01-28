package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CreateTaskCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final CallbackDataMapper dataMapper;

    @Override
    public Boolean canHandle(Update update) {
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var dto = dataMapper.toDtoFromData(data);
            return dto.getType().equals(CallbackType.CREATE_TASK);
        }
        return false;
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();

        dialogService.setState(chatId, DialogState.AWAITING_TASK_TITLE);

        return SendMessage.builder()
                .chatId(chatId)
                .text("✍️ Введи название задачи:")
                .build();
    }
}
