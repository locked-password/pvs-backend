package pvs.app.member.role;

import org.springframework.stereotype.Service;
import pvs.app.common.PVSRepository;

@Service
public class RoleRepository extends PVSRepository<Long, Role, RoleDAO> {
    public RoleRepository(RoleDAO roleDAO) {
        super(roleDAO);
    }

    public Role getByName(String name) {
        return dataAccessor.findByName(name);
    }
}
