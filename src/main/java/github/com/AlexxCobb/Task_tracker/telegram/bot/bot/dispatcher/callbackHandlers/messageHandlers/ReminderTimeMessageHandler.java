package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.messageHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.ReminderReturnService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.util.DateTimeConstants;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.TimeParseException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.TimeValidationException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.ReminderUseCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderTimeMessageHandler implements UpdateHandler {

    private final DialogService dialogService;
    private final ReminderUseCaseService reminderUseCaseService;
    private final ReminderReturnService reminderReturnService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isTextMessage() && context.dialogState().equals(DialogState.AWAITING_REMINDER_TIME);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var taskId = dialogService.getTaskId(chatId);
        var dateStr = dialogService.getRemindDate(chatId);

        LocalTime time;
        try {
            time = LocalTime.parse(context.getText().trim(), DateTimeFormatter.ofPattern("H:mm"));
        } catch (DateTimeParseException e) {
            throw new TimeParseException();
        }

        var date = LocalDate.parse(dateStr);
        var dateTime = LocalDateTime.of(date, time)
                .atOffset(ZoneOffset.ofHours(3));

        if (dateTime.isBefore(OffsetDateTime.now(ZoneOffset.ofHours(3)))) {
            throw new TimeValidationException();
        }

        reminderUseCaseService.createReminderUseCase(chatId, taskId, dateTime);
        dialogService.clearState(chatId);

        var formattedDateTime = dateTime.format(DateTimeConstants.DATE_TIME_FORMATTER);

        return List.of(SendMessage.builder()
                               .chatId(chatId)
                               .text("✅ Напоминание установлено на " + formattedDateTime)
                               .replyMarkup(reminderReturnService.resolveKeyboard(chatId, taskId,
                                                                                  context.dto() != null ? context.dto()
                                                                                          .getSource() : null))
                               .build());
    }
}
