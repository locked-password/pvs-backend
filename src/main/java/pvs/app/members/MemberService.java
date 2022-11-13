package pvs.app.members;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import pvs.app.members.roles.Role;
import pvs.app.members.roles.RoleService;

import java.util.HashSet;
import java.util.Set;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleService roleService;

    public MemberService(
            MemberRepository memberRepository, RoleService roleService) {
        this.memberRepository = memberRepository;
        this.roleService = roleService;
    }

    public MemberOfCreation createMember(MemberOfCreation memberOfCreation) {
        Member member = new Member();

        member.setUsername(memberOfCreation.getUsername());

        String encodePassword = DigestUtils.md5DigestAsHex(memberOfCreation.getPassword().getBytes());
        member.setPassword(encodePassword);

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleService.getByName("USER");
        if (userRole != null) roleSet.add(userRole);
        member.setAuthorities(roleSet);

        Member savedMember = memberRepository.put(member);

        return MemberOfCreation.of(savedMember);
    }

    public MemberOfCreation readMember(long id) {
        Member member = memberRepository.get(id);
        return MemberOfCreation.of(member);
    }
}
