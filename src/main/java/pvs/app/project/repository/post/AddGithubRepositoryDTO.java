package pvs.app.project.repository.post;

import lombok.Data;

@Data
public class AddGithubRepositoryDTO {
    private Long projectId;
    private String repositoryURL;
}
