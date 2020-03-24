package template.base.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import template.base.domain.User;
import template.base.services.dto.UserDetailDTO;
import template.base.services.dto.UserGlobalDTO;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserGlobalDTO dto, @MappingTarget User entity);

    UserDetailDTO toDtoDetail(User entity, @MappingTarget UserDetailDTO dto);

    UserGlobalDTO toDtoGloball(User entity, @MappingTarget UserGlobalDTO dto);

}
