package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DialogService {

    private final Map<Long, DialogState> dialogStateMap = new ConcurrentHashMap<>();

    public void setState(Long chatId, DialogState state) {
        dialogStateMap.put(chatId, state);
    }

    public DialogState getState(Long chatId) {
        return dialogStateMap.getOrDefault(chatId, DialogState.IDLE);
    }

    public void clearState(Long chatId) {
        dialogStateMap.remove(chatId);
    }
}
