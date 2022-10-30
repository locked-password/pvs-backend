package pvs.app.sonar.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class BugDTO {
    Date date;
    Integer value;

}
