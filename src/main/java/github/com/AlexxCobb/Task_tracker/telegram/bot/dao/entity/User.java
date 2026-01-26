package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    private Long chatId;

    private String name;

}
