package ch.globaz.common.ws;

import ch.globaz.common.ws.configuration.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Cette interface permet d'identifier les classes qui permettent de vérifier si l'api fonctionne bien.
 */
public interface ApiHealthChecker {

    HealthDto check();

    default ResponseEntity<HealthDto> getHealth(final String apiPath) {
        try {
            URL url = new URL(apiPath + "/health");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            HealthDto healthDto = new HealthDto();
            if (con.getResponseCode() <= 400) {
                healthDto = JacksonJsonProvider.getInstance().readValue(con.getInputStream(), HealthDto.class);
            }
            ResponseEntity<HealthDto> responsEntity = ResponseEntity.of(con.getResponseCode(), healthDto);
            con.disconnect();
            return responsEntity;
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger(ApiHealthChecker.class);
            logger.error("Impossible to check this api :" + apiPath, e);
        }
        return ResponseEntity.ofKo();
    }
}
