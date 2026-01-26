package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

    private final Map<Long, String> tasks = new ConcurrentHashMap<>();

    public void createTask(Long chatId, String title) {
        tasks.put(chatId, title);
    }

    public String getTasks(Long chatId) {
        return tasks.get(chatId);
    }

    public void removeTask(Long chatId) {
        tasks.remove(chatId);
    }

}
