package pvs.app.member.project;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDAO extends CrudRepository<Project, Long> {
    @NotNull
    List<Project> findAll();

    List<Project> findByMemberId(Long memberId);
}
