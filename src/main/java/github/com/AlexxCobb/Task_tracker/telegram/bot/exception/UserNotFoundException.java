package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
