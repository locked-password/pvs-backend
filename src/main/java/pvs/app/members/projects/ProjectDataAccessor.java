package pvs.app.members.projects;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("DataAccessor")
public interface ProjectDataAccessor extends CrudRepository<Project, Long> {
    @NotNull
    List<Project> findAll();

    List<Project> findByMemberId(Long memberId);
}
