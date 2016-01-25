package ch.globaz.al.business.services.ged;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * Services lié à la GED
 * 
 * @author pta
 * 
 */
public interface GedBusinessService extends JadeApplicationService {
    /**
     * méthode fournissant le type de de sous-dossier en fonction du statut du dossier ou de l'activité de l'allocataire
     * 
     * @param dossierModel
     *            qui permet de récupérer le statut du dossier et l'activité de l'allocataire
     * @return Sting correspondant au type de sous-dossier
     */
    public String getTypeSousDossier(DossierModel dossierModel);

    /**
     * Propage les données d'un allocataire vers la GED
     * 
     * @param dossier
     *            le dossier
     * @throws JadeApplicationException
     *             Exception levée si l'allocataire n'a pas pu être propagé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void propagateAllocataire(DossierModel dossier) throws JadeApplicationException, JadePersistenceException;

    /**
     * Cette signature est exclusive à la FPV
     * Propage les données d'un tiers avec ayant DROIT CAF vers la GED.
     * 
     * @param droit
     *            le droit
     * @throws JadeApplicationException
     *             Exception levée si l'allocataire n'a pas pu être propagé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void propagateDroitForGEDFPV(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Cette signature est exclusive à la FPV
     * Propage les données d'un tiers à la création d'un dossier AF
     * 
     * @param droit
     *            le droit
     * @throws JadeApplicationException
     *             Exception levée si l'allocataire n'a pas pu être propagé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void propagateDossierForGEDFPV(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;
}
