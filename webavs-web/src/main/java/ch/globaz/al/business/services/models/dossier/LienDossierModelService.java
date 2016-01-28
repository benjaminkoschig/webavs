package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.LienDossierModel;
import ch.globaz.al.business.models.dossier.LienDossierSearchModel;

public interface LienDossierModelService extends JadeApplicationService {

    /**
     * Enregistre <code>lienDossierModel</code> en persistance
     * 
     * @param lienDossierModel
     *            lien � enregistrer en persistance
     * 
     * @return LienDossierModel Le lien enregistr�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.LienDossierModel
     */
    public LienDossierModel create(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>lienDossierModel</code> de la persistance
     * 
     * @param lienDossierModel
     *            lien � supprimer de la persistance
     * 
     * @return LienDossierModel Le lien supprim�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.LienDossierModel
     */
    public LienDossierModel delete(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * R�cup�re les donn�es du lien correspondant � <code>idLienDossierModel</code>
     * 
     * @param idLienDossierModel
     *            Id du lien dossier � charger
     * 
     * @return LienDossierModel Le mod�le du lien charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.LienDossierModel
     */
    public LienDossierModel read(String idLienDossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            mod�le de recherche de lien dossier
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LienDossierSearchModel search(LienDossierSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met � jour <code>dossierModel</code> en persistance
     * 
     * @param dossierModel
     *            Dossier � mettre � jour
     * 
     * @return DossierModel Le dossier mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.LienDossierModel
     */
    public LienDossierModel update(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException;

}
