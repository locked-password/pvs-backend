package pvs.app.api.github.commit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.api.github.commit.GithubCommit;
import pvs.app.api.github.commit.GithubCommitDAO;
import pvs.app.api.github.commit.GithubCommitService;
import pvs.app.api.github.commit.post.GithubCommitDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
public class GithubCommitServiceTest {

    @Autowired
    private GithubCommitService sut;

    @MockBean
    private GithubCommitDAO mockGithubCommitDAO;

    private List<GithubCommit> githubCommits;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    public void setup() throws ParseException {
        GithubCommit githubCommit01 = new GithubCommit();
        githubCommit01.setId(1L);
        githubCommit01.setRepoOwner("facebook");
        githubCommit01.setRepoName("react");
        githubCommit01.setCommittedDate(dateFormat.parse("2020-12-19 22:22:22"));

        GithubCommit githubCommit02 = new GithubCommit();
        githubCommit02.setId(2L);
        githubCommit02.setRepoOwner("facebook");
        githubCommit02.setRepoName("react");
        githubCommit02.setCommittedDate(dateFormat.parse("2020-12-21 22:22:22"));

        githubCommits = new LinkedList<>();
        githubCommits.add(githubCommit01);
        githubCommits.add(githubCommit02);
    }

    @Test
    public void getAllCommits() {
        // Context
        when(mockGithubCommitDAO.findByRepoOwnerAndRepoName("facebook", "react"))
                .thenReturn(githubCommits);

        // When
        List<GithubCommitDTO> githubCommits = sut.getAllCommits("facebook", "react");

        //then
        Assertions.assertEquals(2, githubCommits.size());
        verify(mockGithubCommitDAO, times(1))
                .findByRepoOwnerAndRepoName(isA(String.class), isA(String.class));
    }

    @Test
    public void getLastCommit_isExist() throws ParseException {
        //context
        when(mockGithubCommitDAO.findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc("facebook", "react"))
                .thenReturn(githubCommits.get(1));

        //when
        GithubCommitDTO githubCommit = sut.getLastCommit("facebook", "react");

        //then
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Assertions.assertEquals(dateFormat.parse("2020-12-21 22:22:22"), githubCommit.getCommittedDate());
        verify(mockGithubCommitDAO, times(1))
                .findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc(isA(String.class), isA(String.class));
    }

    @Test
    public void getLastCommit_isNotExist() {
        //context
        when(mockGithubCommitDAO.findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc("facebook", "react"))
                .thenReturn(null);

        //when
        GithubCommitDTO githubCommit = sut.getLastCommit("facebook", "react");

        //then
        Assertions.assertNull(githubCommit);
    }

    @Test
    public void save() throws ParseException {
        // context
        when(mockGithubCommitDAO.save(isA(GithubCommit.class)))
                .thenReturn(new GithubCommit());
        when(mockGithubCommitDAO.findByRepoOwnerAndRepoName("facebook", "react"))
                .thenReturn(githubCommits);

        // given
        GithubCommitDTO dto = new GithubCommitDTO();
        dto.setRepoOwner("facebook");
        dto.setRepoName("react");
        dto.setCommittedDate(dateFormat.parse("2020-12-21 22:22:22"));

        // when
        sut.save(dto);

        // then
        verify(mockGithubCommitDAO, times(1)).save(isA(GithubCommit.class));
    }
}
