package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class TaskAlreadyExistException extends RuntimeException {
    public TaskAlreadyExistException(String message) {
        super(message);
    }
}
