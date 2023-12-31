package app.navigational.RoutingReportSystem.Entities;

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
@Table(name = "ReportTypeAttributesValues")
public class ReportTypeAttributesValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "attributeValue")
    private String attributeValue;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyId")
    private ReportTypeAttributesKey key;

    public ReportTypeAttributesValue(String attributeValue, ReportTypeAttributesKey key) {
        this.attributeValue = attributeValue;
        this.key = key;
    }
}
