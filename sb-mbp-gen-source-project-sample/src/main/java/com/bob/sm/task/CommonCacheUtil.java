package com.bob.sm.task;

import com.bob.sm.dto.help.RemoteDictionaryDTO;
import com.bob.sm.service.help.RemoteDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化相关缓存
 */
@Component
public class CommonCacheUtil {

    private static final Logger log = LoggerFactory.getLogger(CommonCacheUtil.class);

    private static Map<String, Map<String, String>> dictionaryMap = new HashMap<>();

    @Autowired
    private RemoteDictionaryService remoteDictionaryService;

    public static Map<String, Map<String, String>> getDictionaryMap() {
        return dictionaryMap;
    }

    /**
     * 数据字典刷新任务
     */
    //@Scheduled(cron="0 * * * * ? ")
    public void getAndRefreshDictionary() {
        try {
            List<RemoteDictionaryDTO> remoteDictionaryDTOList = remoteDictionaryService.loadDictionaryData();
            Map<String, Map<String, String>> remoteDictionaryMap = new HashMap<>();
            for (RemoteDictionaryDTO remoteDictionaryDTO : remoteDictionaryDTOList) {
                String dicType = remoteDictionaryDTO.getDicType();
                String dicCode = remoteDictionaryDTO.getDicCode();
                String dicValue = remoteDictionaryDTO.getDicValue();
                Map<String, String> subDictionaryMap = remoteDictionaryMap.get(dicType);
                if (subDictionaryMap == null) {
                    subDictionaryMap = new HashMap<>();
                    remoteDictionaryMap.put(dicType, subDictionaryMap);
                }
                subDictionaryMap.put(dicCode, dicValue);
            }
            if (remoteDictionaryDTOList != null && remoteDictionaryDTOList.size() > 0) {
                refreshDictionary(remoteDictionaryMap);
            }
        } catch (Exception e) {
            log.error("Task：数据字典刷新任务失败。" + e.getMessage(), e);
            return;
        }
    }

    public synchronized void refreshDictionary(Map<String, Map<String, String>> remoteDictionaryMap) {
        dictionaryMap.clear();
        dictionaryMap.putAll(remoteDictionaryMap);
    }

}
