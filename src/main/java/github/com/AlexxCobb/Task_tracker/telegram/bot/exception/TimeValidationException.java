package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class TimeValidationException extends BotException {
    @Override
    public String getUserMessage() {
        return "❗ Напоминание должно быть в будущем";
    }
}
