package template.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import template.base.domain.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long>{
}
