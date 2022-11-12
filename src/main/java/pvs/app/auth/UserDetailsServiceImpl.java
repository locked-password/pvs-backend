package pvs.app.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pvs.app.members.MemberDataAccessor;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class.getName());

    private final MemberDataAccessor memberDataAccessor;

    UserDetailsServiceImpl(MemberDataAccessor memberDataAccessor) {
        this.memberDataAccessor = memberDataAccessor;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        try {
            return memberDataAccessor.findByUsername(userName);
        } catch (UsernameNotFoundException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
