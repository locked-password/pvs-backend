package pvs.app.member.project.hyperlink;

import pvs.app.api.git.commit.GitCommit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
public class Hyperlink {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long hyperlinkId;

    @NotNull
    private String url;

    @NotNull
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hyperlink")
    private Set<GitCommit> gitCommits;

    public Hyperlink() {
    }

    public @NotNull Long getHyperlinkId() {
        return this.hyperlinkId;
    }

    public void setHyperlinkId(@NotNull Long hyperlinkId) {
        this.hyperlinkId = hyperlinkId;
    }

    public @NotNull String getUrl() {
        return this.url;
    }

    public void setUrl(@NotNull String url) {
        this.url = url;
    }

    public @NotNull String getType() {
        return this.type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public Set<GitCommit> getGitCommits() {
        return this.gitCommits;
    }

    public void setGitCommits(Set<GitCommit> gitCommitSet) {
        this.gitCommits = gitCommitSet;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Hyperlink)) return false;
        final Hyperlink other = (Hyperlink) o;
        if (!other.canEqual(this)) return false;
        final Object this$hyperlinkId = this.getHyperlinkId();
        final Object other$hyperlinkId = other.getHyperlinkId();
        if (!Objects.equals(this$hyperlinkId, other$hyperlinkId))
            return false;
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        if (!Objects.equals(this$url, other$url)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (!Objects.equals(this$type, other$type)) return false;
        final Object this$githubCommitSet = this.getGitCommits();
        final Object other$githubCommitSet = other.getGitCommits();
        return Objects.equals(this$githubCommitSet, other$githubCommitSet);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Hyperlink;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $hyperlinkId = this.getHyperlinkId();
        result = result * PRIME + ($hyperlinkId == null ? 43 : $hyperlinkId.hashCode());
        final Object $url = this.getUrl();
        result = result * PRIME + ($url == null ? 43 : $url.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $githubCommitSet = this.getGitCommits();
        result = result * PRIME + ($githubCommitSet == null ? 43 : $githubCommitSet.hashCode());
        return result;
    }

    public String toString() {
        return "Hyperlink(hyperlinkId=" + this.getHyperlinkId() + ", url=" + this.getUrl() + ", type=" + this.getType() + ", githubCommitSet=" + this.getGitCommits() + ")";
    }

    static public HyperlinkBuilder builder() {
        return new HyperlinkBuilder();
    }
}
