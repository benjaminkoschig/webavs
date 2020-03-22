package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * Service permettant la gestion des dates d'�ch�ances
 * 
 * @author PTA
 * 
 */
public interface DatesEcheanceService extends JadeApplicationService {

    /**
     * Gestion du d�but de la validit� d'un droit pour un droitComplexModel
     * 
     * @param droitComplexModel
     *            mod�le selon le mod�le du droit complexe
     * @param dateDebutDossier
     *            Date du d�but du dossier
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @return date du d�but de validit�
     */
    public String getDateDebutValiditeDroit(DroitComplexModel droitComplexModel, String dateDebutDossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Gestion de la fin d'un droit enfant (capable ou incapable d'exercer) ou de la fin d'un droit Formation pour un
     * droit soumis � la LAFAM
     * 
     * @param droitComplexModel
     *            le droit dont on veut calculer l'�ch�ance
     * @return la date de fin de l'allocation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getDateFinValiditeDroitCalculee(DroitComplexModel droitComplexModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * D�termine la date de fin de validit� d'un droit de formation anticip�e.
     * 
     * @param droit
     *            le droit dont on veut calculer l'�ch�ance
     * @return la date de fin de l'allocation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getDateFinValiditeDroitCalculeeFormAnticipe(DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException;
}
