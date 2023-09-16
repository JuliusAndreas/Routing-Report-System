package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportAttributeValueRepository extends JpaRepository<ReportTypeAttributesValue, Integer> {

    @Query("select v from ReportTypeAttributesValue v where v.key.id = :queryId")
    List<ReportTypeAttributesValue> findValuesByKeyId(@Param("queryId") Integer keyId);
}
