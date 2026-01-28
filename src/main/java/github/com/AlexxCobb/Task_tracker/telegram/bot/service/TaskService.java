package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.TypeOfTask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.SubtaskRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.TaskRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.AlreadyCompleteException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.ForbiddenException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.TaskAlreadyExistException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final UserService userService;
    private final Map<Long, Long> currentEpicTaskMap = new ConcurrentHashMap<>();

    @Transactional
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

    @Transactional
    public void createEpicTask(Long chatId, String title, Boolean isShoppingList) {
        var user = userService.getUser(chatId);
        var taskId = currentEpicTaskMap.get(chatId);
        var existedTask = getTask(taskId);
        if (existedTask != null) {
            throw new TaskAlreadyExistException("Epic task already exist, finish it first");
        }

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

    @Transactional
    public void createSubtaskWithEpic(Long chatId, String title) {
        userService.getUser(chatId);
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

    @Transactional
    public void editTask(Long chatId, Long taskId, String title) {
        var user = userService.getUser(chatId);
        var task = getTask(taskId);
        if (!task.getUser().equals(user)) {
            throw new ForbiddenException("User can only complete his own task");
        }

        task.setTitle(title);
    }

    @Transactional
    public void completeTask(Long chatId, Long taskId) {
        userService.getUser(chatId);
        var task = getTask(taskId);
        if (task.getStatus().equals(Status.DONE)) {
            throw new AlreadyCompleteException("Task already complete");
        }
        task.complete();
    }

    public List<Task> getTasks(Long chatId) {
        userService.getUser(chatId);
        return taskRepository.findByUserChatId(chatId);
    }

    @Transactional
    public void removeTask(Long chatId, Long taskId) {
        var user = userService.getUser(chatId);
        var task = getTask(taskId);
        if (!task.getUser().equals(user)) {
            throw new ForbiddenException("User can only remove his own task");
        }
        taskRepository.delete(task);
    }

    public void finishingCreateEpicTask(Long chatId) {
        currentEpicTaskMap.remove(chatId);
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }
}
