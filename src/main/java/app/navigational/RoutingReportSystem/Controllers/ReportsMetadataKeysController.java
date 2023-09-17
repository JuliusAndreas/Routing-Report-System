package app.navigational.RoutingReportSystem.Controllers;

import app.navigational.RoutingReportSystem.DTOs.AttributeKeyDTO;
import app.navigational.RoutingReportSystem.DTOs.AttributeValueDTO;
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
@RequestMapping("/reportTypes/{typeId}/keys")
public class ReportsMetadataKeysController {
    @Autowired
    private ReportTypeService typeService;
    @Autowired
    private ReportTypeAttributesKeysService keysService;
    @Autowired
    private ReportTypeAttributesValuesService valuesService;

    @JsonView(Views.Public.class)
    @GetMapping("/{keyId}")
    public List<AttributeValueDTO> getAllAvailableValuesPerKey(@PathVariable Integer keyId) {
        List<AttributeValueDTO> attributeValueDTOS = keysService.getAllValuesForAKey(keyId);
        if (attributeValueDTOS.isEmpty()) {
            throw new NotFoundException("No value is available for this key, try adding one");
        }
        return attributeValueDTOS;
    }

    @PostMapping("/")
    public ResponseEntity addKeyToAReportType(@PathVariable Integer typeId,
                                              @RequestBody AttributeKeyDTO attributeKeyDTO) {
        keysService.addAttributeKey(typeId, attributeKeyDTO);
        return new ResponseEntity<>(new OkResponse("Key successfully added"), HttpStatus.OK);
    }

    @PostMapping("/{keyId}")
    public ResponseEntity addValueToAReportTypeKey(@PathVariable Integer keyId,
                                                   @RequestBody AttributeValueDTO attributeValueDTO) {
        valuesService.addAttributeValue(keyId, attributeValueDTO);
        return new ResponseEntity<>(new OkResponse("Value successfully added"), HttpStatus.OK);
    }
}
