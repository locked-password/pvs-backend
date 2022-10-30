package pvs.app.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @MockBean
    private RoleDAO roleDAO;

    Role role;
    RoleDTO roleDTO;

    @BeforeEach
    public void setup() {
        role = new Role();
        role.setRoleId(1L);
        role.setName("USER");

        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("USER");
    }

    @Test
    public void save() {
        Mockito.when(roleDAO.save(any(Role.class))).thenReturn(role);

        Assertions.assertEquals("USER", roleService.save(roleDTO).getName());
    }

    @Test
    public void getByName() {
        Mockito.when(roleDAO.findByName("USER")).thenReturn(role);

        Assertions.assertEquals(role, roleService.getByName("USER"));
    }
}
