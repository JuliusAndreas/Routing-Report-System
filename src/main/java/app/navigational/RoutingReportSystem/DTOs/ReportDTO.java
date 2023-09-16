package app.navigational.RoutingReportSystem.DTOs;

import app.navigational.RoutingReportSystem.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ReportDTO {

    @JsonView(Views.Public.class)
    private Double x;

    @JsonView(Views.Public.class)
    private Double y;

    @JsonView(Views.Public.class)
    private String reportTypeName;

    @JsonView(Views.Public.class)
    private Map<String, String> domainAttributes;
}
