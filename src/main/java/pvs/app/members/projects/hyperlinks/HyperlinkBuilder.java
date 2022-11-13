package pvs.app.members.projects.hyperlinks;

import pvs.app.agents.git.GitCommit;

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

    public HyperlinkBuilder setGitCommits(Set<GitCommit> commits) {
        hl.setGitCommits(commits);
        return this;
    }

    public Hyperlink build() {
        return hl;
    }
}
