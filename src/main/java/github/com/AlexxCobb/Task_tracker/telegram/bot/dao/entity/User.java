package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "telegram_user")
public class User {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return chatId != null && Objects.equals(chatId, user.chatId);
    }

    @Override
    public int hashCode() {
        return chatId != null ? chatId.hashCode() : super.hashCode();
    }
}
