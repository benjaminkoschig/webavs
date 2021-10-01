package globaz.prestation.acor.web.ws;

import ch.globaz.common.ws.ApiHealthChecker;
import ch.globaz.common.ws.HealthDto;
import ch.globaz.common.ws.ResponseEntity;
import globaz.prestation.acor.web.AcorApiRest;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import java.util.Objects;

@Slf4j
@SuppressWarnings("unused" /*Cette class est appelé par réflexion par la class WSConfiguration */)
public class AcorApiHealthChecker implements ApiHealthChecker {
    @Override
    public HealthDto check() {
        String apiPath = AcorTokenServiceAbstract.createApiPath(AcorApiRest.class);
        ResponseEntity<HealthDto> response = getHealth(apiPath);
        if (response != null && Objects.equals(Response.Status.OK.getStatusCode(), response.getStatus())) {
            return response.getEntity().setState("UP").setService("acor");
        } else {
            LOG.error("The service REST for ACOR is down. Check the web.xml or the ACOR back property. " +
                              "The property is used to create this uri {}:", apiPath + "/health");
            return new HealthDto().setState("DOWN").setService("acor");
        }
    }
}
