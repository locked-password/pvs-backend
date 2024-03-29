package pvs.app.members.roles;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
