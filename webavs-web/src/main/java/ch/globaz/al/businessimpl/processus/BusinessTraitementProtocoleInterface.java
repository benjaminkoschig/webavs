package ch.globaz.al.businessimpl.processus;

import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import ch.globaz.al.business.loggers.ProtocoleLogger;

/**
 * 
 * Interface d�finissant le pr�-requis pour un traitement fournissant un protocole
 * 
 * @author GMO
 * 
 */
public interface BusinessTraitementProtocoleInterface {

    /**
     * @return JadePrintDocumentContainer le protocole m�tier li� au traitement
     */
    public JadePrintDocumentContainer[] getProtocole();

    /**
     * @return Logger contenant les �ventuelles erreur survenues pendant le traitement
     */
    public ProtocoleLogger getProtocoleLogger();

    /**
     * @return Liste de protocoles CSV
     */
    public ArrayList<String> getProtocolesCSV();

    /**
     * @return pubInfo � utiliser pour l'envoi de protocoles
     */
    public JadePublishDocumentInfo getPubInfo();

}
