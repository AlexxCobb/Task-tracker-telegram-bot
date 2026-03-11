package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.TaskStatusFilter;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.CallbackDto;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.ReminderService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderReturnService {

    private final KeyboardService keyboardService;
    private final TaskService taskService;
    private final ReminderService reminderService;

    public String resolveText(CallbackType source) {
        if (source == null || source == CallbackType.MAIN_MENU) {
            return "Выбери действие:";
        }
        return switch (source) {
            case SHOW_ALL_TASKS, SHOW_ACTIVE_TASKS, SHOW_COMPLETED_TASKS -> "Выбери задачу:";
            case SELECT_REMIND -> "Напоминания для задач:";
            default -> "Выбери, что хочешь сделать:";
        };
    }

    public InlineKeyboardMarkup resolveKeyboard(Long chatId, Long taskId, CallbackType source) {
        if (source == null || source == CallbackType.MAIN_MENU) {
            return keyboardService.getEditKeyboard(taskId);
        }
        return switch (source) {
            case SHOW_ALL_TASKS, SHOW_ACTIVE_TASKS, SHOW_COMPLETED_TASKS -> {
                var filter = source.toFilter().orElse(TaskStatusFilter.ALL);
                var tasks = taskService.getTasks(chatId, filter);
                yield keyboardService.getTasksSelectionKeyboard(tasks, filter);
            }
            case SELECT_REMIND -> {
                var reminders = reminderService.findReminderDetailsList(chatId);
                yield keyboardService.getReminderSelectionKeyboard(reminders);
            }
            default -> keyboardService.getStartKeyboard();
        };
    }

    public InlineKeyboardMarkup resolveErrorKeyboard(CallbackDto dto) {
        if (dto.getType() == CallbackType.CALENDAR_SELECT_TIME) {
            var dateStr = dto.getExtra() != null
                    ? dto.getExtra().substring(0, 10)
                    : LocalDate.now().toString();

            var backDto = CallbackDto.builder()
                    .type(CallbackType.CALENDAR_SELECT_DAY)
                    .entityId(dto.getEntityId())
                    .source(dto.getSource())
                    .extra(dateStr)
                    .build();

            var button = InlineKeyboardButton.builder()
                    .text("⬅ Выбрать другое время")
                    .callbackData(CallbackDataMapper.toDataFromDto(backDto))
                    .build();

            return InlineKeyboardMarkup.builder()
                    .keyboard(List.of(new InlineKeyboardRow(List.of(button))))
                    .build();
        }

        return keyboardService.buildBackKeyboard(dto.getSource(), dto.getParentId());
    }
}