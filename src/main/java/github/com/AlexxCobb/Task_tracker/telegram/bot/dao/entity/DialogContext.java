package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.DialogState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Getter
@Builder(toBuilder = true)
@Table(name = "dialog_context")
@NoArgsConstructor
@AllArgsConstructor
public class DialogContext {

    @Id
    @Column(name = "user_chat_id")
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "dialog_state")
    private DialogState dialogState;

    @Column(name = "task_id")
    private Long taskId;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DialogContext that = (DialogContext) o;
        return chatId != null && Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return chatId != null ? chatId.hashCode() : super.hashCode();
    }
}
