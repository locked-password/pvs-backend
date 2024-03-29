package pvs.app.members;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDataAccessor extends CrudRepository<Member, Long> {
    Member findByUsername(String username);

    Member findById(long id);
}
