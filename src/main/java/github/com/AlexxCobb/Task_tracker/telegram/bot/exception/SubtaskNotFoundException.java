package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class SubtaskNotFoundException extends BotException {
    @Override
    public String getUserMessage() {
        return "❗ Подзадача не найдена.";
    }
}
