package pvs.app.project.repository.post;

import lombok.Data;

@Data
public class AddSonarRepositoryDTO {
    private Long projectId;
    private String repositoryURL;
}
