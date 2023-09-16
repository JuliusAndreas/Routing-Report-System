package app.navigational.RoutingReportSystem.DTOs;

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
public class AttributeValueDTO {

    @JsonView(Views.Public.class)
    private String attributeValue;
}
