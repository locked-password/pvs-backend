package pvs.app.api.sonarqube;

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

    public List<CodeCoverageDTO> getCoverageHistoryByComponent(String component) throws IOException {
        String responseJson = this.webClient
                .get().uri("/measures/search_history?component=" + component + "&metrics=coverage")
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .block();

        List<CodeCoverageDTO> coverages = new ArrayList<>();
        Optional<JsonNode> coverageJsonNodes = Optional
                .ofNullable(mapper.readTree(responseJson))
                .map(response -> response.get("measures"));
        assert coverageJsonNodes.isPresent();

        JsonNode coverageHistoryJsonNodes = coverageJsonNodes.get().get(0).get("history");
        assert coverageHistoryJsonNodes.isArray();

        for (final JsonNode jsonNode : coverageHistoryJsonNodes) {
            String dateString = jsonNode.get("date").toPrettyString();
            double coverageValue = jsonNode.get("value").asDouble();

            Date date = isoParser.parseDateTime(dateString).toDate();
            coverages.add(new CodeCoverageDTO(date, coverageValue));
        }

        return coverages;
    }

    public List<BugDTO> getBugHistoryByComponent(String component) throws IOException {
        String responseJson = Objects.requireNonNull(this.webClient.get()
                        .uri("/measures/search_history?component=" + component + "&metrics=bugs")
                        .exchange()
                        .block())
                .bodyToMono(String.class)
                .block();

        List<BugDTO> bugList = new ArrayList<>();
        Optional<JsonNode> bugJsonNodes = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("measures"));

        if (bugJsonNodes.isPresent()) {
            JsonNode bugArrayNode = bugJsonNodes.get().get(0).get("history");

            if (bugArrayNode.isArray()) {
                for (final JsonNode jsonNode : bugArrayNode) {
                    Date date =
                            isoParser.parseDateTime(jsonNode.get("date").textValue().replace("\"", ""))
                                    .toDate();
                    int bugValue = 0;
                    if (null != jsonNode.get("value")) {
                        bugValue = jsonNode.get("value").asInt();
                    }
                    bugList.add(new BugDTO(date, bugValue));
                }
            }
        }
        return bugList;
    }

    public List<CodeSmellDTO> getCodeSmellHistoryByComponent(String component) throws IOException {
        String responseJson = Objects.requireNonNull(this.webClient.get()
                        .uri("/measures/search_history?component=" + component + "&metrics=code_smells")
                        .exchange()
                        .block())
                .bodyToMono(String.class)
                .block();

        List<CodeSmellDTO> codeSmellList = new ArrayList<>();
        Optional<JsonNode> codeSmellJsonNodes = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("measures"));

        if (codeSmellJsonNodes.isPresent()) {
            JsonNode codeSmellArrayNode = codeSmellJsonNodes.get().get(0).get("history");
            if (codeSmellArrayNode.isArray()) {
                for (final JsonNode jsonNode : codeSmellArrayNode) {
                    Date date =
                            isoParser.parseDateTime(jsonNode.get("date").textValue().replace("\"", ""))
                                    .toDate();
                    int codeSmellValue = 0;
                    if (null != jsonNode.get("value")) {
                        codeSmellValue = jsonNode.get("value").asInt();
                    }
                    codeSmellList.add(new CodeSmellDTO(date, codeSmellValue));
                }
            }
        }
        return codeSmellList;
    }

    public List<DuplicationDTO> getDuplicationHistoryByComponent(String component) throws IOException {
        String responseJson = Objects.requireNonNull(this.webClient.get()
                        .uri("/measures/search_history?component=" + component + "&metrics=duplicated_lines_density")
                        .exchange()
                        .block())
                .bodyToMono(String.class)
                .block();

        List<DuplicationDTO> duplicationList = new ArrayList<>();
        Optional<JsonNode> duplicationJsonNodes = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("measures"));

        if (duplicationJsonNodes.isPresent()) {
            JsonNode duplicationArrayNode = duplicationJsonNodes.get().get(0).get("history");
            if (duplicationArrayNode.isArray()) {
                for (final JsonNode jsonNode : duplicationArrayNode) {
                    Date date =
                            isoParser.parseDateTime(jsonNode.get("date").textValue().replace("\"", ""))
                                    .toDate();
                    double duplicationValue = 0;
                    if (null != jsonNode.get("value")) {
                        duplicationValue = jsonNode.get("value").asDouble();
                    }
                    duplicationList.add(new DuplicationDTO(date, duplicationValue));
                }
            }
        }
        return duplicationList;
    }
}