package pvs.app.members.projects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pvs.app.members.projects.hyperlinks.HyperlinkOfAddSonarQubeURL;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.spy;

@Tag("BDDUnit")
@ExtendWith(MockitoExtension.class)
class ProjectServiceBDDUnitTest {

    AutoCloseable autoCloseable;

    @InjectMocks
    private ProjectService sut;

    @Spy
    private ProjectRepository spyOnProjectRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createProject() throws IOException {
        ProjectService spyOnSUT = spy(sut);
        willReturn(true)
                .given(spyOnSUT)
                .addSonarRepo(isA(HyperlinkOfAddSonarQubeURL.class));
        willReturn(new Project())
                .given(spyOnProjectRepository)
                .put(anyLong(), isA(Project.class));
        ProjectOfCreation stubbingProjectOfCreation = new ProjectOfCreation();
        stubbingProjectOfCreation.setProjectName("react");
        stubbingProjectOfCreation.setSonarRepositoryURL("http://140.124.181.143:9000/dashboard?id=pvs-springboot");

        spyOnSUT.create(stubbingProjectOfCreation);

        then(spyOnProjectRepository)
                .should(atLeast(1))
                .put(anyLong(), isA(Project.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
}