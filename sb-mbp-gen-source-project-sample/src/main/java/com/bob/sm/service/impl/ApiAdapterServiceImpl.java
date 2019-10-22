package com.bob.sm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.bob.sm.config.YmlConfig;
import com.bob.sm.dto.criteria.BaseCriteria;
import com.bob.sm.dto.criteria.filter.Filter;
import com.bob.sm.dto.help.ApiAdapterConfigDTO;
import com.bob.sm.dto.help.ApiAdapterCriteriaDTO;
import com.bob.sm.dto.help.ApiAdapterResultFieldDTO;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.service.ApiAdapterService;
import com.bob.sm.web.rest.errors.CommonAlertException;
import com.bob.sm.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API适配器
 * @author Bob
 */
@Service
public class ApiAdapterServiceImpl implements ApiAdapterService {

    private final Logger log = LoggerFactory.getLogger(ApiAdapterServiceImpl.class);

    @Autowired
    private YmlConfig ymlConfig;

    private final Pattern pattern = Pattern.compile("/\\d+$");

    private Map<String, ApiAdapterConfigDTO> apiAdapterConfigDTOMap;

    /**
     * 初始化前端接口适配器
     */
    @Override
    public void initApiAdapter() {
        apiAdapterConfigDTOMap = new HashMap<>();
        try {
            // 获取资源目录apiAdapter下的所有json配置文件
            ClassPathResource configFileNameResource = new ClassPathResource("inter/adapter/config_files");
            if (!configFileNameResource.exists()) {
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(configFileNameResource.getInputStream()));
            String line = null;
            List<ApiAdapterConfigDTO> configList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (!line.endsWith(".json")) {
                    continue;
                }
                ClassPathResource configFileResource = new ClassPathResource("inter/adapter/" + line);
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
                if (line.startsWith("#") || !line.equals(key + ".json")) {
                    throw new CommonAlertException(line + "文件名与配置不符");
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
                configList.add(configDTO);
            }
            if ("true".equals(ymlConfig.getApiPdfGenerate()) && configList.size() > 0) {
                // 生成PDF格式的API文档
                Document pdfDocument = new Document(PageSize.A4);
                String apiDocPath = System.getProperty("user.dir") + "\\src\\main\\resources\\inter\\doc\\";
                String pdfFileName = apiDocPath + "api.pdf";
                try {
                    // 生成PDF格式的API说明文档
                    PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdfFileName));
                    pdfDocument.addTitle("API文档说明");
                    pdfDocument.open();
                    for (ApiAdapterConfigDTO configDTO : configList) {
                        writeApiToPdf(pdfDocument, configDTO, configDTO.getReturnConfigTreeList());
                    }
                } finally {
                    pdfDocument.close();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonAlertException(e.getMessage());
        }
    }

