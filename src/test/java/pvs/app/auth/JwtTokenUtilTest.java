package pvs.app.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.DigestUtils;
import pvs.app.Application;
import pvs.app.member.Member;
import pvs.app.member.Role;

import java.io.IOException;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private Member memberUser;
    private Member memberAdmin;

    @BeforeEach
    public void setup() throws IOException {
        memberUser = new Member();
        Role userRole = new Role();
        userRole.setRoleId(1L);
        userRole.setName("USER");

        memberUser.setMemberId(1L);
        memberUser.setUsername("user");
        memberUser.setPassword(DigestUtils.md5DigestAsHex("user".getBytes()));
        memberUser.setAuthorities(Set.of(userRole));

        memberAdmin = new Member();
        Role adminRole = new Role();
        adminRole.setRoleId(2L);
        adminRole.setName("ADMIN");

        memberAdmin.setMemberId(2L);
        memberAdmin.setUsername("admin");
        memberAdmin.setPassword(DigestUtils.md5DigestAsHex("admin".getBytes()));
        memberAdmin.setAuthorities(Set.of(adminRole));
    }

    @Test
    public void validToken() {
        //given
        UserDetails userDetails = memberUser;
        //when
        String token = jwtTokenUtil.generateToken(userDetails);
        boolean tokenValidated = jwtTokenUtil.validateToken(token, userDetails);
        //then
        Assertions.assertTrue(tokenValidated);
    }

    @Test
    public void invalidToken() {
        //given
        UserDetails authenticatedUser = memberUser;
        UserDetails authenticatedAdmin = memberAdmin;

        //when
        String token = jwtTokenUtil.generateToken(authenticatedUser);
        boolean tokenValidated = jwtTokenUtil.validateToken(token, authenticatedAdmin);

        //then
        Assertions.assertFalse(tokenValidated);
    }


}
