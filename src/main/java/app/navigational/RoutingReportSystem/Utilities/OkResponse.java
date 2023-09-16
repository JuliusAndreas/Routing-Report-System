package app.navigational.RoutingReportSystem.Utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class OkResponse implements CustomResponse {

    final private Integer status = HttpStatus.OK.value();

    private String message;

    private String timeStamp;

    public OkResponse(String message) {
        this.message = message;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss.SSS");
        this.timeStamp = dateFormat.format(new Date());
    }
}
