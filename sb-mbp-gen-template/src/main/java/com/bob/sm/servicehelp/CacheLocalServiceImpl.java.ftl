package ${packageName}.servicehelp;

import ${packageName}.util.LocalCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;

/**
 * 进程内的缓存
 * 用于自测及测试环境暂时没有Redis的情况
 * @author Bob
 */
@Service("cacheLocalService")
public class CacheLocalServiceImpl implements CacheService {

    private final Logger log = LoggerFactory.getLogger(CacheLocalServiceImpl.class);

    /**
     * 连接是否有效
     * @return
     */
    @Override
    public boolean connectionValid() {
        // 使用本地内存，在连不上之前，系统肯定挂掉了
        return true;
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @Override
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    LocalCache.removeByKey(key[0]);
                } else {
                    for (Object singleKey : CollectionUtils.arrayToList(key)) {
                        LocalCache.removeByKey(singleKey.toString());
                    }
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    @Override
    public Object get(String key) {
        return key == null ? null : LocalCache.getValue(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public boolean set(String key, Object value) {
        try {
            LocalCache.put(key, value, -1);
            return true;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 如果不存在，则普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public boolean setIfAbsent(String key, Object value, long time) {
        try {
            boolean result = LocalCache.putIfAbsent(key, value, -1);
            return result;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    @Override
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try {
            return LocalCache.increment(key, delta);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 将数据放入list缓存
     * @param key 键
     * @param value 值
     * @return
     */
    @Override
    public <T extends Serializable> boolean lSet(String key, T value) {
        try {
            LocalCache.listRightPush(key, value);
            return true;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获取并移除第一个数据（从左开始）
     * @param key 键
     * @return 移除的数据
     */
    @Override
    public Object lPop(String key) {
        try {
            return LocalCache.listLeftPop(key);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}
