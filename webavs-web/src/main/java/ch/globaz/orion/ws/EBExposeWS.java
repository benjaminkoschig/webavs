package ch.globaz.orion.ws;

import globaz.jade.log.JadeLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.ws.Endpoint;
import ch.globaz.orion.ws.affiliation.WebAvsAffiliationServiceImpl;
import ch.globaz.orion.ws.comptabilite.WebAvsComptabiliteServiceImpl;
import ch.globaz.orion.ws.cotisation.WebAvsCotisationsServiceImpl;

public class EBExposeWS {

    public void go(String wsPort) {
        // inspired by ch.globaz.shared.server.ServerRegistryHelper.publishWebServices(String, Server)
        try {
            JadeLogger.info(this, "I read ORION's cotiservice.ws.port property: [" + wsPort + "].");
            Integer.parseInt(wsPort);
        } catch (NumberFormatException e) {
            JadeLogger.error(this,
                    "An exception raised while checking ORION's cotiservice.ws.port property: " + e.toString());
            throw e;
        }
        try {
            Map<String, Endpoint> webServices = new HashMap<String, Endpoint>();
            String wsBaseLocation = "/webavs/ws";
            webServices.put(wsBaseLocation + "/webAvsCotisationsService",
                    Endpoint.create(new WebAvsCotisationsServiceImpl()));
            webServices.put(wsBaseLocation + "/webAvsAffiliationService",
                    Endpoint.create(new WebAvsAffiliationServiceImpl()));
            webServices.put(wsBaseLocation + "/webAvsComptabiliteService",
                    Endpoint.create(new WebAvsComptabiliteServiceImpl()));

            // publish all WS
            if (webServices.entrySet().size() > 0) {
                JadeLogger.info(this, String.format("Publishing %d Web Service(s)", webServices.entrySet().size()));
                String hostIP = "0.0.0.0";
                String baseLocation = " http://" + hostIP + ":" + wsPort;
                for (Entry<String, Endpoint> e : webServices.entrySet()) {

                    Object wsImpl = e.getValue().getImplementor();

                    String location = baseLocation + e.getKey();
                    JadeLogger.info(this, String.format("Starting web service %s.", location));

                    Endpoint endpoint = e.getValue();
                    endpoint = Endpoint.create(wsImpl);
                    endpoint.publish(location);
                    JadeLogger.info(this, String.format("Web Service started on %s?wsdl", location));
                }
            } else {
                JadeLogger.info(this, "No Web Service to publish");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JadeLogger.warn(this,
                    "An exception raised while starting WS. You can ignore this message on a job/publish/merge serveur. Exception: "
                            + e.toString());
        }
    }
}
