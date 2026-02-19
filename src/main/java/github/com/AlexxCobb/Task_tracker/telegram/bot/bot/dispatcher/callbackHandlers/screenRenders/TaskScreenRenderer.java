package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.screenRenders;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
@RequiredArgsConstructor
public class TaskScreenRenderer {

    private final TaskService taskService;
    private final KeyboardService keyboardService;

    public EditMessageText renderList(UpdateContext context) {

        var chatId = context.chatId();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();
        var filter = context.dto().getSource().toFilter();
        var tasks = taskService.getTasks(chatId, filter);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("📋 Обновленный список задач:")
                .replyMarkup(
                        keyboardService.getTasksSelectionKeyboard(tasks, filter)
                )
                .build();
    }
}
