package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findById(@NotNull Long taskId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Task t set t.title = :title, t.updatedAt = CURRENT_TIMESTAMP where t.id = :id and t.user.chatId = :chatId")
    int updateTitle(Long id, Long chatId, String title);

    @EntityGraph(attributePaths = "subtasks")
    @Query("select t from Task t where t.user.chatId = :chatId and t.status = NEW and t.isShoppingList = :isShoppingList")
    List<Task> findByUserChatIdAndStatusAndIsShoppingList(Long chatId, Boolean isShoppingList);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Task t set t.status = DONE, t.updatedAt = CURRENT_TIMESTAMP where t.id = :id and t.user.chatId = :chatId")
    int updateStatus(Long id, Long chatId);
}
