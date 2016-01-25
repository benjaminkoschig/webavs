package ch.globaz.al.businessimpl.processus;

import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import ch.globaz.al.business.loggers.ProtocoleLogger;

/**
 * 
 * Interface définissant le pré-requis pour un traitement fournissant un protocole
 * 
 * @author GMO
 * 
 */
public interface BusinessTraitementProtocoleInterface {

    /**
     * @return JadePrintDocumentContainer le protocole métier lié au traitement
     */
    public JadePrintDocumentContainer[] getProtocole();

    /**
     * @return Logger contenant les éventuelles erreur survenues pendant le traitement
     */
    public ProtocoleLogger getProtocoleLogger();

    /**
     * @return Liste de protocoles CSV
     */
    public ArrayList<String> getProtocolesCSV();

    /**
     * @return pubInfo à utiliser pour l'envoi de protocoles
     */
    public JadePublishDocumentInfo getPubInfo();

}
