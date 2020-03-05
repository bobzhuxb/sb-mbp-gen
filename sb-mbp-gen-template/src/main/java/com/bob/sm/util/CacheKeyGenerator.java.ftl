package ${packageName}.util;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 缓存的键生成器（根据类名.方法名.参数列表生成）
 * @author Bob
 */
public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        sb.append("." + method.getName());
        if (params == null || params.length == 0 || params[0] == null) {
            return null;
        }
        String join = String.join("&", Arrays.stream(params).map(Object::toString).collect(Collectors.toList()));
        String format = String.format("%s{%s}", sb.toString(), join);
        //log.info("缓存key：" + format);
        return format;
    }

}
