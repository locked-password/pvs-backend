package pvs.app.members.projects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.members.projects.hyperlinks.Hyperlink;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@Tag("Integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectDataAccessorIntegrationTest {
    Set<Project> projects;
    Set<Hyperlink> repositories;
    @Autowired
    private ProjectDataAccessor projectDataAccessor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Project project01 = new Project();
        project01.setMemberId(1L);
        project01.setName("react");

        Project project02 = new Project();
        project02.setMemberId(2L);
        project02.setName("angular");

        Hyperlink hyperlink01 = new Hyperlink();
        hyperlink01.setType("github");
        hyperlink01.setUrl("facebook/react");

        Hyperlink hyperlink02 = new Hyperlink();
        hyperlink02.setType("github");
        hyperlink02.setUrl("angular/angular");

        projects = new HashSet<>();
        repositories = new HashSet<>();

        project01.getHyperlinkSet().add(hyperlink01);
        project01.getHyperlinkSet().add(hyperlink02);
        projectDataAccessor.save(project01);
        projectDataAccessor.save(project02);
    }

    @Test
    public void whenFindAll_thenReturnProjectList() {
        List<Project> foundEntityList = projectDataAccessor.findAll();
        Assertions.assertEquals(2, foundEntityList.size());
        Assertions.assertEquals(2, foundEntityList.get(0).getHyperlinkSet().size());
    }
}
