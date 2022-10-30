package pvs.app.member;

import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleDAO roleDAO;

    RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public RoleDTO save(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());

        Role savedRole = roleDAO.save(role);
        RoleDTO savedRoleDTO = new RoleDTO();
        savedRoleDTO.setName(savedRole.getName());
        return savedRoleDTO;
    }

    public Role getByName(String name) {
        return roleDAO.findByName(name);
    }
}
