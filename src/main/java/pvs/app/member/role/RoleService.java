package pvs.app.member.role;

import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDTO save(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());

        Role savedRole = roleRepository.put(null, role);

        return RoleDTO.of(savedRole);
    }

    public Role getByName(String name) {
        return roleRepository.getByName(name);
    }
}
