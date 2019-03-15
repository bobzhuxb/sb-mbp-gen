package ${packageName}.dto.help;

import java.util.List;

/**
 * 前端接口适配器返回结果DTO
 */
public class ApiAdapterResultDTO {

    private List<ApiAdapterResultFieldDTO> fieldList; // 返回数据转换的字段配置

    public List<ApiAdapterResultFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<ApiAdapterResultFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }
}
