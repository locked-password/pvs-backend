package pvs.app.member.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pvs.app.member.project.hyperlink.HyperlinkOfAddGithubURL;
import pvs.app.member.project.hyperlink.HyperlinkOfAddSonarQubeURL;
import pvs.app.member.project.hyperlink.HyperlinkService;

import java.io.IOException;

@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    static final Logger logger = LogManager.getLogger(ProjectController.class.getName());
    private final ProjectService projectService;
    private final pvs.app.member.project.hyperlink.HyperlinkService HyperlinkService;
    @Value("${message.exception}")
    private String exceptionMessage;
    @Value("${message.invalid.url}")
    private String urlInvalidMessage;
    @Value("${message.success}")
    private String successMessage;
    @Value("${message.fail}")
    private String failMessage;

    @Autowired
    public ProjectController(ProjectService projectService, HyperlinkService HyperlinkService) {
        this.projectService = projectService;
        this.HyperlinkService = HyperlinkService;
    }

    @PostMapping("/projects")
    public ResponseEntity<String> createProject(@RequestBody ProjectOfCreation projectDTO) {
        try {
            projectService.create(projectDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMessage);
        }
    }

    @PostMapping("/projects/{projectId}/hyperlinks/github")
    public ResponseEntity<String> addGithubRepository(@RequestBody HyperlinkOfAddGithubURL hyperlinkOfAddGithubURL) {
        try {
            if (HyperlinkService.checkGithubURL(hyperlinkOfAddGithubURL.getRepositoryURL())) {
                if (projectService.addGithubRepo(hyperlinkOfAddGithubURL)) {
                    return ResponseEntity.status(HttpStatus.OK).body(successMessage);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failMessage);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlInvalidMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMessage);
        }
    }

    @PostMapping("/projects/{projectId}/hyperlinks/sonarqube")
    public ResponseEntity<String> addSonarRepository(@RequestBody HyperlinkOfAddSonarQubeURL hyperlinkOfAddSonarQubeURL) {
        try {
            if (HyperlinkService.checkSonarURL(hyperlinkOfAddSonarQubeURL.getRepositoryURL())) {
                if (projectService.addSonarRepo(hyperlinkOfAddSonarQubeURL)) {
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
}
