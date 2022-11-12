package pvs.app.members;

import lombok.Data;

@Data
public class MemberDTO {
    private Long id;
    private String username;
    private String password;

    static public MemberDTO of(Member m) {
        MemberDTO dto = new MemberDTO();

        dto.setId(m.getMemberId());
        dto.setUsername(m.getUsername());
        dto.setPassword(m.getPassword());

        return dto;
    }
}
