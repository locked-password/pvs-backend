package pvs.app.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @MockBean
    private RoleService mockRoleService;

    @MockBean
    private MemberDAO mockMemberDAO;

    private final Member member01 = new Member();
    private final MemberDTO member01DTO = new MemberDTO();
    private final Role userRole = new Role();

    @BeforeEach
    public void setup() {
        member01.setMemberId(1L);
        member01.setUsername("user");
        member01.setPassword("1234");

        member01DTO.setId(1L);
        member01DTO.setUsername("user");
        member01DTO.setPassword("1234");

        userRole.setRoleId(1L);
        userRole.setName("USER");
    }

    @Test
    public void get() {
        //context
        when(mockMemberDAO.findById(1L))
                .thenReturn(member01);
        //when
        MemberDTO memberDTO = memberService.get(1L);

        //then
        Assertions.assertEquals(member01DTO.toString(), memberDTO.toString());
        verify(mockMemberDAO, times(1)).findById(1L);
    }

    @Test
    public void createUser() {
        //context
        when(mockRoleService.getByName("USER"))
                .thenReturn(userRole);
        when(mockMemberDAO.save(any(Member.class))).thenReturn(member01);

        //when
        MemberDTO memberDTO = memberService.createUser(member01DTO);
        //then
        Assertions.assertEquals(member01DTO.toString(), memberDTO.toString());
    }

}
