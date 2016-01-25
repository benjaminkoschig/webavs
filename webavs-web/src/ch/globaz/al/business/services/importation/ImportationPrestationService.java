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
 * Service d'importation des donn�es de prestations
 * 
 * @author jts
 * 
 */
public interface ImportationPrestationService extends JadeApplicationService {

    /**
     * Importation d'un d�tail de prestation
     * 
     * @param detailPrestation
     *            detail d'une prestation
     * @param idEntetePrestation
     *            identifiant entetePrestation
     * @param idDroit
     *            identifiant du droit
     * @param droitComplexMap
     *            Liste de droits d�j� trait� compos�e de couple id du droit => mod�le complexe du droit
     * @return detailPrestation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel importDetailPrestation(DetailPrestationModel detailPrestation,
            String idEntetePrestation, String idDroit, HashMap<String, DroitComplexModel> droitComplexMap)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation d'une ent�te de prestation
     * 
     * @param entetePrestation
     *            en-t�te de prestation
     * @param idDossier
     *            identifiant du dossier pour l'entet�te
     * @param idRecap
     *            identifiant de la r�capitulatif entreprise pour l'ent�te de prestation
     * @return entetePrestation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel importEntetePrestation(EntetePrestationModel entetePrestation, String idDossier,
            String idRecap) throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation d'un r�capitulatif entreprise
     * 
     * @param recapEntrepImporte
     *            liste des r�capitulatif entreprise
     * @return recapImport
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel importRecapEntreprise(RecapitulatifEntrepriseModel recapEntrepImporte)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation d'un tucana Transfert
     * 
     * 
     * @param idDetailPrestation
     *            identifiant du d�tail de la prestation
     * @param transfertTucana
     *            mod�le de base de transfertTucan identifiant du d�tail de la prestation
     * @return transfertTucana
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel importTransfertTucana(TransfertTucanaModel transfertTucana, String idDetailPrestation)
            throws JadeApplicationException, JadePersistenceException;
}
