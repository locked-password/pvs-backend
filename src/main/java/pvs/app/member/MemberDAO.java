package pvs.app.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pvs.app.member.Member;

@Repository
public interface MemberDAO extends CrudRepository<Member, Long> {
    Member findByUsername(String username);
    Member findById(long id);
}
