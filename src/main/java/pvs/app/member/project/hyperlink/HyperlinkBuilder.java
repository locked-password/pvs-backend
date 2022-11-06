package pvs.app.member.project.hyperlink;

import pvs.app.api.github.commit.GithubCommit;

import java.util.Set;

public class HyperlinkBuilder {
    private final Hyperlink hl = new Hyperlink();

    public HyperlinkBuilder setUrl(String url) {
        hl.setUrl(url);
        return this;
    }

    public HyperlinkBuilder setType(String type) {
        hl.setType(type);
        return this;
    }

    public HyperlinkBuilder setId(Long id) {
        hl.setHyperlinkId(id);
        return this;
    }

    public HyperlinkBuilder setGithubCommits(Set<GithubCommit> commits) {
        hl.setGithubCommitSet(commits);
        return this;
    }

    public Hyperlink build() {
        return hl;
    }
}
