package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class AlreadyCompleteException extends RuntimeException {
    public AlreadyCompleteException(String message) {
        super(message);
    }
}
