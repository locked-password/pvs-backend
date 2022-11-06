package pvs.app.member.project;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.member.project.hyperlink.Hyperlink;
import pvs.app.member.project.hyperlink.HyperlinkDTO;
import pvs.app.member.project.hyperlink.HyperlinkOfAddSonarQubeURL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@Tag("Unit")
public class ProjectServiceTest {
    @InjectMocks
    private ProjectService sut;

    @SpyBean
    private ProjectRepository spyOnProjectRepository;
    private ProjectOfCreation stubbingProjectOfCreation;
    private Project stubbingProject;

    @Autowired
    public ProjectServiceTest(ProjectService sut, ProjectRepository spyOnProjectRepository) {
        this.sut = sut;
        this.spyOnProjectRepository = spyOnProjectRepository;
    }

    @BeforeEach
    public void setup() throws IOException {
        stubbingProjectOfCreation = new ProjectOfCreation();
        stubbingProjectOfCreation.setProjectName("react");
        stubbingProjectOfCreation.setSonarRepositoryURL("http://140.124.181.143:9000/dashboard?id=pvs-springboot");

        stubbingProject = new Project();
        stubbingProject.setProjectId(1L);
        stubbingProject.setName(stubbingProjectOfCreation.getProjectName());
        stubbingProject.setMemberId(1L);
        stubbingProject.setAvatarURL("https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4");
        Set<Hyperlink> stubbingRepositories = new HashSet<>();
        Hyperlink stubbingHyperlink = new Hyperlink();
        stubbingHyperlink.setHyperlinkId(1L);
        stubbingHyperlink.setType("sonar");
        stubbingHyperlink.setUrl(stubbingProjectOfCreation.getSonarRepositoryURL());
        stubbingRepositories.add(stubbingHyperlink);
        stubbingProject.setHyperlinkSet(stubbingRepositories);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create() throws IOException {
        // Context:
        ProjectService spyOnSUT = spy(sut);
        doReturn(true)
                .when(spyOnSUT)
                .addSonarRepo(isA(HyperlinkOfAddSonarQubeURL.class));

        // When:
        spyOnSUT.create(stubbingProjectOfCreation);

        // Then:
        verify(spyOnProjectRepository, atLeast(1)).put(any(), isA(Project.class));
    }

    @Test
    public void getProjectsByMember() {
        // Given:
        List<Project> stubbingProjects = new ArrayList<>();
        stubbingProjects.add(stubbingProject);
        stubbingProjects.add(stubbingProject);
        stubbingProjects.add(stubbingProject);
        doReturn(stubbingProjects).when(spyOnProjectRepository).getAll();

        // When:
        List<ProjectOfResponse> actualMemberProjects = sut.getProjectsByMember(1L);

        // Then:
        List<ProjectOfResponse> expectedMemberProjects = List.of(
                getStubbingResponseProjectDTO(),
                getStubbingResponseProjectDTO(),
                getStubbingResponseProjectDTO()
        );
        Assertions.assertEquals(expectedMemberProjects.size(), actualMemberProjects.size());
        verify(spyOnProjectRepository, atLeast(1)).getAll();
    }

    @NotNull
    private ProjectOfResponse getStubbingResponseProjectDTO() {
        ProjectOfResponse dto = new ProjectOfResponse();
        dto.setProjectId(stubbingProject.getProjectId());
        dto.setProjectName(stubbingProject.getName());
        dto.setAvatarURL(stubbingProject.getAvatarURL());
        dto.setHyperlinkDTOList(stubbingProject.getHyperlinkSet().stream()
                .map(r -> {
                    HyperlinkDTO HyperlinkDTO = new HyperlinkDTO();
                    HyperlinkDTO.setType(r.getType());
                    HyperlinkDTO.setUrl(r.getUrl());
                    return HyperlinkDTO;
                })
                .collect(Collectors.toList()));
        return dto;
    }

}
