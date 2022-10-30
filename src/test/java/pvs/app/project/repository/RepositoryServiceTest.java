package pvs.app.project.repository;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import pvs.app.Application;
import pvs.app.project.repository.RepositoryService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class RepositoryServiceTest {

    @Autowired
    private RepositoryService repositoryService;

    @BeforeEach
    public void setup() {
        MockWebServer mockWebServer = new MockWebServer();
        this.repositoryService = new RepositoryService(WebClient.builder(), mockWebServer.url("/").toString());

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{}")
                .addHeader("Content-Type", "application/json")
        );
    }

    @Test
    public void checkSonarURL_thenReturnFalse() {
        boolean exist = repositoryService.checkSonarURL("pvs-springboot");
        Assertions.assertFalse(exist);
    }

    @Test
    public void checkSonarURL_thenReturnTrue() {
        boolean exist = repositoryService.checkSonarURL("http://140.124.181.143:9002/dashboard");
        Assertions.assertTrue(exist);
    }

    @Test
    public void checkGithubURL_thenReturnFalse() {
        boolean exist = repositoryService.checkGithubURL("pvs-springboot");
        Assertions.assertFalse(exist);
    }

    @Test
    public void checkGithubURL_thenReturnTrue() {
        boolean exist = repositoryService.checkGithubURL("https://github.com/imper0502/rime-full-bopomofo");
        Assertions.assertTrue(exist);
    }

}
