package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.screenRenders;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
@RequiredArgsConstructor
public class SubtaskScreenRenderer {

    private final TaskService taskService;
    private final KeyboardService keyboardService;

    public EditMessageText renderList(UpdateContext context) {

        var chatId = context.chatId();
        var dto = context.dto();
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        var task = taskService.getTaskForUser(chatId, dto.getParentId());

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("📂 Обновленный список подзадач:")
                .replyMarkup(
                        keyboardService.getSubtaskSelectionKeyboard(
                                task.subtasks(),
                                dto.getParentId(),
                                dto.getSource()
                        )
                )
                .build();
    }
}
