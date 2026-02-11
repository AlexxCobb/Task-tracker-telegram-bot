package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class TitleValidationException extends BotException {

    @Override
    public String getUserMessage() {
        return "❗ Название задачи не должно быть пустым.";
    }
}
