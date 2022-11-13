package pvs.app.members;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "要被新建的使用者")
public class MemberOfCreation {
    @ApiModelProperty(notes = "要被新建的使用者ID")
    private Long id;

    @ApiModelProperty(notes = "要被新建的使用者名稱", required = true)
    private String username;

    @ApiModelProperty(notes = "要被新建的使用者密碼", required = true)
    private String password;

    static public MemberOfCreation of(Member m) {
        MemberOfCreation dto = new MemberOfCreation();

        dto.setId(m.getMemberId());
        dto.setUsername(m.getUsername());
        dto.setPassword(m.getPassword());

        return dto;
    }
}
