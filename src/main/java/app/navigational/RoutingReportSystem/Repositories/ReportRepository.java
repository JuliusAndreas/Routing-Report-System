package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.Report;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("select r from Report r JOIN FETCH r.reportType " +
            "where r.id = :queryId " +
            "and r.verified = NOT_VERIFIED")
    Report findNotVerifiedByIdJoinFetchType(@Param("queryId") Integer queryId);

    @Query("select r from Report r JOIN FETCH r.reportType JOIN FETCH r.domainAttributes " +
            "where r.verified = NOT_VERIFIED")
    List<Report> getAllUnverifiedReports();

    @Query(value = "SELECT r from report r JOIN FETCH r.reportType JOIN FETCH r.domainAttributes " +
            "WHERE ST_DWithin(" +
            "ST_Transform(r.location,3857)," +
            "ST_Transform(ST_GeomFromText(:queryGeom),3857)," +
            "10) AND r.verified=TRUE " +
            "AND CAST(:now AS TIMESTAMP)<r.expiresat", nativeQuery = true)
    List<Report> getAllNearbyReports(@Param("queryGeom") String wktLineString,
                                     @Param("now") LocalDateTime now);

    @Query(value = "SELECT r from report r " +
            "WHERE ST_Equals(r.location,CAST(:queryGeom AS geometry)) " +
            "AND r.verified=TRUE " +
            "AND CAST(:now AS TIMESTAMP)<r.expiresat " +
            "AND r.reporttypeid=:queryId", nativeQuery = true)
    List<Report> getAllReportsWithGivenParameters(@Param("queryGeom") Point queryGeom,
                                                  @Param("now") LocalDateTime now,
                                                  @Param("queryId") Integer queryId);
}
