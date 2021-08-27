package ch.globaz.common.ws.configuration;

import com.google.common.base.Throwables;
import lombok.Data;

/**
 * Cette à pour but d'être un dto et de remonter des informations sur une exception.
 */
@Data
public class ExceptionRequestInfo {

    private final RequestInfo requestInfo;
    private final String stackTrace;

    public ExceptionRequestInfo(final RequestInfo requestInfo, final Exception exception) {
        this.stackTrace = Throwables.getStackTraceAsString(exception);
        this.requestInfo = requestInfo;
    }
}
