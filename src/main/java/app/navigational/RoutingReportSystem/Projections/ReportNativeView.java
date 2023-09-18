package app.navigational.RoutingReportSystem.Projections;

import org.locationtech.jts.geom.Point;

public interface ReportNativeView {
    Integer getId();
    Point getLocation();
    Integer getReporttypeid();
}
