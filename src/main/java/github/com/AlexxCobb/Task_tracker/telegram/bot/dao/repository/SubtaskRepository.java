package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
}
