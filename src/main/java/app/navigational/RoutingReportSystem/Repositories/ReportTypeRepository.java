package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {

    @Query("select k.attributeKey from ReportType r, r.keys k where r.id = :queryId")
    List<String> findAttributeKeysStringsPerType(@Param("queryId") Integer id);

    @Query("select k from ReportType r, r.keys k where r.id = :queryId")
    List<ReportTypeAttributesKey> findAttributeKeysPerType(@Param("queryId") Integer id);
}
