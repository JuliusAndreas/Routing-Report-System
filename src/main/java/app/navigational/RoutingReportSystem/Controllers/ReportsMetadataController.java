package app.navigational.RoutingReportSystem.Controllers;

import app.navigational.RoutingReportSystem.DTOs.AttributeValueDTO;
import app.navigational.RoutingReportSystem.DTOs.ReportTypeDTO;
import app.navigational.RoutingReportSystem.Exceptions.NotFoundException;
import app.navigational.RoutingReportSystem.Services.ReportTypeAttributesKeysService;
import app.navigational.RoutingReportSystem.Services.ReportTypeAttributesValuesService;
import app.navigational.RoutingReportSystem.Services.ReportTypeService;
import app.navigational.RoutingReportSystem.Utilities.OkResponse;
import app.navigational.RoutingReportSystem.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportTypes")
public class ReportsMetadataController {
    @Autowired
    private ReportTypeService typeService;
    @Autowired
    private ReportTypeAttributesKeysService keysService;
    @Autowired
    private ReportTypeAttributesValuesService valuesService;

    @JsonView(Views.Public.class)
    @GetMapping("/")
    public List<ReportTypeDTO> getCurrentReportTypes(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int itemsPerPage,
                                                     @RequestParam(defaultValue = "typeName") String sortedBy) {
        List<ReportTypeDTO> reportTypeDTOS = typeService.getAllReportTypes();
        if (reportTypeDTOS.isEmpty()) {
            throw new NotFoundException("No report is available, try adding one");
        }
        return reportTypeDTOS;
    }

    @PostMapping("/")
    public ResponseEntity addReportType(@RequestBody ReportTypeDTO reportTypeDTO) {
        typeService.addReportType(reportTypeDTO);
        return new ResponseEntity<>(new OkResponse("Report type successfully added"), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateReportType(@PathVariable Integer id,
                                           @RequestBody ReportTypeDTO reportTypeDTO) {
        typeService.updateReportType(id, reportTypeDTO);
        return new ResponseEntity<>(new OkResponse("Report type successfully updated"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReportType(@PathVariable Integer id) {
        typeService.removeReportType(id);
        return new ResponseEntity<>(new OkResponse("Report type successfully deleted"), HttpStatus.OK);
    }
}
