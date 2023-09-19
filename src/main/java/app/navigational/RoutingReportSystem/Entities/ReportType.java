package app.navigational.RoutingReportSystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ReportType")
public class ReportType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "typeName")
    private String typeName;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "verifiable")
    private Boolean verifiable;

    @Column(name = "durationUnit")
    private ChronoUnit durationUnit;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "extensionDuration")
    private Long extensionDuration;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "reportType", cascade = CascadeType.ALL)
    private List<Report> reports;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "reportType", cascade = CascadeType.ALL)
    private Set<ReportTypeAttributesKey> keys;

}
