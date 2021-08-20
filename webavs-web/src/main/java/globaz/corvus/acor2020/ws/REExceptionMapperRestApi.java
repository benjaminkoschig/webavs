package globaz.corvus.acor2020.ws;

import acor.rentes.xsd.standard.error.OriginType;
import ch.globaz.common.acor.Acor2020StandardErrorUtil;
import ch.globaz.common.ws.ExceptionHandler;
import ch.globaz.common.ws.ExceptionMapper;
import globaz.prestation.acor.PRACORException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@ExceptionMapper("acor2020")
public class REExceptionMapperRestApi implements ExceptionHandler {
    @Override
    public Response generateResponse(final Exception e, final Response.ResponseBuilder responseBuilder, final HttpServletRequest request) {
        if (e instanceof PRACORException) {
            return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(e.getMessage(), e, 2, OriginType.TECHNICAL_EXPORT))
                    .build();
        } else {
            return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(Acor2020StandardErrorUtil.ERROR_ACOR_EXTERN_IMPORT_UNKOWN, e, 1, OriginType.TECHNICAL_IMPORT))
                    .build();
        }
    }
}
