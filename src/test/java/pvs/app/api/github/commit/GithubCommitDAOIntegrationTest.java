package pvs.app.api.github.commit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.api.github.commit.GithubCommit;
import pvs.app.api.github.commit.GithubCommitDAO;

import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GithubCommitDAOIntegrationTest {

    @Autowired
    private GithubCommitDAO githubCommitDAO;

    private final GithubCommit githubCommit01 = new GithubCommit();
    private final GithubCommit githubCommit02 = new GithubCommit();

    @BeforeEach
    public void setup() {
        githubCommit01.setRepoOwner("facebook");
        githubCommit01.setRepoName("react");
        githubCommit01.setAuthorEmail("test");
        githubCommit01.setAuthorName("test");
        githubCommit01.setCommittedDate(new Date());
        githubCommitDAO.save(githubCommit01);

        githubCommit02.setRepoOwner("angular");
        githubCommit02.setRepoName("angular");
        githubCommit02.setAuthorEmail("test");
        githubCommit02.setAuthorName("test");
        githubCommit02.setCommittedDate(new Date());
        githubCommitDAO.save(githubCommit02);
    }

    @Test
    public void whenFindByRepoOwnerAndRepoName_thenReturnGithubCommitList() {
        List<GithubCommit> foundEntityList = githubCommitDAO
                .findByRepoOwnerAndRepoName("facebook", "react");

        Assertions.assertEquals(githubCommit01.getRepoOwner(), foundEntityList.get(0).getRepoOwner());
    }

    @Test
    public void whenFindFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc_thenReturnGithubCommit() {
        GithubCommit foundEntity =
                githubCommitDAO.findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc("angular", "angular");

        Assertions.assertEquals(githubCommit02.getCommittedDate(), foundEntity.getCommittedDate());
    }
}
