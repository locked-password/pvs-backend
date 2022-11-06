package pvs.app.member.project;

import org.springframework.stereotype.Service;
import pvs.app.common.PVSRepository;

import java.util.List;

@Service
public class ProjectRepository extends PVSRepository<Long, Project, ProjectDataAccessor> {

    public ProjectRepository(ProjectDataAccessor projectDataAccessor) {
        super(projectDataAccessor);
    }

    public List<Project> getAll() {
        return dataAccessor.findAll();
    }
}
