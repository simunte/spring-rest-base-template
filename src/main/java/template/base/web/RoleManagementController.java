package template.base.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import template.base.services.RoleService;
import template.base.services.dto.MenuDTO;
import template.base.services.dto.RoleDetailDTO;
import template.base.services.dto.RoleGlobalDTO;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="User Management")
public class RoleManagementController {

    private final RoleService roleService;

    public RoleManagementController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("@authorization.checkPrivilege('Role_CREATE')")
    @PostMapping("/role")
    @ApiOperation("Add Role and Assign Privelege")
    public ResponseEntity<Boolean> addRole(@Valid @RequestBody RoleDetailDTO roleDetailDTO) throws URISyntaxException{
        Boolean result = roleService.saveRole(roleDetailDTO);
        return ResponseEntity.created(new URI("/api/v1/role"))
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('Role_UPDATE')")
    @PutMapping("/role")
    @ApiOperation("Update Role and Assign Privelege")
    public ResponseEntity<Boolean> updateRole(@Valid @RequestBody RoleDetailDTO roleDetailDTO) throws URISyntaxException{
        Boolean result = roleService.saveRole(roleDetailDTO);
        return ResponseEntity.created(new URI("/api/v1/role"))
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('Role_DELETE')")
    @DeleteMapping("/role")
    @ApiOperation("Delete role")
    public ResponseEntity<Boolean> deleteRole(@RequestBody Integer[] listId) throws URISyntaxException{
        Boolean result =  roleService.deleteRole();
        return ResponseEntity.created(new URI("/api/v1/role"))
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('Role_READ')")
    @GetMapping("/role/list")
    @ApiOperation("get All Role")
    public ResponseEntity<List<RoleGlobalDTO>> getAllRole(){
        final List<RoleGlobalDTO> result= roleService.getAllRole();
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('Role_READ')")
    @GetMapping("/role/menu")
    @ApiOperation("get All Menu")
    public ResponseEntity<List<MenuDTO>> getAllMenu(){
        final List<MenuDTO> result= roleService.getAllMenu();
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('Role_READ')")
    @GetMapping("/role")
    @ApiOperation("get All Role and Assign Privelege")
    public ResponseEntity<List<RoleDetailDTO>> getAllRoleAndPrivilege(){
        final List<RoleDetailDTO> result= roleService.getAllRoleAndDetail();
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('Role_READ')")
    @GetMapping("/role/{id}")
    @ApiOperation("get All Role and Assign Privilege By Role Id")
    public ResponseEntity<RoleDetailDTO> getAllRoleAndPrivilegeByRoleId(@PathVariable Long id){
        Optional<RoleDetailDTO> result= roleService.getDetailRole(id);
        return ResponseEntity.ok()
                .body(result.get());
    }
}
