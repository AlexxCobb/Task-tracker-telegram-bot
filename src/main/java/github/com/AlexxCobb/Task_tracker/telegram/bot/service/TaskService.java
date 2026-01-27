package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.TypeOfTask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.SubtaskRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.TaskRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final UserService userService;
    private final Map<Long, Long> currentEpicTaskMap = new ConcurrentHashMap<>();

    public void createTask(Long chatId, String title) {
        var user = userService.getUser(chatId);

        var task = Task.builder()
                .user(user)
                .title(title)
                .type(TypeOfTask.TASK)
                .status(Status.NEW)
                .build();

        taskRepository.save(task);
    }

    public void createEpicTask(Long chatId, String title, Boolean isShoppingList) {
        var user = userService.getUser(chatId);

        var task = Task.builder()
                .user(user)
                .title(title)
                .type(TypeOfTask.EPIC_TASK)
                .status(Status.NEW)
                .isShoppingList(isShoppingList)
                .build();

        var savedTask = taskRepository.save(task);
        currentEpicTaskMap.put(chatId, savedTask.getId());
    }

    public void createSubtaskWithEpic(Long chatId, String title) {
        var user = userService.getUser(chatId); // ? нужна ли проверка
        var taskId = currentEpicTaskMap.get(chatId);
        if (taskId == null) {
            throw new TaskNotFoundException("No active epic task for chat: " + chatId);
        }

        var subtask = Subtask.builder()
                .task(getTask(taskId))
                .title(title)
                .status(Status.NEW)
                .build();

        subtaskRepository.save(subtask);
    }

    public String getTasks(Long chatId) {
        return null;
    }

    public void removeTask(Long chatId, Long taskId) {

    }

    public void finishingCreateEpicTask(Long chatId) {
        currentEpicTaskMap.remove(chatId);
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

}
