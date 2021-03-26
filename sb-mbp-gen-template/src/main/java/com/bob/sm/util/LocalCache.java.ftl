package ${packageName}.util;

import ${packageName}.config.Constants;
import org.apache.ibatis.javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 实现一个与Redis类似的纯内存缓存（不支持持久化）
 * 在预算不够、Redis不稳定、单机应用的情况下，先用内存缓存撑一段时间再说
 * @author Bob
 */
public class LocalCache {

    private static final Logger logger = LoggerFactory.getLogger(LocalCache.class);
    /**
     * 默认缓存容量
     */
    private static final int DEFAULT_CAPACITY = 500;
    /**
     * 最大缓存容量
     */
    private static final int MAX_CAPACITY = 1000000;
    /**
     * 监控清除过期缓存频率
     */
    private static final int MONITOR_FREQUENCY = 3;

    /**
     * 构建本地缓存的map
     * 此处由于可以执行Value为队列（链表实现）时的插入删除，用ConcurrentHashMap太麻烦，先直接加锁完事
     */
    private static Map<String, LocalCacheEntity> cache = new HashMap<>(DEFAULT_CAPACITY);

    // 监控线程启动
    static {
        new Thread(new MonitorThread()).start();
    }

    /**
     * 监控线程
     */
    static class MonitorThread implements Runnable {
        @Override
        public void run() {
            if ("cacheLocalService".equals(Constants.SERVICE_CACHE_NAME)) {
                // 使用本地缓存的话，就启用，否则关闭
                while (true) {
                    try {
                        logger.debug("START CACHE MONITOR");
                        TimeUnit.SECONDS.sleep(MONITOR_FREQUENCY);
                        checkTime();
                    } catch (Exception e) {
                        logger.error("MONITOR CACHE HAS THROWS AN EXCEPTION:", e);
                    }
                }
            }
        }

