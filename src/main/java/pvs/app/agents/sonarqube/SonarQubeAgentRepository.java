package pvs.app.agents.sonarqube;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;

@Service
@SuppressWarnings("squid:S1192")
public class SonarQubeAgentRepository {

    private final WebClient webClient;

    private final ObjectMapper mapper;

    private final DateTimeFormatter isoParser;

    public SonarQubeAgentRepository(
            WebClient.Builder webClientBuilder,
            @Value("${webClient.baseUrl.sonar}") String baseUrl) {
        String token = System.getenv("PVS_SONAR_TOKEN");
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
        mapper = new ObjectMapper();
        isoParser = ISODateTimeFormat.dateTimeNoMillis().withLocale(Locale.TAIWAN);
    }

    public List<MetricsDTO> getCoverageHistoryByComponent(String component) throws IOException {
        return getMetricsHistoryByComponent(component, "coverage");
    }

    public List<MetricsDTO> getBugHistoryByComponent(String component) throws IOException {
        return getMetricsHistoryByComponent(component, "bugs");
    }

    public List<MetricsDTO> getCodeSmellHistoryByComponent(String component) throws IOException {
        return getMetricsHistoryByComponent(component, "code_smells");
    }

    public List<MetricsDTO> getDuplicationHistoryByComponent(String component) throws IOException {
        return getMetricsHistoryByComponent(component, "duplicated_lines_density");
    }

    private List<MetricsDTO> getMetricsHistoryByComponent(String component, String metrics) throws JsonProcessingException {
        String responseJson = this.webClient
                .get().uri("/measures/search_history?component=" + component + "&metrics=" + metrics)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .block();

        List<MetricsDTO> coverages = new ArrayList<>();
        Optional<JsonNode> coverageJsonNodes = Optional
                .ofNullable(mapper.readTree(responseJson))
                .map(response -> response.get("measures"));
        assert coverageJsonNodes.isPresent();

        JsonNode coverageHistoryJsonNodes = coverageJsonNodes.get().get(0).get("history");
        assert coverageHistoryJsonNodes.isArray();

        for (final JsonNode jsonNode : coverageHistoryJsonNodes) {
            String dateString = jsonNode.get("date").asText();
            double coverageValue = jsonNode.get("value").asDouble();

            Date date = isoParser.parseDateTime(dateString).toDate();
            coverages.add(new MetricsDTO(date, coverageValue));
        }

        return coverages;
    }
}