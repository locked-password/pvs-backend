package pvs.app.project;

import lombok.Data;

@Data
public class AddGithubRepositoryDTO {
    private Long projectId;
    private String repositoryURL;
}
