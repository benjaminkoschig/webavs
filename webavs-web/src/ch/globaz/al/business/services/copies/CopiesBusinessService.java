package ch.globaz.al.business.services.copies;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.CopieSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;

/**
 * Service de gestion des copies de document (ex : décisions).
 * 
 * @author jts
 * 
 */
public interface CopiesBusinessService extends JadeApplicationService {

    /**
     * Vérifie si le dossier a déjà des copies pour le type de copie passé en paramètre. Si ce n'est pas le cas les
     * copies par défaut sont enregistrées
     * 
     * @param dossier
     *            DossierComplexModel du dossier allocataire
     * @param typeCopie
     *            Type de copie {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void createDefaultCopies(DossierComplexModel dossier, String typeCopie) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les copies pour un dossier.
     * 
     * @param idDossier
     *            id du dossier
     * @param type
     *            type de copie à supprimer
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteForDossier(String idDossier, String type) throws JadePersistenceException;

    /**
     * Retourne le libellé de la copie selon le type de dossier, l'ordre et le destinataire de la copie
     * 
     * @param dossier
     *            le dossier générant la décision
     * @param idTiersDestinataire
     *            l'id tiers du destinataire de la copie
     * 
     * @param typeCopie
     *            le type de copie {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * @return le libellé
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getLibelleCopie(DossierComplexModel dossier, String idTiersDestinataire, String typeCopie)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Recherche les copies pour un dossier.
     * 
     * @param idDossier
     *            id du dossier
     * @param type
     *            type de copie à rechercher
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieSearchModel searchForDossier(String idDossier, String type) throws JadePersistenceException;
}
