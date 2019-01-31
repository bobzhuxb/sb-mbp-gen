package ${packageName}.service.micro;

import ${packageName}.dto.help.BasePermissionDTO;

import java.util.List;

public interface BasePermissionService {

    void savePermissionsWithChildren(List<BasePermissionDTO> permissionList);

}
