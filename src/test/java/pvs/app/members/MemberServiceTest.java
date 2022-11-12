package pvs.app.members;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;
import pvs.app.members.roles.Role;
import pvs.app.members.roles.RoleService;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@Tag("Unit")
public class MemberServiceTest {
    @InjectMocks
    private MemberService sut;

    @MockBean
    private MemberRepository mockMemberRepository;
    private Member stubbingMember;
    private MemberDTO stubbingMemberDTO;

    @MockBean
    private RoleService mockRoleService;
    private Role userRole;


    @Autowired
    public MemberServiceTest(MemberService sut, MemberRepository mockMemberRepository, RoleService mockRoleService) {
        this.sut = sut;
        this.mockMemberRepository = mockMemberRepository;
        this.mockRoleService = mockRoleService;
    }

    @BeforeEach
    public void setup() {
        stubbingMember = new Member();
        stubbingMember.setMemberId(1L);
        stubbingMember.setUsername("user");
        stubbingMember.setPassword("1234");

        stubbingMemberDTO = new MemberDTO();
        stubbingMemberDTO.setId(stubbingMember.getMemberId());
        stubbingMemberDTO.setUsername(stubbingMember.getUsername());
        stubbingMemberDTO.setPassword(stubbingMember.getPassword());

        userRole = new Role();
        userRole.setRoleId(1L);
        userRole.setName("USER");

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void get() {
        // Context:
        when(mockMemberRepository.get(1L)).thenReturn(stubbingMember);

        // When:
        MemberDTO dto = sut.readUser(1L);

        // Then:
        Assertions.assertEquals(stubbingMemberDTO.toString(), dto.toString());
        verify(mockMemberRepository, times(1)).get(1L);
    }

    @Test
    public void createUser() {
        // Context
        when(mockRoleService.getByName("USER")).thenReturn(userRole);
        when(mockMemberRepository.put(any(), isA(Member.class)))
                .thenReturn(stubbingMember);

        // When
        MemberDTO dto = new MemberDTO();
        dto.setUsername("ABC");
        dto.setPassword("123456");
        MemberDTO memberDTO = sut.createUser(dto);

        // Then
        Assertions.assertEquals(stubbingMemberDTO.getId(), memberDTO.getId());
    }
}