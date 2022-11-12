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
    private MemberOfCreation stubbingMemberOfCreation;

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

        stubbingMemberOfCreation = new MemberOfCreation();
        stubbingMemberOfCreation.setId(stubbingMember.getMemberId());
        stubbingMemberOfCreation.setUsername(stubbingMember.getUsername());
        stubbingMemberOfCreation.setPassword(stubbingMember.getPassword());

        userRole = new Role();
        userRole.setRoleId(1L);
        userRole.setName("USER");

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createMember() {
        // Context
        when(mockRoleService.getByName("USER")).thenReturn(userRole);
        when(mockMemberRepository.put(any(), isA(Member.class)))
                .thenReturn(stubbingMember);

        // When
        MemberOfCreation dto = new MemberOfCreation();
        dto.setUsername("ABC");
        dto.setPassword("123456");
        MemberOfCreation memberOfCreation = sut.createMember(dto);

        // Then
        Assertions.assertEquals(stubbingMemberOfCreation.getId(), memberOfCreation.getId());
    }

    @Test
    public void readMember() {
        // Context:
        when(mockMemberRepository.get(1L)).thenReturn(stubbingMember);

        // When:
        MemberOfCreation dto = sut.readMember(1L);

        // Then:
        Assertions.assertEquals(stubbingMemberOfCreation.toString(), dto.toString());
        verify(mockMemberRepository, times(1)).get(1L);
    }
}
