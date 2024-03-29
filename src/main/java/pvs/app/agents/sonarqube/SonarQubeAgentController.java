package pvs.app.agents.sonarqube;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(path = "/agents/sonarqube", produces = MediaType.APPLICATION_JSON_VALUE)
public class SonarQubeAgentController {

    static final Logger logger = LogManager.getLogger(SonarQubeAgentController.class.getName());
    private final SonarQubeAgentRepository sonarQubeAgentService;
    @Value("${message.exception}")
    private String exceptionMessage;

    public SonarQubeAgentController(SonarQubeAgentRepository sonarQubeAgentService) {
        this.sonarQubeAgentService = sonarQubeAgentService;
    }

    @GetMapping("/components/{component}/coverages")
    public ResponseEntity<List<CodeCoverageDTO>>
    getCoverages(@PathVariable("component") String component) throws IOException {
        List<CodeCoverageDTO> coverages = sonarQubeAgentService.getCoverageHistoryByComponent(component);
        return ResponseEntity.status(HttpStatus.OK).body(coverages);
    }

    @GetMapping("/components/{component}/bugs")
    public ResponseEntity<List<BugDTO>>
    getBugs(@PathVariable("component") String component) throws IOException {
        List<BugDTO> bugs = sonarQubeAgentService.getBugHistoryByComponent(component);
        return ResponseEntity.status(HttpStatus.OK).body(bugs);
    }

    @GetMapping("/components/{component}/code_smells")
    public ResponseEntity<List<CodeSmellDTO>>
    getCodeSmells(@PathVariable("component") String component) throws IOException {
        List<CodeSmellDTO> codeSmells = sonarQubeAgentService.getCodeSmellHistoryByComponent(component);
        return ResponseEntity.status(HttpStatus.OK).body(codeSmells);
    }

    @GetMapping("/components/{component}/duplications")
    public ResponseEntity<List<DuplicationDTO>>
    getDuplications(@PathVariable("component") String component) throws IOException {
        List<DuplicationDTO> duplications = sonarQubeAgentService.getDuplicationHistoryByComponent(component);
        return ResponseEntity.status(HttpStatus.OK).body(duplications);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> ioExceptionHandler(Exception e) {
        logger.debug(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMessage);
    }
}
