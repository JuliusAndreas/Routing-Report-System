package app.navigational.RoutingReportSystem.Utilities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class JwtResponse implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = -8091879091924046844L;

    private final String jwtToken;

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
