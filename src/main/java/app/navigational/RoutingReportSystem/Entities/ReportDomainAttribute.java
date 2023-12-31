package app.navigational.RoutingReportSystem.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ReportAttributes")
public class ReportDomainAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "attributeKey")
    private String domainAttributeKey;

    @Column(name = "attributeValue")
    private String domainAttributeValue;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportID")
    private Report report;

    public ReportDomainAttribute(String domainAttributeKey, String domainAttributeValue, Report report) {
        this.domainAttributeKey = domainAttributeKey;
        this.domainAttributeValue = domainAttributeValue;
        this.report = report;
    }
}
