package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * Service permettant la gestion des dates d'échéances
 * 
 * @author PTA
 * 
 */
public interface DatesEcheanceService extends JadeApplicationService {

    /**
     * Gestion du début de la validité d'un droit pour un droitComplexModel
     * 
     * @param droitComplexModel
     *            modèle selon le modèle du droit complexe
     * @param dateDebutDossier
     *            Date du début du dossier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @return date du début de validité
     */
    public String getDateDebutValiditeDroit(DroitComplexModel droitComplexModel, String dateDebutDossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Gestion de la fin d'un droit enfant (capable ou incapable d'exercer) ou de la fin d'un droit Formation pour un
     * droit soumis à la LAFAM
     * 
     * @param droitComplexModel
     *            le droit dont on veut calculer l'échéance
     * @return la date de fin de l'allocation
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getDateFinValiditeDroitCalculee(DroitComplexModel droitComplexModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Détermine la date de fin de validité d'un droit de formation anticipée.
     * 
     * @param droit
     *            le droit dont on veut calculer l'échéance
     * @return la date de fin de l'allocation
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getDateFinValiditeDroitCalculeeFormAnticipe(DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException;
}
