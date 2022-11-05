package pvs.app.member.role;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class RoleServiceTest {

    Role role;
    RoleDTO roleDTO;
    @Autowired
    private RoleService sut;
    @MockBean
    private RoleDAO mockRoleDAO;

    @BeforeEach
    public void setup() {
        role = new Role();
        role.setRoleId(1L);
        role.setName("USER");

        roleDTO = new RoleDTO();
        roleDTO.setId(role.getRoleId());
        roleDTO.setName(role.getName());
    }

    @Test
    public void save() {
        when(mockRoleDAO.save(any(Role.class))).thenReturn(role);

        RoleDTO savedRole = sut.save(roleDTO);

        Assertions.assertEquals(role.getName(), savedRole.getName());
    }

    @Test
    public void getByName() {
        when(mockRoleDAO.findByName("USER")).thenReturn(role);

        Role gotRole = sut.getByName("USER");

        Assertions.assertEquals(role.getName(), gotRole.getName());
    }
}
