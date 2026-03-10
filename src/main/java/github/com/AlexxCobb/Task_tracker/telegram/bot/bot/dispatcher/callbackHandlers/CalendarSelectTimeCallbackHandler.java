package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.util.DateTimeConstants;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.ReminderUseCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalendarSelectTimeCallbackHandler implements UpdateHandler {

    private final ReminderUseCaseService reminderUseCaseService;
    private final DialogService dialogService;
    private final KeyboardService keyboardService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.CALENDAR_SELECT_TIME);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var taskId = context.dto().getEntityId();
        var dateTimeStr = context.dto().getExtra(); // "2026-03-15T09:00"
        log.debug("Parsing dateTime: '{}'", dateTimeStr);
        var messageId = context.update().getCallbackQuery().getMessage().getMessageId();

        var dateTime = LocalDateTime.parse(dateTimeStr, DateTimeConstants.DATE_TIME_FORMATTER)
                .atOffset(ZoneOffset.ofHours(3));

        reminderUseCaseService.createReminderUseCase(chatId, taskId, dateTime);
        dialogService.clearState(chatId);

        return List.of(EditMessageText.builder()
                               .chatId(chatId)
                               .messageId(messageId)
                               .text("✅ Напоминание установлено на " + dateTimeStr.replace("T", " "))
                               .replyMarkup(keyboardService.getStartKeyboard())
                               .build());
    }
}
