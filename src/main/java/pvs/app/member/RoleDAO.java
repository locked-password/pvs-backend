package pvs.app.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pvs.app.member.Role;

@Repository
public interface RoleDAO extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
