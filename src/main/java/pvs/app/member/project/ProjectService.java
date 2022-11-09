package pvs.app.member.project;

import org.springframework.stereotype.Service;
import pvs.app.member.project.hyperlink.Hyperlink;
import pvs.app.member.project.hyperlink.HyperlinkOfAddSonarQubeURL;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService() {
    }

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void create(ProjectOfCreation projectDTO) throws IOException {
        Project p = Project.builder()
                .setName(projectDTO.getProjectName())
                .build();

        Project savedProject = projectRepository.put(1L, p);

        if (projectDTO.getSonarRepositoryURL() != null && !projectDTO.getSonarRepositoryURL().isBlank()) {
            HyperlinkOfAddSonarQubeURL hyperlinkOfAddSonarQubeURL = new HyperlinkOfAddSonarQubeURL();
            hyperlinkOfAddSonarQubeURL.setProjectId(savedProject.getProjectId());
            hyperlinkOfAddSonarQubeURL.setRepositoryURL(projectDTO.getSonarRepositoryURL());

            addSonarRepo(hyperlinkOfAddSonarQubeURL);
        }
    }

    public boolean addSonarRepo(HyperlinkOfAddSonarQubeURL hyperlinkOfAddSonarQubeURL) {
        if (projectRepository.contains(hyperlinkOfAddSonarQubeURL.getProjectId())) {
            Project project = projectRepository.get(hyperlinkOfAddSonarQubeURL.getProjectId());

            project.getHyperlinkSet().add(
                    Hyperlink.builder()
                            .setUrl(hyperlinkOfAddSonarQubeURL.getRepositoryURL())
                            .setType("sonar")
                            .build());

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
