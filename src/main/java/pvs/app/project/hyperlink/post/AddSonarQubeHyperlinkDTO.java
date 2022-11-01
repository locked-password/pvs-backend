package pvs.app.project.hyperlink.post;

import lombok.Data;

@Data
public class AddSonarQubeHyperlinkDTO {
    private Long projectId;
    private String repositoryURL;
}
