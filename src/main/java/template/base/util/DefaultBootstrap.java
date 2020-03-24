package template.base.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import template.base.domain.Menu;
import template.base.domain.Privilege;
import template.base.domain.Role;
import template.base.domain.User;
import template.base.repositories.MenuRepository;
import template.base.repositories.PrivilegeRepository;
import template.base.repositories.RoleRepository;
import template.base.repositories.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Service
public class DefaultBootstrap implements CommandLineRunner{
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final PrivilegeRepository privilegeRepository;

    public DefaultBootstrap(RoleRepository roleRepository, UserRepository userRepository, MenuRepository menuRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        this.addMenuDefault();
//        this.addPrivilegesDefault();
//        this.addRoleDefault();
//        this.addUserDefault();
    }

    public void addRoleDefault(){
        Role role = new Role();
        role.setName("admin");
        role.setCode("adm");
        role.setDescription("role admin dengan hak akses penuh");
        role.setStatus("ACTIVE");
        role.setPrivileges(privilegeRepository.findAll());
        roleRepository.save(role);
    }

    public void addUserDefault(){
        User user = new User();
        user.setUsername("admin");
        user.setFirstName("admin");
        user.setLastName("console");
        user.setEmail("admin@mailinator.com");
        String encryptedPassword = new BCryptPasswordEncoder().encode("welcome1");
        user.setPassword(encryptedPassword);
        user.setRoles(roleRepository.findAll());
        userRepository.save(user);
    }

    public void addMenuDefault(){
        Menu menu = new Menu();
        menu.setName("User");
        menu.setAliasMenu("User");
        menu.setHeadMenu(null);
        menu.setDescription("User Menu");
        menuRepository.save(menu);
    }

    public void addPrivilegesDefault(){
        menuRepository.findAll().stream().map(menu -> {
            Privilege privilege = new Privilege();
            privilege.setMenu(menu);
            privilege.setRead(Boolean.TRUE);
            privilege.setCreate(Boolean.TRUE);
            privilege.setUpdate(Boolean.TRUE);
            privilege.setDelete(Boolean.TRUE);
            return privilegeRepository.save(privilege);
        }).collect(Collectors.toCollection(LinkedList::new));
    }
}
