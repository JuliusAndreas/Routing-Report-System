package app.navigational.RoutingReportSystem.DTOs;

import app.navigational.RoutingReportSystem.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.temporal.ChronoUnit;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ReportTypeDTO {

    @JsonView(Views.Public.class)
    private String typeName;

    @JsonView(Views.Public.class)
    private Boolean verifiable;

    @JsonView(Views.Public.class)
    private String durationUnit;

    @JsonView(Views.Public.class)
    private Long duration;

    @JsonView(Views.Public.class)
    private Long extensionDuration;

    @JsonView(Views.Public.class)
    private List<String> keys;
}
