package template.base.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import template.base.domain.Role;
import template.base.services.dto.RoleDetailDTO;
import template.base.services.dto.RoleGlobalDTO;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleGlobalDTO toDto(Role entity, @MappingTarget RoleGlobalDTO dto);

    RoleDetailDTO toDtoDetail(Role entity, @MappingTarget RoleDetailDTO dto);

    Role toEntity(RoleDetailDTO dto, @MappingTarget Role entity);
}
