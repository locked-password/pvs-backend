package pvs.app.api.sonarqube.get;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CodeSmellDTO {
    Date date;
    Integer value;

}
