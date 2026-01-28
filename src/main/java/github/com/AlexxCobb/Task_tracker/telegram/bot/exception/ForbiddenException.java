package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
