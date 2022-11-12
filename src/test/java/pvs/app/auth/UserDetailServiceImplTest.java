package pvs.app.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.DigestUtils;
import pvs.app.Application;
import pvs.app.members.Member;
import pvs.app.members.MemberDataAccessor;
import pvs.app.members.roles.Role;

import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class UserDetailServiceImplTest {

    @MockBean
    private MemberDataAccessor mockMemberDataAccessor;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsServiceImpl;

    private Member member;

    @BeforeEach
    public void setup() {
        this.userDetailsServiceImpl = new UserDetailsServiceImpl(mockMemberDataAccessor);

        member = new Member();
        Role userRole = new Role();
        userRole.setRoleId(1L);
        userRole.setName("USER");

        member.setMemberId(1L);
        member.setUsername("test");
        member.setPassword(DigestUtils.md5DigestAsHex("test".getBytes()));
        member.setAuthorities(Set.of(userRole));
    }

    @Test
    public void loadUserByUsername_found() {
        //given
        Mockito.when(mockMemberDataAccessor.findByUsername("test")).thenReturn(member);
        //when
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("test");
        //then
        Assertions.assertEquals(member, userDetails);
    }

    @Test
    public void loadUserByUsername_notFound() {
        //given
        Mockito.when(mockMemberDataAccessor.findByUsername("test")).thenThrow(new UsernameNotFoundException("not found"));
        //when
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("notFound");
        //then
        Assertions.assertNull(userDetails);
    }
}
