package pvs.app.members;

import org.springframework.stereotype.Service;
import pvs.app.common.PVSRepository;

@Service
public class MemberRepository extends PVSRepository<Long, Member, MemberDataAccessor> {
    public MemberRepository(MemberDataAccessor memberDataAccessor) {
        super(memberDataAccessor);
    }
}
