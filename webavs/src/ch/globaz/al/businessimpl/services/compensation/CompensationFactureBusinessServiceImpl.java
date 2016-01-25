package ch.globaz.al.businessimpl.services.compensation;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Collection;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.exceptions.prestations.ALCompensationPrestationException;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPrestationFullComplexModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPrestationFullComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.compensation.CompensationFactureBusinessService;
import ch.globaz.al.business.services.models.prestation.EntetePrestationModelService;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service fournissant les méthodes nécessaires à une compensation sur facture
 * 
 * @author jts
 * 
 */
public class CompensationFactureBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements
        CompensationFactureBusinessService {

    @Override
    public Collection<CompensationBusinessModel> loadRecapsPreparees(String periodeA, String typeCoti)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALGenerationException("CompensationFactureBusinessServiceImpl#loadRecapsPreparees : " + periodeA
                    + " is not a valid period (MM.YYYY)");
        }

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            throw new ALGenerationException("CompensationFactureBusinessServiceImpl#loadRecapsPreparees : " + typeCoti
                    + " is not valid");
        }

        // recherche les prestations a 0.-

        CompensationPrestationFullComplexSearchModel searchPrestationsZero = searchPrestations(periodeA, typeCoti,
                ALCSPrestation.ETAT_TR, true);
        ArrayList<String> listIdEnteteTraites = new ArrayList<String>();
        // chaque prestation à 0.- sont mises dans une nouvelle récap
        for (int i = 0; i < searchPrestationsZero.getSize(); i++) {
            EntetePrestationModel currentEntete = ALServiceLocator.getEntetePrestationModelService().read(
                    ((CompensationPrestationFullComplexModel) searchPrestationsZero.getSearchResults()[i])
                            .getIdEntete());

            if (!listIdEnteteTraites.contains(currentEntete.getId())) {

                listIdEnteteTraites.add(currentEntete.getId());

                ALServiceLocator.getPrestationBusinessService().deplaceDansRecapOuverte(currentEntete);
            }

        }

        return ALImplServiceLocator.getCompensationFactureService().buildRecaps(
                searchPrestations(periodeA, typeCoti, ALCSPrestation.ETAT_TR, false), true);
    }

    @Override
    public Collection<CompensationBusinessModel> loadRecapsPrepareesByNumProcessus(String idProcessus, String typeCoti)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALGenerationException(
                    "CompensationFactureBusinessServiceImpl#loadRecapsPrepareesByNumProcessus : " + idProcessus
                            + " is not a valid num processus");
        }

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            throw new ALGenerationException("CompensationFactureBusinessServiceImpl#loadRecapsPreparees : " + typeCoti
                    + " is not valid");
        }

        CompensationPrestationFullComplexSearchModel searchPrestationsZero = searchPrestationsByNumProcessus(
                ALCSPrestation.ETAT_TR, typeCoti, idProcessus, true);

        ArrayList<String> listIdEnteteTraites = new ArrayList<String>();

        // chaque prestation à 0.- sont mises dans une nouvelle récap
        for (int i = 0; i < searchPrestationsZero.getSize(); i++) {
            EntetePrestationModel currentEntete = ALServiceLocator.getEntetePrestationModelService().read(
                    ((CompensationPrestationFullComplexModel) searchPrestationsZero.getSearchResults()[i])
                            .getIdEntete());

            if (!listIdEnteteTraites.contains(currentEntete.getId())) {
                listIdEnteteTraites.add(currentEntete.getId());
                ALServiceLocator.getPrestationBusinessService().deplaceDansRecapOuverte(currentEntete);
            }
        }

        return ALImplServiceLocator.getCompensationFactureService().buildRecaps(
                searchPrestationsByNumProcessus(ALCSPrestation.ETAT_TR, typeCoti, idProcessus, false), true);
    }

    @Override
    public void restoreEtatPrestations(String idPassage) throws JadeApplicationException, JadePersistenceException {

        if (!JadeNumericUtil.isIntegerPositif(idPassage)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#rollbackRecaps : idPassage is null, zero or empty");
        }

        EntetePrestationModelService servicePrest = ALServiceLocator.getEntetePrestationModelService();
        RecapitulatifEntrepriseModelService serviceRecap = ALServiceLocator.getRecapitulatifEntrepriseModelService();

        EntetePrestationSearchModel search = new EntetePrestationSearchModel();
        search.setForIdPassage(idPassage);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = servicePrest.search(search);

        ArrayList<String> recapsId = new ArrayList<String>();

        // mise à jour des en-tête
        for (int i = 0; i < search.getSize(); i++) {
            EntetePrestationModel prest = (EntetePrestationModel) search.getSearchResults()[i];

            recapsId.add(prest.getIdRecap());
            // TODO (lot 2): si prestation ADI, remettre / regénérer prestation
            // CH?,
            // remis du décompte en SA
            prest.setEtatPrestation(ALCSPrestation.ETAT_TR);
            prest.setDateVersComp("0");
            prest.setIdPassage("0");
            servicePrest.update(prest);
        }

        // mise à jour des récaps
        for (int i = 0; i < recapsId.size(); i++) {
            RecapitulatifEntrepriseModel recap = serviceRecap.read(recapsId.get(i));
            recap.setEtatRecap(ALCSPrestation.ETAT_TR);
            serviceRecap.update(recap);
        }
    }

    /**
     * Recherche des prestations à compenser selon les critères passés en paramètres
     * 
     * @param periodeA
     *            - période des prestations à compenser
     * @param typeCoti
     *            - type de cotisation des prestations à compenser
     * @param etat
     *            - etat des prestations à compenser
     * @param montantZero
     *            - true si on recherche que des prestations à 0.-, false pour les autres
     * @return les prestations à compenser <CompensationPrestationFullComplexSearchModel>
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private CompensationPrestationFullComplexSearchModel searchPrestations(String periodeA, String typeCoti,
            String etat, boolean montantZero) throws JadePersistenceException, JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALCompensationPrestationException("CompensationFactureBusinessServiceImpl#searchPrestations : "
                    + periodeA + " is not a valid period (MM.YYYY)");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, etat)) {
                throw new ALCompensationPrestationException(
                        "CompensationFactureBusinessServiceImpl#searchPrestations : etat " + etat
                                + " is not a valid value");
            }
        } catch (Exception e) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureBusinessServiceImpl#searchPrestations : unable to check code system etat", e);
        }

        CompensationPrestationFullComplexSearchModel search = new CompensationPrestationFullComplexSearchModel();
        search.setForPeriodeA(periodeA);
        search.setForIdProcessusPeriodique("0");
        search.setForEtat(etat);
        search.setForBonification(ALCSPrestation.BONI_INDIRECT);
        search.setInActivites(ALImplServiceLocator.getDossierBusinessService().getActivitesToProcess(typeCoti));
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        if (montantZero) {
            search.setWhereKey("montantZeroNonAdi");
        } else {
            search.setWhereKey("montantNonZeroAndAdiZero");
        }
        return (CompensationPrestationFullComplexSearchModel) JadePersistenceManager.search(search);

    }

    /**
     * Recherche des prestations à compenser selon les critères passés en paramètres
     * 
     * @param idProcessus
     *            - numéro du processus des prestations à compenser
     * @param typeCoti
     *            - type de cotisation des prestations à compenser
     * @param etat
     *            - etat des prestations à compenser
     * @param montantZero
     *            - true si on recherche que des prestations à 0.-, false pour les autres
     * @return les prestations à compenser <CompensationPrestationFullComplexSearchModel>
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private CompensationPrestationFullComplexSearchModel searchPrestationsByNumProcessus(String etat, String typeCoti,
            String idProcessus, boolean montantZero) throws JadePersistenceException, JadeApplicationException {
        if (JadeStringUtil.isBlankOrZero(idProcessus)) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureBusinessServiceImpl#searchPrestationsByNumProcessus : " + idProcessus
                            + " is not a valid id of processus");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, etat)) {
                throw new ALCompensationPrestationException(
                        "CompensationFactureBusinessServiceImpl#searchPrestationsByNumProcessus : etat " + etat
                                + " is not a valid value");
            }
        } catch (Exception e) {
            throw new ALCompensationPrestationException(
                    "CompensationFactureBusinessServiceImpl#searchPrestationsByNumProcessus : unable to check code system etat",
                    e);
        }

        CompensationPrestationFullComplexSearchModel search = new CompensationPrestationFullComplexSearchModel();
        search.setForIdProcessusPeriodique(idProcessus);
        search.setForEtat(etat);
        search.setForBonification(ALCSPrestation.BONI_INDIRECT);
        search.setInActivites(ALImplServiceLocator.getDossierBusinessService().getActivitesToProcess(typeCoti));
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        if (montantZero) {
            search.setWhereKey("montantZeroNonAdi");
        } else {
            search.setWhereKey("montantNonZeroAndAdiZero");
        }
        return (CompensationPrestationFullComplexSearchModel) JadePersistenceManager.search(search);

    }

    @Override
    public void updatePrestationsCompensees(String idRecap, String date, String idPassage)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeNumericUtil.isIntegerPositif(idRecap)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#updateRecap : idRecap is null, zero or empty");
        }

        if (!JadeNumericUtil.isIntegerPositif(idPassage)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#updateRecap : idPassage is null, zero or empty");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#updateRecap : " + date
                    + " is not a valid date");
        }

        RecapitulatifEntrepriseModelService serviceRecap = ALServiceLocator.getRecapitulatifEntrepriseModelService();
        EntetePrestationModelService servicePrest = ALServiceLocator.getEntetePrestationModelService();

        // Récupération de la récap
        RecapitulatifEntrepriseModel recap = serviceRecap.read(idRecap);

        // Récupération des en-tête
        EntetePrestationSearchModel search = new EntetePrestationSearchModel();
        search.setForIdRecap(idRecap);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = servicePrest.search(search);

        // mise à jour des en-tête
        for (int i = 0; i < search.getSize(); i++) {

            EntetePrestationModel prest = (EntetePrestationModel) search.getSearchResults()[i];
            // si prestation ADI comptabilisée
            if (ALCSPrestation.STATUT_ADI.equals(prest.getStatut())) {
                // comptabilise le décompte
                ALServiceLocator.getDecompteAdiBusinessService().comptabiliserDecompteLie(prest.getIdEntete());
                // on supprime la prestation de travail (TMP)
                ALServiceLocator.getDecompteAdiBusinessService().supprimerPrestationTravailDossier(
                        prest.getIdDossier(), prest.getPeriodeDe(), prest.getPeriodeA());

            }

            prest.setEtatPrestation(ALCSPrestation.ETAT_CO);
            prest.setDateVersComp(date);
            prest.setIdPassage(idPassage);
            servicePrest.update(prest);
        }

        // mise à jour de la récap
        recap.setEtatRecap(ALCSPrestation.ETAT_CO);
        serviceRecap.update(recap);
    }
}
