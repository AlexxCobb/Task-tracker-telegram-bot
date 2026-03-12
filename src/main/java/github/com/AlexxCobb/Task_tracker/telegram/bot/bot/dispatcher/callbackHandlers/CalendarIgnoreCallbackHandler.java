package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

@Component
public class CalendarIgnoreCallbackHandler implements UpdateHandler {

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.CALENDAR_IGNORE);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        return List.of(
                AnswerCallbackQuery.builder()
                        .callbackQueryId(
                                context.update().getCallbackQuery().getId()
                        )
                        .build()
        );
    }
}
