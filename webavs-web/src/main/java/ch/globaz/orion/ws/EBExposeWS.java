package ch.globaz.orion.ws;

import globaz.jade.log.JadeLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.ws.Endpoint;
import ch.globaz.orion.ws.affiliation.WebAvsAffiliationServiceImpl;
import ch.globaz.orion.ws.allocationfamiliale.WebAvsAllocationFamilialeServiceImpl;
import ch.globaz.orion.ws.common.WebAvsCommonServiceImpl;
import ch.globaz.orion.ws.comptabilite.WebAvsComptabiliteServiceImpl;
import ch.globaz.orion.ws.cotisation.WebAvsCotisationsServiceImpl;

public class EBExposeWS {
    private static final String WEBAVS_COMMON_SERVICE = "/webAvsCommonService";
    private static final String WEBAVS_COMPTABILITE_SERVICE = "/webAvsComptabiliteService";
    private static final String WEBAVS_AFFILIATION_SERVICE = "/webAvsAffiliationService";
    private static final String WEBAVS_COTISATIONS_SERVICE = "/webAvsCotisationsService";
    private static final String WEBAVS_ALLOCATION_FAMILIALE_SERVICE = "/webAvsAllocationFamilialeService";
    private static final String WS_BASE_LOCATION = "/webavs/ws";
    private static final String IP_0_0_0_0 = "0.0.0.0";

    public void go(String wsPort) {
        JadeLogger.info(this, "I read ORION's cotiservice.ws.port property: [" + wsPort + "].");
        checkWsPort(wsPort);

        try {
            Map<String, Endpoint> webServices = new HashMap<String, Endpoint>();
            String wsBaseLocation = WS_BASE_LOCATION;
            webServices.put(wsBaseLocation + WEBAVS_COTISATIONS_SERVICE,
                    Endpoint.create(new WebAvsCotisationsServiceImpl()));
            webServices.put(wsBaseLocation + WEBAVS_AFFILIATION_SERVICE,
                    Endpoint.create(new WebAvsAffiliationServiceImpl()));
            webServices.put(wsBaseLocation + WEBAVS_COMPTABILITE_SERVICE,
                    Endpoint.create(new WebAvsComptabiliteServiceImpl()));
            webServices.put(wsBaseLocation + WEBAVS_COMMON_SERVICE, Endpoint.create(new WebAvsCommonServiceImpl()));
            webServices.put(wsBaseLocation + WEBAVS_ALLOCATION_FAMILIALE_SERVICE,
                    Endpoint.create(new WebAvsAllocationFamilialeServiceImpl()));

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
