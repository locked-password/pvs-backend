package pvs.app.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pvs.app.member.post.MemberDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    static final Logger logger = LogManager.getLogger(AuthController.class.getName());

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody MemberDTO memberDTO) {
        return authService.login(memberDTO.getUsername(), memberDTO.getPassword());
    }
}
