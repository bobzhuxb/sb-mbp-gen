package ${packageName}.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * @author Bob
 */
@Component
public class ScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);

}
