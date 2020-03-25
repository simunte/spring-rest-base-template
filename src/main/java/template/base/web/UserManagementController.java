package template.base.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import template.base.services.UserService;
import template.base.services.dto.UserDetailDTO;
import template.base.services.dto.UserGlobalDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="User Management")
public class UserManagementController {

    @Autowired
    TokenStore tokenStore;

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    /* START USER API */
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/logout")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null) {
//            //TODO AUDIT TRAIL LOGOUT
//            userService.createLogoutAuditTrail();
//            String tokenValue = authHeader.replace("Bearer", "").trim();
//            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
//            tokenStore.removeAccessToken(accessToken);
//        }
//        return ResponseEntity.ok().body(Boolean.TRUE);
//    }
    @PreAuthorize("@authorization.checkPrivilege('User_READ')")
    @GetMapping("users")
    @ApiOperation("get All User")
    public ResponseEntity<List<UserGlobalDTO>> getAllUsers() {
        final List<UserGlobalDTO> result = userService.getAllUser();
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('User_READ')")
    @GetMapping("users/{id}")
    @ApiOperation("get Users")
    public ResponseEntity<UserDetailDTO> getDetailUser(@PathVariable Long id) {
        final Optional<UserDetailDTO> result = userService.getDetailUser(id);
        return ResponseEntity.ok()
                .body(result.get());
    }

    @PreAuthorize("@authorization.checkPrivilege('User_CREATE')")
    @PostMapping("/users")
    @ApiOperation("Add User")
    public ResponseEntity<Boolean> addUser(@Valid @RequestBody UserGlobalDTO userGlobalDTO) throws Exception {
        Boolean result = userService.saveUser(userGlobalDTO);
        return ResponseEntity.created(new URI("/api/v1/users"+userGlobalDTO.getUsername()))
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('User_UPDATE')")
    @PutMapping("/users")
    @ApiOperation("Update User")
    public ResponseEntity<Boolean> updateUser(@Valid @RequestBody UserGlobalDTO userGlobalDTO) throws Exception {
        Boolean result = userService.saveUser(userGlobalDTO);
        return ResponseEntity.created(new URI("/api/v1/users"+userGlobalDTO.getUsername()))
                .body(result);
    }

    @PreAuthorize("@authorization.checkPrivilege('User_DELETE')")
    @PostMapping("/delete-user")
    @ApiOperation("Delete User")
    public ResponseEntity<Boolean> reqDelete(@RequestBody Integer[] userId){
        Boolean result = userService.deleteUser(userId);
        return ResponseEntity.ok()
                .body(result);
    }
}
