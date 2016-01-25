/**
 * 
 */
package ch.globaz.al.business.services.entetesDocument.enteteDocument;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service permettant de récupérer l'entête d'une caisse d'une caisse destinée à une template
 * 
 * @author PTA
 * 
 */
public interface EnteteDocumentService extends JadeApplicationService {

    /**
     * Détermine et retourne l'entête à utiliser en fonction des paramètres
     * 
     * @param acvitieAllocataire
     *            activité de l'allocataire du dossier pour lequel le document va être imprimé (paramètre facultatif)
     * @param typeDocument
     *            type de document pour lequel l'en-tête doit être déterminée
     * @param langue
     *            Langue dans laquelle l'en-tête doit être récupérée
     * @return id de l'en-tête à utiliser (voir topaz-config.xml)
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException;

}
