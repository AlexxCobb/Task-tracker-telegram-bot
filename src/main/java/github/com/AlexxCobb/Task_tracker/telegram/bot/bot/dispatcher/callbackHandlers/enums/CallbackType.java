package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums;

public enum CallbackType {
    CREATE_TASK,
    CREATE_TASK_WITH_SUBTASKS,
    CREATE_SUBTASK,
    CREATE_SHOPPING_LIST,
    CREATE_SHOPPING_ITEM,
    CREATE_ANOTHER_TASK,
    SHOW_ALL_TASKS,
    SHOW_ACTIVE_TASKS,
    SHOW_COMPLETED_TASKS,
    SHOW_SHOPPING_LIST,
    OPEN_SUBTASKS,
    TASK_EDIT,
    TASK_COMPLETE,
    TASK_DELETE,
    SELECT_TASK,
    SELECT_SUBTASK,
    SUBTASK_COMPLETE,
    SUBTASK_DELETE,
    SHOPPING_ITEM_TOGGLE,
    MAIN_MENU,
    LIST_DONE,
    DELETE_COMPLETED_TASKS,
    BACK_TO;

    public TaskStatusFilter toFilter() {
        return switch (this) {
            case SHOW_ACTIVE_TASKS -> TaskStatusFilter.ACTIVE;
            case SHOW_COMPLETED_TASKS -> TaskStatusFilter.COMPLETED;
            case SHOW_ALL_TASKS, SHOW_SHOPPING_LIST -> TaskStatusFilter.ALL;
            default -> throw new IllegalStateException("Unexpected callback type: " + this);
        };
    }
}
