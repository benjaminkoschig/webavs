package ch.globaz.common.ws;

import ch.globaz.common.ws.configuration.JacksonReader;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Cette interface permet d'identifier les classes qui permettent de vérifier si l'api fonctionne bien.
 */
public interface ApiHealthChecker {

    HealthDto check();

    default Response getHealth(final String apiPath) {
        return ClientBuilder.newClient()
                            .target(apiPath)
                            .path("/health")
                            .register(JacksonReader.class)
                            .request(MediaType.APPLICATION_JSON).get();
    }
}
