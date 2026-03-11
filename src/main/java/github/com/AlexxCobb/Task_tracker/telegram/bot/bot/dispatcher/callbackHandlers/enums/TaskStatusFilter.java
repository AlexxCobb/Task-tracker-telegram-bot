package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.Status;

import java.util.Optional;

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

    public Optional<Status> toStatus() {
        return switch (this) {
            case ACTIVE -> Optional.of(Status.NEW);
            case COMPLETED -> Optional.of(Status.DONE);
            case ALL -> Optional.empty();
        };
    }
}
