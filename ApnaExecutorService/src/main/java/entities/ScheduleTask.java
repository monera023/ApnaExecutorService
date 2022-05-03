package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class ScheduleTask {
    Runnable runnable;
    @Setter
    long scheduledTime;
    int taskType;
    long period;
    long delay;
    TimeUnit unit;
    String taskId;
}
