package ch.globaz.vulpecula;

import globaz.jade.client.xml.JadeXmlException;
import globaz.jade.client.xml.JadeXmlUtil;
import globaz.jade.common.JadeInitializationException;
import globaz.jade.log.JadeLogger;
import globaz.jade.patterns.JadeServiceContainer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ch.globaz.vulpecula.ws.VulpeculaPublisher;

public class VulpeculaAppStarter extends JadeServiceContainer {
    // singleton
    private static VulpeculaAppStarter singleton = null;
    String protocole = null;
    String port = null;
    boolean verbose = false;
    // static instantiation
    static {
        VulpeculaAppStarter.singleton = new VulpeculaAppStarter();
    }

    @Override
    public synchronized void initialize(Node node) throws JadeInitializationException {
        super.initialize(node);
        if (!Element.class.isAssignableFrom(node.getClass())) {
            throw new JadeInitializationException("cannot initialize: the received Node ain't an XML Element");
        }
        final Element element = (Element) node;
        // Exposition des Webservices
        try {
            port = JadeXmlUtil.readTagValue(element, "vulpecula.metierservice.ws.port");
            protocole = JadeXmlUtil.readTagValue(element, "vulpecula.metierservice.ws.protocole");

            verbose = Boolean.parseBoolean(element.getAttribute("verbose"));
        } catch (JadeXmlException e) {
            JadeLogger
                    .error(this,
                            "VulpeculaAppStarter service is present, but no no tag [vulpecula.metierservice.ws.port] could be found.");
            throw new JadeInitializationException(
                    "VulpeculaAppStarter service is present, but no no tag [vulpecula.metierservice.ws.port] could be found.");
        }
    }

    @Override
    public synchronized void start() throws JadeInitializationException {
        // Permet de gérer les exception des services web
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
        // System.setProperty(JAXBContext.class.getName(), ContextFactory.class.getName());

        if (port != null && protocole != null) {
            new VulpeculaPublisher().go(protocole, port, verbose);
        } else {
            throw new JadeInitializationException(
                    "property [vulpecula.metierservice.ws.port] or [vulpecula.metierservice.ws.protocole] is null");
        }
    }

    public static VulpeculaAppStarter getInstance() {
        return VulpeculaAppStarter.singleton;
    }
}
