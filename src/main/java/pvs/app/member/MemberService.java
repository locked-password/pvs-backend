package pvs.app.member;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import pvs.app.member.post.MemberDTO;
import pvs.app.member.role.Role;
import pvs.app.member.role.RoleService;

import java.util.HashSet;
import java.util.Set;

@Service
public class MemberService {

    private final MemberDAO memberDAO;
    private final RoleService roleService;

    MemberService(MemberDAO memberDAO, RoleService roleService) {
        this.memberDAO = memberDAO;
        this.roleService = roleService;
    }

    public MemberDTO createUser(MemberDTO memberDTO) {
        Member member = new Member();

        member.setUsername(memberDTO.getUsername());

        String encodePassword = DigestUtils.md5DigestAsHex(memberDTO.getPassword().getBytes());
        member.setPassword(encodePassword);

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleService.getByName("USER");
        if (userRole != null) {
            roleSet.add(userRole);
            member.setAuthorities(roleSet);
        }

        Member savedMember = memberDAO.save(member);

        memberDTO.setId(savedMember.getMemberId());
        return memberDTO;
    }

    public MemberDTO get(long id) {
        Member member = memberDAO.findById(id);
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getMemberId());
        memberDTO.setUsername(member.getUsername());
        memberDTO.setPassword(member.getPassword());
        return memberDTO;
    }
}
