/**
 * 
 */
package ch.globaz.al.business.services.entetesDocument.enteteDocument;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service permettant de r�cup�rer l'ent�te d'une caisse d'une caisse destin�e � une template
 * 
 * @author PTA
 * 
 */
public interface EnteteDocumentService extends JadeApplicationService {

    /**
     * D�termine et retourne l'ent�te � utiliser en fonction des param�tres
     * 
     * @param acvitieAllocataire
     *            activit� de l'allocataire du dossier pour lequel le document va �tre imprim� (param�tre facultatif)
     * @param typeDocument
     *            type de document pour lequel l'en-t�te doit �tre d�termin�e
     * @param langue
     *            Langue dans laquelle l'en-t�te doit �tre r�cup�r�e
     * @return id de l'en-t�te � utiliser (voir topaz-config.xml)
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException;

}
