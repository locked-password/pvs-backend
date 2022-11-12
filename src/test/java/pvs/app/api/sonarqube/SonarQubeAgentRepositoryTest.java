package pvs.app.api.sonarqube;

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

import java.io.IOException;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class SonarQubeAgentRepositoryTest {
    @Autowired
    private SonarQubeAgentRepository sonarQubeAgentService;

    private MockWebServer mockWebServer;

    @BeforeEach
    public void setup() {
        this.mockWebServer = new MockWebServer();
        this.sonarQubeAgentService = new SonarQubeAgentRepository(WebClient.builder(), mockWebServer.url("/").toString());
    }

    @Test
    public void getSonarCodeCoverage() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"measures\":[{\"history\":[{\"date\":\"2020-11-20T19:38:25+0800\", \"value\":\"22.5\"}]}]}")
                .addHeader("Content-Type", "application/json")
        );
        List<CodeCoverageDTO> data = sonarQubeAgentService.getAllSonarCodeCoverageByComponent("pvs-springboot");
        Assertions.assertEquals(Double.valueOf(22.5), data.get(0).getValue());
    }

    @Test
    public void getSonarBug() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"measures\":[{\"history\":[{\"date\":\"2020-11-20T19:38:25+0800\", \"value\":\"22\"}]}]}")
                .addHeader("Content-Type", "application/json")
        );
        List<BugDTO> data = sonarQubeAgentService.getSonarBug("pvs-springboot");
        Assertions.assertEquals(Integer.valueOf(22), data.get(0).getValue());
    }

    @Test
    public void getSonarCodeSmell() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"measures\":[{\"history\":[{\"date\":\"2020-11-20T19:38:25+0800\", \"value\":\"22\"}]}]}")
                .addHeader("Content-Type", "application/json")
        );
        List<CodeSmellDTO> data = sonarQubeAgentService.getSonarCodeSmell("pvs-springboot");
        Assertions.assertEquals(Integer.valueOf(22), data.get(0).getValue());
    }

    @Test
    public void getDuplication() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"measures\":[{\"history\":[{\"date\":\"2020-11-20T19:38:25+0800\", \"value\":\"22.5\"}]}]}")
                .addHeader("Content-Type", "application/json")
        );
        List<DuplicationDTO> data = sonarQubeAgentService.getDuplication("pvs-springboot");
        Assertions.assertEquals(Double.valueOf(22.5), data.get(0).getValue());
    }
}
