package template.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import template.base.domain.Menu;
import template.base.domain.Privilege;
import template.base.domain.Role;
import template.base.repositories.MenuRepository;
import template.base.repositories.PrivilegeRepository;
import template.base.repositories.RoleRepository;
import template.base.services.RoleService;
import template.base.services.dto.MenuDTO;
import template.base.services.dto.PrivelegeDTO;
import template.base.services.dto.RoleDetailDTO;
import template.base.services.dto.RoleGlobalDTO;
import template.base.services.mapper.MenuMapper;
import template.base.services.mapper.PrivilegeMapper;
import template.base.services.mapper.RoleMapper;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final PrivilegeRepository privilegeRepository;

    public RoleServiceImpl(RoleRepository roleRepository, MenuRepository menuRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Transactional
    @Override
    public Boolean saveRole(RoleDetailDTO roleDetailDTO) {
        Role role = new Role();
        if (roleDetailDTO.getId() != null){
            Role existRole = roleRepository.findById(roleDetailDTO.getId())
                    .orElseThrow(()-> new EntityNotFoundException("role not found"));
            role = RoleMapper.INSTANCE.toEntity(roleDetailDTO, existRole);
        }else {
            role = RoleMapper.INSTANCE.toEntity(roleDetailDTO, new Role());
        }
        role.setPrivileges(
                roleDetailDTO.getMenuDTOList().stream().map(menuDTO -> {
                    Menu menu = menuRepository.findById(menuDTO.getId())
                            .orElseThrow(()-> new EntityNotFoundException("menu not found"));
                    Privilege privilege = PrivilegeMapper.INSTANCE.toEntity(menuDTO.getPrivelegeDTO(), new Privilege());
                    privilege.setMenu(menu);
                    return privilegeRepository.save(privilege);
                }).collect(Collectors.toCollection(LinkedList::new))
        );
        roleRepository.save(role);
        return Boolean.TRUE;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoleGlobalDTO> getAllRole() {
        return roleRepository.findAll().stream().map(role -> RoleMapper.INSTANCE.toDto(role, new RoleGlobalDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    @Override
    public List<MenuDTO> getAllMenu() {
        return menuRepository.findAll().stream().map(menu -> MenuMapper.INSTANCE.toDto(menu, new MenuDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional
    @Override
    public List<RoleDetailDTO> getAllRoleAndDetail(){
        List<RoleDetailDTO> roleDetailDTOS = roleRepository.findAll().stream().map(role -> {
            RoleDetailDTO roleDetailDTO = RoleMapper.INSTANCE.toDtoDetail(role, new RoleDetailDTO());
            roleDetailDTO.setMenuDTOList(
                    role.getPrivileges().stream().map(privilege -> {
                        MenuDTO menuDTO = MenuMapper.INSTANCE.toDto(privilege.getMenu(), new MenuDTO());
                        menuDTO.setPrivelegeDTO(PrivilegeMapper.INSTANCE.toDto(privilege, new PrivelegeDTO()));
                        return menuDTO;
                    }).collect(Collectors.toCollection(LinkedList::new))
            );
            return roleDetailDTO;
        }).collect(Collectors.toCollection(LinkedList::new));
        return roleDetailDTOS;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<RoleDetailDTO> getDetailRole(Long id) {
        return roleRepository.findById(id).map(role -> {
            RoleDetailDTO roleDetailDTO = RoleMapper.INSTANCE.toDtoDetail(role, new RoleDetailDTO());
            roleDetailDTO.setMenuDTOList(
                    role.getPrivileges().stream().map(privilege -> {
                        MenuDTO menuDTO = MenuMapper.INSTANCE.toDto(privilege.getMenu(), new MenuDTO());
                        menuDTO.setPrivelegeDTO(PrivilegeMapper.INSTANCE.toDto(privilege, new PrivelegeDTO()));
                        return menuDTO;
                    }).collect(Collectors.toCollection(LinkedList::new))
            );
            return roleDetailDTO;
        });
    }

    @Transactional
    @Override
    public Boolean deleteRole() {
        return null;
    }
}
