package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Subtask s set s.status = DONE, s.updatedAt = CURRENT_TIMESTAMP where s.id = :id")
    int updateStatus(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Subtask s WHERE s.id = :taskId")
    int deleteByIdAndUserChatId(Long taskId);
}
