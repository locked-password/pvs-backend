package pvs.app.member.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pvs.app.member.role.Role;

@Repository
public interface RoleDAO extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
