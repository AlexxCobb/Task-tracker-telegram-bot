package github.com.AlexxCobb.Task_tracker.telegram.bot.exception;

public class ReminderAlreadyExistsException extends BotException {
    @Override
    public String getUserMessage() {
        return "⏰ Напоминание на это время для задачи уже установлено";
    }
}
