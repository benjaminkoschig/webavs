package ch.globaz.al.business.services.adiDecomptes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Interface définissant les méthodes communes à l'établissement de document d'attestation des décomptes ADI
 * 
 * @author PTA
 * 
 */
public interface AdiDecompteService extends JadeApplicationService {

    /**
     * Génére un document de décompte ADI
     * 
     * 
     * @param adiDecompte
     *            Modèle de l'adi
     * @param periode
     *            période
     * @param typeDecompte
     *            type de décompte
     * @param doc
     *            Document auxquel ajouter les données
     * @return DocumentData
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData getDocument(AdiDecompteComplexModel adiDecompte, String typeDecompte, DocumentData doc,
            String langueDocument) throws JadeApplicationException, JadePersistenceException;

}
