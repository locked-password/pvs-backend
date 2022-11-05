package pvs.app.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.member.role.Role;
import pvs.app.member.role.RoleService;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@Tag("Unit")
public class MemberServiceTest {
    @Autowired
    private MemberService sut;

    @MockBean
    private MemberDAO mockMemberDAO;
    private Member stubbingMember;
    private MemberDTO stubbingMemberDTO;

    @MockBean
    private RoleService mockRoleService;
    private Role userRole;

    public MemberServiceTest() {
        stubbingMember = new Member();
        stubbingMemberDTO = new MemberDTO();
        userRole = new Role();
    }

    @BeforeEach
    public void setup() {
        stubbingMember.setMemberId(1L);
        stubbingMember.setUsername("user");
        stubbingMember.setPassword("1234");

        stubbingMemberDTO.setId(stubbingMember.getMemberId());
        stubbingMemberDTO.setUsername(stubbingMember.getUsername());
        stubbingMemberDTO.setPassword(stubbingMember.getPassword());

        userRole.setRoleId(1L);
        userRole.setName("USER");
    }

    @Test
    public void get() {
        // Context:
        when(mockMemberDAO.findById(1L)).thenReturn(stubbingMember);

        // When:
        MemberDTO dto = sut.get(1L);

        // Then:
        Assertions.assertEquals(stubbingMemberDTO.toString(), dto.toString());
        verify(mockMemberDAO, times(1)).findById(1L);
    }

    @Test
    public void createUser() {
        // Context
        when(mockRoleService.getByName("USER")).thenReturn(userRole);
        when(mockMemberDAO.save(any(Member.class))).thenReturn(stubbingMember);

        // When
        MemberDTO dto = new MemberDTO();
        dto.setUsername("ABC");
        dto.setPassword("123456");
        MemberDTO memberDTO = sut.createUser(dto);

        // Then
        Assertions.assertEquals(stubbingMemberDTO.getId(), memberDTO.getId());
    }
}
