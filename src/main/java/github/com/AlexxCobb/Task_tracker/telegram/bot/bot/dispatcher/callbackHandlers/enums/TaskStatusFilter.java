package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums;

public enum TaskStatusFilter {
    ACTIVE,
    COMPLETED,
    ALL;

    public CallbackType toShowCallback() {
        return switch (this) {
            case ACTIVE -> CallbackType.SHOW_ACTIVE_TASKS;
            case COMPLETED -> CallbackType.SHOW_COMPLETED_TASKS;
            case ALL -> CallbackType.SHOW_ALL_TASKS;
        };
    }
}
