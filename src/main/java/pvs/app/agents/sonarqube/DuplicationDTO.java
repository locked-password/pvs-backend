package pvs.app.agents.sonarqube;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DuplicationDTO {
    Date date;
    Double value;

}
