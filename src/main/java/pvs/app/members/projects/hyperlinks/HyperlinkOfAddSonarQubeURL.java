package pvs.app.members.projects.hyperlinks;

import lombok.Data;

@Data
public class HyperlinkOfAddSonarQubeURL {
    private Long projectId;
    private String repositoryURL;
}
