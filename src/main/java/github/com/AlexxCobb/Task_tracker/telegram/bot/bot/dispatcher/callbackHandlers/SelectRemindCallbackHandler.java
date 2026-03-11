package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter.TaskMessageFormatter;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SelectRemindCallbackHandler implements UpdateHandler {

    private final ReminderService reminderService;
    private final KeyboardService keyboardService;
    private final TaskMessageFormatter formatter;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.SELECT_REMIND);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {

        var reminderId = context.dto().getEntityId();
        var chatId = context.chatId();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        var reminderDetails = reminderService.findReminderDetail(reminderId, chatId);
        var text = formatter.formatTaskDetails(reminderDetails.taskDetails());

        return List.of(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text("Выбери что сделать c напоминанием для задачи :\n\n" + text)
                        .replyMarkup(
                                keyboardService.getReminderEditKeyboard(reminderDetails, CallbackType.SELECT_REMIND))
                        .build()
        );
    }
}
