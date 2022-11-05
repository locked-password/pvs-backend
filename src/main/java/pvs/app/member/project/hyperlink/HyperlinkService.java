package pvs.app.member.project.hyperlink;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@SuppressWarnings("squid:S1192")
public class HyperlinkService {
    private final WebClient webClient;

    public HyperlinkService(WebClient.Builder webClientBuilder, @Value("${webClient.baseUrl.test}") String baseUrl) {
        String token = System.getenv("PVS_GITHUB_TOKEN");
        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    public boolean checkGithubURL(String url) {
        if (!url.contains("github.com")) {
            return false;
        }
        String targetURL = url.replace("github.com", "api.github.com/repos");
        AtomicBoolean result = new AtomicBoolean(false);

        this.webClient
                .get()
                .uri(targetURL)
                .exchange()
                .doOnSuccess(clientResponse ->
                        result.set(clientResponse.statusCode().equals(HttpStatus.OK))
                )
                .block();
        return result.get();
    }

    public boolean checkSonarURL(String url) {
        if (!url.contains("140.124.181.143")) {
            return false;
        }

        String targetURL = url.replace("dashboard?id", "api/components/show?component");
        AtomicBoolean result = new AtomicBoolean(false);

        this.webClient
                .get()
                .uri(targetURL)
                .exchange()
                .doOnSuccess(clientResponse ->
                        result.set(clientResponse.statusCode().equals(HttpStatus.OK))
                )
                .block();
        return result.get();
    }
}
