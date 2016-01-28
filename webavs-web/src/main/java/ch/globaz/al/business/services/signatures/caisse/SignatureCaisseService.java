package ch.globaz.al.business.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service générique permettant de récupérer la signature d'une caisse destinée à une template
 * 
 * @author PTA
 * 
 */

public interface SignatureCaisseService extends JadeApplicationService {
    /**
     * 
     * @param nomCaisse
     *            nom de la caisse
     * @param activiteAllocataire
     *            activité de l'allocataire
     * @param typeDocument
     *            type de document
     * @return libelle de la signature de la caisse passé en paramètre
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    String getLibelleSignature(/* String nomCaisse, */String activiteAllocataire, String typeDocument)
            throws JadeApplicationException, JadePersistenceException;
}
