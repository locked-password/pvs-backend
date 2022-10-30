package pvs.app.project.repository;

import pvs.app.github.api.GithubCommit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Repository {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long repositoryId;

    @NotNull
    private String url;

    @NotNull
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "repository")
    private Set<GithubCommit> githubCommitSet;

    public Repository() {
    }

    public @NotNull Long getRepositoryId() {
        return this.repositoryId;
    }

    public @NotNull String getUrl() {
        return this.url;
    }

    public @NotNull String getType() {
        return this.type;
    }

    public Set<GithubCommit> getGithubCommitSet() {
        return this.githubCommitSet;
    }

    public void setRepositoryId(@NotNull Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void setUrl(@NotNull String url) {
        this.url = url;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public void setGithubCommitSet(Set<GithubCommit> githubCommitSet) {
        this.githubCommitSet = githubCommitSet;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Repository)) return false;
        final Repository other = (Repository) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$repositoryId = this.getRepositoryId();
        final Object other$repositoryId = other.getRepositoryId();
        if (this$repositoryId == null ? other$repositoryId != null : !this$repositoryId.equals(other$repositoryId))
            return false;
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$githubCommitSet = this.getGithubCommitSet();
        final Object other$githubCommitSet = other.getGithubCommitSet();
        if (this$githubCommitSet == null ? other$githubCommitSet != null : !this$githubCommitSet.equals(other$githubCommitSet))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Repository;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $repositoryId = this.getRepositoryId();
        result = result * PRIME + ($repositoryId == null ? 43 : $repositoryId.hashCode());
        final Object $url = this.getUrl();
        result = result * PRIME + ($url == null ? 43 : $url.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $githubCommitSet = this.getGithubCommitSet();
        result = result * PRIME + ($githubCommitSet == null ? 43 : $githubCommitSet.hashCode());
        return result;
    }

    public String toString() {
        return "Repository(repositoryId=" + this.getRepositoryId() + ", url=" + this.getUrl() + ", type=" + this.getType() + ", githubCommitSet=" + this.getGithubCommitSet() + ")";
    }
}
