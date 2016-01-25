package ch.globaz.ij.businessimpl.services;

import globaz.globall.db.BSessionUtil;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.helpers.controleAbsences.IJBaseIndemnisationAjaxHelper;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.utils.PRDateUtils;
import ch.globaz.ij.business.models.IJAbsence;
import ch.globaz.ij.business.models.IJAbsenceSearchModel;
import ch.globaz.ij.business.models.IJPeriodeControleAbsences;
import ch.globaz.ij.business.models.IJPeriodeControleAbsencesSearchModel;
import ch.globaz.ij.business.models.IJSimpleAbsence;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisation;
import ch.globaz.ij.business.services.IJAbsenceService;
import ch.globaz.ij.business.services.IJBaseIndemnisationService;
import ch.globaz.ij.business.services.IJPeriodeControleAbsencesService;
import ch.globaz.ij.business.services.IJServiceLocator;
import ch.globaz.ij.businessimpl.services.Exception.ServiceBusinessException;
import ch.globaz.ij.businessimpl.services.Exception.ServiceTechnicalException;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;

public class IJAbsenceServiceImpl implements IJAbsenceService {

    private static final String MODE_UPDATE = "update";

    /**
     * Vérifie que la base d'indémnisation est dans un état autorisé pour l'ajout d'un absence.
     * 
     * @param baseIndemnisation
     * @return true si l'état de la base d'indemnisation permet l'ajout d'une absence
     * @throws ServiceBusinessException
     * @throws ServiceTechnicalException
     */
    private boolean checkBaseIndemnisationPourAjoutAbsence(IJSimpleBaseIndemnisation baseIndemnisation)
            throws ServiceBusinessException, ServiceTechnicalException {
        boolean result = true;

        if ((IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getEtatCS()))) {
            result = false;
        }

