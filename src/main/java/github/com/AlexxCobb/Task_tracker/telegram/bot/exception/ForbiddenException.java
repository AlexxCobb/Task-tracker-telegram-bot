package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class ForbiddenException extends BotException {
    @Override
    public String getUserMessage() {
        return "⛔ У вас нет доступа к этой задаче.";
    }
}
