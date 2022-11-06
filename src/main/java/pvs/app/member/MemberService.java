package pvs.app.member;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import pvs.app.member.role.Role;
import pvs.app.member.role.RoleService;

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

    public MemberDTO createUser(MemberDTO memberDTO) {
        Member member = new Member();

        member.setUsername(memberDTO.getUsername());

        String encodePassword = DigestUtils.md5DigestAsHex(memberDTO.getPassword().getBytes());
        member.setPassword(encodePassword);

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleService.getByName("USER");
        if (userRole != null) roleSet.add(userRole);
        member.setAuthorities(roleSet);

        Member savedMember = memberRepository.put(null, member);

        return MemberDTO.of(savedMember);
    }

    public MemberDTO readUser(long id) {
        Member member = memberRepository.get(id);
        return MemberDTO.of(member);
    }


}
