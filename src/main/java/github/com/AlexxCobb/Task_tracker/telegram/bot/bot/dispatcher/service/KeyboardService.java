package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.enums.KeyboardButton;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Arrays;
import java.util.List;

@Service
public class KeyboardService {

    public InlineKeyboardMarkup getStartKeyboard() {
        return keyboard(row(KeyboardButton.CREATE_TASK, KeyboardButton.CREATE_TASK_WITH_SUBTASKS),
                        row(KeyboardButton.CREATE_SHOPPING_LIST, KeyboardButton.SHOW_TASKS));
    }

    public InlineKeyboardMarkup getEditKeyboard() {
        return keyboard(row(KeyboardButton.TASK_EDIT, KeyboardButton.TASK_COMPLETE),
                        row(KeyboardButton.TASK_DELETE, KeyboardButton.RETURN));
    }

    private InlineKeyboardRow row(KeyboardButton... buttons) {
        return new InlineKeyboardRow(Arrays.stream(buttons).map(KeyboardButton::toButton).toList());
    }

    private InlineKeyboardMarkup keyboard(InlineKeyboardRow... rows) {
        return InlineKeyboardMarkup.builder().keyboard(List.of(rows)).build();
    }
}
