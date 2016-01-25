package ch.globaz.al.businessimpl.services.echeances;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.business.ALDroitBusinessException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierComplexModelException;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.exceptions.utils.ALUtilsException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.tarif.EcheanceComplexModel;
import ch.globaz.al.business.models.tarif.EcheanceComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.DatesEcheancePrivateService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * classe contenant les spécificités liées aux calculs des échéances
 * 
 * @author PTA
 * 
 */
public class DatesEcheancePrivateServiceImpl extends ALAbstractBusinessServiceImpl implements
        DatesEcheancePrivateService {
    @Override
    public String calculFinValiditeEcheance(DroitComplexModel droitComplex, String dateDebutValidite,
            ArrayList<EcheanceComplexModel> echeanceCriter) throws JadePersistenceException, JadeApplicationException {

        // vérification des paramètres
        if (droitComplex == null) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#calculFinValiditeEcheance : droitComplex is null");
        }
        if (!JadeDateUtil.isGlobazDate(dateDebutValidite)) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#calculFinValiditeEcheance : La date de début de validité "
                            + dateDebutValidite + " is not a globaz date");
        }
        if (echeanceCriter == null) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#calculFinValiditeEcheance : echeanceCriter is null");
        }

        String finValiditeEcheance = null;
        Iterator<EcheanceComplexModel> iter = echeanceCriter.iterator();
        EcheanceComplexModel echeance = new EcheanceComplexModel();
        while (iter.hasNext() && (finValiditeEcheance == null)) {
            echeance = iter.next();

            // si l'âge de fin est plus grande ou égale à l'âge de l'enfant
            // à la date définie
            if ((JadeStringUtil.toInt(echeance.getAgeFin()) >= JadeDateUtil.getNbYearsBetween(droitComplex
                    .getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance(),
                    dateDebutValidite, JadeDateUtil.FULL_DATE_COMPARISON))) {
                // rajouter le nombre d'années et
                // calculer la date d'échéance
                finValiditeEcheance = ALDateUtils.getDateAjoutAnneesFinMois(droitComplex.getEnfantComplexModel()
                        .getPersonneEtendueComplexModel().getPersonne().getDateNaissance(),
                        JadeStringUtil.toInt(echeance.getAgeFin()));
            }

        }

        return finValiditeEcheance;
    }

    @Override
    public String calculFinValiditeEcheance(String dateDebutValidite, EcheanceComplexModel EcheanceCriter,
            String dateNaissance) throws JadeApplicationException, JadePersistenceException {

        String finValiditeEcheance = null;
        if (!JadeDateUtil.isGlobazDate(dateDebutValidite)) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#calculFinValiditeEcheance : DateDebutValidite is not a globaz date");
        }
        if (EcheanceCriter == null) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#calculFinValiditeEcheance : echeanceCriter is null");
        }
        if (!JadeDateUtil.isGlobazDate(dateNaissance)) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#calculFinValiditeEcheance : dateNaissance is not a globaz date");
        }

        if ((JadeStringUtil.toInt(EcheanceCriter.getAgeFin()) >= JadeDateUtil.getNbYearsBetween(dateNaissance,
                dateDebutValidite, JadeDateUtil.FULL_DATE_COMPARISON))) {

            // rajouter le nombre d'années et calculer la date d'échéance
            finValiditeEcheance = ALDateUtils.getDateAjoutAnneesFinMois(dateNaissance,
                    JadeStringUtil.toInt(EcheanceCriter.getAgeFin()));
        }

        return finValiditeEcheance;

    }

    @Override
    public String getAgeEnfant(String dateNaissance, String dateEcheance) throws JadeApplicationException,
            JadePersistenceException {
        String ageEnfant = null;

        String dateNaissanceEnfantCorrige = null;

        if (!JadeDateUtil.isGlobazDate(dateNaissance)) {
            throw new ALDroitBusinessException(
                    "DatesEcheancePrivateServiceImpl#getAgeEnfant: dateNaissance is not a valid date dayMonthYear Globaz");
        } else if (!JadeDateUtil.isGlobazDate(dateEcheance)) {
            throw new ALDroitBusinessException(
                    "DatesEcheancePrivateServiceImpl#getAgeEnfant: dateEcheance is not a valid date dayMonthYear Globaz");

        }

        // on corrige la date de naissance pour le cas où cette dernière se
        // trouve dans le même mois qu'une prestation
        dateNaissanceEnfantCorrige = "01." + JadeStringUtil.substring(dateNaissance, 3);

        if (!JadeDateUtil.isDateBefore(dateNaissanceEnfantCorrige, dateEcheance)
                && !JadeDateUtil.areDatesEquals(dateNaissanceEnfantCorrige, dateEcheance)) {
            throw new ALDroitBusinessException(
                    "DatesEcheancePrivateServiceImpl#getAgeEnfant: dateEcheance is older than dateNaissance");
        }
        //

        if (JadeDateUtil.areDatesEquals(dateEcheance, dateNaissanceEnfantCorrige)) {
            ageEnfant = "0";
        } else {
            ageEnfant = JadeDateUtil.getNbYearsBetweenAsString(dateNaissanceEnfantCorrige, dateEcheance,
                    JadeDateUtil.YEAR_MONTH_COMPARISON);
        }

        return ageEnfant;
    }

    @Override
    public String getDateDebutValiditeDroit(String dateFinValiditeDroitEnfant) throws JadeApplicationException {
        String dateDebutValidite = null;

        if (!JadeDateUtil.isGlobazDate(dateFinValiditeDroitEnfant)) {
            throw new ALUtilsException("DatesEcheancePrivateServiceImpl#getDateDebutValiditeDroit : "
                    + dateFinValiditeDroitEnfant + " is not a valid date");
        }

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");

        Calendar newDate = java.util.Calendar.getInstance();

        try {
            newDate.setTime(sdf.parse(dateFinValiditeDroitEnfant));
        } catch (ParseException e) {
            throw new ALUtilsException(
                    "DatesEcheancePrivateServiceImpl#getDateDebutValiditeDroit : Unable to parse date :"
                            + dateFinValiditeDroitEnfant);
        }
        // mise de la date au mois suivant
        newDate.add(Calendar.MONTH, 1);
        // affectation de la nouvelle date au début de validité
        dateDebutValidite = sdf.format(newDate.getTime());
        dateDebutValidite = "01" + dateDebutValidite.substring(2);

        return dateDebutValidite;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.echeances.DatesEcheanceService# getDateDebutValiditeDroit(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getDateDebutValiditeDroit(String dateNaissance, String dateDebutActivite)
            throws JadeApplicationException {
        String dateDebutValidite = null;

        if (!JadeDateUtil.isGlobazDate(dateNaissance)) {
            throw new ALUtilsException("DatesEcheancePrivateServiceImpl#getDateDebutValiditeDroit : " + dateNaissance
                    + " is not a valid date");
        }
        if (!JadeDateUtil.isGlobazDate(dateDebutActivite)) {
            throw new ALUtilsException("DatesEcheancePrivateServiceImpl#getDateDebutValiditeDroit : "
                    + dateDebutActivite + " is not a valid date");
        }
        // date de naissance antérieure à date de début activité, date début
        // activité fait référence pour le début de validité du droit
        if (JadeDateUtil.isDateBefore(dateNaissance, dateDebutActivite)) {
            dateDebutValidite = dateDebutActivite;
        }

        // date de naissance postérieure à la date de début validité mais du
        // même
        // mois et même année, début activité est la référence pour le début de
        // validité du droit
        else if (JadeDateUtil.isDateAfter(dateNaissance, dateDebutActivite)
                && (JadeStringUtil.equals(JadeStringUtil.substring(dateNaissance, 3, 8),
                        JadeStringUtil.substring(dateDebutActivite, 3, 8), true))) {

            dateDebutValidite = dateDebutActivite;

        }

        // dans les autres cas (si date naissance est postérieure à la date du
        // début d'activité , la date de validité correspond au début du mois de
        // la date de naissance

        else {
            dateDebutValidite = "01" + dateNaissance.substring(2);
        }

        return dateDebutValidite;

    }

    @Override
    public ArrayList<EcheanceComplexModel> getDebutFinValiditeEcheance(String typePrestation, String catTarif,
            String catResident, String dateDebutValidite, Boolean caExercer) throws JadePersistenceException,
            JadeApplicationException {

        // vérification des paramètres
        try {
            if (!JadeStringUtil.equals(ALCSDroit.TYPE_ENF, typePrestation, false)
                    && !JadeStringUtil.equals(ALCSDroit.TYPE_FORM, typePrestation, false)) {
                throw new ALEcheanceModelException(
                        "DatesEcheancePrivateServiceImpl#getDebutFinValiditeEcheance : typePrestation" + typePrestation
                                + " is not a valid type (validType is ALCSDroit.TYPE_ENF or ALCSDroit.TYPE_FORM)");

            }
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE, catTarif)) {
                throw new ALEcheanceModelException(
                        "DatesEcheancePrivateServiceImpl#getDebutFinValiditeEcheance : catTarif " + catTarif
                                + "is not a valid type catégorie de tarif ");
            }

            // l'Allocataire (pays: ETR, CH)?
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_RESIDENT, catResident)) {
                throw new ALEcheanceModelException(
                        "DatesEcheancePrivateServiceImpl#getDebutFinValiditeEcheance: catResident " + catResident
                                + " is not a valid categorie resident");
            }

        } catch (Exception e) {
            throw new ALEcheanceModelException(
                    "getDebutFinValiditeEcheances problem during checking codes system integrity", e);
        }
        if (!JadeDateUtil.isGlobazDate(dateDebutValidite)) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#getDebutFinValiditeEcheance : dateDebutValididite: "
                            + dateDebutValidite + " is not a valid globaz date");
        }
        if (caExercer == null) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#getDebutFinValiditeEcheance : capExercer has not a valid value ");

        }

        ArrayList<EcheanceComplexModel> listEcheances = new ArrayList<EcheanceComplexModel>();

        // recherches des âges début et fin d'échéances
        EcheanceComplexSearchModel se = new EcheanceComplexSearchModel();
        se.setForCategorieResident(catResident);
        se.setForFinValiditePrestation(dateDebutValidite);
        se.setForDebutValiditePrestation(dateDebutValidite);
        se.setForCategorieTarif(catTarif);
        se.setForTypePrestation(typePrestation);
        se.setForCapableExercer(caExercer);

        se = ALImplServiceLocator.getEcheanceComplexModelService().search(se);
        if (se.getSize() == 0) {
            throw new ALEcheanceModelException(
                    "DatesEcheancePrivateServiceImpl#getDebutFinValiditeEcheance : Aucune echeance pour cette requête");

        } else {
            for (int i = 0; i < se.getSize(); i++) {
                // echeance = (EcheanceComplexModel) se.getSearchResults()[i];
                listEcheances.add((EcheanceComplexModel) se.getSearchResults()[i]);
            }

            return listEcheances;
        }

    }

    /**
     * Méthode de recherche d'un dossier complex
     * 
     * @param droitComplexModel
     *            permet de récupérer les champs nécessaire à la recherche
     * @return dossierComplex
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws ALDroitBusinessException
     *             Exception levée
     */
    private DossierComplexModel getDossierForDroit(DroitComplexModel droitComplexModel)
            throws JadeApplicationException, JadePersistenceException, ALDroitBusinessException {

        if (droitComplexModel == null) {
            throw new ALDossierComplexModelException(
                    "DatesEcheancePrivateServiceImpl#getDossierForDroit: droitComplexModel is null");
        }

        DossierComplexModel dossierComplex = ALServiceLocator.getDossierComplexModelService().read(
                droitComplexModel.getDroitModel().getIdDossier());

        if (dossierComplex.isNew()) {
            throw new ALDossierComplexModelException(
                    "DatesEcheancePrivateServiceImpl#getDossierForDroit : dossier ID #"
                            + droitComplexModel.getDroitModel().getIdDossier() + " does not exist");
        }

        return dossierComplex;
    }

    @Override
    public String getFinValiditeEcheance(DroitComplexModel droitComplex, String dateDebutValidite)
            throws JadeApplicationException, JadePersistenceException {
        String finValiditeEcheance = null;

        if (droitComplex == null) {
            throw new ALDroitBusinessException("DatesEcheanceServiceImpl#getFinValiditeEcheance : DroitModel is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebutValidite)) {
            throw new ALDroitBusinessException("DatesEcheanceServiceImpl#getFinValiditeEcheance : dateDebutValidite :"
                    + dateDebutValidite + " is not a valid globaz date");
        }

        // rechercher le dossier complex
        DossierComplexModel dossierComplex = getDossierForDroit(droitComplex);

        if (JadeStringUtil.equals(droitComplex.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_ENF, true)
                || JadeStringUtil.equals(droitComplex.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_FORM, true)) {

            // Récupération du type de résident, voir méthode
            String categResidentAlloc = ALServiceLocator.getAllocataireBusinessService().getTypeResident(
                    dossierComplex.getAllocataireComplexModel().getAllocataireModel());
            // récupération de la catégorie de tarif

            // dans le droit, si tarifForce n'est pas vide, il faut récupérer ce
            // tarif
            ArrayList<EcheanceComplexModel> echeanceCriter = new ArrayList<EcheanceComplexModel>();
            if (!JadeStringUtil.isBlankOrZero(droitComplex.getDroitModel().getTarifForce())) {
                // récupération des ages limites inférieure et supérieures pour
                // le
                // droit
                echeanceCriter = ALImplServiceLocator.getDatesEcheancePrivateService().getDebutFinValiditeEcheance(
                        droitComplex.getDroitModel().getTypeDroit(), droitComplex.getDroitModel().getTarifForce(),
                        categResidentAlloc, dateDebutValidite,
                        droitComplex.getEnfantComplexModel().getEnfantModel().getCapableExercer());
            }
            // dans le dossier, si tarif force est saisie
            else if (!JadeStringUtil.isBlankOrZero(dossierComplex.getDossierModel().getTarifForce())) {

                echeanceCriter = ALImplServiceLocator.getDatesEcheancePrivateService().getDebutFinValiditeEcheance(
                        droitComplex.getDroitModel().getTypeDroit(), dossierComplex.getDossierModel().getTarifForce(),
                        categResidentAlloc, dateDebutValidite,
                        droitComplex.getEnfantComplexModel().getEnfantModel().getCapableExercer());

            }
            // dans les autres cas, il faut récupérer le tarif en fonction du
            // canton
            // de l'affilié
            else {

                String cantonAffilie = null;
                String tarifAffilie = null;
                // récupération du canton de l'affilié
                cantonAffilie = ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(
                        ALServiceLocator.getAffiliationBusinessService()
                                .getAssuranceInfo(dossierComplex.getDossierModel(), dateDebutValidite).getCanton());

                // récupération du tarif du canton
                tarifAffilie = ALImplServiceLocator.getCalculService().getTarifForCanton(cantonAffilie);

                // calcul de la fin de validité
                echeanceCriter = ALImplServiceLocator.getDatesEcheancePrivateService().getDebutFinValiditeEcheance(
                        droitComplex.getDroitModel().getTypeDroit(), tarifAffilie, categResidentAlloc,
                        dateDebutValidite, droitComplex.getEnfantComplexModel().getEnfantModel().getCapableExercer());
            }

            finValiditeEcheance = ALImplServiceLocator.getDatesEcheancePrivateService().calculFinValiditeEcheance(
                    droitComplex, dateDebutValidite, echeanceCriter);

        }
        return finValiditeEcheance;

    }
}
