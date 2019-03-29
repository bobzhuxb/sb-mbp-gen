package ${packageName}.dto.help;

import java.util.List;

/**
 * 用于共通化Service代码的实体配置DTO
 */
public class BaseEntityConfigDTO {

    private String tableName;       // 当前实体对应的表名

    private String fullDomainName;       // 当前实体对应的domain全限定名（com.bob.sm.domain.XXX）

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFullDomainName() {
        return fullDomainName;
    }

    public void setFullDomainName(String fullDomainName) {
        this.fullDomainName = fullDomainName;
    }
}
