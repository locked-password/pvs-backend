package pvs.app.api.sonarqube;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping(path = "/sonarqube-agent", produces = MediaType.APPLICATION_JSON_VALUE)
public class SonarQubeAgentController {

    static final Logger logger = LogManager.getLogger(SonarQubeAgentController.class.getName());
    private final SonarQubeAgentService sonarQubeAgentService;
    @Value("${message.exception}")
    private String exceptionMessage;
    @Value("${message.fail}")
    private String failMessage;

    public SonarQubeAgentController(SonarQubeAgentService sonarQubeAgentService) {
        this.sonarQubeAgentService = sonarQubeAgentService;
    }

    @GetMapping("/components/{component}/coverage")
    public ResponseEntity<String> getCoverage(@PathVariable("component") String component) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CodeCoverageDTO> coverages = sonarQubeAgentService.getSonarCodeCoverage(component);
        if (!coverages.isEmpty()) {
            String coverageString = objectMapper.writeValueAsString(coverages);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(coverageString);
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(failMessage);
        }
    }

    @GetMapping("/components/{component}/bug")
    public ResponseEntity<String> getBug(@PathVariable("component") String component) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<BugDTO> bugList = sonarQubeAgentService.getSonarBug(component);
        if (!bugList.isEmpty()) {
            String bugListString = objectMapper.writeValueAsString(bugList);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bugListString);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(failMessage);
        }
    }

    @GetMapping("/components/{component}/code_smell")
    public ResponseEntity<String> getCodeSmell(@PathVariable("component") String component) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CodeSmellDTO> codeSmellList = sonarQubeAgentService.getSonarCodeSmell(component);
        if (!codeSmellList.isEmpty()) {
            String codeSmellListString = objectMapper.writeValueAsString(codeSmellList);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(codeSmellListString);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(failMessage);
        }
    }

    @GetMapping("/components/{component}/duplication")
    public ResponseEntity<String> getDuplication(@PathVariable("component") String component) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<DuplicationDTO> duplicationList = sonarQubeAgentService.getDuplication(component);
        if (!duplicationList.isEmpty()) {
            String duplicationListString = objectMapper.writeValueAsString(duplicationList);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(duplicationListString);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(failMessage);
        }
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> ioExceptionHandler(Exception e) {
        logger.debug(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionMessage);
    }
}
