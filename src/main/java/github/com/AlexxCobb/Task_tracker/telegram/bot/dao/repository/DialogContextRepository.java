package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.DialogContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogContextRepository extends JpaRepository<DialogContext, Long> {

    @Modifying
    @Query(value = """
            INSERT INTO dialog_context (user_chat_id, dialog_state, task_id, updated_at)
            VALUES (:chatId, :state, :taskId, now())
            ON CONFLICT (user_chat_id) DO UPDATE
            SET dialog_state = EXCLUDED.dialog_state,
                task_id = EXCLUDED.task_id,
                updated_at = EXCLUDED.updated_at
            """, nativeQuery = true)
    void updateContext(@Param("chatId") Long chatId,
                       @Param("state") String state,
                       @Param("taskId") Long taskId);

}
