package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Callback {
    private CallbackType type;
    private Long entityId;
}
