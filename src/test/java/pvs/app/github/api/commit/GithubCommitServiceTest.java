package pvs.app.github.api.commit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.github.api.commit.GithubCommit;
import pvs.app.github.api.commit.GithubCommitDAO;
import pvs.app.github.api.commit.GithubCommitService;
import pvs.app.github.api.commit.post.GithubCommitDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class GithubCommitServiceTest {

    @Autowired
    private GithubCommitService githubCommitService;

    @MockBean
    private GithubCommitDAO mockGithubCommitDAO;

    private List<GithubCommit> githubCommits;
    private final GithubCommit githubCommit01 = new GithubCommit();
    private final GithubCommit githubCommit02 = new GithubCommit();
    private final GithubCommitDTO githubCommitDTO01 = new GithubCommitDTO();

    @BeforeEach
    public void setup() throws ParseException {
        githubCommits = new LinkedList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期格式

        githubCommit01.setId(1L);
        githubCommit01.setRepoOwner("facebook");
        githubCommit01.setRepoName("react");
        githubCommit01.setCommittedDate(dateFormat.parse("2020-12-19 22:22:22"));

        githubCommitDTO01.setRepoOwner("facebook");
        githubCommitDTO01.setRepoName("react");
        githubCommitDTO01.setCommittedDate(dateFormat.parse("2020-12-19 22:22:22"));

        githubCommit02.setId(2L);
        githubCommit02.setRepoOwner("facebook");
        githubCommit02.setRepoName("react");
        githubCommit02.setCommittedDate(dateFormat.parse("2020-12-21 22:22:22"));

        githubCommits.add(githubCommit01);
        githubCommits.add(githubCommit02);
    }

    @Test
    public void getAllCommits() {
        //context
        when(mockGithubCommitDAO.findByRepoOwnerAndRepoName("facebook", "react"))
                .thenReturn(githubCommits);

        //when
        List<GithubCommitDTO> githubCommits = githubCommitService.getAllCommits("facebook", "react");

        //then
        Assertions.assertEquals(2, githubCommits.size());
        verify(mockGithubCommitDAO, times(1)).findByRepoOwnerAndRepoName("facebook", "react");
    }

    @Test
    public void getLastCommit_isExist() throws ParseException {
        //context
        when(mockGithubCommitDAO.findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc("facebook", "react"))
                .thenReturn(githubCommits.get(1));

        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期格式

        //when
        GithubCommitDTO githubCommit = githubCommitService.getLastCommit("facebook", "react");

        //then
        Assertions.assertEquals(dateFormat.parse("2020-12-21 22:22:22"), githubCommit.getCommittedDate());
        verify(mockGithubCommitDAO, times(1)).findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc("facebook", "react");
    }

    @Test
    public void getLastCommit_isNotExist() {
        //context
        when(mockGithubCommitDAO.findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc("facebook", "react"))
                .thenReturn(null);

        //when
        GithubCommitDTO githubCommit = githubCommitService.getLastCommit("facebook", "react");

        //then
        Assertions.assertNull(githubCommit);
    }

    @Test
    public void save() {
        List<GithubCommit> fakeGithubCommits = new ArrayList<>();
        fakeGithubCommits.add(githubCommit01);

        //context
        when(mockGithubCommitDAO.save(githubCommit01))
                .thenReturn(githubCommit01);
        when(mockGithubCommitDAO.findByRepoOwnerAndRepoName("facebook", "react"))
                .thenReturn(fakeGithubCommits);

        //when
        githubCommitService.save(githubCommitDTO01);

        //then
        List<GithubCommitDTO> githubCommitDTOList = githubCommitService.getAllCommits("facebook", "react");
        Assertions.assertEquals(1, githubCommitDTOList.size());
        verify(mockGithubCommitDAO, times(1)).save(githubCommit01);
    }
}