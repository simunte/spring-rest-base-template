package template.base.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PrivelegeDTO {

    private Long id;

    @NotNull
    @JsonProperty(value = "menu_id")
    private Long menu;

    @NotNull
    @JsonProperty(value = "create_access")
    private Boolean create;

    @NotNull
    @JsonProperty(value = "read_access")
    private Boolean read;

    @NotNull
    @JsonProperty(value = "update_access")
    private Boolean update;

    @NotNull
    @JsonProperty(value = "delete_access")
    private Boolean delete;

}
