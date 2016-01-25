package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;

/**
 * Service de gestion de la persistance d'un dossier complexe agricole
 * 
 * @author jts
 */
public interface DossierAgricoleComplexModelService extends JadeApplicationService {

    /**
     * Enregistre <code>dossierComplexModel</code> en persistance
     * 
     * @param dossierComplexModel
     *            Dossier � enregistrer en persistance
     * @return Le mod�le ajout� en persistance
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public DossierAgricoleComplexModel create(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime <code>dossierComplexModel</code> de la persistance
     * 
     * @param dossierComplexModel
     *            Dossier � supprimer de la persistance
     * @return Le dossier supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public DossierAgricoleComplexModel delete(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re le dossier correspondant � <code>idDossier</code> depuis la persistance
     * 
     * @param idDossier
     *            Id du dossier � charger
     * 
     * @return Le mod�le du dossier charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public DossierAgricoleComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour <code>dossierComplexModel</code> en persistance
     * 
     * @param dossierComplexModel
     *            Dossier � mettre � jour
     * 
     * @return Le dossier mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public DossierAgricoleComplexModel update(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException;
}
