package app.navigational.RoutingReportSystem.Entities;

import app.navigational.RoutingReportSystem.Utilities.VerifiedType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;

    @Column(columnDefinition = "geometry")
    private Point location;

    @Column(name = "verified")
    private VerifiedType verified;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "dislikes")
    private Integer dislikes;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportTypeId")
    private ReportType reportType;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<ReportDomainAttribute> domainAttributes;

    public void setPropertiesForNonVerifiableReport(LocalDateTime createdAt, LocalDateTime expiresAt, ReportType reportType,
                                                    User user, List<ReportDomainAttribute> domainAttributes) {
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.reportType = reportType;
        this.user = user;
        this.domainAttributes = domainAttributes;
        this.verified = VerifiedType.VERIFIED;
    }

    public void setPropertiesForVerifiableReport(ReportType reportType, User user,
                                                 List<ReportDomainAttribute> domainAttributes) {
        this.reportType = reportType;
        this.user = user;
        this.domainAttributes = domainAttributes;
        this.verified = VerifiedType.NOT_VERIFIED;
    }

    public void incrementLikes() {
        this.likes++;
        this.expiresAt.plus(reportType.getExtensionDuration(), reportType.getDurationUnit());
    }

    public void incrementDislikes() {
        this.dislikes++;
        this.expiresAt.minus(reportType.getExtensionDuration(), reportType.getDurationUnit());
    }

}
