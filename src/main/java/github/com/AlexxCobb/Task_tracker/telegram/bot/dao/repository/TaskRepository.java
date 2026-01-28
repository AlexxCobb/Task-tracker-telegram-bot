package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = "subtasks")
    Optional<Task> findById(@NotNull Long taskId);

    @EntityGraph(attributePaths = "subtasks")
    List<Task> findByUserChatId(Long chatId);
}
