package template.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import template.base.domain.Role;
import template.base.repositories.RoleRepository;
import template.base.repositories.UserRepository;
import template.base.services.RoleService;
import template.base.services.UserService;
import template.base.services.dto.RoleDetailDTO;
import template.base.services.dto.RoleGlobalDTO;
import template.base.services.dto.UserDetailDTO;
import template.base.services.dto.UserGlobalDTO;
import template.base.domain.User;
import template.base.services.mapper.RoleMapper;
import template.base.services.mapper.UserMapper;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService{
    private static final String ERROR_NOT_FOUND = "error.not.found";
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, MessageSource messageSource, RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        template.base.domain.User user = userRepository.findOneByUsernameAndIsEnabled(s, true)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage(ERROR_NOT_FOUND,
                        new String[]{s},
                        LocaleContextHolder.getLocale())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getAuthorities());
    }


    @Transactional
    @Override
    public Boolean saveUser(UserGlobalDTO userGlobalDTO) throws Exception{
        User user = new User();
        if (userGlobalDTO.getId() != null){
            User existUser = userRepository.findById(userGlobalDTO.getId())
                    .orElseThrow(() -> new Exception("User Not Found"));
            user = UserMapper.INSTANCE.toEntity(userGlobalDTO, existUser);
        }else {
            user = UserMapper.INSTANCE.toEntity(userGlobalDTO, new User());
        }
        user.setPassword(new BCryptPasswordEncoder().encode(userGlobalDTO.getPassword()));
        user.setRoles(
            userGlobalDTO.getRoleGlobalDTOS().stream().map(
                roleGlobalDTO -> {
                    Role roleFound = roleRepository.findById(roleGlobalDTO.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Role Not Found"));
                    return roleFound;
                }).collect(Collectors.toCollection(LinkedList::new))
        );
        userRepository.save(user);
        return Boolean.TRUE;
    }

    @Override
    public List<UserGlobalDTO> getAllUser() {
        List<UserGlobalDTO> userGlobalDTOS = userRepository.findAll().stream().map(user -> {
            UserGlobalDTO userGlobalDTO = UserMapper.INSTANCE.toDtoGloball(user, new UserGlobalDTO());
            userGlobalDTO.setRoleGlobalDTOS(
                    user.getRoles().stream().map(role -> {
                        RoleGlobalDTO roleGlobalDTO = RoleMapper.INSTANCE.toDto(role, new RoleGlobalDTO());
                        return roleGlobalDTO;
                    }).collect(Collectors.toCollection(LinkedList::new))
            );
            return userGlobalDTO;
        }).collect(Collectors.toCollection(LinkedList::new));
        return userGlobalDTOS;
    }

    @Transactional
    @Override
    public Optional<UserDetailDTO> getDetailUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        UserDetailDTO userDetailDTO = UserMapper.INSTANCE.toDtoDetail(user, new UserDetailDTO());
        userDetailDTO.setRoleDetailDTOS(
                user.getRoles().stream().map(role -> {
                    Optional<RoleDetailDTO> roleDetailDTO = roleService.getDetailRole(role.getId());
                    return roleDetailDTO.get();
                }).collect(Collectors.toCollection(LinkedList::new))
        );
        return Optional.ofNullable(userDetailDTO);
    }


    @Transactional
    @Override
    public Boolean deleteUser(Integer[] userId) {

        return Boolean.TRUE;
    }
}
