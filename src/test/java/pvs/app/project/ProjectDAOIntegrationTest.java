package pvs.app.project;

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
import pvs.app.project.repository.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectDAOIntegrationTest {
    @Autowired
    private ProjectDAO projectDAO;
    Set<Project> projects;
    Set<Repository> repositories;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Project project01 = new Project();
        project01.setMemberId(1L);
        project01.setName("react");

        Project project02 = new Project();
        project02.setMemberId(2L);
        project02.setName("angular");

        Repository repository01 = new Repository();
        repository01.setType("github");
        repository01.setUrl("facebook/react");

        Repository repository02 = new Repository();
        repository02.setType("github");
        repository02.setUrl("angular/angular");

        projects = new HashSet<>();
        repositories = new HashSet<>();

        project01.getRepositorySet().add(repository01);
        project01.getRepositorySet().add(repository02);
        projectDAO.save(project01);
        projectDAO.save(project02);
    }

    @Test
    public void whenFindAll_thenReturnProjectList() {
        List<Project> foundEntityList = projectDAO.findAll();
        Assertions.assertEquals(2, foundEntityList.size());
        Assertions.assertEquals(2, foundEntityList.get(0).getRepositorySet().size());
    }
}
