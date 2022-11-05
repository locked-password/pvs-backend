package pvs.app.member.project;

import lombok.Data;

@Data
public class ProjectOfCreation {
    private String projectName;
    private String githubRepositoryURL;
    private String sonarRepositoryURL;
}
