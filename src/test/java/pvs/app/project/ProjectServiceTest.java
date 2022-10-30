package pvs.app.project;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.project.repository.Repository;
import pvs.app.project.repository.RepositoryDTO;

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
    private ProjectService sut;

    @SpyBean
    @Autowired
    private ProjectDAO spyOnProjectDAO;
    CreateProjectDTO stubbingCreateProjectDTO;
    Project stubbingProject;
    Set<Repository> stubbingRepositories;
    Repository stubbingRepository;

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
        stubbingRepositories = new HashSet<>();
        stubbingRepository = new Repository();
        stubbingRepository.setRepositoryId(1L);
        stubbingRepository.setType("github");
        stubbingRepository.setUrl(stubbingCreateProjectDTO.getGithubRepositoryURL());
        stubbingRepositories.add(stubbingRepository);
        stubbingRepository = new Repository();
        stubbingRepository.setRepositoryId(2L);
        stubbingRepository.setType("sonar");
        stubbingRepository.setUrl(stubbingCreateProjectDTO.getSonarRepositoryURL());
        stubbingRepositories.add(stubbingRepository);
        stubbingProject.setRepositorySet(stubbingRepositories);
    }

    @Test
    public void create() throws IOException {
        // When:
        sut.create(stubbingCreateProjectDTO);

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
        dto.setRepositoryDTOList(stubbingProject.getRepositorySet().stream()
                .map(r -> {
                    RepositoryDTO repositoryDTO = new RepositoryDTO();
                    repositoryDTO.setType(r.getType());
                    repositoryDTO.setUrl(r.getUrl());
                    return repositoryDTO;
                })
                .collect(Collectors.toList()));
        return dto;
    }
}
