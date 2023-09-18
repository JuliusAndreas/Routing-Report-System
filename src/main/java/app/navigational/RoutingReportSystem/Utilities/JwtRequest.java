package app.navigational.RoutingReportSystem.Utilities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JwtRequest implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 5926468583005150707L;

    private String username;

    private String password;

}
