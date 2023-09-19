package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.ReportType;
import app.navigational.RoutingReportSystem.Entities.ReportTypeAttributesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {

    @Query("select k.attributeKey from ReportTypeAttributesKey k where k.reportType.id = :queryId")
    List<String> findAttributeKeysStringsPerType(@Param("queryId") Integer id);

    @Query("select k from ReportTypeAttributesKey k where k.reportType.id = :queryId")
    List<ReportTypeAttributesKey> findAttributeKeysPerType(@Param("queryId") Integer id);

    @Query("select k.attributeKey from ReportTypeAttributesKey k where k.reportType.id = :queryId")
    Set<String> findAttributeKeysSetPerType(@Param("queryId") Integer id);

    @Query("select t from ReportType t where t.typeName = :queryName")
    ReportType findReportTypeByTypeName(@Param("queryName") String name);
}
