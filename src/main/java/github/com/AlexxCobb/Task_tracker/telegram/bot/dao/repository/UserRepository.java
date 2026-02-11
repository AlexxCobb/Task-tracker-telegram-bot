package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.name = :name where u.chatId = :chatId")
    void updateNameByChatId(String name, Long chatId);
}
