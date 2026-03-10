package github.com.AlexxCobb.Task_tracker.telegram.bot.dao.repository;

import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.entity.Reminder;
import github.com.AlexxCobb.Task_tracker.telegram.bot.dao.enums.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    @Query(value = "select r from Reminder r join fetch r.task t left join fetch t.subtasks " +
            "where r.task.status = NEW and r.id in :ids")
    List<Reminder> findPendingWithTasks(List<Long> ids);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Reminder r set r.status = :status, r.sentAt = :sentAt where r.id in :ids")
    void updateStatusAtByIds(ReminderStatus status, OffsetDateTime sentAt, List<Long> ids);

    @Query(value = "select reminder_id from reminder where status = 'SCHEDULED' and remind_at <= now() "
            + "for update skip locked limit :batchSize", nativeQuery = true)
    List<Long> selectForUpdate(int batchSize);
}
