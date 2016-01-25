package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstCaisse;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentAGLSService;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentCCJUService;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentCCVDService;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentCICIService;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentFVEService;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH510Service;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH513Service;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH514Service;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH515Service;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH517Service;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH51XService;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentService;

/**
 * Classe permettant de ddéfinir quel type de service à utiliser pour récupérer l'entete d'une caisse d'une caisse
 * 
 * @author PTA
 * 
 */

public abstract class EnteteDocumentFactory {
    /**
     * Liste des service disponible en fonction de la caisse
     */

    private static HashMap<String, Class<?>> enteteMap;

    static {
        EnteteDocumentFactory.enteteMap = new HashMap<String, Class<?>>();
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_CCJU, EnteteDocumentCCJUService.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_H51X, EnteteDocumentH51XService.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_CCVD, EnteteDocumentCCVDService.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_H515, EnteteDocumentH515Service.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_CICI, EnteteDocumentCICIService.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_H510, EnteteDocumentH510Service.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_AGLS, EnteteDocumentAGLSService.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_H514, EnteteDocumentH514Service.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_H513, EnteteDocumentH513Service.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_H517, EnteteDocumentH517Service.class);
        EnteteDocumentFactory.enteteMap.put(ALConstCaisse.CAISSE_FVE, EnteteDocumentFVEService.class);

    }

    /**
     * Récupère le service de signature en fonction du nom de la caisse récupéré
     * 
     * @param date
     *            Date pour laquelle récupérer le service
     * @return Classe à instancier
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static Class getServiceEnteteDocument() throws JadeApplicationException, JadePersistenceException {

        String caisse = ALServiceLocator.getParametersServices().getNomCaisse();

        if (EnteteDocumentFactory.enteteMap.containsKey(caisse)) {
            return EnteteDocumentFactory.enteteMap.get(caisse);
        } else {
            return EnteteDocumentService.class;
        }
    }

}
