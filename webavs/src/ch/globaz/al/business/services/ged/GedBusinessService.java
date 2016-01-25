package ch.globaz.al.business.services.ged;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * Services li� � la GED
 * 
 * @author pta
 * 
 */
public interface GedBusinessService extends JadeApplicationService {
    /**
     * m�thode fournissant le type de de sous-dossier en fonction du statut du dossier ou de l'activit� de l'allocataire
     * 
     * @param dossierModel
     *            qui permet de r�cup�rer le statut du dossier et l'activit� de l'allocataire
     * @return Sting correspondant au type de sous-dossier
     */
    public String getTypeSousDossier(DossierModel dossierModel);

    /**
     * Propage les donn�es d'un allocataire vers la GED
     * 
     * @param dossier
     *            le dossier
     * @throws JadeApplicationException
     *             Exception lev�e si l'allocataire n'a pas pu �tre propag�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void propagateAllocataire(DossierModel dossier) throws JadeApplicationException, JadePersistenceException;

    /**
     * Cette signature est exclusive � la FPV
     * Propage les donn�es d'un tiers avec ayant DROIT CAF vers la GED.
     * 
     * @param droit
     *            le droit
     * @throws JadeApplicationException
     *             Exception lev�e si l'allocataire n'a pas pu �tre propag�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void propagateDroitForGEDFPV(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Cette signature est exclusive � la FPV
     * Propage les donn�es d'un tiers � la cr�ation d'un dossier AF
     * 
     * @param droit
     *            le droit
     * @throws JadeApplicationException
     *             Exception lev�e si l'allocataire n'a pas pu �tre propag�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void propagateDossierForGEDFPV(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;
}
