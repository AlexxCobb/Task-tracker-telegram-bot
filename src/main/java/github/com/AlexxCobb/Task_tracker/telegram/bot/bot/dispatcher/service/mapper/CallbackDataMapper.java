package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.mapper;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.CallbackDto;
import org.springframework.stereotype.Component;

@Component
public final class CallbackDataMapper {

    private CallbackDataMapper() {
    }

    public static String toDataFromDto(CallbackDto dto) {
        var sb = new StringBuilder();
        sb.append(dto.getType()).append(":");
        if (dto.getEntityId() != null) {
            sb.append(dto.getEntityId());
        }
        return sb.toString();
    }

    public static CallbackDto toDtoFromData(String data) {
        var strArray = data.split(":");
        var type = CallbackType.valueOf(strArray[0]);
        var entityId = strArray.length == 2 ? Long.valueOf(strArray[1]) : null;
        return CallbackDto.builder()
                .type(type)
                .entityId(entityId)
                .build();
    }
}
