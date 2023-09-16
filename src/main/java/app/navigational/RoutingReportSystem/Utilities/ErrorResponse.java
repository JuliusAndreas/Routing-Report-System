package app.navigational.RoutingReportSystem.Utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse implements CustomResponse{

    private Integer status;

    private String message;

    private String timeStamp;

    public void updateTimeStamp() {
        if (timeStamp != null) return;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss.SSS");
        this.timeStamp = dateFormat.format(new Date());
    }
}
