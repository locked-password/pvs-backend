package pvs.app.member.project;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import pvs.app.api.github.GithubApiService;
import pvs.app.member.project.hyperlink.Hyperlink;
import pvs.app.member.project.hyperlink.HyperlinkDTO;
import pvs.app.member.project.hyperlink.HyperlinkOfAddGithubURL;
import pvs.app.member.project.hyperlink.HyperlinkOfAddSonarQubeURL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectDAO projectDAO;
    private final GithubApiService githubApiService;

    public ProjectService(ProjectDAO projectDAO, GithubApiService githubApiService) {
        this.projectDAO = projectDAO;
        this.githubApiService = githubApiService;
    }

    public void create(ProjectOfCreation projectDTO) throws IOException {
        Project p = Project.builder()
                .setMemberId(1L)
                .setName(projectDTO.getProjectName())
                .build();

        Project savedProject = projectDAO.save(p);

        if (!projectDTO.getGithubRepositoryURL().isBlank()) {
            HyperlinkOfAddGithubURL hyperlinkOfAddGithubURL = new HyperlinkOfAddGithubURL();
            hyperlinkOfAddGithubURL.setProjectId(savedProject.getProjectId());
            hyperlinkOfAddGithubURL.setRepositoryURL(projectDTO.getGithubRepositoryURL());

            addGithubRepo(hyperlinkOfAddGithubURL);
        }

        if (!projectDTO.getSonarRepositoryURL().isBlank()) {
            HyperlinkOfAddSonarQubeURL hyperlinkOfAddSonarQubeURL = new HyperlinkOfAddSonarQubeURL();
            hyperlinkOfAddSonarQubeURL.setProjectId(savedProject.getProjectId());
            hyperlinkOfAddSonarQubeURL.setRepositoryURL(projectDTO.getSonarRepositoryURL());

            addSonarRepo(hyperlinkOfAddSonarQubeURL);
        }
    }

    public boolean addGithubRepo(HyperlinkOfAddGithubURL hyperlinkOfAddGithubURL) throws IOException {
        Optional<Project> projectOptional = projectDAO.findById(hyperlinkOfAddGithubURL.getProjectId());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            String url = hyperlinkOfAddGithubURL.getRepositoryURL();
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setUrl(url);
            hyperlink.setType("github");
            project.getHyperlinkSet().add(hyperlink);
            String owner = url.split("/")[3];
            JsonNode responseJson = githubApiService.getAvatarURL(owner);
            if (!responseJson.isNull()) {
                String json = responseJson.textValue();
                project.setAvatarURL(json);
            }
            projectDAO.save(project);
            return true;
        } else {
            return false;
        }
    }

    public boolean addSonarRepo(HyperlinkOfAddSonarQubeURL hyperlinkOfAddSonarQubeURL) {
        Optional<Project> projectOptional = projectDAO.findById(hyperlinkOfAddSonarQubeURL.getProjectId());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setUrl(hyperlinkOfAddSonarQubeURL.getRepositoryURL());
            hyperlink.setType("sonar");
            project.getHyperlinkSet().add(hyperlink);
            projectDAO.save(project);
            return true;
        } else {
            return false;
        }
    }

    public List<ProjectOfResponse> getProjectsByMember(Long memberId) {
        List<Project> projectList = projectDAO.findByMemberId(memberId);
        List<ProjectOfResponse> projectDTOList = new ArrayList<>();

        for (Project project : projectList) {
            ProjectOfResponse projectDTO = new ProjectOfResponse();
            projectDTO.setProjectId(project.getProjectId());
            projectDTO.setProjectName(project.getName());
            projectDTO.setAvatarURL(project.getAvatarURL());
            for (Hyperlink hyperlink : project.getHyperlinkSet()) {
                HyperlinkDTO HyperlinkDTO = new HyperlinkDTO();
                HyperlinkDTO.setUrl(hyperlink.getUrl());
                HyperlinkDTO.setType(hyperlink.getType());
                projectDTO.getHyperlinkDTOList().add(HyperlinkDTO);
            }
            projectDTOList.add(projectDTO);
        }
        return projectDTOList;
    }

    public Optional<ProjectOfResponse> getProjectsFromMemberById(Long projectId, Long memberId) {
        List<ProjectOfResponse> projectList = getProjectsByMember(memberId);
        return projectList.stream()
                .filter(project -> project.getProjectId().equals(projectId))
                .findFirst();
    }
}
