package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class TaskNotFoundException extends BotException {

    @Override
    public String getUserMessage() {
        return "❗ Задача не найдена.";
    }
}
