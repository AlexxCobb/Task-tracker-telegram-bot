package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.TypeOfTask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.SubtaskRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.TaskRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.AlreadyCompleteException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.ForbiddenException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final UserService userService;

    @Transactional
    public Long createTask(Long chatId, String title) {
        var user = userService.getUser(chatId);

        var task = Task.builder()
                .user(user)
                .title(title)
                .type(TypeOfTask.TASK)
                .status(Status.NEW)
                .isShoppingList(false)
                .build();

        var savedTask = taskRepository.save(task);
        return savedTask.getId();
    }

    @Transactional
    public Long createEpicTask(Long chatId, String title, Boolean isShoppingList) {
        var user = userService.getUser(chatId);

        var task = Task.builder()
                .user(user)
                .title(title)
                .type(TypeOfTask.EPIC_TASK)
                .status(Status.NEW)
                .isShoppingList(isShoppingList)
                .build();

        var savedTask = taskRepository.save(task);
        return savedTask.getId();
    }

    @Transactional
    public void createSubtaskWithEpic(Long taskId, String title) {

        var existedTask = getTaskById(taskId);
        if (!existedTask.getType().equals(TypeOfTask.EPIC_TASK)) {
            throw new TaskNotFoundException();
        }
        var subtask = Subtask.builder()
                .task(getTaskById(taskId))
                .title(title)
                .status(Status.NEW)
                .build();

        subtaskRepository.save(subtask);
    }

    @Transactional
    public void editTask(Long chatId, Long taskId, String title) {
        var updated = taskRepository.updateTitle(taskId, chatId, title);
        if (updated == 0) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void completeTask(Long chatId, Long taskId) {
        var task = getTaskById(taskId);
        if (task.getStatus().equals(Status.DONE)) {
            throw new AlreadyCompleteException();
        }
        var updated = taskRepository.updateStatus(taskId, chatId);
        if (updated == 0) {
            throw new ForbiddenException();
        }
    }

    public List<Task> getTasks(Long chatId) {
        return taskRepository.findByUserChatIdAndStatusAndIsShoppingList(chatId, false);
    }

    public List<Task> getShoppingList(Long chatId) {
        return taskRepository.findByUserChatIdAndStatusAndIsShoppingList(chatId, true);
    }

    @Transactional
    public void removeTask(Long chatId, Long taskId) {
        checkUserOwnTask(chatId, taskId);
        taskRepository.deleteById(taskId);
    }

    private void checkUserOwnTask(Long chatId, Long taskId) {
        var task = getTaskById(taskId);
        if (!task.getUser().getChatId().equals(chatId)) {
            throw new ForbiddenException();
        }
    }

    private Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    }
}
