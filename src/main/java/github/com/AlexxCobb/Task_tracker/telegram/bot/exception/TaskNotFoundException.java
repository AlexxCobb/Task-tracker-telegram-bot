package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