        // 过期key剔除
        private void checkTime() {
            Set<Map.Entry<String, LocalCacheEntity>> cacheSet = null;
            synchronized (LocalCache.class) {
                cacheSet = cache.entrySet();
            }
            cacheSet.stream().forEach(item -> {
                LocalCacheEntity value = item.getValue();
                if (value.getExpireTime() != -1
                        && TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - value.getGmtCreate()) > value.getExpireTime()) {
                    String key = item.getKey();
                    synchronized (LocalCache.class) {
                        cache.remove(key);
                    }
                    logger.debug("REMOVE EXPIRE KEY:{}", key);
                }
            });
        }
    }

    /**
     * 将key-value 保存到本地缓存并设置缓存过期时间
     * @param key
     * @param value
     * @param expireTime 过期时间，如果是-1 则表示永不过期
     * @return
     */
    public synchronized static boolean put(String key, Object value, int expireTime) {
        if (cache.size() >= MAX_CAPACITY) {
            throw new RuntimeException("CAPACITY OVERFLOW!");
        }
        return putCloneValue(key, value, expireTime);
    }

    /**
     * 如果key不存在，将key-value 保存到本地缓存并设置缓存过期时间
     * @param key
     * @param value
     * @param expireTime 过期时间，如果是-1 则表示永不过期
     * @return
     */
    public synchronized static boolean putIfAbsent(String key, Object value, int expireTime) {
        if (cache.size() >= MAX_CAPACITY) {
            throw new RuntimeException("CAPACITY OVERFLOW!");
        }
        return putCloneValueIfAbsent(key, value, expireTime);
    }

    /**
     * 将值通过序列化 clone 处理后保存到缓存中，可以解决值引用的问题
     *
     * 备注说明：clone方法只是一个保险的方法，防止出现值传递的问题，
     * 会出现这个问题的原因是因为在外部存储对象（不包括String及包装类型）做缓存时，
     * 如果再存储过后，对对象进行修改的话，也会对缓存中的对象进行修改
     * @param key
     * @param value
     * @param expireTime 过期时间，如果是-1 则表示永不过期
     * @return boolean
     */
    private static boolean putCloneValue(String key, Object value, int expireTime) {
        try {
            LocalCacheEntity cacheEntity = clone(new LocalCacheEntity(value, System.nanoTime(), expireTime));
            cache.put(key, cacheEntity);
            return true;
        } catch (Exception e) {
            logger.error("PUT VALUE HAS THROWS AN EXCEPTION:", e);
            return false;
        }
    }

    /**
     * 将值通过序列化 clone 处理后保存到缓存中，可以解决值引用的问题
     * @param key
     * @param value
     * @param expireTime 过期时间，如果是-1 则表示永不过期
     * @return boolean
     */
    private static boolean putCloneValueIfAbsent(String key, Object value, int expireTime) {
        try {
            LocalCacheEntity cacheEntity = clone(new LocalCacheEntity(value, System.nanoTime(), expireTime));
            LocalCacheEntity beforeEntity = cache.putIfAbsent(key, cacheEntity);
            return true;
        } catch (Exception e) {
            logger.error("PUT VALUE HAS THROWS AN EXCEPTION:", e);
            return false;
        }
    }

    /**
     * 删除
     * @param key
     * @return boolean
     */
    public synchronized static boolean removeByKey(String key) {
        try {
            LocalCacheEntity cacheEntity = cache.remove(key);
            return cacheEntity != null;
        } catch (Exception e) {
            logger.error("PUT VALUE HAS THROWS AN EXCEPTION:", e);
            return false;
        }
    }

    /**
     * javassist测试
     */
    public <T extends Serializable> T javassist() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("com.zaxxer.hikari.cacheLocal.LocalCacheEntity1");
        CtField ctField1 = new CtField(classPool.get("java.lang.Object"), "value", ctClass);
        CtField ctField2 = new CtField(CtClass.longType, "gmtCreate", ctClass);
        CtField ctField3 = new CtField(CtClass.intType, "expireTime", ctClass);
        CtField ctField4 = new CtField(CtClass.intType, "serialVersionUID", ctClass);
        ctField1.setModifiers(Modifier.PRIVATE);
        ctField2.setModifiers(Modifier.PRIVATE);
        ctField3.setModifiers(Modifier.PRIVATE);
        ctField4.setModifiers(Modifier.PRIVATE);
        ctField4.setModifiers(Modifier.STATIC);
        ctField4.setModifiers(Modifier.FINAL);
        ctClass.addField(ctField4, "7172649826282703560L");
        ctClass.setInterfaces(new CtClass[]{classPool.get("java.io.Serializable")});
        ctClass.addMethod(CtNewMethod.setter("setValue", ctField1));
        ctClass.addMethod(CtNewMethod.setter("setGmtCreate", ctField2));
        ctClass.addMethod(CtNewMethod.setter("setExpireTime", ctField3));
        ctClass.addMethod(CtNewMethod.getter("getValue", ctField1));
        ctClass.addMethod(CtNewMethod.getter("getGmtCreate", ctField2));
        ctClass.addMethod(CtNewMethod.getter("getExpireTime", ctField3));
        CtConstructor constructor = new CtConstructor(new CtClass[]{classPool.get("java.lang.Object"), classPool.get("long"), classPool.get("int")}, ctClass);
        constructor.setBody("{$0.value = $1;$0.gmtCreate = $2;$0.expireTime = $3;}");
        ctClass.addConstructor(constructor);
        ctClass.writeFile("/dd/aa");
        Object o = ctClass.toClass().newInstance();
        ctClass.detach();
        return (T) o;
    }

    /**
     * 序列化 克隆处理
     * @param obj
     * @return LocalCacheEntity
     */
    public static <T extends Serializable> T clone(T obj) {
        T target = null;
        // ===========测试代码===========
//      try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos);
//           ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))){
//         oos.writeObject(obj);
//         target = (LocalCacheEntity) ois.readObject();
//      }catch (Exception e){
//         logger.error("CLONE VALUE HAS THROWS AN EXCEPTION:", e);
//      }
//      return target;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            target = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            logger.error("CLONE VALUE HAS THROWS AN EXCEPTION:", e);
        }
        return target;
    }

    /**
     *  清除缓存
     */
    public synchronized static void clear() {
        cache.clear();
    }

    /**
     * 根据key得到value
     * @param key
     * @return Object
     */
    public synchronized static LocalCacheEntity getValue(String key) {
        return cache.get(key);
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几
     * @return
     */
    public synchronized static long increment(String key, long delta) {
        LocalCacheEntity cacheEntity = cache.get(key);
        if (cacheEntity == null) {
            throw new RuntimeException("Key is not exist");
        }
        Object value = cacheEntity.getObj();
        if (value == null) {
            throw new RuntimeException("Value is null");
        }
        if (value instanceof Long || value instanceof Integer) {
            long result = (Long)value + delta;
            cacheEntity.setObj(result);
            return result;
        } else {
            throw new RuntimeException("Value is not countable");
        }
    }

    /**
     * 左侧进队列
     * @param key 键
     * @param appendData 追加的数据
     * @return
     */
    public synchronized static <T extends Serializable> long listLeftPush(String key, T appendData) {
        LocalCacheEntity cacheEntity = cache.get(key);
        T appendDataClone = clone(appendData);
        if (cacheEntity == null) {
            List<T> valueList = new LinkedList<T>() {{add(appendDataClone);}};
            cache.put(key, new LocalCacheEntity(valueList, System.nanoTime(), -1));
            return 1L;
        } else {
            Object value = cacheEntity.getObj();
            if (value == null) {
                value = new LinkedList<>();
                cacheEntity.setObj(value);
            }
            if (value instanceof List) {
                ((List) value).add(0, appendDataClone);
                return 1L;
            } else {
                throw new RuntimeException("Value is not a list");
            }
        }
    }

    /**
     * 右侧进队列
     * @param key 键
     * @param appendData 追加的数据
     * @return
     */
    public synchronized static <T extends Serializable> long listRightPush(String key, T appendData) {
        LocalCacheEntity cacheEntity = cache.get(key);
        T appendDataClone = clone(appendData);
        if (cacheEntity == null) {
            List<T> valueList = new LinkedList<T>() {{add(appendDataClone);}};
            cache.put(key, new LocalCacheEntity(valueList, System.nanoTime(), -1));
            return 1L;
        } else {
            Object value = cacheEntity.getObj();
            if (value == null) {
                value = new LinkedList<>();
                cacheEntity.setObj(value);
            }
            if (value instanceof List) {
                ((List) value).add(appendDataClone);
                return 1L;
            } else {
                throw new RuntimeException("Value is not a list");
            }
        }
    }

    /**
     * 左侧出队列
     * @param key 键
     * @return
     */
    public synchronized static <T extends Serializable> T listLeftPop(String key) {
        LocalCacheEntity cacheEntity = cache.get(key);
        if (cacheEntity == null) {
            return null;
        }
        Object value = cacheEntity.getObj();
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            if (((List) value).size() > 0) {
                T data = ((LinkedList<T>)value).removeFirst();
                return data;
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Value is not a list");
        }
    }

    /**
     * 右侧出队列
     * @param key 键
     * @param appendData 追加的数据
     * @return
     */
    public synchronized static <T extends Serializable> T listRightPop(String key, Object appendData) {
        LocalCacheEntity cacheEntity = cache.get(key);
        if (cacheEntity == null) {
            return null;
        }
        Object value = cacheEntity.getObj();
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            if (((List) value).size() > 0) {
                T data = ((LinkedList<T>)value).removeLast();
                return data;
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Value is not a list");
        }
    }
}
