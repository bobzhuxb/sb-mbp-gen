package com.bob.sm.service.micro;

import com.bob.sm.dto.help.BasePermissionDTO;

import java.util.List;

public interface BasePermissionService {

    void savePermissionsWithChildren(List<BasePermissionDTO> permissionList);

}
