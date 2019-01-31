package ${packageName}.dto.help;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the RemotePermissionList entity.
 */
public class RemotePermissionListDTO implements Serializable {

    private List<BasePermissionDTO> permissionDTOMoreList;

    public List<BasePermissionDTO> getPermissionDTOMoreList() {
        return permissionDTOMoreList;
    }

    public void setPermissionDTOMoreList(List<BasePermissionDTO> permissionDTOMoreList) {
        this.permissionDTOMoreList = permissionDTOMoreList;
    }

}
