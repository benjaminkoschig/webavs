package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.exceptions.business.ALPrestationBusinessException;
import ch.globaz.al.business.exceptions.prestations.ALCompensationPrestationException;
import ch.globaz.al.business.exceptions.prestations.ALPaiementPrestationException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPaiementPrestationComplexModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPaiementPrestationComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.prestation.EntetePrestationModelService;
import ch.globaz.al.business.services.models.prestation.PrestationBusinessService;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.al.utils.ALImportUtils;

/**
 * Implémentation des service métier liés aux prestations
 * 
 * @author PTA/JTS
 */
public class PrestationBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements PrestationBusinessService {
    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.prestation.PrestationBusinessService#deplaceDansRecapOuverte(ch.globaz.
     * al.business.models.prestation.EntetePrestationModel)
     */
    @Override
    public void deplaceDansRecapOuverte(EntetePrestationModel entete) throws JadeApplicationException,
            JadePersistenceException {
        this.deplaceDansRecapOuverte(entete, false);

    }

    @Override
    public void deplaceDansRecapOuverte(EntetePrestationModel entete, boolean deplaceIfNonZero)
            throws JadeApplicationException, JadePersistenceException {
        if (entete == null) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#deplaceDansRecapOuverte: entete is null");
        }

        // seul le déplacement d'une prestation TR est autorisé via ce service
        if (!ALCSPrestation.ETAT_TR.equals(entete.getEtatPrestation())) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#deplaceDansRecapOuverte: entete is unmovable (not TR)");
        }

        // si seul le déplacement d'une prestation à 0.- est autorisé, on contrôle
        if (!deplaceIfNonZero && !JadeNumericUtil.isEmptyOrZero(entete.getMontantTotal())) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#deplaceDansRecapOuverte: entete is unmovable (montant <> 0)");
        }
        DossierModel dossierLie = ALServiceLocator.getDossierModelService().read(entete.getIdDossier());

        RecapitulatifEntrepriseModel recapActuelle = ALServiceLocator.getRecapitulatifEntrepriseModelService().read(
                entete.getIdRecap());
        // seul le déplacement depuis récap TR est autorisé via ce service
        if (!ALCSPrestation.ETAT_TR.equals(recapActuelle.getEtatRecap())) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#deplaceDansRecapOuverte: can't move prestation from this recap (not TR)");
        }

        // on initialise la récap de la période suivante

        String resultDate = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addMoisDate(1,
                ALDateUtils.getCalendarDate("01.".concat(recapActuelle.getPeriodeA()))).getTime());
        entete.setIdRecap(ALServiceLocator.getRecapitulatifEntrepriseBusinessService()
                .initRecap(dossierLie, resultDate.substring(3), recapActuelle.getBonification()).getIdRecap());

        entete.setEtatPrestation(ALCSPrestation.ETAT_SA);
        ALServiceLocator.getEntetePrestationModelService().update(entete);

        EntetePrestationSearchModel searchPrestRecapActuelle = new EntetePrestationSearchModel();
        searchPrestRecapActuelle.setForIdRecap(recapActuelle.getIdRecap());
        if (ALServiceLocator.getEntetePrestationModelService().count(searchPrestRecapActuelle) == 0) {
            // si on la laisse en TR, le checker empêcher la suppression de cette récap
            recapActuelle.setEtatRecap(ALCSPrestation.ETAT_TMP);
            ALServiceLocator.getRecapitulatifEntrepriseModelService().delete(recapActuelle);
        }

    }

    @Override
    public String getAgeEnfantDetailPrestation(DroitComplexModel droitComplex, DetailPrestationModel detailPrestation)
            throws JadeApplicationException, JadePersistenceException {
        String ageEnfant = null;
        String periodePresta = null;
        String dateNaissanceEnfantCorrige = null;

        if (!JadeDateUtil.isGlobazDateMonthYear(detailPrestation.getPeriodeValidite())) {
            throw new ALPrestationBusinessException("PrestationBusinessServiceImpl#getAgeEnfantDetailPrestation : "
                    + detailPrestation.getPeriodeValidite() + " is not a valid period");
        } else if (!JadeDateUtil.isGlobazDate(droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel()
                .getPersonne().getDateNaissance())) {
            throw new ALPrestationBusinessException("PrestationBusinessServiceImpl#getAgeEnfantDetailPrestation : "
                    + droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                            .getDateNaissance() + " is not a valid date");

        }
        periodePresta = "01." + detailPrestation.getPeriodeValidite();

        // on corrige la date de naissance pour le cas où cette dernière se
        // trouve dans le même mois qu'une prestation
        dateNaissanceEnfantCorrige = "01."
                + JadeStringUtil.substring(droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel()
                        .getPersonne().getDateNaissance(), 3);

        if (!ALImportUtils.isImport && !JadeDateUtil.isDateBefore(dateNaissanceEnfantCorrige, periodePresta)
                && !JadeDateUtil.areDatesEquals(dateNaissanceEnfantCorrige, periodePresta)) {
            throw new ALPrestationBusinessException(
                    "PrestationBusinessServiceImpl#getAgeEnfantDetailPrestation : date periodePrestation is older than dateNaissance ("
                            + droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers()
                                    .getDesignation1()
                            + " "
                            + droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers()
                                    .getDesignation2() + ", " + detailPrestation.getPeriodeValidite() + ")");
        }

        if (JadeDateUtil.areDatesEquals(periodePresta, dateNaissanceEnfantCorrige)) {
            ageEnfant = "0";
        } else {
            ageEnfant = JadeDateUtil.getNbYearsBetweenAsString(dateNaissanceEnfantCorrige, periodePresta,
                    JadeDateUtil.YEAR_MONTH_COMPARISON);
        }

        if (ALImportUtils.isImport && !JadeNumericUtil.isNumericPositif(ageEnfant)
                && !JadeNumericUtil.isEmptyOrZero(ageEnfant)) {
            ageEnfant = "0";
        }

        return ageEnfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.PrestationBusinessService #getEtatPrestationOuverte()
     */
    @Override
    public String getEtatPrestationOuverte() throws JadeApplicationException, JadePersistenceException {
        return ALCSPrestation.ETAT_SA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.PrestationBusinessService #isEditable(java.lang.String)
     */
    @Override
    public boolean isEditable(String etatPrestation) throws JadeApplicationException, JadePersistenceException {
        if (etatPrestation == null) {
            throw new ALPrestationBusinessException("PrestationBusinessServiceImpl#isEditable : etatPrestation is null");
        }

        return (ALCSPrestation.ETAT_TMP.equals(etatPrestation) || ALCSPrestation.ETAT_SA.equals(etatPrestation) || ALCSPrestation.ETAT_PR
                .equals(etatPrestation));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.PrestationBusinessService
     * #preparePrestations(ch.globaz.al.business.models.prestation.paiement.
     * CompensationPaiementPrestationComplexSearchModel)
     */
    @Override
    public void updateEtat(CompensationPaiementPrestationComplexSearchModel prestations, String etat)
            throws JadeApplicationException, JadePersistenceException {

        if (prestations == null) {
            throw new ALCompensationPrestationException(
                    "PrestationBusinessServiceImpl#preparePrestations : prestations is null");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, etat)) {
                throw new ALPaiementPrestationException("PaiementDirectServiceImpl#searchPrestations : etat " + etat
                        + " is not a valid value");
            }
        } catch (Exception e) {
            throw new ALPaiementPrestationException(
                    "PaiementDirectServiceImpl#searchPrestations : unable to check code system etat", e);
        }

        // initialisation des services
        EntetePrestationModelService serviceEntete = ALServiceLocator.getEntetePrestationModelService();
        RecapitulatifEntrepriseModelService serviceRecap = ALServiceLocator.getRecapitulatifEntrepriseModelService();

        HashSet<String> idsRecap = new HashSet<String>();
        HashSet<String> idsEntetes = new HashSet<String>();

        // préparation des listes d'en-tête de prestation et de récap
        // évite de mettre à jour plusieurs fois les même en-tête/récap
        for (int i = 0; i < prestations.getSize(); i++) {
            CompensationPaiementPrestationComplexModel line = (CompensationPaiementPrestationComplexModel) (prestations
                    .getSearchResults()[i]);
            idsEntetes.add(line.getIdEntete());
            idsRecap.add(line.getIdRecap());
        }

        // mise à jour des en-tête
        for (String idEntete : idsEntetes) {
            EntetePrestationModel entete = serviceEntete.read(idEntete);
            entete.setEtatPrestation(etat);
            serviceEntete.update(entete);
        }

        // mise à jour des récaps
        for (String idRecap : idsRecap) {
            RecapitulatifEntrepriseModel recap = serviceRecap.read(idRecap);
            recap.setEtatRecap(etat);
            serviceRecap.update(recap);
        }
    }
}