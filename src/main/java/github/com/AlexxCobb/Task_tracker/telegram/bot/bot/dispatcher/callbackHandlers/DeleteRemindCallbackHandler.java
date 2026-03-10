package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.NavigationService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.ReminderUseCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteRemindCallbackHandler implements UpdateHandler {

    private final ReminderUseCaseService reminderUseCaseService;
    private final NavigationService navigationService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isCallback() && context.dto().getType().equals(CallbackType.CANCEL_REMIND);
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        reminderUseCaseService.cancelReminderUseCase(context.dto().getEntityId());

        return List.of(navigationService.returnAfterMutation(context));
    }
}
