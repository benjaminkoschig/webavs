package ch.globaz.al.business.services.signatures;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface SignatureService extends JadeApplicationService {

    /**
     * 
     * @param nomCaisse
     *            nom de la caisse
     * @param activiteAllocataire
     *            activit� de l'allocataire
     * @param typeDocument
     *            type de document
     * @param date
     * @return libelle de la signature de la caisse pass� en param�tre
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    String getLibelleSignature(/* String nomCaisse, */String activiteAllocataire, String typeDocument)
            throws JadeApplicationException, JadePersistenceException;

}
