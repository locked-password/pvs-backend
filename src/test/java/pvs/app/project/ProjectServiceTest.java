package pvs.app.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.github.api.GithubApiService;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @MockBean
    private GithubApiService githubApiService;

    @MockBean
    private ProjectDAO projectDAO;

    CreateProjectDTO projectDTO;

    Project project;
    Repository githubRepository;
    Set<Repository> repositorySet;

    final String responseJson = "{\"avatarUrl\":\"https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4\"}";
    Optional<JsonNode> mockAvatar;

    @BeforeEach
    public void setup() throws IOException {
        projectDTO = new CreateProjectDTO();
        projectDTO.setProjectName("react");
        projectDTO.setGithubRepositoryURL("https://github.com/facebook/react");
        projectDTO.setSonarRepositoryURL("http://140.124.181.143:9000/dashboard?id=pvs-springboot");

        project = new Project();
        project.setProjectId(1L);
        project.setMemberId(1L);
        project.setName(projectDTO.getProjectName());

        githubRepository = new Repository();
        githubRepository.setType("github");
        githubRepository.setUrl("https://github.com/facebook/react");
        githubRepository.setRepositoryId(1L);

        repositorySet = new HashSet<>();
        repositorySet.add(githubRepository);
        project.setRepositorySet(repositorySet);

        ObjectMapper mapper = new ObjectMapper();
        mockAvatar = Optional.ofNullable(mapper.readTree(responseJson));
    }

    @Test
    public void create() throws IOException {
        //context
        when(githubApiService.getAvatarURL("facebook"))
                .thenReturn(mockAvatar.orElse(null));

        when(projectDAO.save(any(Project.class)))
                .thenReturn(project);
        when(projectDAO.findById(1L))
                .thenReturn(Optional.of(project));

        //when
        projectService.create(projectDTO);

        //then
        verify(githubApiService, times(1)).getAvatarURL("facebook");
    }

    @Test
    public void getMemberProjects() {
        //given
        project.setAvatarURL("https://avatars3.githubusercontent.com/u/17744001?u=038d9e068c4205d94c670d7d89fb921ec5b29782&v=4");
        
        List<ResponseProjectDTO> projectDTOList = new ArrayList<>();
        ResponseProjectDTO projectDTO = new ResponseProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getName());
        projectDTO.setAvatarURL(project.getAvatarURL());
        projectDTO.setRepositoryDTOList(List.of(new RepositoryDTO() {{
            setUrl("https://github.com/facebook/react");
            setType("github");
        }}));

        projectDTOList.add(projectDTO);

        //when
        when(projectDAO.findByMemberId(1L))
                .thenReturn(List.of(project));
        //then
        Assertions.assertEquals(1, projectService.getMemberProjects(1L).size());
        Assertions.assertEquals(projectDTOList, projectService.getMemberProjects(1L));
    }
}
