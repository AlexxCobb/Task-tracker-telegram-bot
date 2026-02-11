package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class DialogContextNotFoundException extends BotException {

    @Override
    public String getUserMessage() {
        return "⌛ Сессия истекла. Начните заново.";
    }
}