    /**
     * 初始化基本接口的文档
     */
    @Override
    public void initApiDocBase() {
        try {
            // 获取资源目录apiAdapter下的所有json配置文件
            ClassPathResource configFileNameResource = new ClassPathResource("inter/base/config_files");
            if (!configFileNameResource.exists()) {
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(configFileNameResource.getInputStream()));
            String line = null;
            List<ApiAdapterConfigDTO> configList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (!line.endsWith(".json")) {
                    continue;
                }
                ClassPathResource configFileResource = new ClassPathResource("inter/base/" + line);
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
                        + "_" + configDTO.getHttpMethod();
                if (line.startsWith("#") || !line.equals(key + ".json")) {
                    throw new CommonAlertException(line + "文件名与配置不符");
                }
                // 按处理结果的层级排序（需要一层一层处理）
                List<ApiAdapterResultFieldDTO> fieldConfigSortList = sortReturnConfigList(configDTO);
                // 按处理结果的层级组装成树状结构
                List<ApiAdapterResultFieldDTO> fieldConfigTreeList = new ArrayList<>();
                formReturnConfigTree(fieldConfigTreeList, "", fieldConfigSortList, 1);
                // 将结果设置进configDTO中
                configDTO.setReturnConfigTreeList(fieldConfigTreeList);
                configList.add(configDTO);
            }
            // 获取资源目录apiAdapter下的所有json配置文件
            ClassPathResource configFileNameExtendResource = new ClassPathResource("inter/extend/config_files");
            if (!configFileNameResource.exists()) {
                return;
            }
            BufferedReader readerExtend = new BufferedReader(new InputStreamReader(configFileNameExtendResource.getInputStream()));
            String lineExtend = null;
            while ((lineExtend = readerExtend.readLine()) != null) {
                if (lineExtend.startsWith("#") || !lineExtend.endsWith(".json")) {
                    continue;
                }
                ClassPathResource configFileResource = new ClassPathResource("inter/extend/" + lineExtend);
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
                        + "_" + configDTO.getHttpMethod();
                if (!lineExtend.equals(key + ".json")) {
                    throw new CommonAlertException(lineExtend + "文件名与配置不符");
                }
                // 按处理结果的层级排序（需要一层一层处理）
                List<ApiAdapterResultFieldDTO> fieldConfigSortList = sortReturnConfigList(configDTO);
                // 按处理结果的层级组装成树状结构
                List<ApiAdapterResultFieldDTO> fieldConfigTreeList = new ArrayList<>();
                formReturnConfigTree(fieldConfigTreeList, "", fieldConfigSortList, 1);
                // 将结果设置进configDTO中
                configDTO.setReturnConfigTreeList(fieldConfigTreeList);
                configList.add(configDTO);
            }
            if ("true".equals(ymlConfig.getApiPdfGenerate()) && configList.size() > 0) {
                // 生成PDF格式的API文档
                Document pdfDocument = new Document(PageSize.A4);
                String apiDocPath = System.getProperty("user.dir") + "\\src\\main\\resources\\inter\\doc\\";
                String pdfFileName = apiDocPath + "apibase.pdf";
                try {
                    // 生成PDF格式的API说明文档
                    PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdfFileName));
                    pdfDocument.addTitle("API文档说明(Base)");
                    pdfDocument.open();
                    for (ApiAdapterConfigDTO configDTO : configList) {
                        writeApiToPdf(pdfDocument, configDTO, configDTO.getReturnConfigTreeList());
                    }
                } finally {
                    pdfDocument.close();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonAlertException(e.getMessage());
        }
    }

    /**
     * 将API写入PDF文档
     * @param pdfDocument
     * @param configDTO
     * @param fieldConfigTreeList
     */
    private void writeApiToPdf(Document pdfDocument, ApiAdapterConfigDTO configDTO,
                               List<ApiAdapterResultFieldDTO> fieldConfigTreeList) throws Exception {
        JSONObject descrJsonObj = new JSONObject();
        getJsonObjFromConfig(descrJsonObj, fieldConfigTreeList);

        // 往PDF格式的接口文档写入内容
        Font fontText = new Font(BaseFont.createFont("/inter/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 7);
        Font fontTitle = new Font(BaseFont.createFont("/inter/font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 8, Font.BOLD);
        Paragraph urlParagraph = new Paragraph();
        urlParagraph.add(new Chunk("接口URL：", fontTitle));
        urlParagraph.add(new Chunk(configDTO.getHttpUrl(), fontText));
        pdfDocument.add(urlParagraph);
        Paragraph methodParagraph = new Paragraph();
        methodParagraph.add(new Chunk("接口方法：", fontTitle));
        methodParagraph.add(new Chunk(configDTO.getHttpMethod(), fontText));
        pdfDocument.add(methodParagraph);
        Paragraph noParagraph = new Paragraph();
        noParagraph.add(new Chunk("接口编号：", fontTitle));
        noParagraph.add(new Chunk(configDTO.getInterNo() == null ? "（无）" : configDTO.getInterNo(), fontText));
        pdfDocument.add(noParagraph);
        Paragraph descrParagraph = new Paragraph();
        descrParagraph.add(new Chunk("接口描述：", fontTitle));
        descrParagraph.add(new Chunk(configDTO.getInterDescr() == null ? "（无）" : configDTO.getInterDescr(), fontText));
        pdfDocument.add(descrParagraph);
        if (configDTO.getParam() != null && configDTO.getParam().getCriteriaList() != null) {
            Paragraph paramParagraph = new Paragraph();
            paramParagraph.add(new Chunk("接口URL参数：", fontTitle));
            pdfDocument.add(paramParagraph);
            // 生成一个两列的表格
            PdfPTable pdfPTableParam = new PdfPTable(2);
            // 设置单元格高度
            int fixedHeight = 12;
            // 设置参数表头
            PdfPCell pdfPCellParamTitle = new PdfPCell(new Phrase("参数名", fontText));
            pdfPCellParamTitle.setFixedHeight(fixedHeight);
            pdfPCellParamTitle.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            pdfPCellParamTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            pdfPTableParam.addCell(pdfPCellParamTitle);
            pdfPCellParamTitle = new PdfPCell(new Phrase("参数说明", fontText));
            pdfPCellParamTitle.setFixedHeight(fixedHeight);
            pdfPCellParamTitle.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
            pdfPCellParamTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
            pdfPTableParam.addCell(pdfPCellParamTitle);
            // 设置表内容
            configDTO.getParam().getCriteriaList().forEach(apiAdapterCriteriaDTO -> {
                if (apiAdapterCriteriaDTO.getFromParam() != null && apiAdapterCriteriaDTO.getDescr() != null) {
                    PdfPCell pdfPCellParam = new PdfPCell(new Phrase(apiAdapterCriteriaDTO.getFromParam(), fontText));
                    pdfPCellParam.setNoWrap(false);
                    pdfPTableParam.addCell(pdfPCellParam);
                    pdfPCellParam = new PdfPCell(new Phrase(apiAdapterCriteriaDTO.getDescr(), fontText));
                    pdfPCellParam.setNoWrap(false);
                    pdfPTableParam.addCell(pdfPCellParam);
                }
            });
            pdfDocument.add(pdfPTableParam);
        }
        if (configDTO.getParam() != null && configDTO.getParam().getJsonBody() != null) {
            Paragraph bodyParagraph = new Paragraph();
            bodyParagraph.add(new Chunk("接口Body参数：", fontTitle));
            String paramFormattedJson = JSON.toJSONString(configDTO.getParam().getJsonBody(), SerializerFeature.PrettyFormat);
            paramFormattedJson = paramFormattedJson.replace("\t", "\u00a0\u00a0");
            bodyParagraph.add(new Chunk(paramFormattedJson, fontText));
            pdfDocument.add(bodyParagraph);
        }
        // 设置返回说明
        pdfDocument.add(new Paragraph("接口返回：", fontTitle));
        pdfDocument.add(new Paragraph("resultCode - "
                + (configDTO.getResult() == null || configDTO.getResult().getResultCode() == null ?
                        "" : configDTO.getResult().getResultCode()), fontText));
        pdfDocument.add(new Paragraph("errMsg - "
                + (configDTO.getResult() == null || configDTO.getResult().getErrMsg() == null ?
                        "" : configDTO.getResult().getErrMsg()), fontText));
        pdfDocument.add(new Paragraph("data - "
                + (configDTO.getResult() == null || configDTO.getResult().getData() == null ?
                "" : configDTO.getResult().getData()), fontText));
        String resultFormattedJson = JSON.toJSONString(descrJsonObj, SerializerFeature.PrettyFormat);
        resultFormattedJson = resultFormattedJson.replace("\t", "\u00a0\u00a0");
        pdfDocument.add(new Paragraph(resultFormattedJson, fontText));
        pdfDocument.add(new Paragraph("==============================================================================" +
                "==================================================", fontText));
    }

    /**
     * 生成最终格式的JSON描述
     * @param jsonObject
     * @param fieldConfigTreeList
     */
    private void getJsonObjFromConfig(JSONObject jsonObject, List<ApiAdapterResultFieldDTO> fieldConfigTreeList) {
        for (ApiAdapterResultFieldDTO fieldConfigTree : fieldConfigTreeList) {
            JSONObject nextJsonObject = new JSONObject();
            String keyName = fieldConfigTree.getName();
            if (keyName.contains(".")) {
                keyName = keyName.substring(keyName.lastIndexOf(".") + 1);
            }
            if ("list".equals(fieldConfigTree.getType())) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(nextJsonObject);
                jsonObject.put(keyName, jsonArray);
                getJsonObjFromConfig(nextJsonObject, fieldConfigTree.getSubFieldList());
            } else if ("object".equals(fieldConfigTree.getType())) {
                jsonObject.put(keyName, nextJsonObject);
                getJsonObjFromConfig(nextJsonObject, fieldConfigTree.getSubFieldList());
            } else {
                jsonObject.put(keyName, fieldConfigTree.getDescr());
            }
        }
    }

    /**
     * 处理查询类请求参数
     * @param request HTTP请求
     * @param parameters Controller方法的参数
     */
    @Override
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
    @Override
    public void processReturn(HttpServletRequest request, Object[] parameters, Object retVal) {
        ApiAdapterConfigDTO apiAdapterConfigDTO = getApiAdapterConfigFromRequest(request);
        if (apiAdapterConfigDTO == null || apiAdapterConfigDTO.getResult() == null
                || apiAdapterConfigDTO.getResult().getFieldList() != null
                || apiAdapterConfigDTO.getResult().getFieldList().size() == 0
                || apiAdapterConfigDTO.getResult().getFieldList().get(0).getFromName() == null) {
            // 不需要特殊处理的返回
            return;
        }
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
                        Object fixedValue = criteriaDTO.getFixedValue();
                        if (fixedValue != null) {
                            // 参数设置为固定值
                            String[] fromParamSingles = criteriaDTO.getFromParam().split("\\.");
                            // 参数迭代器（一层一层迭代）
                            Object objIter = parameter;
                            for (int index = 0; index < fromParamSingles.length; index++) {
                                String fromParamSingle = fromParamSingles[index];
                                if (objIter == null) {
                                    break;
                                }
                                // 获取属性（使用apache的包可以获取包括父类的属性）
                                Field field = FieldUtils.getField(objIter.getClass(), fromParamSingle, true);
                                // 设置对象的访问权限，保证对private的属性的访问
                                if (field != null) {
                                    field.setAccessible(true);
                                    Object fieldData = field.get(objIter);
                                    if (index != fromParamSingles.length - 1) {
                                        // BaseCriteria类别的参数（前面几层）
                                        if (fieldData == null) {
                                            // 参数值为空的话，创建该参数
                                            Object subCriteria = Class.forName(field.getGenericType().getTypeName()).newInstance();
                                            field.set(objIter, subCriteria);
                                            objIter = subCriteria;
                                        } else if (fieldData instanceof BaseCriteria || fieldData instanceof Filter) {
                                            objIter = fieldData;
                                        } else {
                                            log.warn("条件：" + fromParamSingle + "=" + fixedValue + " 配置错误（属性：" + fromParamSingle
                                                    + "不是BaseCriteria或其子类型），忽略该条配置");
                                        }
                                    } else {
                                        // Filter类别的参数（最后一层）或普通类别的参数
                                        if ("class java.lang.String".equals(field.getGenericType().toString())) {
                                            field.set(objIter, fixedValue);
                                        } else if ("class java.lang.Integer".equals(field.getGenericType().toString())) {
                                            field.set(objIter, Integer.parseInt(fixedValue.toString()));
                                        } else if ("class java.lang.Double".equals(field.getGenericType().toString())) {
                                            field.set(objIter, Double.parseDouble(fixedValue.toString()));
                                        } else {
                                            field.set(objIter, fixedValue);
                                        }
                                        objIter = null;
                                    }
                                }
                            }
                        }
                        if (criteriaDTO.getToCriteriaList() == null) {
                            // 不需要转换，继续下一条
                            continue;
                        }
                        String value = null;
                        if (fixedValue != null) {
                            value = fixedValue.toString();
                        } else {
                            value = request.getParameter(criteriaDTO.getFromParam());
                            if (value == null) {
                                // 没有该参数，继续下一条验证
                                continue;
                            }
                        }
                        // 参数转换
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
                                        } else if (fieldData instanceof BaseCriteria || fieldData instanceof Filter) {
                                            objIter = fieldData;
                                        } else {
                                            log.warn("条件：" + toCriteria + "=" + value + " 配置错误（属性：" + toCriteriaSingle
                                                    + "不是BaseCriteria或其子类型），忽略该条配置");
                                        }
                                    } else {
                                        // Filter类别的参数（最后一层）或普通类别的参数
                                        if ("class java.lang.String".equals(field.getGenericType().toString())) {
                                            field.set(objIter, value);
                                        } else if ("class java.lang.Integer".equals(field.getGenericType().toString())) {
                                            field.set(objIter, Integer.parseInt(value));
                                        } else if ("class java.lang.Double".equals(field.getGenericType().toString())) {
                                            field.set(objIter, Double.parseDouble(value));
                                        } else {
                                            field.set(objIter, value);
                                        }
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
            throw new CommonAlertException(e.getMessage());
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
            } else if (retData instanceof IPage) {
                newRetData = new JSONObject();
                JSONArray pageListArray = new JSONArray();
                ((JSONObject) newRetData).put("total", ((IPage) retData).getTotal());
                ((JSONObject) newRetData).put("size", ((IPage) retData).getSize());
                ((JSONObject) newRetData).put("current", ((IPage) retData).getCurrent());
                ((JSONObject) newRetData).put("pages", ((IPage) retData).getPages());
                for (int i = 0; i < (((IPage) retData).getRecords()).size(); i++) {
                    Object pageSingleData = (((IPage) retData).getRecords()).get(i);
                    // List的下一层必然是Object
                    JSONObject nextNewData = new JSONObject();
                    formNewReturnData(fieldConfigTreeList, nextNewData, 0, pageSingleData, indexList);
                    pageListArray.add(nextNewData);
                }
                ((JSONObject) newRetData).put("records", pageListArray);
            } else {
                newRetData = new JSONObject();
                formNewReturnData(fieldConfigTreeList, newRetData, 0, retData, indexList);
            }

            ((ReturnCommonDTO)((ResponseEntity)retVal).getBody()).setData(newRetData);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonAlertException(e.getMessage());
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
        if (configDTO.getResult() == null || configDTO.getResult().getFieldList() == null) {
            return fieldConfigSortList;
        }
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
            throw new CommonAlertException(e.getMessage());
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
