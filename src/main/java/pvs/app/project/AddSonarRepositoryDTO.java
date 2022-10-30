package pvs.app.project;

import lombok.Data;

@Data
public class AddSonarRepositoryDTO {
    private Long projectId;
    private String repositoryURL;
}
