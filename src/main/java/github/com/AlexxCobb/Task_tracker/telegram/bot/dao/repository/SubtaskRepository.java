package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Subtask s set s.status = DONE, s.updatedAt = CURRENT_TIMESTAMP where s.id = :id")
    int updateStatusToDone(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Subtask s set s.status = DONE, s.updatedAt = CURRENT_TIMESTAMP where s.task.id = :taskId")
    void updateAllByTaskIdToDone(Long taskId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Subtask s WHERE s.id = :subtaskId AND s.task.user.chatId = :chatId")
    int deleteByIdAndUserChatId(Long subtaskId, Long chatId);

    @Query("SELECT s FROM Subtask s WHERE s.id = :subtaskId AND s.task.user.chatId = :chatId")
    Optional<Subtask> findByIdAndUserChatId(Long subtaskId, Long chatId);
}
