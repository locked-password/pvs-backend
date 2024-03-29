package pvs.app.members.roles;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RoleDAOIntegrationTest {
    Role userRole;
    @Autowired
    private RoleDAO roleDAO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        userRole = new Role();
        userRole.setName("ROLE");
        roleDAO.save(userRole);
    }

    @Test
    public void whenFindByName_thenReturnRole() {
        Role foundEntity = roleDAO.findByName("ROLE");
        Assertions.assertEquals("ROLE", foundEntity.getName());
    }
}
