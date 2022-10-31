package pvs.app.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pvs.app.member.post.MemberDTO;

@RestController
public class AuthController {

    static final Logger logger = LogManager.getLogger(AuthController.class.getName());

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 登录
     */
    @PostMapping(value = "/auth/login")
    public String login( @RequestBody MemberDTO memberDTO ) {
        // 登录成功会返回Token给用户
        return authService.login(memberDTO.getUsername(), memberDTO.getPassword());
    }
}
