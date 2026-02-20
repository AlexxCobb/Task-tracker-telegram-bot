package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.mapper;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.CallbackDto;

public final class CallbackDataMapper {

    private CallbackDataMapper() {
    }

    public static String toDataFromDto(CallbackDto dto) {
        return String.join(":",
                           dto.getType().name(),
                           dto.getEntityId() != null ? dto.getEntityId().toString() : "",
                           dto.getParentId() != null ? dto.getParentId().toString() : "",
                           dto.getSource() != null ? dto.getSource().name() : "");
    }

    public static CallbackDto toDtoFromData(String data) {
        var parts = data.split(":");
        return CallbackDto.builder()
                .type(CallbackType.valueOf(parts[0]))
                .entityId(parts.length > 1 && !parts[1].isBlank() ? Long.valueOf(parts[1]) : null)
                .parentId(parts.length > 2 && !parts[2].isBlank() ? Long.valueOf(parts[2]) : null)
                .source(parts.length > 3 && !parts[3].isBlank() ? CallbackType.valueOf(parts[3]) : null)
                .build();
    }
}
