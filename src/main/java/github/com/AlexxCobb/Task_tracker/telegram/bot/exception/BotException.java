package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public abstract class BotException extends RuntimeException {
    public abstract String getUserMessage();
}
