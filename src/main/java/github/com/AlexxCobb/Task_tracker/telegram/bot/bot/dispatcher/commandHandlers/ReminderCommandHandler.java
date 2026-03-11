package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.commandHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderCommandHandler implements UpdateHandler {

    private final ReminderService reminderService;
    private final KeyboardService keyboardService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isTextMessage() && context.getText().equals("/reminders");
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var reminders = reminderService.findReminderDetailsList(chatId);
        var text =
                reminders.isEmpty() ? "Нет напоминаний для задач.\n\n" : "Есть напоминания для следующих задач: \n\n";

        return List.of(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .replyMarkup(keyboardService.getReminderSelectionKeyboard(reminders))
                        .build()
        );
    }
}
