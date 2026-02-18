package github.com.AlexxCobb.Task_tracker.telegram.bot.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.TaskStatusFilter;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.TaskViewType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.TypeOfTask;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.SubtaskRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository.TaskRepository;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.AlreadyCompleteException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.ForbiddenException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.SubtaskNotFoundException;
import github.com.AlexxCobb.Task_tracker.telegram.bot.exception.TaskNotFoundException;
import lombok.NonNull;
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
                .task(existedTask)
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
        var updated = taskRepository.updateStatusToDone(taskId, chatId);
        if (updated == 0) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void completeSubtask(Long chatId, Long subtaskId) {
        var subtask =
                subtaskRepository.findByIdAndUserChatId(subtaskId, chatId).orElseThrow(SubtaskNotFoundException::new);
        if (subtask.getStatus().equals(Status.DONE)) {
            throw new AlreadyCompleteException();
        }
        var updated = subtaskRepository.updateStatusToDone(subtaskId);
        if (updated == 0) {
            throw new ForbiddenException();
        }
        checkTaskStatusWithSubtasks(subtask.getTask().getId());
    }

    public Task getTaskForUser(Long chatId, Long taskId) {
        return taskRepository.findUserChatIdAndById(chatId, taskId).orElseThrow(TaskNotFoundException::new);
    }

    public Subtask getSubtaskForUser(Long chatId, Long subtaskId) {
        return subtaskRepository.findByIdAndUserChatId(subtaskId, chatId).orElseThrow(SubtaskNotFoundException::new);
    }

    public List<Task> getTasks(Long chatId, @NonNull TaskStatusFilter filter, TaskViewType type) {
        //маппинг фильтра и типа
        var isShoppingList = type == TaskViewType.SHOPPING_LIST;
        return switch (filter) {
            case ALL -> isShoppingList ? taskRepository.findUserShoppingList(chatId)
                    : taskRepository.findAllUserTasks(chatId);
            case ACTIVE -> taskRepository.findUserTasksWithStatus(chatId, Status.NEW);
            case COMPLETED -> taskRepository.findUserTasksWithStatus(chatId, Status.DONE);
        };
    }

    @Transactional
    public void removeTask(Long chatId, Long taskId) {
        int deleted = taskRepository.deleteByIdAndUserChatId(taskId, chatId);
        if (deleted == 0) {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void removeSubtask(Long chatId, Long subtaskId) {
        int deleted = subtaskRepository.deleteByIdAndUserChatId(subtaskId, chatId);
        if (deleted == 0) {
            throw new ForbiddenException();
        }
    }

    private Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    }

    private void checkTaskStatusWithSubtasks(Long taskId) {
        var task = getTaskById(taskId);

        if (!task.getType().equals(TypeOfTask.EPIC_TASK)) {
            return;
        }
        if (task.getStatus() == Status.DONE) {
            return;
        }

        var isDoneAllSubtasks =
                task.getSubtasks().stream().allMatch(subtask -> subtask.getStatus().equals(Status.DONE));
        if (isDoneAllSubtasks) {
            taskRepository.updateStatusToDone(task.getId(), task.getUser().getChatId());
        }
    }
}
