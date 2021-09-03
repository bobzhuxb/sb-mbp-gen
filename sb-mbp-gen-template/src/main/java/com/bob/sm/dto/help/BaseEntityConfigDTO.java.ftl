package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import java.util.List;

/**
 * 用于共通化Service代码的实体配置DTO
 * @author Bob
 */
public class BaseEntityConfigDTO {

    @GenComment("当前实体对应的表名")
    private String tableName;

    @GenComment("当前实体对应的domain全限定名（com.bob.sm.domain.XXX）")
    private String fullDomainName;

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
