package pvs.app.member.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@Tag("Unit")
class ProjectServiceUnitTest {

    private ProjectService sut;

    private ProjectRepository spyOnProjectRepository;

    @BeforeEach
    void setUp() {
        ProjectDataAccessor mockProjectDataAccessor =
                when(mock(ProjectDataAccessor.class).existsById(anyLong())).thenReturn(false).getMock();
        when(mockProjectDataAccessor.save(isA(Project.class))).thenReturn(new Project());
        spyOnProjectRepository = spy(new ProjectRepository(mockProjectDataAccessor));
        sut = new ProjectService(spyOnProjectRepository);
    }

    @Test
    public void createProject() throws IOException {
        ProjectService spyOnSUT = spy(sut);
        ProjectOfCreation stubbingProjectOfCreation = new ProjectOfCreation();
        stubbingProjectOfCreation.setProjectName("react");
        stubbingProjectOfCreation.setSonarRepositoryURL("http://140.124.181.143:9000/dashboard?id=pvs-springboot");

        spyOnSUT.create(stubbingProjectOfCreation);

        verify(spyOnProjectRepository, atLeast(1))
                .put(anyLong(), isA(Project.class));

    }
}