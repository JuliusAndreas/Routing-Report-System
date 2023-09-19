package app.navigational.RoutingReportSystem.DTOs;

import app.navigational.RoutingReportSystem.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class RabbitSubmitReportDTO {
    @JsonView(Views.Public.class)
    private ReportDTO reportDTO;

    @JsonView(Views.Public.class)
    private Integer userId;
}
