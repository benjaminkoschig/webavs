package ch.globaz.naos.ws;

import ch.globaz.naos.ws.contact.WebAvsContactServiceImpl;
import globaz.jade.log.JadeLogger;

import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AFExposeWS {
    private static final String WEBAVS_CONTACT_SERVICE = "/webAvsContactService";
    private static final String WS_BASE_LOCATION = "/webavs/ws";

    private static final String IP_0_0_0_0 = "0.0.0.0";

    public void go(String wsPort) {
        checkWsPort(wsPort);

        try {
            Map<String, Endpoint> webServices = new HashMap<String, Endpoint>();
            String wsBaseLocation = WS_BASE_LOCATION;
            webServices.put(wsBaseLocation + WEBAVS_CONTACT_SERVICE,
                    Endpoint.create(new WebAvsContactServiceImpl()));

            // publish all WS
            if (!webServices.entrySet().isEmpty()) {
                JadeLogger.info(this, String.format("Publishing %d Web Service(s)", webServices.entrySet().size()));
                String hostIP = IP_0_0_0_0;
                String baseLocation = " http://" + hostIP + ":" + wsPort;
                for (Entry<String, Endpoint> webService : webServices.entrySet()) {
                    Object wsImpl = webService.getValue().getImplementor();
                    String location = baseLocation + webService.getKey();
                    JadeLogger.info(this, String.format("Starting web service %s.", location));

                    Endpoint endpoint = Endpoint.create(wsImpl);
                    endpoint.publish(location);
                    JadeLogger.info(this, String.format("Web Service started on %s?wsdl", location));
                }
            } else {
                JadeLogger.info(this, "No Web Service to publish");
            }
        } catch (Exception e) {
            JadeLogger.warn(this,
                    "An exception raised while starting WS. You can ignore this message on a job/publish/merge serveur. Exception: "
                            + e.toString());
        }
    }

    private void checkWsPort(String wsPort) {
        try {
            Integer.parseInt(wsPort);
        } catch (NumberFormatException e) {
            JadeLogger.error(this,
                    "An exception raised while checking ORION's cotiservice.ws.port property: " + e.toString());
            throw e;
        }
    }
}
