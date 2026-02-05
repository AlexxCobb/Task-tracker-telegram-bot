package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class EditCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final TaskService taskService;
    private final CallbackDataMapper dataMapper;

    @Override
    public Boolean canHandle(Update update) {
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var dto = dataMapper.toDtoFromData(data);
            return dto.getType().equals(CallbackType.TASK_EDIT);
        }
        return false;
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        var data = update.getCallbackQuery().getData();
        var dto = dataMapper.toDtoFromData(data);

        dialogService.setState(chatId, DialogState.EDIT_TASK);
        taskService.getTask(chatId, dto.getEntityId());

        return SendMessage.builder()
                .chatId(chatId)
                .text("✍️ Напиши новое название:")
                .build();
    }
}
