package pvs.app.member.project;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import pvs.app.api.github.GithubApiService;
import pvs.app.member.project.hyperlink.Hyperlink;
import pvs.app.member.project.hyperlink.HyperlinkOfAddGithubURL;
import pvs.app.member.project.hyperlink.HyperlinkOfAddSonarQubeURL;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final GithubApiService githubApiService;

    public ProjectService(ProjectRepository projectRepository, GithubApiService githubApiService) {
        this.projectRepository = projectRepository;
        this.githubApiService = githubApiService;
    }

    public void create(ProjectOfCreation projectDTO) throws IOException {
        Project p = Project.builder()
                .setName(projectDTO.getProjectName())
                .build();

        Project savedProject = projectRepository.put(null, p);

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
        if (projectRepository.contains(hyperlinkOfAddGithubURL.getProjectId())) {
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setUrl(hyperlinkOfAddGithubURL.getRepositoryURL());
            hyperlink.setType("github");

            Project project = projectRepository.get(hyperlinkOfAddGithubURL.getProjectId());
            project.getHyperlinkSet().add(hyperlink);

            String owner = hyperlinkOfAddGithubURL.getRepositoryURL().split("/")[3];
            JsonNode responseJson = githubApiService.getAvatarURL(owner);
            if (!responseJson.isNull()) {
                String json = responseJson.textValue();
                project.setAvatarURL(json);
            }
            projectRepository.put(hyperlinkOfAddGithubURL.getProjectId(), project);
            return true;
        } else {
            return false;
        }
    }

    public boolean addSonarRepo(HyperlinkOfAddSonarQubeURL hyperlinkOfAddSonarQubeURL) {
        if (projectRepository.contains(hyperlinkOfAddSonarQubeURL.getProjectId())) {
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setUrl(hyperlinkOfAddSonarQubeURL.getRepositoryURL());
            hyperlink.setType("sonar");

            Project project = projectRepository.get(hyperlinkOfAddSonarQubeURL.getProjectId());
            project.getHyperlinkSet().add(hyperlink);

            projectRepository.put(hyperlinkOfAddSonarQubeURL.getProjectId(), project);
            return true;
        } else {
            return false;
        }
    }

    public List<ProjectOfResponse> getProjectsByMember(Long memberId) {
        List<Project> projects = projectRepository.getAll();
        return projects.stream()
                .filter(project -> project.getMemberId().equals(memberId))
                .map(ProjectOfResponse::of)
                .collect(Collectors.toList());
    }

    public ProjectOfResponse getProjectsFromMemberById(Long projectId, Long memberId) {
        List<ProjectOfResponse> projectsOfResponse = getProjectsByMember(memberId);
        return projectsOfResponse.stream()
                .filter(project -> project.getProjectId().equals(projectId))
                .findFirst()
                .orElse(null);
    }
}