        return result;
    }

    private void computeEntityValues(IJAbsence absence, boolean updateValeurSaisies) throws ServiceBusinessException,
            ServiceTechnicalException {
        String codeAbsence = absence.getCodeAbsence();
        if (!JadeStringUtil.isEmpty(codeAbsence)) {

            JadeCodeSysteme codeSystemeTypeAbsence;
            try {
                codeSystemeTypeAbsence = JadeBusinessServiceLocator.getCodeSystemeService().getCodeSysteme(codeAbsence);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new ServiceTechnicalException("Unable to find code system " + codeAbsence, e);
            } catch (JadePersistenceException e) {
                throw new ServiceTechnicalException("Unable to find code system " + codeAbsence, e);
            }
            absence.setTypeAbsenceCS(codeSystemeTypeAbsence);

            if (!JadeStringUtil.isEmpty(absence.getDateDeDebut()) && !JadeStringUtil.isEmpty(absence.getDateDeDebut())) {
                if (JadeDateUtil.isGlobazDate(absence.getDateDeDebut())
                        && JadeDateUtil.isGlobazDate(absence.getDateDeFin())) {

                    // Calcul du nombre de jours de la période
                    int nombreDeJoursPeriode = PRDateUtils.getNbDayBetween(absence.getDateDeDebut(),
                            absence.getDateDeFin());
                    nombreDeJoursPeriode++;
                    int nombreDeJoursCalcule = nombreDeJoursPeriode;
                    if (isAbsenceTypeAccident(codeAbsence) && (nombreDeJoursPeriode > 3)) {
                        nombreDeJoursCalcule = 3;
                    }

                    // D'office on remplit le nombre de jours (calculé)
                    absence.setNombreDeJours(String.valueOf(nombreDeJoursCalcule));

                    // si besoin, on remplit le nombre de jours saisis
                    if (updateValeurSaisies) {
                        absence.setJoursSaisis(absence.getNombreDeJours());
                    }

                    // Sinon on contrôle que le nombre de jours saisis si <= nombre de jours de la période
                    else {
                        int _nombreDeJoursSaisis = 0;
                        if (!JadeStringUtil.isBlankOrZero(absence.getJoursSaisis())) {
                            _nombreDeJoursSaisis = Integer.valueOf(absence.getJoursSaisis());
                        }
                        if (_nombreDeJoursSaisis > nombreDeJoursPeriode) {
                            throw new ServiceBusinessException(
                                    translate("ERROR_SAISIE_ABSENCE_NOMBRE_JOURS_SAISIS_TROP_GRAND"));
                        }
                    }

                    // Calcul du nombre de jour non payés
                    int nombreDeJoursNonPayes = 0;
                    if (isAbsenceTypeAccident(codeAbsence)) {
                        if (JadeStringUtil.isEmpty(absence.getJoursSaisis())) {
                            absence.setJoursSaisis("0");
                        }
                        nombreDeJoursNonPayes = nombreDeJoursPeriode - Integer.valueOf(absence.getJoursSaisis());
                    }

                    else if (codeAbsence.equals("52433007") || codeAbsence.equals("52433008")) {
                        nombreDeJoursNonPayes = nombreDeJoursPeriode;
                    }

                    else {
                        nombreDeJoursNonPayes = 0;
                    }

                    // D'office on remplit le nombre de jours non payés calculés
                    absence.setJoursNonPaye(String.valueOf(nombreDeJoursNonPayes));
                    // si besoin, on remplit le nombre de jours saisis
                    if (updateValeurSaisies) {
                        absence.setJoursNonPayeSaisis(absence.getJoursNonPaye());
                    }

                    // Sinon on contrôle que le nombre de jours non payés saisis si <= nombre de jours de la période
                    else {
                        int _nombreDeJoursNonPayes = 0;
                        if (!JadeStringUtil.isBlankOrZero(absence.getJoursNonPayeSaisis())) {
                            _nombreDeJoursNonPayes = Integer.valueOf(absence.getJoursNonPayeSaisis());
                        }
                        if (_nombreDeJoursNonPayes > nombreDeJoursPeriode) {
                            throw new ServiceBusinessException(
                                    translate("ERROR_SAISIE_ABSENCE_NOMBRE_JOURS_NON_PAYE_SAISIS_TROP_GRAND"));
                        }
                    }

                    int joursSaisis = 0;
                    int joursNonPayeSaisis = 0;
                    // Nombre de jours saisis
                    if (!JadeStringUtil.isBlankOrZero(absence.getJoursSaisis())) {
                        try {
                            joursSaisis = Integer.valueOf(absence.getJoursSaisis());
                        } catch (NumberFormatException exception) {
                            throw new ServiceBusinessException(translate("ERROR_UNABLE_TO_GET_VALUE") + " : "
                                    + translate("JSP_SAISIE_ABSENCE_NOMBRE_JOURS_SAISIS"));
                        }
                    }
                    // else {
                    // joursSaisis = nombreDeJoursCalcule;
                    // }

                    // Non payé saisis
                    if (!JadeStringUtil.isBlankOrZero(absence.getJoursNonPayeSaisis())) {
                        try {
                            joursNonPayeSaisis = Integer.valueOf(absence.getJoursNonPayeSaisis());
                        } catch (NumberFormatException exception) {
                            throw new ServiceBusinessException(translate("ERROR_UNABLE_TO_GET_VALUE") + " : "
                                    + translate("JSP_SAISIE_ABSENCE_NON_PAYES_SAISIS"));
                        }
                    }
                    // else {
                    // joursNonPayeSaisis = nombreDeJoursNonPayes;
                    // }

                    // Calcul des jours d'interruption
                    if (isAbsenceTypeAccident(codeAbsence)) {
                        absence.setJoursInterruption(absence.getJoursSaisis());
                    }

                    else {
                        absence.setJoursInterruption(String.valueOf(joursSaisis - joursNonPayeSaisis));
                    }
                }
            }
        }
    }

    /**
     * Contrôle que l'absence soit situé dans la période de la base d'indémnisation ET qu'une période de contrôle couvre
     * la période de l'absence sans chevauchement. <strong>L'entitée doit préalablement être validée via la méthode
     * validateEntity(IJAbsence)</strong>
     * 
     * @param absence
     *            Absence à tester
     * @return True si l'absence ne pose pas de problème de période
     * @throws ServiceTechnicalException
     */
    private void controlDesChevauchement(IJAbsence absence) throws ServiceTechnicalException, ServiceBusinessException {
        IJSimpleBaseIndemnisation baseIndemnisation = null;
        IJPeriodeControleAbsencesSearchModel periodesControl = null;
        IJPeriodeControleAbsencesSearchModel search = new IJPeriodeControleAbsencesSearchModel();
        search.setForIdDossier(absence.getIdDossierControleAbsences());

        try {
            baseIndemnisation = IJServiceLocator.getBaseIndemnisationService().read(absence.getIdBaseIndemnisation());
            periodesControl = IJServiceLocator.getControlePeriodeService().search(search);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        }

        if (baseIndemnisation == null) {
            throw new ServiceTechnicalException("Unable to retreive the IJSimpleBaseIndemnisation object");
        }
        if (periodesControl == null) {
            throw new ServiceTechnicalException("Unable to retreive the IJPeriodeControleAbsences object");
        }

        // Contrôle que l'absence soit dans la période de la base
        // d'indémnisation
        JadePeriodWrapper biPeriode = new JadePeriodWrapper(baseIndemnisation.getDateDeDebut(),
                baseIndemnisation.getDateDeFin());
        if (!biPeriode.isDateDansLaPeriode(absence.getDateDeDebut())
                || !biPeriode.isDateDansLaPeriode(absence.getDateDeFin())) {
            throw new ServiceBusinessException(translate("ERROR_PERIODE_ABSENCE_ISNOTIN_PERIODE_BASE_INDEMNISATION"));
        }

        if (periodesControl.getSize() == 0) {
            throw new ServiceBusinessException(translate("ERROR_AUCUNE_PERIODE_CONTROLE_DEFINIE"));
        }

        boolean result = false;
        for (JadeAbstractModel x : periodesControl.getSearchResults()) {
            IJPeriodeControleAbsences pca = (IJPeriodeControleAbsences) x;
            JadePeriodWrapper wrapper = new JadePeriodWrapper(pca.getDateDeDebut(), pca.getDateDeFin());
            if (wrapper.isDateDansLaPeriode(absence.getDateDeDebut())) {
                if (wrapper.isDateDansLaPeriode(absence.getDateDeFin())) {
                    result = true;
                }
            }
        }
        if (!result) {
            throw new ServiceBusinessException(translate("ERROR_AUCUNE_PERIODE_CONTROLE_COUVRE_ABSENCE"));
        }
    }

    /**
     * Vérifie qu'il n'y ait pas de jours non payés si la base d'indemnisation est à l'état "communiqué"
     * 
     * @param entity
     * @param baseIndemnisation
     * @throws ServiceBusinessException
     */
    private void checkJourNonPayesIfBaseIndemnisationCommuniquee(IJAbsence entity,
            IJSimpleBaseIndemnisation baseIndemnisation) throws ServiceBusinessException {
        if (IIJBaseIndemnisation.CS_COMMUNIQUE.equals(baseIndemnisation.getEtatCS())) {
            int nbJoursNonPaye = Integer.valueOf(entity.getJoursNonPaye());
            if (nbJoursNonPaye != 0) {
                throw new ServiceBusinessException(
                        translate("ERROR_IMPOSSIBLE_AJOUTER_ABSENCE_BASE_INDEMNISATION_COMMUNIQUEE"));
            }
        }
    }

    /**
     * retourne la base d'indemnisation en fonction de l'id
     * 
     * @param idBaseIndemnisation
     * @return
     * @throws ServiceBusinessException
     * @throws ServiceTechnicalException
     */
    private IJSimpleBaseIndemnisation findBaseIndemnisation(String idBaseIndemnisation)
            throws ServiceBusinessException, ServiceTechnicalException {
        IJSimpleBaseIndemnisation baseIndemnisation = null;
        try {
            IJBaseIndemnisationService baseIndemnisationService = IJServiceLocator.getBaseIndemnisationService();
            if (JadeStringUtil.isBlankOrZero(idBaseIndemnisation)) {
                throw new ServiceBusinessException(translate("ERROR_IMPOSSIBLE_AJOUTER_ABSENCE_PAS_BASE_INDEMNISATION"));
            }
            baseIndemnisation = baseIndemnisationService.read(idBaseIndemnisation);
            if (baseIndemnisation == null) {
                throw new ServiceTechnicalException("Unable to find IJSimpleBaseIndemnisation with id : "
                        + idBaseIndemnisation);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        }

        return baseIndemnisation;
    }

    @Override
    public int count(IJAbsenceSearchModel search) throws ServiceBusinessException, ServiceTechnicalException {
        if (search == null) {
            throw new ServiceTechnicalException("Service : " + this.getClass().getName()
                    + " : unable to count values because searchModel is null");
        }
        try {
            return JadePersistenceManager.count(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException("Service : " + this.getClass().getName() + " : unable to count values");
        }
    }

    @Override
    public IJAbsence create(IJAbsence entity) throws ServiceBusinessException, ServiceTechnicalException {
        // Avant d'ajouter d'une absence, on vérifie qu'il n'y à pas de
        // chevauchement dans les périodes de contrôles
        IJPeriodeControleAbsencesService periodeControleService = null;
        try {
            periodeControleService = IJServiceLocator.getControlePeriodeService();
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        }

        // S'il y a un chevauchement dans les périodes de contrôles, on interdit
        // l'ajout d'absences
        if (periodeControleService.hasChevauchementDePeriode(entity.getIdDossierControleAbsences())) {
            throw new ServiceBusinessException(translate("ERROR_IMPOSSIBLE_AJOUTER_ABSENCE_PERIODES_SE_CHEVAUCHENT"));
        }

        // on s'assure que la base d'indemnisation liée aux absences ne soit pas annulée
        IJSimpleBaseIndemnisation baseIndemnisation = null;
        baseIndemnisation = findBaseIndemnisation(entity.getIdBaseIndemnisation());

        if (!checkBaseIndemnisationPourAjoutAbsence(baseIndemnisation)) {
            throw new ServiceBusinessException(translate("ERROR_IMPOSSIBLE_AJOUTER_ABSENCE_BASE_INDEMNISATION_BLOQUE"));
        }

        validateEntity(entity);

        // Calcul des valeurs de l'entité + copie des valeurs calculées dans les saisies si besoin
        computeEntityValues(entity, true);

        // si la base d'indemnisation est en état "communiqué", il ne faut pas qu'il y ait de jours non payés
        checkJourNonPayesIfBaseIndemnisationCommuniquee(entity, baseIndemnisation);

        // Si les jours d'interruption sont négatif ==> Erreur
        if (Integer.valueOf(entity.getJoursInterruption()) < 0) {
            throw new ServiceBusinessException(translate("ERROR_ABSENCE_JOURS_INTERRUPTION_INFERIEUR_ZERO"));
        }

        try {
            JadePersistenceManager.add(entity.getAbsence());
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }

        // Mise à jour de la base d'indemnisation
        updateBaseIndemnisation(entity.getIdBaseIndemnisation());

        return entity;
    }

    @Override
    public IJAbsence delete(IJAbsence entity) throws ServiceBusinessException, ServiceTechnicalException {
        deleteWithoutBaseIndemnisationUpdate(entity);
        // Mise à jour de la base d'indemnisation
        updateBaseIndemnisation(entity.getIdBaseIndemnisation());
        return entity;
    }

    @Override
    public IJAbsence deleteWithoutBaseIndemnisationUpdate(IJAbsence entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        IJSimpleAbsence simpleAbsence = null;
        try {
            simpleAbsence = (IJSimpleAbsence) JadePersistenceManager.delete(entity.getAbsence());
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }

        if (simpleAbsence != null) {
            entity.setAbsence(simpleAbsence);
            computeEntityValues(entity, false);
        }
        return entity;
    }

    private boolean isAbsenceTypeAccident(String codeAbsence) {
        return ("52433001".equals(codeAbsence) || "52433003".equals(codeAbsence));
    }

    @Override
    public IJAbsence read(String idEntity) throws ServiceBusinessException, ServiceTechnicalException {
        IJAbsence entity = new IJAbsence();
        entity.setIdAbsence(idEntity);
        IJAbsence returnedAbsence = null;
        try {
            returnedAbsence = (IJAbsence) JadePersistenceManager.read(entity);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        // Mise à jour des champs calculés
        if (returnedAbsence != null) {
            computeEntityValues(returnedAbsence, false);
        }
        return returnedAbsence;
    }

    @Override
    public IJAbsenceSearchModel search(IJAbsenceSearchModel search) throws ServiceBusinessException,
            ServiceTechnicalException {
        IJAbsenceSearchModel returnedSearchModel = null;
        try {
            search.setOrderKey("orderByDateDeDebutAsc");
            returnedSearchModel = (IJAbsenceSearchModel) JadePersistenceManager.search(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        // Mise à jour des champs calculés
        for (JadeAbstractModel entity : search.getSearchResults()) {
            computeEntityValues((IJAbsence) entity, false);
        }
        return returnedSearchModel;
    }

    private String translate(String messageKey) {
        return BSessionUtil.getSessionFromThreadContext().getLabel(messageKey);
    }

    @Override
    public IJAbsence update(IJAbsence entity) throws ServiceBusinessException, ServiceTechnicalException {

        if (entity.getAbsence() != null) {
            // Mise à jour des champs calculés
            validateEntity(entity);
            entity.setAbsence(entity.getAbsence());

            IJAbsence tmpAbsence = read(entity.getId());
            boolean wantUpdateValeursSaisies = false;
            wantUpdateValeursSaisies = mustUpdateValeursSaisies(entity, tmpAbsence);
            computeEntityValues(entity, wantUpdateValeursSaisies);
        }

        // on s'assure que la base d'indemnisation liée aux absences ne soit pas annulée
        IJSimpleBaseIndemnisation baseIndemnisation = null;
        baseIndemnisation = findBaseIndemnisation(entity.getIdBaseIndemnisation());

        if (!checkBaseIndemnisationPourAjoutAbsence(baseIndemnisation)) {
            throw new ServiceBusinessException(translate("ERROR_IMPOSSIBLE_AJOUTER_ABSENCE_BASE_INDEMNISATION_BLOQUE"));
        }

        // si la base d'indemnisation est en état "communiqué", il ne faut pas qu'il y ait de jours non payés
        checkJourNonPayesIfBaseIndemnisationCommuniquee(entity, baseIndemnisation);

        // Si les jours d'interruption sont négatif ==> Erreur
        if (Integer.valueOf(entity.getJoursInterruption()) < 0) {
            throw new ServiceBusinessException(translate("ERROR_ABSENCE_JOURS_INTERRUPTION_INFERIEUR_ZERO"));
        }

        try {
            JadePersistenceManager.update(entity.getAbsence());
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        // Mise à jour de la base d'indemnisation
        updateBaseIndemnisation(entity.getIdBaseIndemnisation());
        return entity;
    }

    /**
     * Calcul et mise à jour de la base d'indemnisation
     * 
     * @param idBaseIndemnisation
     * @throws ServiceTechnicalException
     */
    private void updateBaseIndemnisation(String idBaseIndemnisation) throws ServiceTechnicalException {
        try {
            IJSimpleBaseIndemnisation baseIndemnisation = IJBaseIndemnisationAjaxHelper.actualise(idBaseIndemnisation);
            IJBaseIndemnisationService baseIndemnisationService = IJServiceLocator.getBaseIndemnisationService();
            baseIndemnisationService.update(baseIndemnisation);
        } catch (Exception e) {
            throw new ServiceTechnicalException(e.toString(), e);
        }
    }

    private void validateEntity(IJAbsence absence) throws ServiceBusinessException, ServiceTechnicalException {
        // Check code absence
        if (JadeStringUtil.isBlank(absence.getCodeAbsence())) {
            throw new ServiceBusinessException(translate("ERROR_CODE_SYSTEM_IS_EMPTY"));
        }
        // Date de début
        if (!JadeDateUtil.isGlobazDate(absence.getDateDeDebut())) {
            throw new ServiceBusinessException(translate("ERROR_START_DATE_IS_EMPTY"));
        }
        // Date de fin
        if (!JadeDateUtil.isGlobazDate(absence.getDateDeFin())) {
            throw new ServiceBusinessException(translate("ERROR_END_DATE_IS_EMPTY"));
        }
        // Précédence de la date de début
        if (!JadeDateUtil.isDateAfter(absence.getDateDeFin(), absence.getDateDeDebut())) {
            // Cela peut être normal uniquement dans le cas ou c'est la même
            // date (absence d'un jour)
            if (!JadeDateUtil.areDatesEquals(absence.getDateDeFin(), absence.getDateDeDebut())) {
                throw new ServiceBusinessException(translate("ERROR_START_DATE_IS_NOT_BEFORE_END_DATE"));
            }
        }

        // Nombre de jours saisis
        if (!JadeStringUtil.isEmpty(absence.getJoursSaisis())) {

            Integer joursSaisis = null;
            try {
                joursSaisis = Integer.valueOf(absence.getJoursSaisis());
            } catch (NumberFormatException exception) {
                throw new ServiceBusinessException(translate("ERROR_INVALID_VALUE") + " : "
                        + translate("JSP_SAISIE_ABSENCE_NOMBRE_JOURS_SAISIS") + " = " + joursSaisis);
            }
            if (joursSaisis < 0) {
                throw new ServiceBusinessException(translate("ERROR_INVALID_VALUE") + " : "
                        + translate("JSP_SAISIE_ABSENCE_NOMBRE_JOURS_SAISIS") + " = " + joursSaisis);
            }
        }
        // Non payé saisis
        if (!JadeStringUtil.isEmpty(absence.getJoursNonPayeSaisis())) {

            Integer joursNonPayeSaisis = null;
            try {
                joursNonPayeSaisis = Integer.valueOf(absence.getJoursNonPayeSaisis());
            } catch (NumberFormatException exception) {
                throw new ServiceBusinessException(translate("ERROR_INVALID_VALUE") + " : "
                        + translate("JSP_SAISIE_ABSENCE_NON_PAYES_SAISIS") + " = " + joursNonPayeSaisis);
            }
            if (joursNonPayeSaisis < 0) {
                throw new ServiceBusinessException(translate(translate("ERROR_INVALID_VALUE") + " : "
                        + translate("JSP_SAISIE_ABSENCE_NON_PAYES_SAISIS") + " = " + joursNonPayeSaisis));
            }
        }
        controlDesChevauchement(absence);
    }

    @Override
    public String verifierSiSoldeNegatifApresSaisieAbsence(String idDossierControle, String idBaseIndemnisation,
            String idAbsence, String codeAbsence, String dateDeDebut, String dateDeFin, String jourNonPayeSaisis,
            String joursSaisis, String mode) throws ServiceBusinessException, ServiceTechnicalException {

        Integer soldeApresSaisie = null;
        IJAbsence persistedAbsence = null;
        boolean wantUpdateValeursSaisies = false;

        joursSaisis = joursSaisis.trim();
        jourNonPayeSaisis = jourNonPayeSaisis.trim();

        // création d'une absence avec les paramètres reçus
        IJAbsence absence = buildIJAbsence(idDossierControle, idBaseIndemnisation, codeAbsence, dateDeDebut, dateDeFin,
                jourNonPayeSaisis, joursSaisis);

        // on s'assure qu'il existe une base d'indemnisation liée aux absences et qu'elle ne soit pas annulée
        IJSimpleBaseIndemnisation baseIndemnisation = null;
        baseIndemnisation = findBaseIndemnisation(absence.getIdBaseIndemnisation());

        if (!checkBaseIndemnisationPourAjoutAbsence(baseIndemnisation)) {
            throw new ServiceBusinessException(translate("ERROR_IMPOSSIBLE_AJOUTER_ABSENCE_BASE_INDEMNISATION_BLOQUE"));
        }

        // on vérifie que l'absence est valide
        validateEntity(absence);

        // si mode "update" on charge l'absence actuellement persistée en DB
        if (MODE_UPDATE.equals(mode)) {
            persistedAbsence = read(idAbsence);
            wantUpdateValeursSaisies = mustUpdateValeursSaisies(absence, persistedAbsence);
        } else {
            wantUpdateValeursSaisies = true;
        }

        // met à jour l'absence en fonction des règles métier
        computeEntityValues(absence, wantUpdateValeursSaisies);

        // recherche des périodes de contrôle du dossier
        IJPeriodeControleAbsencesSearchModel periodesControlSearchModel = findPeriodeControleAbsenceForIdDossier(idDossierControle);

        // itération sur toutes les périodes de contrôle du dossier
        for (JadeAbstractModel periodeControle : periodesControlSearchModel.getSearchResults()) {
            IJPeriodeControleAbsences currentPeriodeControle = (IJPeriodeControleAbsences) periodeControle;

            // Si l'absence appartient à la période courante, on calcul le solde
            JadePeriodWrapper currentPeriodeControleWrapper = new JadePeriodWrapper(
                    currentPeriodeControle.getDateDeDebut(), currentPeriodeControle.getDateDeFin());
            if (isPeriodeForAbsence(absence, currentPeriodeControleWrapper)) {
                soldeApresSaisie = calculerSoldeApresSaisie(mode, soldeApresSaisie, persistedAbsence, absence,
                        currentPeriodeControle);
            }
        }

        // si le solde est négatif on retourne un message d'erreur indiquant que le nombre de jours maladie/accident est
        // épuisé sinon on retourne null
        if (soldeApresSaisie != null && soldeApresSaisie < 0) {
            return translate("WARNING_SOLDE_PERIODE_CONTROLE_NEGATIF");
        } else {
            return null;
        }
    }

    /**
     * Retourne le solde qui sera effectif après la saisie de l'absence
     * 
     * @param mode
     * @param solde
     * @param persistedAbsence
     * @param absence
     * @param currentPeriodeControle
     * @return
     */
    private Integer calculerSoldeApresSaisie(String mode, Integer solde, IJAbsence persistedAbsence, IJAbsence absence,
            IJPeriodeControleAbsences currentPeriodeControle) {
        int persistedSoldePeriode = Integer.parseInt(currentPeriodeControle.getSolde());
        int joursPayesSolde = 0;
        if (currentPeriodeControle.getJoursPayesSolde() != null) {
            joursPayesSolde = Integer.parseInt(currentPeriodeControle.getJoursPayesSolde());

            if (MODE_UPDATE.equals(mode)) {
                int persistedJoursInterruptionAbsence = Integer.parseInt(persistedAbsence.getJoursInterruption());
                int diffJoursInterruptionNewAndPersistedAbsence = persistedJoursInterruptionAbsence
                        - Integer.parseInt(absence.getJoursInterruption());
                solde = Integer.valueOf(persistedSoldePeriode)
                        + (joursPayesSolde + diffJoursInterruptionNewAndPersistedAbsence);
            } else {
                int joursInterruption = Integer.parseInt(absence.getJoursInterruption());
                if (persistedSoldePeriode < 0) {
                    solde = Integer.valueOf(persistedSoldePeriode) + (joursPayesSolde + joursInterruption);
                } else {
                    solde = Integer.valueOf(persistedSoldePeriode) - (joursPayesSolde + joursInterruption);
                }
            }
        }
        return solde;
    }

    private boolean mustUpdateValeursSaisies(IJAbsence newAbsence, IJAbsence oldAbsence) {
        boolean wantUpdateValeursSaisies = false;

        // Si la période est différente, on force la recopie des valeurs calculées dans les données saisies
        if (!newAbsence.getDateDeDebut().equals(oldAbsence.getDateDeDebut())
                || !newAbsence.getDateDeFin().equals(oldAbsence.getDateDeFin())) {
            wantUpdateValeursSaisies = true;
        }

        // Si le code d'absence est différent, on force la recopie des valeurs calculées dans les données saisies
        if (!newAbsence.getCodeAbsence().equals(oldAbsence.getCodeAbsence())) {
            wantUpdateValeursSaisies = true;
        }

        return wantUpdateValeursSaisies;
    }

    /**
     * Retourne toutes les périodes de contrôle pour l'idDossier
     * 
     * @param idDossierControle
     * @return
     * @throws ServiceTechnicalException
     */
    private IJPeriodeControleAbsencesSearchModel findPeriodeControleAbsenceForIdDossier(String idDossierControle)
            throws ServiceTechnicalException {
        IJPeriodeControleAbsencesService periodeControleService = null;
        try {
            periodeControleService = IJServiceLocator.getControlePeriodeService();
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        }

        IJPeriodeControleAbsencesSearchModel periodesControlSearchModel = null;
        IJPeriodeControleAbsencesSearchModel search = new IJPeriodeControleAbsencesSearchModel();
        search.setForIdDossier(idDossierControle);
        try {
            periodesControlSearchModel = periodeControleService.search(search);
        } catch (JadeApplicationException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.toString(), e);
        }

        return periodesControlSearchModel;
    }

    /**
     * Retourne true si l'absence passée en paramètre appartient à la période passée en paramètre
     * 
     * @param absence
     * @param currentPeriodeControleWrapper
     * @return
     */
    private boolean isPeriodeForAbsence(IJAbsence absence, JadePeriodWrapper currentPeriodeControleWrapper) {
        return currentPeriodeControleWrapper.isDateDansLaPeriode(absence.getAbsence().getDateDeDebut())
                && currentPeriodeControleWrapper.isDateDansLaPeriode(absence.getAbsence().getDateDeFin());
    }

    /**
     * Construit une IJAbsence avec les paramètres passés en arguments. Rien n'est persisté !
     * 
     * @param idDossierControle
     * @param idBaseIndemnisation
     * @param codeAbsence
     * @param dateDeDebut
     * @param dateDeFin
     * @param jourNonPayeSaisis
     * @param joursSaisis
     * @return
     */
    private IJAbsence buildIJAbsence(String idDossierControle, String idBaseIndemnisation, String codeAbsence,
            String dateDeDebut, String dateDeFin, String jourNonPayeSaisis, String joursSaisis) {
        IJSimpleAbsence simpleAbsence = new IJSimpleAbsence();
        simpleAbsence.setIdDossierControle(idDossierControle);
        simpleAbsence.setIdBaseIndemnisation(idBaseIndemnisation);
        simpleAbsence.setCodeAbsence(codeAbsence);
        simpleAbsence.setDateDeDebut(dateDeDebut);
        simpleAbsence.setDateDeFin(dateDeFin);
        simpleAbsence.setJoursNonPayeSaisis(jourNonPayeSaisis);
        simpleAbsence.setJoursSaisis(joursSaisis);

        IJAbsence absence = new IJAbsence();
        absence.setAbsence(simpleAbsence);

        return absence;
    }
}
