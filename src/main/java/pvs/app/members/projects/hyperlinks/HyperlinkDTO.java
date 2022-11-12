package pvs.app.members.projects.hyperlinks;

import java.util.Objects;

public class HyperlinkDTO {
    private String url;
    private String type;

    public HyperlinkDTO() {
    }

    static public HyperlinkDTO of(Hyperlink hyperlink) {
        HyperlinkDTO dto = new HyperlinkDTO();

        dto.setUrl(hyperlink.getUrl());
        dto.setType(hyperlink.getType());

        return dto;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof HyperlinkDTO other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        if (!Objects.equals(this$url, other$url)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        return Objects.equals(this$type, other$type);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof HyperlinkDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $url = this.getUrl();
        result = result * PRIME + ($url == null ? 43 : $url.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        return result;
    }

    public String toString() {
        return "RepositoryDTO(url=" + this.getUrl() + ", type=" + this.getType() + ")";
    }
}
