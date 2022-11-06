package pvs.app.member;

import lombok.Data;

@Data
public class MemberDTO {
    private Long id;
    private String username;
    private String password;

    static public MemberDTO of(Member m) {
        MemberDTO out = new MemberDTO();

        out.setId(m.getMemberId());
        out.setUsername(m.getUsername());
        out.setPassword(m.getPassword());

        return out;
    }
}
