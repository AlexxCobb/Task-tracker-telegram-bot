package github.com.AlexxCobb.Task_tracker.telegram.bot.service.scheduler;

import github.com.AlexxCobb.Task_tracker.telegram.bot.service.ReminderUseCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ReminderUseCaseService reminderUseCaseService;

    @Scheduled(fixedDelay = 60000)
    public void processReminders(){
        reminderUseCaseService.sendReminderUseCase();;
    }

}
