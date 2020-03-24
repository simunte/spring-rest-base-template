package template.base.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleDetailDTO extends RoleGlobalDTO {
    @JsonProperty(value = "list_menu")
    List<MenuDTO> menuDTOList;
}
