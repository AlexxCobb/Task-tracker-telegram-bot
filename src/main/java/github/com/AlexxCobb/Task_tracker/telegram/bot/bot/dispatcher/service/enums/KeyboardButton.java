package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.enums;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.CallbackDto;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.mapper.CallbackDataMapper;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public enum KeyboardButton {

    CREATE_TASK("➕ Создать задачу", CallbackType.CREATE_TASK),
    CREATE_TASK_WITH_SUBTASKS("🧩 Создать задачу с подзадачами", CallbackType.CREATE_TASK_WITH_SUBTASKS),
    CREATE_SHOPPING_LIST("🛒 Создать список покупок", CallbackType.CREATE_SHOPPING_LIST),
    SHOW_TASKS("📋 Показать все задачи", CallbackType.SHOW_TASKS),
    SHOW_SHOPPING_LIST("📋 Показать списки покупок", CallbackType.SHOW_SHOPPING_LIST),

    TASK_EDIT("✏️ Редактировать", CallbackType.TASK_EDIT),
    TASK_COMPLETE("✅ Завершить", CallbackType.TASK_COMPLETE),
    TASK_DELETE("🗑 Удалить", CallbackType.TASK_DELETE),
    MAIN_MENU("В главное меню", CallbackType.MAIN_MENU),

    LIST_DONE("Список составлен", CallbackType.LIST_DONE);

    private final String text;
    private final CallbackType callbackType;

    KeyboardButton(String text, CallbackType callbackType) {
        this.text = text;
        this.callbackType = callbackType;
    }

    public InlineKeyboardButton toButton() {
        return toButton(null);
    }

    public InlineKeyboardButton toButton(Long entityId) {
        var callbackData = CallbackDto.builder()
                .type(callbackType)
                .entityId(entityId)
                .build();

        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(CallbackDataMapper.toDataFromDto(callbackData))
                .build();
    }
}
