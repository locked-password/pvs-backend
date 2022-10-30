package pvs.app.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import pvs.app.Application;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RoleDAOIntegrationTest {
    @Autowired
    private RoleDAO roleDAO;

    Role userRole;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        userRole = new Role();
        userRole.setName("ROLE");
        roleDAO.save(userRole);
    }
        @Test
    public void whenFindByName_thenReturnRole() {
        Role foundEntity = roleDAO.findByName("ROLE");
        assertEquals("ROLE", foundEntity.getName());
    }
}
