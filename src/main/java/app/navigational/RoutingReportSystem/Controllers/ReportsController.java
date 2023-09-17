package app.navigational.RoutingReportSystem.Controllers;

import app.navigational.RoutingReportSystem.DTOs.ReportDTO;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Services.ReportService;
import app.navigational.RoutingReportSystem.Utilities.OkResponse;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportsController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/")
    public List<ReportDTO> getNearbyReports(@RequestParam String wktPath) {
        List<ReportDTO> reportDTOList;
        try {
            reportDTOList = reportService.getAllNearbyActiveReports(wktPath);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid wkt for the path was sent");
        }
        if (reportDTOList.isEmpty()) {
            throw new NotFoundException("No Verifiable report was found");
        }
        return reportDTOList;
    }

    @PostMapping("/submit")
    public ResponseEntity submitReport(@RequestBody ReportDTO reportDTO,
                                       @RequestParam Integer userId) {
        reportService.submitReport(reportDTO, userId);
        return new ResponseEntity<>(new OkResponse("report successfully submitted"), HttpStatus.OK);
    }

    @PatchMapping("/{reportId}/like")
    public ResponseEntity likeReport(@PathVariable Integer reportId) {
        reportService.likeReport(reportId);
        return new ResponseEntity<>(new OkResponse("report successfully liked"), HttpStatus.OK);
    }

    @PatchMapping("/{reportId}/dislike")
    public ResponseEntity dislikeReport(@PathVariable Integer reportId) {
        reportService.dislikeReport(reportId);
        return new ResponseEntity<>(new OkResponse("report successfully disliked"), HttpStatus.OK);
    }

    @GetMapping("/verifiable")
    public List<ReportDTO> getVerifiableReports() {
        List<ReportDTO> reportDTOList = reportService.getVerifiableReports();
        if (reportDTOList.isEmpty()) {
            throw new NotFoundException("No Verifiable report was found");
        }
        return reportDTOList;
    }

    @PatchMapping("/{reportId}/verify")
    public ResponseEntity verifyReport(@PathVariable Integer reportId) {
        reportService.verifyReport(reportId);
        return new ResponseEntity<>(new OkResponse("report successfully verified"), HttpStatus.OK);
    }
}
