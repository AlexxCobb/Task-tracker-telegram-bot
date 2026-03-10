package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.CalendarKeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CalendarNavigateCallbackHandler implements UpdateHandler {

    private final CalendarKeyboardService keyboardService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.CALENDAR_NAVIGATE);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var taskId = context.dto().getEntityId();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        var yearMonth = parseYearMonth(context.dto().getExtra());

        return List.of(EditMessageText.builder()
                               .chatId(chatId)
                               .messageId(messageId)
                               .text("Выбери дату для напоминания:")
                               .replyMarkup(keyboardService.getCalendarKeyboard(taskId, yearMonth,
                                                                                context.dto().getSource()))
                               .build());
    }

    private YearMonth parseYearMonth(String extra) {
        if (extra == null || extra.isBlank()) {
            return YearMonth.now();
        }
        try {
            return YearMonth.parse(extra);
        } catch (DateTimeParseException e) {
            return YearMonth.now();
        }
    }
}
