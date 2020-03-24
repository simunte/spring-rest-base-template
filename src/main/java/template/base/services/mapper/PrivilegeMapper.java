package template.base.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import template.base.domain.Privilege;
import template.base.services.dto.PrivelegeDTO;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper{

        PrivilegeMapper INSTANCE = Mappers.getMapper(PrivilegeMapper.class);


        @Mapping(source = "menu.id", target = "menu")
        PrivelegeDTO toDto(Privilege entity, @MappingTarget PrivelegeDTO dto);

        @Mapping(target = "menu", ignore = true)
        Privilege toEntity(PrivelegeDTO dto, @MappingTarget Privilege entity);
}
