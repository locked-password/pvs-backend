package pvs.app.members.roles;

import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    private String name;

    static public RoleDTO of(Role role) {
        RoleDTO dto = new RoleDTO();

        dto.setId(role.getRoleId());
        dto.setName(role.getName());

        return dto;
    }
}
