package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.formatter.TaskMessageFormatter;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.ReminderDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderNotificationService {

    private final TelegramClient telegramClient;
    private final KeyboardService keyboardService;
    private final TaskMessageFormatter messageFormatter;

    public List<Long> sendAll(List<ReminderDetails> reminders) {
        var sentReminderIds = new ArrayList<Long>();

        for (ReminderDetails reminderDetails : reminders) {
            try {
                send(reminderDetails);
                sentReminderIds.add(reminderDetails.id());
            } catch (TelegramApiException e) {
                log.error("Failed to send reminder id={}", reminderDetails.id(), e);
            }
        }
        return sentReminderIds;
    }

    private void send(ReminderDetails details) throws TelegramApiException {
        var keyboard = keyboardService.getReminderKeyboard(details);
        var text = messageFormatter.formatTaskDetails(details.taskDetails());
        telegramClient.execute(SendMessage.builder()
                                       .chatId(details.chatId())
                                       .replyMarkup(keyboard)
                                       .text("\uD83D\uDD14 Напоминание! \n\n" + text)
                                       .build());
    }

}
