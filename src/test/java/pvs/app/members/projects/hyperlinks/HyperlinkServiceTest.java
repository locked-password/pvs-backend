package pvs.app.members.projects.hyperlinks;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import pvs.app.Application;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@Tag("Unit")
public class HyperlinkServiceTest {

    @Autowired
    private HyperlinkService sut;

    private MockWebServer mockGithubAPIServer;

    @BeforeEach
    public void setup() {
        mockGithubAPIServer = new MockWebServer();
        mockGithubAPIServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{}")
                .addHeader("Content-Type", "application/json")
        );
        mockGithubAPIServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{}")
                .addHeader("Content-Type", "application/json")
        );

        this.sut = new HyperlinkService(WebClient.builder(), mockGithubAPIServer.url("/").toString());
    }

    @Test
    public void checkSonarURL_GivenInvalidURL_ThenReturnFalse() {
        boolean exist = sut.checkSonarURL("pvs-springboot");
        Assertions.assertFalse(exist);
    }
}
