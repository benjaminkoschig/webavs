package ch.globaz.al.business.services.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;

/**
 * Service d'importation des données de prestations
 * 
 * @author jts
 * 
 */
public interface ImportationPrestationService extends JadeApplicationService {

    /**
     * Importation d'un détail de prestation
     * 
     * @param detailPrestation
     *            detail d'une prestation
     * @param idEntetePrestation
     *            identifiant entetePrestation
     * @param idDroit
     *            identifiant du droit
     * @param droitComplexMap
     *            Liste de droits déjà traité composée de couple id du droit => modèle complexe du droit
     * @return detailPrestation
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel importDetailPrestation(DetailPrestationModel detailPrestation,
            String idEntetePrestation, String idDroit, HashMap<String, DroitComplexModel> droitComplexMap)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation d'une entête de prestation
     * 
     * @param entetePrestation
     *            en-tête de prestation
     * @param idDossier
     *            identifiant du dossier pour l'entetête
     * @param idRecap
     *            identifiant de la récapitulatif entreprise pour l'entête de prestation
     * @return entetePrestation
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel importEntetePrestation(EntetePrestationModel entetePrestation, String idDossier,
            String idRecap) throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation d'un récapitulatif entreprise
     * 
     * @param recapEntrepImporte
     *            liste des récapitulatif entreprise
     * @return recapImport
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel importRecapEntreprise(RecapitulatifEntrepriseModel recapEntrepImporte)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation d'un tucana Transfert
     * 
     * 
     * @param idDetailPrestation
     *            identifiant du détail de la prestation
     * @param transfertTucana
     *            modèle de base de transfertTucan identifiant du détail de la prestation
     * @return transfertTucana
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel importTransfertTucana(TransfertTucanaModel transfertTucana, String idDetailPrestation)
            throws JadeApplicationException, JadePersistenceException;
}
