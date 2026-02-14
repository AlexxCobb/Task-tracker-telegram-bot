package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.enums.KeyboardButton;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    public InlineKeyboardMarkup getStartKeyboard() {
        return keyboard(row(KeyboardButton.CREATE_TASK.toButton()),
                        row(KeyboardButton.CREATE_TASK_WITH_SUBTASKS.toButton()),
                        row(KeyboardButton.SHOW_ALL_TASKS.toButton()),
                        row(KeyboardButton.SHOW_ACTIVE_TASKS.toButton()),
                        row(KeyboardButton.SHOW_COMPLETED_TASKS.toButton()));
    }

    public InlineKeyboardMarkup getEditKeyboard(Long taskId) {
        return keyboard(
                row(KeyboardButton.TASK_EDIT.toButton(taskId), KeyboardButton.CREATE_ANOTHER_TASK.toButton(taskId)),
                row(KeyboardButton.TASK_DELETE.toButton(taskId), KeyboardButton.MAIN_MENU.toButton()));
    }

    public InlineKeyboardMarkup getListDoneKeyboard() {
        return keyboard(row(KeyboardButton.LIST_DONE.toButton()));
    }

    public InlineKeyboardMarkup getTasksActionsKeyboard(Task task) {

        List<InlineKeyboardRow> rows = new ArrayList<>();

        rows.add(row(
                KeyboardButton.TASK_EDIT.toButton(task.getId()),
                KeyboardButton.TASK_COMPLETE.toButton(task.getId()),
                KeyboardButton.TASK_DELETE.toButton(task.getId())
        ));

        if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
            rows.add(row(
                    KeyboardButton.OPEN_SUBTASKS.toButton(task.getId())
            ));
        }

        rows.add(row(
                KeyboardButton.MAIN_MENU.toButton()
        ));

        return keyboard(rows.toArray(new InlineKeyboardRow[0]));
    }

    public InlineKeyboardMarkup getSubtaskSelectionKeyboard(List<Subtask> subtasks) {

        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (Subtask subtask : subtasks) {
            rows.add(row(
                    KeyboardButton.SELECT_SUBTASK.toButton(subtask.getId(), subtask.getTitle())
            ));
        }

        rows.add(row(
                KeyboardButton.MAIN_MENU.toButton()
        ));

        return keyboard(rows.toArray(new InlineKeyboardRow[0]));
    }

    public InlineKeyboardMarkup getSubtaskActionsKeyboard(Long subtaskId) {
        return keyboard(
                row(KeyboardButton.SUBTASK_COMPLETE.toButton(subtaskId),
                    KeyboardButton.SUBTASK_DELETE.toButton(subtaskId)));
    }

    public InlineKeyboardMarkup getTaskSelectionKeyboard(List<Task> tasks) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (Task task : tasks) {
            rows.add(row(
                    KeyboardButton.SELECT_TASK.toButton(task.getId(), task.getTitle())
            ));
        }
        rows.add(row(KeyboardButton.MAIN_MENU.toButton()));

        return keyboard(rows.toArray(new InlineKeyboardRow[0]));
    }

    private InlineKeyboardRow row(InlineKeyboardButton... buttons) {
        return new InlineKeyboardRow(Arrays.asList(buttons));
    }

    private InlineKeyboardMarkup keyboard(InlineKeyboardRow... rows) {
        return InlineKeyboardMarkup.builder().keyboard(List.of(rows)).build();
    }
}
