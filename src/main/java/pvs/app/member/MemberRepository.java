package pvs.app.member;

import org.springframework.stereotype.Service;
import pvs.app.common.PVSRepository;

@Service
public class MemberRepository extends PVSRepository<Long, Member, MemberDAO> {
    public MemberRepository(MemberDAO memberDAO) {
        super(memberDAO);
    }
}
