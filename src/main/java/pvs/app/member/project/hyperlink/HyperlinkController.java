package pvs.app.member.project.hyperlink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class HyperlinkController {
    private final HyperlinkService hyperlinkService;

    @Value("${message.success}")
    private String successMessage;

    @Value("${message.invalid.url}")
    private String urlInvalidMessage;

    @Autowired
    public HyperlinkController(HyperlinkService hyperlinkService) {
        this.hyperlinkService = hyperlinkService;
    }

    @GetMapping("/hyperlinks/sonarqube")
    public ResponseEntity<String> checkSonarURL(@RequestParam("url") String url) {
        if (hyperlinkService.checkSonarURL(url)) {
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlInvalidMessage);
        }
    }
}
