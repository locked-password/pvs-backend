package pvs.app.sonar.api;

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
import pvs.app.sonar.api.get.BugDTO;
import pvs.app.sonar.api.get.CodeCoverageDTO;
import pvs.app.sonar.api.get.CodeSmellDTO;
import pvs.app.sonar.api.get.DuplicationDTO;

import java.io.IOException;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class SonarApiServiceTest {
    @Autowired
    private SonarApiService sonarApiService;

    private MockWebServer mockWebServer;

    @BeforeEach
    public void setup() {
        this.mockWebServer = new MockWebServer();
        this.sonarApiService = new SonarApiService(WebClient.builder(), mockWebServer.url("/").toString());
    }

    @Test
    public void getSonarCodeCoverage() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"measures\":[{\"history\":[{\"date\":\"2020-11-20T19:38:25+0800\", \"value\":\"22.5\"}]}]}")
                .addHeader("Content-Type", "application/json")
        );
        List<CodeCoverageDTO> data = sonarApiService.getSonarCodeCoverage("pvs-springboot");
        Assertions.assertEquals(Double.valueOf(22.5), data.get(0).getValue());
    }

    @Test
    public void getSonarBug() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"measures\":[{\"history\":[{\"date\":\"2020-11-20T19:38:25+0800\", \"value\":\"22\"}]}]}")
                .addHeader("Content-Type", "application/json")
        );
        List<BugDTO> data = sonarApiService.getSonarBug("pvs-springboot");
        Assertions.assertEquals(Integer.valueOf(22), data.get(0).getValue());
    }

    @Test
    public void getSonarCodeSmell() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"measures\":[{\"history\":[{\"date\":\"2020-11-20T19:38:25+0800\", \"value\":\"22\"}]}]}")
                .addHeader("Content-Type", "application/json")
        );
        List<CodeSmellDTO> data = sonarApiService.getSonarCodeSmell("pvs-springboot");
        Assertions.assertEquals(Integer.valueOf(22), data.get(0).getValue());
    }

    @Test
    public void getDuplication() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"measures\":[{\"history\":[{\"date\":\"2020-11-20T19:38:25+0800\", \"value\":\"22.5\"}]}]}")
                .addHeader("Content-Type", "application/json")
        );
        List<DuplicationDTO> data = sonarApiService.getDuplication("pvs-springboot");
        Assertions.assertEquals(Double.valueOf(22.5), data.get(0).getValue());
    }
}
