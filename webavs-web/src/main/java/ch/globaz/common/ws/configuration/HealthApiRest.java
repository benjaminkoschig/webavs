package ch.globaz.common.ws.configuration;

import ch.globaz.common.ws.HealthDto;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Slf4j
@Path("/infos")
@Produces(MediaType.APPLICATION_JSON)
public class HealthApiRest {

    @GET
    @Path("/health")
    public List<HealthDto> health() {
        return new ApiHealthCheckerService().checkApi();
    }

}
