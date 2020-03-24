package template.base.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoleGlobalDTO {
    private Long id;

    private String name;

    @JsonProperty(value = "code")
    private String  code;

    @JsonProperty(value="status")
    private String status;

    private String description;
}
