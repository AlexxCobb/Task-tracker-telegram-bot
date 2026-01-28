package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.User;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.UserRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

}
