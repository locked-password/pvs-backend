package pvs.app.project;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pvs.app.api.github.GithubApiService;
import pvs.app.project.get.ResponseProjectDTO;
import pvs.app.project.post.CreateProjectDTO;
import pvs.app.project.hyperlink.Hyperlink;
import pvs.app.project.hyperlink.post.AddGithubRepositoryHyperlinkDTO;
import pvs.app.project.hyperlink.post.AddSonarQubeHyperlinkDTO;
import pvs.app.project.hyperlink.HyperlinkDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectDAO projectDAO;

    private final GithubApiService githubApiService;

    static final Logger logger = LogManager.getLogger(ProjectService.class.getName());

    public ProjectService(ProjectDAO projectDAO, GithubApiService githubApiService) {
        this.projectDAO = projectDAO;
        this.githubApiService = githubApiService;
    }

    public void create(CreateProjectDTO projectDTO) throws IOException {
        Project savedProject;
        Project project = new Project();
        project.setMemberId(1L);
        project.setName(projectDTO.getProjectName());

        savedProject = projectDAO.save(project);

        if(!projectDTO.getGithubRepositoryURL().equals("")){
            AddGithubRepositoryHyperlinkDTO addGithubRepositoryHyperlinkDTO = new AddGithubRepositoryHyperlinkDTO();
            addGithubRepositoryHyperlinkDTO.setProjectId(savedProject.getProjectId());
            addGithubRepositoryHyperlinkDTO.setRepositoryURL(projectDTO.getGithubRepositoryURL());
            addGithubRepo(addGithubRepositoryHyperlinkDTO);
        }

        if(!projectDTO.getSonarRepositoryURL().equals("")){
            AddSonarQubeHyperlinkDTO addSonarQubeHyperlinkDTO = new AddSonarQubeHyperlinkDTO();
            addSonarQubeHyperlinkDTO.setProjectId(savedProject.getProjectId());
            addSonarQubeHyperlinkDTO.setRepositoryURL(projectDTO.getSonarRepositoryURL());
            addSonarRepo(addSonarQubeHyperlinkDTO);
        }
    }

    public List<ResponseProjectDTO> getMemberProjects(Long memberId) {
        List<Project> projectList = projectDAO.findByMemberId(memberId);
        List<ResponseProjectDTO> projectDTOList = new ArrayList<>();

        for (Project project:projectList) {
            ResponseProjectDTO projectDTO = new ResponseProjectDTO();
            projectDTO.setProjectId(project.getProjectId());
            projectDTO.setProjectName(project.getName());
            projectDTO.setAvatarURL(project.getAvatarURL());
            for(Hyperlink hyperlink : project.getHyperlinkSet()) {
                HyperlinkDTO HyperlinkDTO = new HyperlinkDTO();
                HyperlinkDTO.setUrl(hyperlink.getUrl());
                HyperlinkDTO.setType(hyperlink.getType());
                projectDTO.getHyperlinkDTOList().add(HyperlinkDTO);
            }
            projectDTOList.add(projectDTO);
        }
        return projectDTOList;
    }

    public boolean addSonarRepo(AddSonarQubeHyperlinkDTO addSonarQubeHyperlinkDTO) {
        Optional<Project> projectOptional = projectDAO.findById(addSonarQubeHyperlinkDTO.getProjectId());
        if(projectOptional.isPresent()) {
            Project project = projectOptional.get();
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setUrl(addSonarQubeHyperlinkDTO.getRepositoryURL());
            hyperlink.setType("sonar");
            project.getHyperlinkSet().add(hyperlink);
            projectDAO.save(project);
            return true;
        } else {
            return false;
        }
    }

    public boolean addGithubRepo(AddGithubRepositoryHyperlinkDTO addGithubRepositoryHyperlinkDTO) throws IOException {
        Optional<Project> projectOptional = projectDAO.findById(addGithubRepositoryHyperlinkDTO.getProjectId());
        if(projectOptional.isPresent()) {
            Project project = projectOptional.get();
            String url = addGithubRepositoryHyperlinkDTO.getRepositoryURL();
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setUrl(url);
            hyperlink.setType("github");
            project.getHyperlinkSet().add(hyperlink);
            String owner = url.split("/")[3];
            JsonNode responseJson = githubApiService.getAvatarURL(owner);
            if(null != responseJson) {
                String json = responseJson.textValue();
                project.setAvatarURL(json);
            }
            projectDAO.save(project);
            return true;
        } else {
            return false;
        }
    }
}
