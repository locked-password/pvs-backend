package pvs.app.members.projects;

import lombok.Data;
import pvs.app.members.projects.hyperlinks.HyperlinkDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class ProjectOfResponse {
    Long projectId;
    String projectName;
    String avatarURL;
    List<HyperlinkDTO> HyperlinkDTOList = new ArrayList<>();

    static public ProjectOfResponse of(Project project) {
        ProjectOfResponse dto = new ProjectOfResponse();

        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getName());
        dto.setAvatarURL(project.getAvatarURL());
        dto.setHyperlinkDTOList(project.getHyperlinkSet()
                .stream()
                .map(HyperlinkDTO::of)
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectOfResponse that = (ProjectOfResponse) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(projectName, that.projectName) &&
                Objects.equals(avatarURL, that.avatarURL) &&
                Objects.equals(HyperlinkDTOList, that.HyperlinkDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, avatarURL, HyperlinkDTOList);
    }
}
