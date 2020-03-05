package ${packageName}.web.rest;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试Controller类
 * @author Bob
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private CommonService commonService;

    /**
     * 生成序列化ID
     */
    @GetMapping("/id-worker-gen")
    public ResponseEntity<Long> generateIdByIdWorker () {
        return ResponseEntity.ok().headers(null).body(IdWorker.getId());
    }

    /**
     * 生成序列化ID
     */
    @GetMapping("/id-worker-gen/{number}")
    public ResponseEntity<List<Long>> generateIdMultiByIdWorker (@PathVariable Integer number) {
        List<Long> idList = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            idList.add(IdWorker.getId());
        }
        return ResponseEntity.ok().headers(null).body(idList);
    }

    /**
     * 获取缓存内容
     */
    @GetMapping("/get-cache")
    public ResponseEntity<ReturnCommonDTO<String>> getCacheData(String cacheName, String key) {
        ReturnCommonDTO<String> rtnData = commonService.getDataFromCache(cacheName, key);
        return ResponseEntity.ok().headers(null).body(rtnData);
    }

    /**
     * 获取LocalCache的缓存内容
     */
    @GetMapping("/get-local-cache-data")
    public ResponseEntity<ReturnCommonDTO<String>> getLocalCacheData(String key) {
        ReturnCommonDTO<String> rtnData = commonService.getDataFromLocalCache(key);
        return ResponseEntity.ok().headers(null).body(rtnData);
    }

    /**
     * 获取LocalCache的TTL
     */
    @GetMapping("/get-local-cache-ttl")
    public ResponseEntity<ReturnCommonDTO<Integer>> getLocalCacheTTL(String key) {
        ReturnCommonDTO<Integer> rtnData = commonService.getTtlFromLocalCache(key);
        return ResponseEntity.ok().headers(null).body(rtnData);
    }

}
