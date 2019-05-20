package ${packageName}.security.permission;

import ${packageName}.service.SystemPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;


/**
 * copy code from MethodSecurityExpressionRoot
 */
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    @Autowired
    private SystemPermissionService ps;

    private Object filterObject;
    private Object returnObject;
    private Object target;

//    CustomMethodSecurityExpressionRoot(Authentication a) {
//        super(a);
//    }

    CustomMethodSecurityExpressionRoot(Authentication a, SystemPermissionService ps) {
        super(a);
        this.ps = ps;
    }


    public boolean hasCreate(String objectName) {
        String action = "create";
        return hasPermission(null, objectName, action);
    }

    public boolean hasUpdate(String objectName) {
        String action = "update";
        return hasPermission(null, objectName, action);
    }

    public boolean hasDelete(String objectName) {
        String action = "delete";
        return hasPermission(null, objectName, action);
    }

    public boolean hasRead(String objectName) {
        String action = "read";
        return hasPermission(null, objectName, action);
    }

    public boolean hasReadAll(String objectName) {
        String action = "readall";
        return hasPermission(null, objectName, action);
    }


    public boolean hasExport(String objectName) {
        String action = "export";
        return hasPermission(null, objectName, action);
    }

    public boolean hasPublish(String objectName) {
        String action = "publish";
        return hasPermission(null, objectName, action);
    }

    public boolean hasAudit(String objectName) {
        String action = "audit";
        return hasPermission(null, objectName, action);
    }

    public boolean hasAssign(String objectName) {
        String action = "assign";
        return hasPermission(null, objectName, action);
    }

    public boolean hasSubmit(String objectName) {
        String action = "submit";
        return hasPermission(null, objectName, action);
    }

    public boolean hasUpload(String objectName) {
        String action = "upload";
        return hasPermission(null, objectName, action);
    }

    public boolean hasOperation(String objectName) {
        String action = "operation";
        return hasPermission(null, objectName, action);
    }

    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    public Object getFilterObject() {
        return filterObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    void setThis(Object target) {
        this.target = target;
    }

    public Object getThis() {
        return target;
    }
}
