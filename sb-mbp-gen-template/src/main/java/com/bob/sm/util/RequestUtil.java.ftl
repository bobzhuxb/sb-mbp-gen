package ${packageName}.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具类
 * @author Bob
 */
public class RequestUtil {

    private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);

    private RequestUtil() {}


    /**
     * request的全部参数转换成map
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String[]> getMap(HttpServletRequest request) {
        return request.getParameterMap();
    }

    /**
     * request的部分参数转换成map：
     * 根据的keys指定的的Key从request取出值放到Map中
     * @param request
     * @param keys 需要转的key,多个key用逗号隔开
     * @return
     */
    public static Map<String, Object> getMap(HttpServletRequest request, String keys) {
        String[] split = keys.split(",");
        Map<String,Object> map = new HashMap<>(split.length);
        for (String key : split) {
            map.put(key, request.getParameter(key));
        }
        return map;
    }

    /**
     * 解析JSONObject对象转换为 map
     * @param keys
     * @param MainJsonObj
     * @return
     */
    public static Map<String,Object> getMap(String keys, JSONObject MainJsonObj) {
        String[] split = keys.split(",");
        Map<String,Object> map = new HashMap<>(split.length);
        for (String key : split) {
            map.put(key, MainJsonObj.getString(key));
        }
        return map;
    }

    /**
     * 将map转换成bean对象
     * 根据Map的keyValues创建Bean对象
     * @param keyValues
     * @param theClass
     * @return
     */
    public static <T> T getBean(Map<String, Object> keyValues, Class<T> theClass) throws Exception {
        T bean = null;
        bean = theClass.newInstance();
        BeanUtils.populate(bean, keyValues);
        return bean;
    }

    /**
     * 将request转换成bean对象
     * @param theClass
     * @param request
     * @return
     */
    public static <T> T getBean(HttpServletRequest request, Class<T> theClass) throws Exception {
        Map<String, String[]> map = getMap(request);
        Map<String, Object> mapToBean = new HashMap<>();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            mapToBean.put(key, value);
        }
        return getBean(mapToBean, theClass);
    }
}
