package com.bob.sm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.bob.sm.dto.criteria.BaseCriteria;
import com.bob.sm.dto.help.ApiAdapterConfigDTO;
import com.bob.sm.dto.help.ApiAdapterCriteriaDTO;
import com.bob.sm.dto.help.ApiAdapterResultFieldDTO;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.service.ApiAdapterService;
import com.bob.sm.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApiAdapterServiceImpl implements ApiAdapterService {

    private final Logger log = LoggerFactory.getLogger(ApiAdapterServiceImpl.class);

    private Map<String, ApiAdapterConfigDTO> apiAdapterConfigDTOMap;

    /**
     * 初始化前端接口适配器
     */
    public void initApiAdapter() {
        apiAdapterConfigDTOMap = new HashMap<>();
        try {
            // 获取资源目录apiAdapter下的所有json配置文件
            ClassPathResource configFileNameResource = new ClassPathResource("apiAdapter/config_files");
            if (!configFileNameResource.exists()) {
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(configFileNameResource.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                ClassPathResource configFileResource = new ClassPathResource("apiAdapter/" + line);
                if (!configFileResource.exists()) {
                    continue;
                }
                ApiAdapterConfigDTO configDTO =
                        JSON.parseObject(configFileResource.getInputStream(), StandardCharsets.UTF_8, ApiAdapterConfigDTO.class,
                                // 自动关闭流
                                Feature.AutoCloseSource,
                                // 允许注释
                                Feature.AllowComment,
                                // 允许单引号
                                Feature.AllowSingleQuotes,
                                // 使用 Big decimal
                                Feature.UseBigDecimal);
                String key = configDTO.getHttpUrl().substring(1).replace("/", "_")
                        + "_" + configDTO.getHttpMethod() + "_" + configDTO.getInterNo();
                if (!line.equals(key + ".json")) {
                    throw new CommonException(line + "文件名与配置不符");
                }
                // 按处理结果的层级排序（需要一层一层处理）
                List<ApiAdapterResultFieldDTO> fieldConfigSortList = sortReturnConfigList(configDTO);
                // 按处理结果的层级组装成树状结构
                List<ApiAdapterResultFieldDTO> fieldConfigTreeList = new ArrayList<>();
                formReturnConfigTree(fieldConfigTreeList, "", fieldConfigSortList, 1);
                // 将结果设置进configDTO中
                configDTO.setReturnConfigTreeList(fieldConfigTreeList);
                // 最终将配置放到Map中
                apiAdapterConfigDTOMap.put(key, configDTO);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 处理查询类请求参数
     * @param request HTTP请求
     * @param parameters Controller方法的参数
     */
    public void processQueryParam(HttpServletRequest request, Object[] parameters) {
        ApiAdapterConfigDTO apiAdapterConfigDTO = getApiAdapterConfigFromRequest(request);
        processCriteriaParam(apiAdapterConfigDTO, request, parameters);
        processAssociationNameList(apiAdapterConfigDTO, request, parameters);
        processDictionaryNameList(apiAdapterConfigDTO, request, parameters);
    }

    /**
     * 处理返回结果
     * @param request HTTP请求
     * @param parameters Controller方法的参数
     * @param retVal Controller方法的返回值
     */
    public void processReturn(HttpServletRequest request, Object[] parameters, Object retVal) {
        ApiAdapterConfigDTO apiAdapterConfigDTO = getApiAdapterConfigFromRequest(request);
        processReturnField(apiAdapterConfigDTO, request, parameters, retVal);
    }

    /**
     * 根据HTTP请求获取前端接口适配器
     * @param request
     * @return
     */
    private ApiAdapterConfigDTO getApiAdapterConfigFromRequest(HttpServletRequest request) {
        if (request.getParameter("interNo") == null) {
            return null;
        }
        String requestURI = request.getRequestURI();
        Pattern pattern = Pattern.compile("/\\d+$");
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.find()) {
            requestURI = requestURI.substring(0, requestURI.lastIndexOf("/"));
        }
        String configMapKey = requestURI.substring(1).replace("/", "_")
                + "_" + request.getMethod() + "_" + request.getParameter("interNo");
        ApiAdapterConfigDTO apiAdapterConfigDTO = apiAdapterConfigDTOMap.get(configMapKey);
        if (apiAdapterConfigDTO == null) {
            return null;
        }
        return apiAdapterConfigDTO;
    }

    /**
     * 处理条件参数
     * @param configDTO 配置
     * @param request HTTP请求
     * @param parameters 处理前/后的参数
     */
    private void processCriteriaParam(ApiAdapterConfigDTO configDTO, HttpServletRequest request, Object[] parameters) {
        // 验证该request的配置
        if (configDTO == null || configDTO.getParam() == null || configDTO.getParam().getCriteriaList() == null) {
            return;
        }
        try {
            // 遍历所有参数
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                if (parameter instanceof BaseCriteria) {
                    // 正常情况下，该if分支只会进来一次
                    for (ApiAdapterCriteriaDTO criteriaDTO : configDTO.getParam().getCriteriaList()) {
                        String value = request.getParameter(criteriaDTO.getFromParam());
                        if (value == null) {
                            // 没有该参数，继续下一条验证
                            continue;
                        }
                        for (String toCriteria : criteriaDTO.getToCriteriaList()) {
                            String[] toCriteriaSingles = toCriteria.split("\\.");
                            // 参数迭代器（一层一层迭代）
                            Object objIter = parameter;
                            for (int index = 0; index < toCriteriaSingles.length; index++) {
                                String toCriteriaSingle = toCriteriaSingles[index];
                                if (objIter == null) {
                                    break;
                                }
                                // 获取属性（使用apache的包可以获取包括父类的属性）
                                Field field = FieldUtils.getField(objIter.getClass(), toCriteriaSingle, true);
                                // 设置对象的访问权限，保证对private的属性的访问
                                if (field != null) {
                                    field.setAccessible(true);
                                    Object fieldData = field.get(objIter);
                                    if (index != toCriteriaSingles.length - 1) {
                                        // BaseCriteria类别的参数（前面几层）
                                        if (fieldData == null) {
                                            // 参数值为空的话，创建该参数
                                            Object subCriteria = Class.forName(field.getGenericType().getTypeName()).newInstance();
                                            field.set(objIter, subCriteria);
                                            objIter = subCriteria;
                                        } else if (fieldData instanceof BaseCriteria) {
                                            objIter = fieldData;
                                        } else {
                                            log.warn("条件：" + toCriteria + "=" + value + " 配置错误（属性：" + toCriteriaSingle
                                                    + "不是BaseCriteria或其子类型），忽略该条配置");
                                        }
                                    } else {
                                        // Filter类别的参数（最后一层）或普通类别的参数
                                        field.set(objIter, value);
                                        objIter = null;
                                    }
                                } else {
                                    log.warn("条件：" + toCriteria + "=" + value + " 配置错误（没有" + toCriteriaSingle
                                            + "属性），忽略该条配置。");
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 处理级联查询结果参数
     * @param configDTO 配置
     * @param request HTTP请求
     * @param parameters 处理前/后的参数
     */
    private void processAssociationNameList(ApiAdapterConfigDTO configDTO, HttpServletRequest request, Object[] parameters) {
        // 验证该request的配置
        if (configDTO == null || configDTO.getParam() == null || configDTO.getParam().getAssociationNameList() == null) {
            return;
        }
        // 遍历所有参数
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            if (parameter instanceof BaseCriteria) {
                // 正常情况下，该if分支只会进来一次
                ((BaseCriteria)parameter).setAssociationNameList(configDTO.getParam().getAssociationNameList());
            }
        }
    }

    /**
     * 处理数据字典查询参数
     * @param configDTO 配置
     * @param request HTTP请求
     * @param parameters 处理前/后的参数
     */
    private void processDictionaryNameList(ApiAdapterConfigDTO configDTO, HttpServletRequest request, Object[] parameters) {
        // 验证该request的配置
        if (configDTO == null || configDTO.getParam() == null || configDTO.getParam().getDictionaryNameList() == null) {
            return;
        }
        // 遍历所有参数
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            if (parameter instanceof BaseCriteria) {
                // 正常情况下，该if分支只会进来一次
                ((BaseCriteria)parameter).setDictionaryNameList(configDTO.getParam().getDictionaryNameList());
            }
        }
    }

    /**
     * 处理返回数据
     * @param configDTO 配置
     * @param request HTTP请求
     * @param parameters 参数
     * @param retVal 处理前后的数据
     */
    private void processReturnField(ApiAdapterConfigDTO configDTO, HttpServletRequest request, Object[] parameters,
                                    Object retVal) {
        // 验证该request的配置
        if (configDTO == null || configDTO.getResult() == null || configDTO.getResult().getFieldList() == null) {
            return;
        }
        // 只处理标准格式的返回数据
        Object retData = null;
        if (retVal instanceof ResponseEntity && ((ResponseEntity)retVal).getBody() instanceof ReturnCommonDTO) {
            retData = ((ReturnCommonDTO)((ResponseEntity)retVal).getBody()).getData();
        }
        if (retData == null) {
            return;
        }
        // 接下来处理返回数据
        try {
            // 从配置中获取树状结构的返回配置
            List<ApiAdapterResultFieldDTO> fieldConfigTreeList = configDTO.getReturnConfigTreeList();

            // 数组序号列表（每进入一次数组，记录下当前遍历的Index）
            List<Integer> indexList = new ArrayList<>();
            // 新的返回数据（按照树状的结构配置组装返回结果的结构）
            Object newRetData = null;
            if (retData instanceof List) {
                newRetData = new JSONArray();
                for (int i = 0; i < ((List) retData).size(); i++) {
                    Object singleRetData = ((List) retData).get(i);
                    // List的下一层必然是Object
                    JSONObject nextNewData = new JSONObject();
                    formNewReturnData(fieldConfigTreeList, nextNewData, 0, singleRetData, indexList);
                    ((JSONArray) newRetData).add(nextNewData);
                }
            } else {
                newRetData = new JSONObject();
                formNewReturnData(fieldConfigTreeList, newRetData, 0, retData, indexList);
            }

            ((ReturnCommonDTO)((ResponseEntity)retVal).getBody()).setData(newRetData);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 对返回数据的配置进行排序
     * @param configDTO 配置
     * @return 排序好的配置
     */
    private List<ApiAdapterResultFieldDTO> sortReturnConfigList(ApiAdapterConfigDTO configDTO) {
        // 按处理结果的层级排序（需要一层一层处理）
        List<ApiAdapterResultFieldDTO> fieldConfigSortList = new ArrayList<>();
        // 层级数，每个点分隔不同层级
        int currentLevel = 0;
        while (fieldConfigSortList.size() < configDTO.getResult().getFieldList().size()) {
            currentLevel++;
            for (ApiAdapterResultFieldDTO resultFieldDTO : configDTO.getResult().getFieldList()) {
                String newName = resultFieldDTO.getName();
                String[] newNames = newName.split("\\.");
                if (newNames.length == currentLevel) {
                    resultFieldDTO.setLevel(currentLevel);
                    fieldConfigSortList.add(resultFieldDTO);
                }
            }
        }
        return fieldConfigSortList;
    }

    /**
     * 生成返回配置树
     * @param currentLevelConfigList 当前层级配置列表
     * @param lastName 上一层配置的name（作为本次迭代的name前缀）
     * @param fieldConfigSortList 排序后的所有配置
     * @param levelLoop 当前层级
     */
    private void formReturnConfigTree(List<ApiAdapterResultFieldDTO> currentLevelConfigList, String lastName,
                                      List<ApiAdapterResultFieldDTO> fieldConfigSortList, int levelLoop) {
        for (ApiAdapterResultFieldDTO fieldConfigSort : fieldConfigSortList) {
            String newName = fieldConfigSort.getName();
            if (!newName.startsWith(lastName)) {
                // 不是上一层级对应的层级，过滤掉
                continue;
            }
            String[] newNames = newName.split("\\.");
            if (newNames.length < levelLoop) {
                // 未到当前层级，继续循环遍历
                continue;
            }
            if (newNames.length > levelLoop) {
                // 当前层级遍历结束
                break;
            }
            // 设置第levelLoop层级列表数据
            currentLevelConfigList.add(fieldConfigSort);
            // 追加子层级的数据
            List<ApiAdapterResultFieldDTO> nextLevelConfigList = new ArrayList<>();
            formReturnConfigTree(nextLevelConfigList, newName, fieldConfigSortList, levelLoop + 1);
            fieldConfigSort.setSubFieldList(nextLevelConfigList);
        }
    }

    /**
     * 生成新的返回数据结构和数据
     * @param fieldConfigTreeList 树状结构的配置
     * @param lastNewObjIter 新的返回数据：上一层的数据
     * @param currentLevel 当前newObjIter所在的层级（从0开始）
     * @param oldObjIter 原始的返回数据的迭代器
     * @param indexList 数组序号列表（每进入一次数组，记录下当前遍历的Index）
     */
    private void formNewReturnData(List<ApiAdapterResultFieldDTO> fieldConfigTreeList, Object lastNewObjIter, int currentLevel,
                                   Object oldObjIter, List<Integer> indexList) {
        try {
            for (ApiAdapterResultFieldDTO resultFieldDTO : fieldConfigTreeList) {
                String newName = resultFieldDTO.getName();                // 配置的name
                String newType = resultFieldDTO.getType();                // 配置的数据类型（list、object、普通类型）
                String oldName = resultFieldDTO.getFromName();            // 配置的fromName
                List<ApiAdapterResultFieldDTO> subFieldList = resultFieldDTO.getSubFieldList();     // 下一层属性
                String[] newNames = newName.split("\\.");           // 用.切割开的name的数组
                String newNameAttr = newNames[currentLevel]; // 当前层级数据的key（属性）

                Object newObjIterNow = null;
                if ("object".equals(newType)) {
                    // 转换后当前层是Object
                    newObjIterNow = new JSONObject();
                    // 还有下一层
                    if (subFieldList != null) {
                        // 继续往下迭代，塞数据
                        formNewReturnData(subFieldList, newObjIterNow, currentLevel + 1, oldObjIter, indexList);
                    }
                } else if ("list".equals(newType)) {
                    // 转换后当前层是List
                    newObjIterNow = new JSONArray();
                    // 获取对应的List（主要是要获取List的个数），此时返回值为List类型
                    Object realData = getDataFromOldReturn(oldName, oldObjIter, indexList);
                    if (realData == null) {
                        // 获取到的List为null，则不继续往下迭代了，直接置空
                        newObjIterNow = null;
                    } else {
                        // 获取到的List不为null，说明还有下一层，对于每一个元素继续往下迭代
                        if (realData instanceof List) {
                            for (int i = 0; i < ((List) realData).size(); i++) {
                                // 往数组末尾添加序号
                                indexList.add(i);
                                // List的下一层必然是Object
                                JSONObject nextNewData = new JSONObject();
                                formNewReturnData(subFieldList, nextNewData, currentLevel + 1, oldObjIter, indexList);
                                // 迭代结束，移除最后一个序号
                                indexList.remove(indexList.size() - 1);
                                ((JSONArray) newObjIterNow).add(nextNewData);
                            }
                        } else {
                            // 配置错误，不继续往下迭代了
                            log.warn("返回配置错误（name=" + newName + ",type=" + newType + ",fromName=" + oldName + "），忽略该配置");
                        }
                    }
                } else {
                    // 转换后当前层是普通属性
                    // 获取对应的数据（最终的属性值），此时返回值为Object类型
                    newObjIterNow = getDataFromOldReturn(oldName, oldObjIter, indexList);
                }
                // 将后续迭代完成获取到的值设置到上层属性中
                ((JSONObject) lastNewObjIter).put(newNameAttr, newObjIterNow);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 从原始retData获取数据值
     * @param oldName 某条字段配置的fromName
     * @param oldObjIter 原始的返回数据的迭代器
     * @param indexList 数组序号列表（每进入一次数组，记录下当前遍历的Index）
     * @return 获取到的数据值
     * @throws Exception
     */
    private Object getDataFromOldReturn(String oldName, Object oldObjIter, List<Integer> indexList) throws Exception {
        // 参数字段转换时，oldName字段必须存在
        String[] oldNames = oldName.split("\\.");               // 用.切割开的fromName的数组

        // 获取转换前的字段数据
        Object fieldValue = null;
        // 原始返回数据的list的层级（需要与新的返回数据的层级对应）
        int oldListLevel = 0;
        for (int i = 0; i < oldNames.length; i++) {
            if (oldObjIter == null) {
                // 迭代到中间某一层的时候数据为null，则直接返回null
                return null;
            }
            String oldNameSingle = oldNames[i];
            // 获取属性（使用apache的包可以获取包括父类的属性）
            Field oldField = FieldUtils.getField(oldObjIter.getClass(), oldNameSingle, true);
            // 设置对象的访问权限，保证对private的属性的访问
            if (oldField != null) {
                oldField.setAccessible(true);
                Object oldFieldData = oldField.get(oldObjIter);
                if (i != oldNames.length - 1) {
                    // 前面几层的参数
                    if (oldFieldData instanceof List && ((List)oldFieldData).size() > 0) {
                        // List格式，获取对应Index的数据，继续迭代
                        Object currentLevelData = ((List) oldFieldData).get(indexList.get(oldListLevel));
                        oldObjIter = currentLevelData;
                        // 原始返回数据的list的层级加1
                        oldListLevel++;
                    } else {
                        // Object格式，继续迭代
                        oldObjIter = oldFieldData;
                    }
                } else {
                    // 最后一层的参数（真实属性或List），获取数据
                    fieldValue = oldFieldData;
                    return fieldValue;
                }
            } else {
                // 原字段不存在
                log.warn("返回：fromName=" + oldName + " 配置错误（该属性不存在），该数据置空");
                return null;
            }
        }

        return fieldValue;
    }

}