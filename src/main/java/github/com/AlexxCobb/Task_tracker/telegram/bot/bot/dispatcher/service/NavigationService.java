package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.screenRenders.SubtaskScreenRenderer;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.screenRenders.TaskScreenRenderer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
@RequiredArgsConstructor
public class NavigationService {

    private final TaskScreenRenderer taskScreenRenderer;
    private final SubtaskScreenRenderer subtaskScreenRenderer;

    public EditMessageText returnAfterMutation(UpdateContext context) {
        var actionType = context.dto().getType();
        var target = context.dto().getSource();
        if (actionType == CallbackType.SUBTASK_COMPLETE || actionType == CallbackType.SUBTASK_DELETE) {
            return subtaskScreenRenderer.renderList(context);
        }

        return switch (target) {
            case SHOW_ALL_TASKS, SHOW_ACTIVE_TASKS, SHOW_COMPLETED_TASKS -> taskScreenRenderer.renderList(context);
            case OPEN_SUBTASKS -> subtaskScreenRenderer.renderList(context);
            default -> throw new IllegalStateException("Unexpected value: " + target);
        };
    }
}
