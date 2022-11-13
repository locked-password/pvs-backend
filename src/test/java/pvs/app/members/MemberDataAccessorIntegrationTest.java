package pvs.app.members;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pvs.app.Application;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Tag("Integration")
public class MemberDataAccessorIntegrationTest {
    @Autowired
    private MemberDataAccessor memberDataAccessor;

    private Member member01 = new Member();

    @BeforeEach
    public void setup() {
        member01.setUsername("aaaa");
        member01.setPassword("1234");
        member01 = memberDataAccessor.save(member01);

        Member member02 = new Member();
        member02.setUsername("bbb");
        member02.setPassword("1234");
        memberDataAccessor.save(member02);
    }

    @Test
    public void whenFindByAccount_thenReturnMember() {
        Member foundEntity = memberDataAccessor.findByUsername("aaaa");

        Assertions.assertEquals(member01.getUsername(), foundEntity.getUsername());
    }

    @Test
    public void whenFindById_thenReturnMember() {
        Optional<Member> foundEntity = memberDataAccessor.findById(member01.getMemberId());

        assert foundEntity.orElse(null) != null;
        Assertions.assertEquals(member01.getUsername(), foundEntity.orElse(null).getUsername());
    }

}