package globaz.prestation.acor.web;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.ws.ApiHealthChecker;
import ch.globaz.common.ws.HealthDto;
import ch.globaz.common.ws.ResponseEntity;
import globaz.prestation.acor.web.ws.AcorTokenServiceAbstract;

import javax.ws.rs.core.Response;
import java.util.Objects;

public class AcorWebHealthChecker implements ApiHealthChecker {
    @Override
    public HealthDto check() {
        String apiPath = AcorTokenServiceAbstract.getAcorBaseUrl()+"/healthCheck";
        ResponseEntity<HealthDto> response = get(apiPath);
        if (response != null && Objects.equals(Response.Status.OK.getStatusCode(), response.getStatus())) {
            return response.getEntity().setState("UP").setService("WebAcor");
        } else {
            return new HealthDto().setState("DOWN").setService("WebAcor");
        }
    }

    @Override
    public boolean isCheckable() {
        return CommonProperties.ACOR_NAVIGATEUR.exist() && CommonProperties.ACOR_BACKEND_PATH.exist();
    }
}
