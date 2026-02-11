package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowTasksAndListCallbackHandler implements UpdateHandler {

    private final TaskService taskService;
    private final KeyboardService keyboardService;

    @Override
    public Boolean canHandle(Update update) {
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var dto = CallbackDataMapper.toDtoFromData(data);
            return dto.getType().equals(CallbackType.SHOW_SHOPPING_LIST) || dto.getType()
                    .equals(CallbackType.SHOW_TASKS);
        }
        return false;
    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        List<Task> tasks = Collections.emptyList();

        var data = update.getCallbackQuery().getData();
        var dto = CallbackDataMapper.toDtoFromData(data);
        var isShoppingList = Boolean.FALSE;

        if (dto.getType().equals(CallbackType.SHOW_SHOPPING_LIST)) {
            tasks = taskService.getShoppingList(chatId);
            isShoppingList = Boolean.TRUE;
        } else if (dto.getType().equals(CallbackType.SHOW_TASKS)) {
            tasks = taskService.getTasks(chatId);
        }

        var responseMessage = isShoppingList ? "📋 ** Ваш список: **\n\n" : "📋 ** Ваши задачи: **\n\n";
        var messageText = new StringBuilder(responseMessage);

        if (tasks != null && !tasks.isEmpty()) {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                messageText.append("**  ").append(i + 1).append(". ").append(task.getTitle()).append("  **\n");

                if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
                    for (Subtask subtask : task.getSubtasks()) {
                        String statusEmoji = subtask.getStatus() == Status.DONE ? "✅" : "⏳";
                        messageText.append("   └ ")
                                .append(statusEmoji)
                                .append(" ")
                                .append(subtask.getTitle())
                                .append("\n");
                    }
                }
                messageText.append("\n");
            }
        }

        var responseText = isShoppingList ?
                "Еще нет списка покупок" :
                "📭 У вас пока нет задач";

        return SendMessage.builder()
                .chatId(chatId)
                .text(tasks == null || tasks.isEmpty() ? responseText : messageText.toString())
                .replyMarkup(tasks == null ?
                                     keyboardService.getStartKeyboard()
                                     : keyboardService.getTasksActionsKeyboard(tasks))
                .build();
    }
}
