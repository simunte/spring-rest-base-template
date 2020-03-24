package template.base.services;


import template.base.services.dto.UserDetailDTO;
import template.base.services.dto.UserGlobalDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Boolean saveUser(UserGlobalDTO userGlobalDTO)  throws Exception;
    Optional<UserDetailDTO> getDetailUser(Long id);
    List<UserGlobalDTO> getAllUser();
    Boolean deleteUser(Integer[] userId);
}
