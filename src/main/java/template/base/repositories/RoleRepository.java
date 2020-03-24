package template.base.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import template.base.domain.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
