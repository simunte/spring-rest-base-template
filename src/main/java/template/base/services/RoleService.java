package template.base.services;


import template.base.services.dto.MenuDTO;
import template.base.services.dto.RoleDetailDTO;
import template.base.services.dto.RoleGlobalDTO;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    Boolean saveRole(RoleDetailDTO roleDetailDTO);
    List<RoleGlobalDTO> getAllRole();
    List<MenuDTO> getAllMenu();
    List<RoleDetailDTO> getAllRoleAndDetail();
    Optional<RoleDetailDTO> getDetailRole(Long id);
    Boolean deleteRole();
}
