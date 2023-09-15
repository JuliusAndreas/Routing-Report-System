package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportAttributeValueRepository extends JpaRepository<ReportTypeAttributesValue, Integer> {

}
