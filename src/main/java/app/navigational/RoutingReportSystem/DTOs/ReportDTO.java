package app.navigational.RoutingReportSystem.DTOs;

import app.navigational.RoutingReportSystem.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Transient;
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
    @JsonView(Views.Private.class)
    private Integer id;

    @JsonView(Views.Public.class)
    private Double x;

    @JsonView(Views.Public.class)
    private Double y;

    @JsonView(Views.Public.class)
    private String reportTypeName;

    @Transient
    @JsonView(Views.Public.class)
    private Map<String, String> domainAttributes;

    public void put(String key, String value) {
        this.domainAttributes.put(key, value);
    }
}
