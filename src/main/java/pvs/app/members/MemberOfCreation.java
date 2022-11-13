package pvs.app.members;

import lombok.Data;

@Data
public class MemberOfCreation {
    private Long id;
    private String username;
    private String password;

    static public MemberOfCreation of(Member m) {
        MemberOfCreation dto = new MemberOfCreation();

        dto.setId(m.getMemberId());
        dto.setUsername(m.getUsername());
        dto.setPassword(m.getPassword());

        return dto;
    }
}
