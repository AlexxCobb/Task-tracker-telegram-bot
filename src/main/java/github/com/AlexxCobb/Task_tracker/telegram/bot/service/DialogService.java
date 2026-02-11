package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.DialogContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.DialogContextRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.DialogContextNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DialogService {

    private final DialogContextRepository contextRepository;

    @Transactional
    public void setDialogState(Long chatId, DialogState state, Long taskId) {
        var context = contextRepository.findById(chatId)
                .map(existing -> existing.toBuilder()
                        .dialogState(state)
                        .taskId(taskId)
                        .build())
                .orElseGet(() -> DialogContext.builder()
                        .chatId(chatId)
                        .dialogState(state)
                        .taskId(taskId)
                        .build());

        contextRepository.save(context);
    }

    public DialogState getStateOrDefault(Long chatId) {
        return contextRepository.findById(chatId).map(DialogContext::getDialogState)
                .orElse(DialogState.IDLE);
    }

    public Long getTaskId(Long chatId) {
        return contextRepository.findById(chatId).map(DialogContext::getTaskId).orElseThrow(
                DialogContextNotFoundException::new);

    }

    @Transactional
    public void clearState(Long chatId) {
        contextRepository.deleteById(chatId);
    }
}
