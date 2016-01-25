package ch.globaz.al.business.services.adiDecomptes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Interface d�finissant les m�thodes communes � l'�tablissement de document d'attestation des d�comptes ADI
 * 
 * @author PTA
 * 
 */
public interface AdiDecompteService extends JadeApplicationService {

    /**
     * G�n�re un document de d�compte ADI
     * 
     * 
     * @param adiDecompte
     *            Mod�le de l'adi
     * @param periode
     *            p�riode
     * @param typeDecompte
     *            type de d�compte
     * @param doc
     *            Document auxquel ajouter les donn�es
     * @return DocumentData
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData getDocument(AdiDecompteComplexModel adiDecompte, String typeDecompte, DocumentData doc,
            String langueDocument) throws JadeApplicationException, JadePersistenceException;

}
