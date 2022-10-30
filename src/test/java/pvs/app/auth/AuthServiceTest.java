package pvs.app.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.DigestUtils;
import pvs.app.Application;
import pvs.app.member.Member;
import pvs.app.member.role.Role;

import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private Member member;

    @BeforeEach
    public void setup() {
        this.authService = new AuthService(authenticationManager,
                userDetailsService, jwtTokenUtil);

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
    public void login() {
        //given
        UserDetails userDetails = member;
        Mockito.when(userDetailsService.loadUserByUsername("test")).thenReturn(userDetails);
        Mockito.when(jwtTokenUtil.generateToken(userDetails)).thenReturn("this is jwtToken");
        //when
        String jwtToken = authService.login("test", "test");
        //then
        Assertions.assertEquals("this is jwtToken", jwtToken);
    }
}
