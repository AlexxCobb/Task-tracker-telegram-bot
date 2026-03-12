package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class TimeParseException extends BotException {
    @Override
    public String getUserMessage() {
        return "❗ Время должно быть в формате ЧЧ:ММ -> (10:03)";
    }
}
