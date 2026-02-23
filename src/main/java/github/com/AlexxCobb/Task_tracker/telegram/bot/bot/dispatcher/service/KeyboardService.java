package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.TaskStatusFilter;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.CallbackDto;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.enums.KeyboardButton;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.SubtaskDetails;
import github.com.AlexxCobb.Task_tracker.telegram.bot.model.TaskDetails;
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

    public InlineKeyboardMarkup getTaskActionsKeyboard(TaskDetails taskDetails, CallbackType source) {

        List<InlineKeyboardRow> rows = new ArrayList<>();

        rows.add(row(
                KeyboardButton.TASK_EDIT.toButton(taskDetails.id(), null, source),
                KeyboardButton.TASK_COMPLETE.toButton(taskDetails.id(), null, source),
                KeyboardButton.TASK_DELETE.toButton(taskDetails.id(), null, source)
        ));

        if (taskDetails.subtasks() != null && !taskDetails.subtasks().isEmpty()) {
            rows.add(row(
                    KeyboardButton.OPEN_SUBTASKS.toButton(taskDetails.id(), null, source)
            ));
        }
        rows.add(row(KeyboardButton.BACK_TO.backButton(source, null, null, source)));

        return keyboard(rows.toArray(new InlineKeyboardRow[0]));
    }

    public InlineKeyboardMarkup getTasksSelectionKeyboard(List<TaskDetails> tasks, TaskStatusFilter filter) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        var source = filter.toShowCallback();

        for (TaskDetails task : tasks) {
            var title = task.title();

            if (task.status() == Status.DONE) {
                title = "✅ " + title;
            }
            rows.add(row(KeyboardButton.SELECT_TASK.toButton(task.id(), null, source, title)));
        }

        if (filter.equals(TaskStatusFilter.COMPLETED) && !tasks.isEmpty()) {
            rows.add(row(KeyboardButton.DELETE_COMPLETED_TASKS.toButton(null, null, source)));
        }

        rows.add(row(KeyboardButton.MAIN_MENU.toButton()));

        return keyboard(rows.toArray(new InlineKeyboardRow[0]));
    }

    public InlineKeyboardMarkup getSubtaskSelectionKeyboard(List<SubtaskDetails> subtasks, Long taskId,
                                                            CallbackType source) {

        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (SubtaskDetails subtask : subtasks) {
            var title = subtask.title();

            if (subtask.status() == Status.DONE) {
                title = "✅ " + title;
            }

            rows.add(row(
                    KeyboardButton.SELECT_SUBTASK.toButton(subtask.id(), taskId, source, title)
            ));
        }
        rows.add(row(KeyboardButton.BACK_TO.backButton(CallbackType.SELECT_TASK, taskId, null, source)));

        return keyboard(rows.toArray(new InlineKeyboardRow[0]));
    }

    public InlineKeyboardMarkup getSubtaskActionsKeyboard(Long subtaskId, Long taskId, CallbackType source) {
        return keyboard(
                row(KeyboardButton.SUBTASK_COMPLETE.toButton(subtaskId, taskId, source),
                    KeyboardButton.SUBTASK_DELETE.toButton(subtaskId, taskId, source)),
                row(KeyboardButton.BACK_TO.backButton(CallbackType.OPEN_SUBTASKS, taskId, null, source)));
    }

    public InlineKeyboardMarkup buildBackKeyboard(CallbackType source, Long parentId) {

        CallbackDto backDto;

        if (source == CallbackType.OPEN_SUBTASKS) {
            backDto = CallbackDto.builder()
                    .type(CallbackType.OPEN_SUBTASKS)
                    .entityId(parentId)
                    .source(source)
                    .build();
        } else {
            backDto = CallbackDto.builder()
                    .type(source)
                    .source(source)
                    .build();
        }

        var button = InlineKeyboardButton.builder()
                .text("⬅ Назад")
                .callbackData(CallbackDataMapper.toDataFromDto(backDto))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(new InlineKeyboardRow(List.of(button))))
                .build();
    }

    private InlineKeyboardRow row(InlineKeyboardButton... buttons) {
        return new InlineKeyboardRow(Arrays.asList(buttons));
    }

    private InlineKeyboardMarkup keyboard(InlineKeyboardRow... rows) {
        return InlineKeyboardMarkup.builder().keyboard(List.of(rows)).build();
    }
}
