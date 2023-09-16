package app.navigational.RoutingReportSystem.Repositories;

import app.navigational.RoutingReportSystem.Entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("select r from Report r JOIN FETCH r.reportType JOIN FETCH r.domainAttributes " +
            "where r.verified = NOT_VERIFIED")
    List<Report> getAllUnverifiedReports();

    @Query(value = "SELECT r from report r JOIN FETCH r.reportType JOIN FETCH r.domainAttributes " +
            "WHERE ST_DWithin(" +
            "ST_Transform(r.location,3857)," +
            "ST_Transform(ST_GeomFromText(:queryGeom),3857)," +
            "10) AND r.verified=TRUE", nativeQuery = true)
    List<Report> getAllNearbyReports(@Param("queryGeom") String wktLineString);
}
