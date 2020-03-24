package template.base.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import template.base.domain.Menu;
import template.base.services.dto.MenuDTO;

@Mapper(componentModel = "string")
public interface MenuMapper{

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    MenuDTO toDto(Menu entity, @MappingTarget MenuDTO dto);



}
