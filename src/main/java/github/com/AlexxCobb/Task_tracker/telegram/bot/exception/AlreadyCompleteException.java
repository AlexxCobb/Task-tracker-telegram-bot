package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class AlreadyCompleteException extends BotException {

    @Override
    public String getUserMessage() {
        return "✅ Задача уже выполнена.";
    }
}
