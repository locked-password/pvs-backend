package pvs.app.member.project.hyperlink;

import lombok.Data;

@Data
public class HyperlinkOfAddSonarQubeURL {
    private Long projectId;
    private String repositoryURL;
}
