package ch.globaz.common.ws.configuration;

import ch.globaz.common.ws.HealthDto;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
@Path("/infos")
@Produces(MediaType.APPLICATION_JSON)
public class HealthApiRest {

    @GET
    @Path("/health")
    public Response health() {
        return Response.ok(new ApiHealthCheckerService().checkApi())
                .build();
    }

    @GET
    @Path("/health1")
    public List<HealthDto> test1() {
        return new ApiHealthCheckerService().checkApi();
    }

    @GET
    @Path(value = "/healthv")
    public Response testv() {
        return Response.ok(new ApiHealthCheckerService().checkApi())
                .build();
    }

    @GET
    @Path(value = "/healthv2")
    public List<HealthDto> testv2() {
        return new ApiHealthCheckerService().checkApi();
    }

    @GET
    @Path("/health2")
    public Response health2() {
        return Response.ok(new ApiHealthCheckerService().checkApi())
                .build();
    }


    @GET
    @Path("/health3")
    @Produces(MediaType.TEXT_PLAIN)
    public String health3() {
        return "Test";
    }

    @GET
    public Response health1() {
        return Response.ok(new ApiHealthCheckerService().checkApi())
                .build();
    }
}
