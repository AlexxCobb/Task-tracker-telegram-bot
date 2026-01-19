package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateDispatcher {

    private final List<UpdateHandler> handlers;

    public UpdateDispatcher(List<UpdateHandler> handlers) {
        this.handlers = handlers;
    }
}