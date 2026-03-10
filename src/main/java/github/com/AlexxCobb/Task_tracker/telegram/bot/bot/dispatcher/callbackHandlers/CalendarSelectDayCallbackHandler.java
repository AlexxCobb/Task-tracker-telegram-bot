package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.CalendarKeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CalendarSelectDayCallbackHandler implements UpdateHandler {

    private final CalendarKeyboardService keyboardService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.CALENDAR_SELECT_DAY);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var taskId = context.dto().getEntityId();
        var date = parseDate(context.dto().getExtra());
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        return List.of(EditMessageText.builder()
                               .chatId(chatId)
                               .messageId(messageId)
                               .text("Выбери время для напоминания:")
                               .replyMarkup(keyboardService.getTimeKeyboard(taskId, date, context.dto().getSource()))
                               .build());
    }

    private LocalDate parseDate(String extra) {
        if (extra == null || extra.isBlank()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(extra);
        } catch (DateTimeParseException e) {
            return LocalDate.now();
        }
    }
}
