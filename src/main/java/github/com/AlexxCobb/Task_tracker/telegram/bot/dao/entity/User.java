package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void updateName(String newName){
        this.name = newName;
    }
}
