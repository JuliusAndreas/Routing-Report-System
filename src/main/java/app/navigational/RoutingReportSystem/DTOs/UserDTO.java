package app.navigational.RoutingReportSystem.DTOs;

import app.navigational.RoutingReportSystem.Utilities.RoleType;
import app.navigational.RoutingReportSystem.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class UserDTO {

    @JsonView(Views.Public.class)
    private String username;

    @JsonView(Views.Private.class)
    private String password;

    @JsonView(Views.Public.class)
    private RoleType roleType;
}
