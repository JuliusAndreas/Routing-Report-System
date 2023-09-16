package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.ReportDTO;
import app.navigational.RoutingReportSystem.Entities.Report;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "x", expression = "java(report.getLocation().getX())")
    @Mapping(target = "y", expression = "java(report.getLocation().getY())")
    ReportDTO toDTO(Report report);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "location", expression = "java(coordinationToPoint(reportDTO.getX(), reportDTO.getY()))")
    Report fromDTO(ReportDTO reportDTO);

    default Point coordinationToPoint(Double x, Double y) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(x, y);
        Point point = geometryFactory.createPoint(coordinate);
        point.setSRID(4326);
        return point;
    }
}
