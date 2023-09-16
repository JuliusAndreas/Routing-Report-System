package app.navigational.RoutingReportSystem.Mappers;

import app.navigational.RoutingReportSystem.DTOs.ReportDTO;
import app.navigational.RoutingReportSystem.Entities.Report;
import app.navigational.RoutingReportSystem.Entities.ReportDomainAttribute;
import app.navigational.RoutingReportSystem.Entities.ReportType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "x", expression = "java(report.getLocation().getX())")
    @Mapping(target = "y", expression = "java(report.getLocation().getY())")
    ReportDTO toDTO(Report report);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "x", expression = "java(reports.getLocation().getX())")
    @Mapping(target = "y", expression = "java(reports.getLocation().getY())")
    @Mapping(target = "reportTypeName", expression = "java(roleTypeToTypeName(reports.getReportType()))")
    @Mapping(target = "domainAttributes", expression = "java(attributeListToMap(reports.getDomainAttributes()))")
    List<ReportDTO> toDTO(Collection<Report> reports);

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

    default String roleTypeToTypeName(ReportType reportType) {
        return reportType.getTypeName();
    }

    default Map<String, String> attributeListToMap(List<ReportDomainAttribute> attributes) {
        Map<String, String> resultMap = new HashMap<>();
        for (ReportDomainAttribute attribute : attributes) {
            String key = attribute.getDomainAttributeKey();
            String value = attribute.getDomainAttributeValue();
            resultMap.put(key, value);
        }
        return resultMap;
    }
}
