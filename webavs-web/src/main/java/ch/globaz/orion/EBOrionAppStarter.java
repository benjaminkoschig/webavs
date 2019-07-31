package ch.globaz.orion;

import ch.globaz.naos.ws.AFExposeWS;
import globaz.jade.client.xml.JadeXmlException;
import globaz.jade.client.xml.JadeXmlUtil;
import globaz.jade.common.JadeInitializationException;
import globaz.jade.log.JadeLogger;
import globaz.jade.patterns.JadeServiceContainer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ch.globaz.orion.ws.EBExposeWS;

public class EBOrionAppStarter extends JadeServiceContainer {
    // singleton
    private static EBOrionAppStarter singleton = null;
    String port = null;

    // static instantiation
    static {
        EBOrionAppStarter.singleton = new EBOrionAppStarter();
    }

    @Override
    public synchronized void initialize(Node node) throws JadeInitializationException {
        super.initialize(node);
        if (!Element.class.isAssignableFrom(node.getClass())) {
            throw new JadeInitializationException("cannot initialize: the received Node ain't an XML Element");
        }
        final Element element = (Element) node;

        // Exposition des WebServices
        try {
            port = JadeXmlUtil.readTagValue(element, "orion.cotiservice.ws.port");
        } catch (JadeXmlException e) {
            JadeLogger.error(this,
                    "EBOrionAppStarter service is present, but no no tag [orion.cotiservice.ws.port] could be found.");
            throw new JadeInitializationException(
                    "EBOrionAppStarter service is present, but no no tag [orion.cotiservice.ws.port] could be found.",
                    e);
        }
    }

    @Override
    public synchronized void start() throws JadeInitializationException {
        if (port != null) {
            new EBExposeWS().go(port);
            new AFExposeWS().go(port);
        } else {
            throw new JadeInitializationException("property [orion.cotiservice.ws.port] is null");
        }
    }

    public static EBOrionAppStarter getInstance() {
        return EBOrionAppStarter.singleton;
    }
}
