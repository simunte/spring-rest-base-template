package template.base.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MenuDTO {
    private Long id;

    private String name;

    @JsonProperty(value = "alias_menu")
    private String aliasMenu;

    @JsonProperty(value = "head_of_menu")
    private String headMenu;

    private String description;

    @JsonProperty(value = "privelege_menu")
    private PrivelegeDTO privelegeDTO;
}
