package pvs.app.project;

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
import pvs.app.project.get.ResponseProjectDTO;
import pvs.app.project.post.CreateProjectDTO;
import pvs.app.project.hyperlink.Hyperlink;
import pvs.app.project.hyperlink.HyperlinkDTO;
import pvs.app.project.hyperlink.post.AddGithubRepositoryHyperlinkDTO;

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
    @Autowired
    @InjectMocks
    private ProjectService sut;

    @SpyBean
    @Autowired
    private ProjectDAO spyOnProjectDAO;
    private CreateProjectDTO stubbingCreateProjectDTO;
    private Project stubbingProject;

    @BeforeEach
    public void setup() throws IOException {
        stubbingCreateProjectDTO = new CreateProjectDTO();
        stubbingCreateProjectDTO.setProjectName("react");
        stubbingCreateProjectDTO.setGithubRepositoryURL("https://github.com/facebook/react");
        stubbingCreateProjectDTO.setSonarRepositoryURL("http://140.124.181.143:9000/dashboard?id=pvs-springboot");

        stubbingProject = new Project();
        stubbingProject.setProjectId(1L);
        stubbingProject.setName(stubbingCreateProjectDTO.getProjectName());
        stubbingProject.setMemberId(1L);
        stubbingProject.setAvatarURL("https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4");
        Set<Hyperlink> stubbingRepositories = new HashSet<>();
        Hyperlink stubbingHyperlink1 = new Hyperlink();
        stubbingHyperlink1.setHyperlinkId(1L);
        stubbingHyperlink1.setType("github");
        stubbingHyperlink1.setUrl(stubbingCreateProjectDTO.getGithubRepositoryURL());
        stubbingRepositories.add(stubbingHyperlink1);
        Hyperlink stubbingHyperlink2 = new Hyperlink();
        stubbingHyperlink2.setHyperlinkId(2L);
        stubbingHyperlink2.setType("sonar");
        stubbingHyperlink2.setUrl(stubbingCreateProjectDTO.getSonarRepositoryURL());
        stubbingRepositories.add(stubbingHyperlink2);
        stubbingProject.setHyperlinkSet(stubbingRepositories);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create() throws IOException {
        // Context:
        ProjectService spyOnSUT = spy(sut);
        doReturn(true)
                .when(spyOnSUT)
                .addGithubRepo(isA(AddGithubRepositoryHyperlinkDTO.class));

        // When:
        spyOnSUT.create(stubbingCreateProjectDTO);

        // Then:
        verify(spyOnProjectDAO, atLeast(1)).save(isA(Project.class));
    }

    @Test
    public void getMemberProjects() {
        // Given:
        List<Project> stubbingProjects = new ArrayList<>();
        stubbingProjects.add(stubbingProject);
        stubbingProjects.add(stubbingProject);
        stubbingProjects.add(stubbingProject);
        doReturn(stubbingProjects).when(spyOnProjectDAO).findByMemberId(isA(Long.class));

        // When:
        List<ResponseProjectDTO> actualMemberProjects = sut.getMemberProjects(1L);

        // Then:
        List<ResponseProjectDTO> expectedMemberProjects = List.of(
                getStubbingResponseProjectDTO(),
                getStubbingResponseProjectDTO(),
                getStubbingResponseProjectDTO()
        );
        Assertions.assertEquals(expectedMemberProjects.size(), actualMemberProjects.size());
        verify(spyOnProjectDAO, atLeast(1)).findByMemberId(isA(Long.class));
    }

    @NotNull
    private ResponseProjectDTO getStubbingResponseProjectDTO() {
        ResponseProjectDTO dto = new ResponseProjectDTO();
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
