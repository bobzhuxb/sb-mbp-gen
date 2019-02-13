package ${packageName}.util;

import ${packageName}.annotation.PermissionConfigAllow;
import ${packageName}.config.Constants;
import ${packageName}.dto.SystemPermissionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    public static List<SystemPermissionDTO> getAllPermissions(List<String> packageNameList) {
        List<String> classNames = getClassName(packageNameList);
        List<SystemPermissionDTO> allPermissionList = new ArrayList<>();
        for (String className : classNames) {
            List<SystemPermissionDTO> classPermissionList = getPermission(className);
            allPermissionList.addAll(classPermissionList);
        }

        return allPermissionList;
    }

    public static List<String> getClassName(List<String> packageNameList) {
        List<String> fileNames = new ArrayList<>();
        for (String packageName : packageNameList) {
            String filePath = ClassLoader.getSystemResource("").getPath() + packageName.replace(".", "\\");
            fileNames.addAll(getClassName(filePath, null));
        }
        return fileNames;
    }

    private static List<String> getClassName(String filePath, List<String> className) {
        List<String> myClassName = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles != null) {
            for (File childFile : childFiles) {
                if (childFile.isDirectory()) {
                    // 只计算当前层
                    continue;
//                    myClassName.addAll(getClassName(childFile.getPath(), myClassName));
                } else {
                    String childFilePath = childFile.getPath();
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }

    public static List<SystemPermissionDTO> getPermission(String className) {
        List<SystemPermissionDTO> systemPermissionList = new ArrayList<>();
        try {
            // 获取类及注解
            Class clazz = Class.forName(className);
            Annotation classRestControllerAnnotation = clazz.getDeclaredAnnotation(RestController.class);
            Annotation classRequestMappingAnnotation = clazz.getDeclaredAnnotation(RequestMapping.class);
            Annotation classApiAnnotation = clazz.getDeclaredAnnotation(Api.class);
            if (classRestControllerAnnotation == null || classRequestMappingAnnotation == null) {
                // 没有RestController或没有RequestMapping注解的过滤掉
                return new ArrayList<>();
            }
            String[] prefixUrls = ((RequestMapping)classRequestMappingAnnotation).value();
            if (prefixUrls == null || prefixUrls.length == 0) {
                // 不填写URL的过滤掉
                return new ArrayList<>();
            }
            String description = classApiAnnotation == null ? "" : ((Api)classApiAnnotation).description();
            for (String prefixUrl : prefixUrls) {
                SystemPermissionDTO systemPermissionDTO = new SystemPermissionDTO();
                systemPermissionDTO.setName(description);
                systemPermissionDTO.setFunctionCategroy(className);
                systemPermissionDTO.setHttpUrl(prefixUrl);
                systemPermissionDTO.setCurrentLevel(1);
                systemPermissionList.add(systemPermissionDTO);
            }
            // 如果该类下的所有方法都不允许配置，则该类不允许配置
            boolean parentConfigPermissionAllow = false;
            // 获取方法及注解
            List<SystemPermissionDTO> methodPermissionList = new ArrayList<>();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Annotation methodApiOperationAnnotation = method.getDeclaredAnnotation(ApiOperation.class);
                Annotation methodGetMappingAnnotation = method.getDeclaredAnnotation(GetMapping.class);
                Annotation methodPostMappingAnnotation = method.getDeclaredAnnotation(PostMapping.class);
                Annotation methodPutMappingAnnotation = method.getDeclaredAnnotation(PutMapping.class);
                Annotation methodDeleteMappingAnnotation = method.getDeclaredAnnotation(DeleteMapping.class);
                Annotation permissionConfigAllowAnnotation = method.getDeclaredAnnotation(PermissionConfigAllow.class);
                String httpType = "";
                String[] httpUrls = null;
                if (methodGetMappingAnnotation != null) {
                    httpType = "GET";
                    httpUrls = ((GetMapping)methodGetMappingAnnotation).value();
                } else if (methodPostMappingAnnotation != null) {
                    httpType = "POST";
                    httpUrls = ((PostMapping)methodPostMappingAnnotation).value();
                } else if (methodPutMappingAnnotation != null) {
                    httpType = "PUT";
                    httpUrls = ((PutMapping)methodPutMappingAnnotation).value();
                } else if (methodDeleteMappingAnnotation != null) {
                    httpType = "DELETE";
                    httpUrls = ((DeleteMapping)methodDeleteMappingAnnotation).value();
                } else {
                    // 只接受GET/POST/PUT/DELETE方法
                    continue;
                }
                if (httpUrls == null) {
                    // URL为null的过滤掉（但是如果不填写URL，默认使用类的URL）
                    continue;
                }
                boolean permissionConfigAllow = true;
                if (permissionConfigAllowAnnotation != null) {
                    permissionConfigAllow = ((PermissionConfigAllow)permissionConfigAllowAnnotation).allow();
                }
                String name = methodApiOperationAnnotation == null ? "" : ((ApiOperation)methodApiOperationAnnotation).value();
                for (String httpUrl : httpUrls) {
                    SystemPermissionDTO systemPermissionDTO = new SystemPermissionDTO();
                    systemPermissionDTO.setName(name);
                    systemPermissionDTO.setHttpType(httpType);
                    systemPermissionDTO.setHttpUrl(httpUrl);
                    systemPermissionDTO.setCurrentLevel(2);
                    if (permissionConfigAllow) {
                        // 允许配置
                        systemPermissionDTO.setAllowConfig(Constants.permissionAllowConfig.YES.getValue());
                        parentConfigPermissionAllow = true;
                    } else {
                        // 不允许配置
                        systemPermissionDTO.setAllowConfig(Constants.permissionAllowConfig.NO.getValue());
                    }
                    methodPermissionList.add(systemPermissionDTO);
                }
            }
            // 将类注解和方法注解组合
            for (SystemPermissionDTO systemPermission : systemPermissionList) {
                systemPermission.setAllowConfig(parentConfigPermissionAllow ?
                    Constants.permissionAllowConfig.YES.getValue() : Constants.permissionAllowConfig.NO.getValue());
                systemPermission.setChildList(
                    (ArrayList<SystemPermissionDTO>)((ArrayList<SystemPermissionDTO>) methodPermissionList).clone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemPermissionList;
    }

}
