package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

import java.util.Map;
import java.util.function.Predicate;

/**
 * 权限验证过程中的特殊操作
 * @author Bob
 */
public class InnerAuthFilterOperateDTO {

    @GenComment("角色")
    private String roleName;

    @GenComment("覆盖原本的操作")
    private Predicate<Map<String, Object>> operateOverwrite;

    @GenComment("原本操作前的动作")
    private Predicate<Map<String, Object>> operateBefore;

    @GenComment("原本操作后的动作")
    private Predicate<Map<String, Object>> operateAfter;

    public InnerAuthFilterOperateDTO() {}

    public InnerAuthFilterOperateDTO(String roleName, Predicate<Map<String, Object>> operateOverwrite) {
        this.roleName = roleName;
        this.operateOverwrite = operateOverwrite;
    }

    public InnerAuthFilterOperateDTO(String roleName, Predicate<Map<String, Object>> operateOverwrite,
                                     Predicate<Map<String, Object>> operateBefore,
                                     Predicate<Map<String, Object>> operateAfter) {
        this(roleName, operateOverwrite);
        this.operateBefore = operateBefore;
        this.operateAfter = operateAfter;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Predicate<Map<String, Object>> getOperateOverwrite() {
        return operateOverwrite;
    }

    public void setOperateOverwrite(Predicate<Map<String, Object>> operateOverwrite) {
        this.operateOverwrite = operateOverwrite;
    }

    public Predicate<Map<String, Object>> getOperateBefore() {
        return operateBefore;
    }

    public void setOperateBefore(Predicate<Map<String, Object>> operateBefore) {
        this.operateBefore = operateBefore;
    }

    public Predicate<Map<String, Object>> getOperateAfter() {
        return operateAfter;
    }

    public void setOperateAfter(Predicate<Map<String, Object>> operateAfter) {
        this.operateAfter = operateAfter;
    }
}
