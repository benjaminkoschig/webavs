package globaz.prestation.acor.web;

import ch.globaz.common.ws.HealthDto;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Slf4j
@Path("/acor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AcorApiRest {

    private static final LocalDateTime START_TIME = LocalDateTime.now();

    @GET
    @Path(value = "/health")
    public Response check() {
        HealthDto healthDto = new HealthDto().setUpSince(START_TIME);
        return Response.ok(healthDto).build();
    }
}
