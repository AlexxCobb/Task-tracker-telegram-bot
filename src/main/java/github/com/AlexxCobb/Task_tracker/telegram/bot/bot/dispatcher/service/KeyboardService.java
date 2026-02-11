package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.CallbackDto;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.enums.KeyboardButton;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
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

    private final TaskService taskService;

    public InlineKeyboardMarkup getStartKeyboard() {
        return keyboard(row(KeyboardButton.CREATE_TASK.toButton()),
                        row(KeyboardButton.CREATE_TASK_WITH_SUBTASKS.toButton()),
                        row(KeyboardButton.CREATE_SHOPPING_LIST.toButton()),
                        row(KeyboardButton.SHOW_TASKS.toButton()),
                        row(KeyboardButton.SHOW_SHOPPING_LIST.toButton()));
    }

    public InlineKeyboardMarkup getEditKeyboard(Long taskId) {
        return keyboard(row(KeyboardButton.TASK_EDIT.toButton(taskId), KeyboardButton.TASK_COMPLETE.toButton(taskId)),
                        row(KeyboardButton.TASK_DELETE.toButton(taskId), KeyboardButton.MAIN_MENU.toButton()));
    }

    public InlineKeyboardMarkup getListDoneKeyboard() {
        return keyboard(row(KeyboardButton.LIST_DONE.toButton()));
    }

    public InlineKeyboardMarkup getTasksActionsKeyboard(List<Task> tasks) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            InlineKeyboardRow row = new InlineKeyboardRow(
                    InlineKeyboardButton.builder()
                            .text("✏️ - " + (i + 1))
                            .callbackData(CallbackDataMapper.toDataFromDto(
                                    CallbackDto.builder()
                                            .type(CallbackType.TASK_EDIT)
                                            .entityId(task.getId())
                                            .build()))
                            .build(),

                    InlineKeyboardButton.builder()
                            .text("✅ - " + (i + 1))
                            .callbackData(CallbackDataMapper.toDataFromDto(
                                    CallbackDto.builder()
                                            .type(CallbackType.TASK_COMPLETE)
                                            .entityId(task.getId())
                                            .build()))
                            .build(),

                    InlineKeyboardButton.builder()
                            .text("🗑️ - " + (i + 1))
                            .callbackData(CallbackDataMapper.toDataFromDto(
                                    CallbackDto.builder()
                                            .type(CallbackType.TASK_DELETE)
                                            .entityId(task.getId())
                                            .build()))
                            .build()
            );
            rows.add(row);
        }

        rows.add(new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text("🔙 Назад")
                        .callbackData(CallbackType.MAIN_MENU.name())
                        .build()
        ));

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    private InlineKeyboardRow row(InlineKeyboardButton... buttons) {
        return new InlineKeyboardRow(Arrays.asList(buttons));
    }

    private InlineKeyboardMarkup keyboard(InlineKeyboardRow... rows) {
        return InlineKeyboardMarkup.builder().keyboard(List.of(rows)).build();
    }
}
