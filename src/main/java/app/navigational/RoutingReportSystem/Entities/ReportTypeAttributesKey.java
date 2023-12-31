package app.navigational.RoutingReportSystem.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ReportTypeAttributesKeys")
public class ReportTypeAttributesKey {
    @ToString.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "attributeKey")
    private String attributeKey;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportTypeId")
    private ReportType reportType;

    @ToString.Exclude
    @OneToMany(mappedBy = "key", cascade = CascadeType.ALL)
    private List<ReportTypeAttributesValue> values;

    public ReportTypeAttributesKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public ReportTypeAttributesKey(String attributeKey, ReportType reportType) {
        this.attributeKey = attributeKey;
        this.reportType = reportType;
    }

    @Override
    public boolean equals(Object obj) {
        ReportTypeAttributesKey key = (ReportTypeAttributesKey) obj;
        return key.getAttributeKey() == this.attributeKey;
    }
}
