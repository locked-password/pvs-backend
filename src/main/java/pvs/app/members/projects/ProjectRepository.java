package pvs.app.members.projects;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import pvs.app.common.PVSRepository;

import java.util.List;

@NoArgsConstructor
@Service("Repository")
public class ProjectRepository extends PVSRepository<Long, Project, ProjectDataAccessor> {

    public ProjectRepository(ProjectDataAccessor projectDataAccessor) {
        super(projectDataAccessor);
    }

    public List<Project> getAll() {
        return dataAccessor.findAll();
    }
}
