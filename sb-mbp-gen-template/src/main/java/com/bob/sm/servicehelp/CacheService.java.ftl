package ${packageName}.servicehelp;

import java.io.Serializable;
import java.util.Map;

public interface CacheService {

    /**
     * 连接是否有效
     * @return
     */
    boolean connectionValid();

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    void del(String... key);

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    boolean set(String key, Object value);

    /**
     * 如果不存在，则普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    boolean setIfAbsent(String key, Object value, long time);

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    long incr(String key, long delta);

    /**
     * 将数据放入list缓存
     * @param key 键
     * @param value 值
     * @return
     */
    <T extends Serializable> boolean lSet(String key, T value);

    /**
     * 获取并移除第一个数据（从左开始）
     * @param key 键
     * @return 移除的数据
     */
    Object lPop(String key);

}
