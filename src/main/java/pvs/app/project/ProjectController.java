package pvs.app.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pvs.app.project.get.ResponseProjectDTO;
import pvs.app.project.hyperlink.HyperlinkService;
import pvs.app.project.hyperlink.post.AddGithubRepositoryHyperlinkDTO;
import pvs.app.project.hyperlink.post.AddSonarQubeHyperlinkDTO;
import pvs.app.project.post.CreateProjectDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/projects", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    static final Logger logger = LogManager.getLogger(ProjectController.class.getName());
    private final ProjectService projectService;
    private final HyperlinkService HyperlinkService;
    @Value("${message.exception}")
    private String exceptionMessage;
    @Value("${message.invalid.url}")
    private String urlInvalidMessage;
    @Value("${message.success}")
    private String successMessage;
    @Value("${message.fail}")
    private String failMessage;

    public ProjectController(ProjectService projectService, HyperlinkService HyperlinkService) {
        this.projectService = projectService;
        this.HyperlinkService = HyperlinkService;
    }

    @GetMapping("/hyperlinks/github")
    public ResponseEntity<String> checkGithubURL(@RequestParam("url") String url) {
        if (HyperlinkService.checkGithubURL(url)) {
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlInvalidMessage);
        }
    }

    @GetMapping("/hyperlinks/sonarqube")
    public ResponseEntity<String> checkSonarURL(@RequestParam("url") String url) {
        if (HyperlinkService.checkSonarURL(url)) {
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlInvalidMessage);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> createProject(@RequestBody CreateProjectDTO projectDTO) {
        try {
            projectService.create(projectDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMessage);
        }
    }

    @PostMapping("/{projectId}/hyperlinks/sonarqube")
    public ResponseEntity<String> addSonarRepository(@RequestBody AddSonarQubeHyperlinkDTO addSonarQubeHyperlinkDTO) {
        try {
            if (HyperlinkService.checkSonarURL(addSonarQubeHyperlinkDTO.getRepositoryURL())) {
                if (projectService.addSonarRepo(addSonarQubeHyperlinkDTO)) {
                    return ResponseEntity.status(HttpStatus.OK).body(successMessage);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failMessage);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlInvalidMessage);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMessage);
        }
    }

    @PostMapping("/{projectId}/hyperlinks/github")
    public ResponseEntity<String> addGithubRepository(@RequestBody AddGithubRepositoryHyperlinkDTO addGithubRepositoryHyperlinkDTO) {
        try {
            if (HyperlinkService.checkGithubURL(addGithubRepositoryHyperlinkDTO.getRepositoryURL())) {
                if (projectService.addGithubRepo(addGithubRepositoryHyperlinkDTO)) {
                    return ResponseEntity.status(HttpStatus.OK).body(successMessage);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failMessage);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlInvalidMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMessage);
        }
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<List<ResponseProjectDTO>> readMemberAllProjects(@PathVariable Long memberId) {
        List<ResponseProjectDTO> projectList = projectService.getMemberProjects(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(projectList);
    }

    @GetMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ResponseProjectDTO> readSelectedProject
            (@PathVariable Long memberId, @PathVariable Long projectId) {
        List<ResponseProjectDTO> projectList = projectService.getMemberProjects(memberId);
        Optional<ResponseProjectDTO> selectedProject =
                projectList.stream()
                        .filter(project -> project.getProjectId().equals(projectId))
                        .findFirst();

        return selectedProject.map(responseProjectDTO -> ResponseEntity.status(HttpStatus.OK).body(responseProjectDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }
}
