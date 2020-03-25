package template.base.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import template.base.domain.Menu;
import template.base.domain.User;
import template.base.repositories.MenuRepository;
import template.base.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class Authorization {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    public Authorization(UserRepository userRepository, MenuRepository menuRepository) {
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
    }

    public Boolean checkPrivilege(String privilegeName) throws Exception {
        Boolean result = Boolean.FALSE;
        final Boolean[] temp = {Boolean.FALSE};

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findOneByUsernameAndIsEnabled(authentication.getName(),true)
                .orElseThrow(() -> new Exception("User Not Found"));

        List<String> privilegeListParam = Arrays.asList(privilegeName.split("\\s*,\\s*"));
        for (int x =0; x < privilegeListParam.size(); x++){
            try {
                result = result || checkMenuAndPrivilege(privilegeListParam.get(x), user);
            } catch (Exception e) {
                log.error("ERROR ", e);
            }
        }
        return result;
    }

    private Boolean checkMenuAndPrivilege(String name, User user) throws Exception {
        String[] split = name.split("_");
        String menuName = split[0];
        String privilegeAccess = split[1];

        Menu menu = menuRepository.findByNameLikeIgnoreCase(menuName)
                .orElseThrow(() -> new Exception("Menu Not Found"));

        Optional<User> existUser = userRepository.findByIdAndRolesPrivilegesMenu(user.getId(), menu);

        log.info(existUser.get().toString());
        if (existUser.isPresent()) {
            if (privilegeAccess.trim().equals(Constants.Privilege.READ) && existUser.get().getRoles().get(0).getPrivileges().get(0).getRead()) {
                return Boolean.TRUE;
            }
            if (privilegeAccess.trim().equals(Constants.Privilege.CREATE) && existUser.get().getRoles().get(0).getPrivileges().get(0).getCreate()) {
                return Boolean.TRUE;
            }
            if (privilegeAccess.trim().equals(Constants.Privilege.UPDATE) && existUser.get().getRoles().get(0).getPrivileges().get(0).getUpdate()) {
                return Boolean.TRUE;
            }
            if (privilegeAccess.trim().equals(Constants.Privilege.DELETE) && existUser.get().getRoles().get(0).getPrivileges().get(0).getDelete()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
