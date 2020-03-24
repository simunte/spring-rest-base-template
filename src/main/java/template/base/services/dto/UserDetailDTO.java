package template.base.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserDetailDTO {
    private Long id;

    private String username;

    private String email;

    private String password;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    private String status;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    @JsonProperty(value = "list_role")
    private List<RoleDetailDTO> roleDetailDTOS;
}
