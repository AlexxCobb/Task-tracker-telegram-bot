package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CallbackDto {
    private CallbackType type;
    private Long entityId;
}
