package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.Report;
import app.navigational.RoutingReportSystem.Projections.ReportSimpleView;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("select r from Report r LEFT JOIN FETCH r.reportType " +
            "where r.id = :queryId " +
            "and r.verified = NOT_VERIFIED")
    Report findNotVerifiedByIdJoinFetchType(@Param("queryId") Integer queryId);

    @Query("select r from Report r LEFT JOIN FETCH r.reportType LEFT JOIN FETCH r.domainAttributes " +
            "where r.verified = NOT_VERIFIED")
    List<Report> getAllUnverifiedReports();

    @Query(value = "select r.id as id " +
            "from report r " +
            "JOIN reporttype rt on r.reporttypeid = rt.id " +
            "WHERE ST_DWithin(" +
            "ST_Transform(r.location, 3857), " +
            "ST_Transform(ST_GeomFromText(:queryGeom,4326), 3857)," +
            "10) " +
            "AND r.verified = TRUE " +
            "AND current_timestamp < r.expiresat", nativeQuery = true)
    List<Integer> getAllNearbyReports(@Param("queryGeom") String wktLineString);

    @Query(value = "SELECT r from report r " +
            "WHERE ST_Equals(r.location,CAST(:queryGeom AS geometry)) " +
            "AND r.verified=TRUE " +
            "AND current_timestamp < r.expiresat " +
            "AND r.reporttypeid=:queryId", nativeQuery = true)
    List<ReportSimpleView> getAllReportsWithGivenParameters(@Param("queryGeom") Point queryGeom,
                                                            @Param("queryId") Integer queryId);

    @Query("select r from Report r LEFT JOIN FETCH r.reportType LEFT JOIN FETCH r.domainAttributes " +
            "where r.id IN :queryList")
    List<Report> getReportsJoinFetchTypeAttributesByIdList(@Param("queryList") List<Integer> queryList);
}
