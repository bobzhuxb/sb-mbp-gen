package com.bob.sm.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bob.sm.config.YmlConfig;
import com.bob.sm.service.WxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
@Component
public class ScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    private YmlConfig ymlConfig;

    @Autowired
    private WxService wxService;

    /**
     * 微信公众号的token获取
     * 每隔两分钟执行
     */
    @Scheduled(cron="0 0 * * * ? ")
    public void wxPublicTokenGet() {
        if ("open".equals(ymlConfig.getWxAccessTokenSwitch())) {
            boolean result = wxService.refreshAccessToken(10);
            if (!result) {
                log.error("Task：微信公众号ACCESS_TOKEN刷新任务失败。");
            }
        }
    }

}
