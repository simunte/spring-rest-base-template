package template.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import template.base.domain.Menu;
import template.base.domain.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);
    Optional<User> findOneByUsernameAndIsEnabled(String username, Boolean enabled);
    Optional<User> findByIdAndRolesPrivilegesMenu(Long id, Menu menu);

}
