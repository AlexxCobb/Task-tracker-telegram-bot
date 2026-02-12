package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.enums.KeyboardButton;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    public InlineKeyboardMarkup getStartKeyboard() {
        return keyboard(row(KeyboardButton.CREATE_TASK.toButton()),
                        row(KeyboardButton.CREATE_TASK_WITH_SUBTASKS.toButton()),
                        row(KeyboardButton.CREATE_SHOPPING_LIST.toButton()),
                        row(KeyboardButton.SHOW_ALL_TASKS.toButton()),
                        row(KeyboardButton.SHOW_ACTIVE_TASKS.toButton()),
                        row(KeyboardButton.SHOW_COMPLETED_TASKS.toButton()),
                        row(KeyboardButton.SHOW_SHOPPING_LIST.toButton()));
    }

    public InlineKeyboardMarkup getEditKeyboard(Long taskId) {
        return keyboard(
                row(KeyboardButton.TASK_EDIT.toButton(taskId), KeyboardButton.CREATE_ANOTHER_TASK.toButton(taskId)),
                row(KeyboardButton.TASK_DELETE.toButton(taskId), KeyboardButton.MAIN_MENU.toButton()));
    }

    public InlineKeyboardMarkup getListDoneKeyboard() {
        return keyboard(row(KeyboardButton.LIST_DONE.toButton()));
    }

    public InlineKeyboardMarkup getTasksActionsKeyboard(Long taskId) {
        return keyboard(
                row(KeyboardButton.TASK_EDIT.toButton(taskId),
                    KeyboardButton.TASK_COMPLETE.toButton(taskId),
                    KeyboardButton.TASK_DELETE.toButton(taskId)));
    }

    public InlineKeyboardMarkup getSubtaskActionsKeyboard(Long subtaskId) {
        return keyboard(
                row(KeyboardButton.SUBTASK_COMPLETE.toButton(subtaskId),
                    KeyboardButton.SUBTASK_DELETE.toButton(subtaskId)));
    }

    private InlineKeyboardRow row(InlineKeyboardButton... buttons) {
        return new InlineKeyboardRow(Arrays.asList(buttons));
    }

    private InlineKeyboardMarkup keyboard(InlineKeyboardRow... rows) {
        return InlineKeyboardMarkup.builder().keyboard(List.of(rows)).build();
    }
}
