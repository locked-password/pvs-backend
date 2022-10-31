package pvs.app.sonar.api.get;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DuplicationDTO {
    Date date;
    Double value;

}
