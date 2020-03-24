package template.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import template.base.domain.Menu;


@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
