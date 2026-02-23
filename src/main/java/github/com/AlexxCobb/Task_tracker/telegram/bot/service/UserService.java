package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.User;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User should be created in dispatcher before usage"));
    }

    @Transactional
    public boolean ensureUser(Update update) {
        if (!update.hasMessage() || update.getMessage().getFrom() == null) {
            return false;
        }

        var chatId = update.getMessage().getChatId();
        var tgUser = update.getMessage().getFrom();

        var existing = userRepository.findById(chatId);
        if (existing.isPresent()) {
            String currentName = existing.get().getName();
            String newName = tgUser.getUserName();
            if (newName != null && !newName.equals(currentName)) {
                userRepository.updateNameByChatId(newName, chatId);
            }
            return false;
        } else {
            var newUser = User.builder()
                    .chatId(chatId)
                    .name(tgUser.getUserName())
                    .build();
            userRepository.save(newUser);
            return true;
        }
    }
}
