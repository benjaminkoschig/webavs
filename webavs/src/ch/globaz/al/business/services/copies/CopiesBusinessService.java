package ch.globaz.al.business.services.copies;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.CopieSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;

/**
 * Service de gestion des copies de document (ex : d�cisions).
 * 
 * @author jts
 * 
 */
public interface CopiesBusinessService extends JadeApplicationService {

    /**
     * V�rifie si le dossier a d�j� des copies pour le type de copie pass� en param�tre. Si ce n'est pas le cas les
     * copies par d�faut sont enregistr�es
     * 
     * @param dossier
     *            DossierComplexModel du dossier allocataire
     * @param typeCopie
     *            Type de copie {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void createDefaultCopies(DossierComplexModel dossier, String typeCopie) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les copies pour un dossier.
     * 
     * @param idDossier
     *            id du dossier
     * @param type
     *            type de copie � supprimer
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteForDossier(String idDossier, String type) throws JadePersistenceException;

    /**
     * Retourne le libell� de la copie selon le type de dossier, l'ordre et le destinataire de la copie
     * 
     * @param dossier
     *            le dossier g�n�rant la d�cision
     * @param idTiersDestinataire
     *            l'id tiers du destinataire de la copie
     * 
     * @param typeCopie
     *            le type de copie {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * @return le libell�
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getLibelleCopie(DossierComplexModel dossier, String idTiersDestinataire, String typeCopie)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Recherche les copies pour un dossier.
     * 
     * @param idDossier
     *            id du dossier
     * @param type
     *            type de copie � rechercher
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieSearchModel searchForDossier(String idDossier, String type) throws JadePersistenceException;
}
