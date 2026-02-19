package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateFlowCallbackHandler implements UpdateHandler {

    private final DialogService dialogService;

    private final Map<CallbackType, Pair<DialogState, String>> FLOW_CONFIG = Map.of(
            CallbackType.CREATE_SHOPPING_LIST,
            Pair.of(DialogState.AWAITING_TASK_WITH_SHOPPING_ITEMS_TITLE, "✍️ Введи название списка:"),
            CallbackType.CREATE_TASK, Pair.of(DialogState.AWAITING_TASK_TITLE, "✍️ Введи название задачи:"),
            CallbackType.CREATE_TASK_WITH_SUBTASKS,
            Pair.of(DialogState.AWAITING_TASK_WITH_SUBTASK_TITLE, "✍️ Введи название основной задачи:"),
            CallbackType.CREATE_ANOTHER_TASK, Pair.of(DialogState.AWAITING_TASK_TITLE, "✍️ Введи название задачи:"));

    @Override
    public Boolean canHandle(UpdateContext context) {
        return context.isCallback() && FLOW_CONFIG.containsKey(context.dto().getType());
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var config = FLOW_CONFIG.get(context.dto().getType());
        var messageId = context.update()
                .getCallbackQuery()
                .getMessage()
                .getMessageId();

        dialogService.setDialogState(chatId, config.getFirst(), null);

        return List.of(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text(config.getSecond())
                        .build()
        );
    }
}
