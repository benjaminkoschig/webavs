package ch.globaz.vulpecula.ws;

import globaz.jade.log.JadeLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.ws.Endpoint;
import ch.globaz.vulpecula.ws.services.DecompteEbuServiceImpl;
import ch.globaz.vulpecula.ws.services.EmployeurEbuServiceImpl;
import ch.globaz.vulpecula.ws.services.PosteTravailEbuServiceImpl;
import ch.globaz.vulpecula.ws.services.QualificationEbuServiceImpl;
import ch.globaz.vulpecula.ws.services.TravailleurEbuServiceImpl;

/**
 * Indique quels services exposer et à quel emplacement
 * 
 * @since eBMS 1.0
 */
public class VulpeculaPublisher {

    public void go(String protocole, String wsPort, boolean verbose) {
        try {
            JadeLogger.info(this, "I read VULPECULA's metierservice.ws.port property: [" + wsPort + "].");
            Integer.parseInt(wsPort);
        } catch (NumberFormatException e) {
            JadeLogger.error(this, "An exception raised while checking VULPECULA's metierservice.ws.port property: "
                    + e.toString());
            throw e;
        }

        try {
            Map<String, Endpoint> webServices = new HashMap<String, Endpoint>();
            String wsBaseLocation = "/webavs/ws";
            webServices.put(wsBaseLocation + "/decompteService", Endpoint.create(new DecompteEbuServiceImpl()));
            webServices.put(wsBaseLocation + "/posteTravailService", Endpoint.create(new PosteTravailEbuServiceImpl()));
            webServices.put(wsBaseLocation + "/employeurService", Endpoint.create(new EmployeurEbuServiceImpl()));
            webServices.put(wsBaseLocation + "/travailleurService", Endpoint.create(new TravailleurEbuServiceImpl()));
            webServices.put(wsBaseLocation + "/qualificationService",
                    Endpoint.create(new QualificationEbuServiceImpl()));

            if (verbose) {
                System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
                System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
                System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
                System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
            }

            // publish all WS
            if (webServices.entrySet().size() > 0) {
                JadeLogger.info(this, String.format("Publishing %d Web Service(s)", webServices.entrySet().size()));
                String hostIP = "0.0.0.0";
                String baseLocation = " " + protocole + "://" + hostIP + ":" + wsPort;

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
