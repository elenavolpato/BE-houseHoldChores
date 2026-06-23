package raposinha.houseHoldChores.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import raposinha.houseHoldChores.service.TaskService;


@Slf4j
@Component
@AllArgsConstructor
public class ChoreScheduler {

    private final TaskService taskService;

    @Scheduled(cron = "0 0 3 * * *") 
    public void generateUpcomingOccurrences() {
        log.info("Running task scheduler...");
        taskService.generateUpcomingOccurrences(); // 👈 business logic stays in service
        log.info("Task scheduler finished.");
    }

}
