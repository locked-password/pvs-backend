package pvs.app.project.hyperlink.post;

import lombok.Data;

@Data
public class AddGithubRepositoryHyperlinkDTO {
    private Long projectId;
    private String repositoryURL;
}
