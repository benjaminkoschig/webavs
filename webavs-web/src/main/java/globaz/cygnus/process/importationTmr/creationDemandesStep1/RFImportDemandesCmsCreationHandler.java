package globaz.cygnus.process.importationTmr.creationDemandesStep1;

import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import com.google.gson.Gson;
import globaz.cygnus.RFCodeTraitementDemandeTmrCleEnum;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeMai13;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.cygnus.process.importationTmr.RFProcessImportationTmrEnum;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RFImportDemandesCmsCreationHandler implements JadeProcessEntityInterface, JadeProcessEntityNeedProperties,
        JadeProcessEntityPropertySavable<RFProcessImportationTmrEnum>,
        JadeProcessEntityDataFind<RFProcessImportationTmrEnum> {

    public static final String ESPACE = " ";
    private static final String NSS_PREFIXE = "756";
    public static final String NUMERO = " n°: ";
    public static final String PARENTHESE_FERMEE = ")";
    public static final String PARENTHESE_OUVERTE = " (";
    private static final String POINT = ".";
    public static final String SEPARATEUR_MESSAGE_ERREUR = ";";
    public static final String SEPARATEUR_MESSAGE_ERREUR_CODE = "-";
    private static final String DATE_DEBUT_TRAITEMENT = "Date de début de traitement";
    private static final String DATE_FIN_TRAITEMENT = "Date de fin de traitement";
    private static final String DATE_FACTURE = "Date de facture";

    public static StringBuffer formatNssFromEntity(String nssBeneficiaireEntity) {
        StringBuffer nssBeneficiaireFormatte = new StringBuffer();

        String sub_1_5 = nssBeneficiaireEntity.substring(1, 5);
        String sub_5_9 = nssBeneficiaireEntity.substring(5, 9);
        String sub_9_11 = nssBeneficiaireEntity.substring(9, 11);

        if (!JadeStringUtil.isBlank(sub_1_5) && !JadeStringUtil.isBlank(sub_5_9) && !JadeStringUtil.isBlank(sub_9_11)) {

            nssBeneficiaireFormatte.append(RFImportDemandesCmsCreationHandler.NSS_PREFIXE);
            nssBeneficiaireFormatte.append(RFImportDemandesCmsCreationHandler.POINT);
            nssBeneficiaireFormatte.append(sub_1_5);
            nssBeneficiaireFormatte.append(RFImportDemandesCmsCreationHandler.POINT);
            nssBeneficiaireFormatte.append(sub_5_9);
            nssBeneficiaireFormatte.append(RFImportDemandesCmsCreationHandler.POINT);
            nssBeneficiaireFormatte.append(sub_9_11);

        }

        return nssBeneficiaireFormatte;
    }

    public static boolean isPasPremierNiDerniereLigne(RFImportDemandesCmsData entityData) {
        return !(entityData.isPremiereLigne() || entityData.isDerniereLigne());
    }

    private JACalendar cal = new JACalendarGregorian();
    private Map<RFProcessImportationTmrEnum, String> dataToSave = new HashMap<RFProcessImportationTmrEnum, String>();
    private JadeProcessEntity entity = null;
    private RFImportDemandesCmsData entityData = null;
    private boolean hasErrors = false;
    private String idDemande = "";
    private String idExecutionProcess = "";
    private String idTiersAf = "";
    private List<String[]> logsList = new ArrayList<String[]>();
    private List<String[]> messagesErreurImportations = new ArrayList<String[]>();

    private String nssBeneficiaireFormatte = "";

    private Map<Enum<?>, String> properties = null;

    public RFImportDemandesCmsCreationHandler(List<String[]> logsList, String idTiersAf, String idExecutionProcess) {
        super();
        this.logsList = logsList;
        this.idTiersAf = idTiersAf;
        this.idExecutionProcess = idExecutionProcess;
    }

    private String addDemandeRFM(String dateDebutTraitementFormatte, String dateFinTraitementFormattee,
            String dateFactureFormattee, String montantAPayerFormatte, String idDossier, String idTiersEquipe)
            throws JadePersistenceException {

        try {

            RFDemande rfDemande = new RFDemande();
            rfDemande.setSession(BSessionUtil.getSessionFromThreadContext());

            rfDemande.setIdGestionnaire(entityData.getGestionnaire());

            rfDemande.setIdSousTypeDeSoin(IRFTypesDeSoins.st_3_TMR);

            rfDemande.setIdDossier(idDossier);

            rfDemande.setDateFacture(dateFactureFormattee);
            rfDemande.setDateReception(JACalendar.todayJJsMMsAAAA());
            rfDemande.setDateDebutTraitement(dateDebutTraitementFormatte);
            rfDemande.setDateFinTraitement(dateFinTraitementFormattee);

            rfDemande.setMontantAPayer(montantAPayerFormatte);
            rfDemande.setMontantFacture(montantAPayerFormatte);

            rfDemande.setIdAdressePaiement(idTiersAf);

            rfDemande.setIdFournisseur(idTiersEquipe);

            rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
            rfDemande.setCsSource(IRFDemande.SYSTEME);

            rfDemande.setIsForcerPaiement(Boolean.TRUE);

            rfDemande.setIdExecutionProcess(idExecutionProcess);

            rfDemande.setSpy(new BSpy("importationRfmTmr" + JACalendar.today().toStrAMJ()));

            rfDemande.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

            RFDemandeMai13 rfDema13 = new RFDemandeMai13();
            rfDema13.setSession(BSessionUtil.getSessionFromThreadContext());

            rfDema13.setNombreHeure("");
            rfDema13.setIdDemandeMaintienDom13(rfDemande.getIdDemande());

            rfDema13.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

            logInfos("RFImportDemandesCmsCreationHandler.addDemandeRFM()", BSessionUtil.getSessionFromThreadContext()
                    .getLabel("RF_IMPORT_TMR_PROCESS_CREATION_DEMANDE")
                    + RFImportDemandesCmsCreationHandler.ESPACE
                    + rfDemande.getIdDemande());

            return rfDemande.getIdDemande();

        } catch (Exception e) {
            logErreurs("RFImportDemandesCmsCreationHandler.addDemandeRFM()", BSessionUtil.getSessionFromThreadContext()
                    .getLabel("RF_IMPORT_TMR_DONNEE_INCOHERENTE")
                    + RFImportDemandesCmsCreationHandler.PARENTHESE_OUVERTE
                    + e.getMessage()
                    + RFImportDemandesCmsCreationHandler.PARENTHESE_FERMEE, true,
                    RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            throw new JadePersistenceException(e.getMessage() + " [RFImportDemandesCmsCreationHandler.addDemandeRFM()]");
        }
    }

    private StringBuffer buildMessagesErreursToSave() {
        StringBuffer messageErreurStb = new StringBuffer();
        for (String[] message : messagesErreurImportations) {
            messageErreurStb.append(message[0] + RFImportDemandesCmsCreationHandler.SEPARATEUR_MESSAGE_ERREUR_CODE
                    + message[1] + RFImportDemandesCmsCreationHandler.SEPARATEUR_MESSAGE_ERREUR);
        }
        return messageErreurStb;
    }

    private String getAdministrationEquipeParCode(String code) {

        if (JadeStringUtil.isBlankOrZero(code)) {
            logErreurs("getAdministrationEquipeParCode()",
                    BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_NUMERO_EQUIPE_VIDE"), true,
                    RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            return null;
        }

        PRTiersWrapper[] administrationCms = null;
        try {
            BISession cygnusSession = GlobazSystem.getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS)
                    .newSession();
            administrationCms = PRTiersHelper.getAdministrationActiveForGenreAndCode(
                    BSessionUtil.getSessionFromThreadContext(), RFPropertiesUtils.getCsGenreAdminstrationCMS(), code);
        } catch (Exception e) {
            logErreurs("getAdministrationEquipeParCode()",
                    BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_DONNEE_INCOHERENTE")
                            + RFImportDemandesCmsCreationHandler.PARENTHESE_OUVERTE + e.getMessage()
                            + RFImportDemandesCmsCreationHandler.PARENTHESE_FERMEE, true,
                    RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            return null;
        }

        if ((administrationCms == null) || (administrationCms.length == 0)) {
            String message = MessageFormat.format(
                    BSessionUtil.getSessionFromThreadContext().getLabel(
                            "RF_IMPORT_TMR_ADMINISTRATION_EQUIPE_INEXISANT"), code);
            logErreurs("getAdministrationEquipeParCode()", message, true,
                    RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            return null;

        } else {
            String idTiers = administrationCms[0].getIdTiers();
            if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                return idTiers;
            } else {
                String message = MessageFormat.format(
                        BSessionUtil.getSessionFromThreadContext().getLabel(
                                "RF_IMPORT_TMR_ADMINISTRATION_EQUIPE_INEXISANT"), code);
                logErreurs("getAdministrationAfParCode()", message, true,
                        RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
                return null;
            }
        }

    }

    @Override
    public Map<RFProcessImportationTmrEnum, String> getValueToSave() {
        return dataToSave;
    }

    private boolean hasDateSeptPositions() {
        return !JadeStringUtil.isBlankOrZero(entityData.getMontantDemande())
                && (entityData.getMontantDemande().length() == 7);
    }

    private void logErreurs(String source, String message, boolean isErreurImportation, String codeErreurImportation)
            throws JadeNoBusinessLogSessionError {
        // Les entités ne doivent pas être en erreur, car celles-ci sont réutilisées dans les étapes suivantes
        JadeThread.logWarn(source, message);
        hasErrors = true;
        RFUtils.ajouterLogImportationsTmr(JadeBusinessMessageLevels.ERROR, entityData.getNumeroLigne(),
                nssBeneficiaireFormatte, message,  logsList);

        if (isErreurImportation) {
            messagesErreurImportations.add(new String[] { message, codeErreurImportation });
        }
    }

    private void logInfos(String source, String message) throws JadeNoBusinessLogSessionError {
        JadeThread.logInfo(source, message);
        RFUtils.ajouterLogImportationsTmr(JadeBusinessMessageLevels.WARN, entityData.getNumeroLigne(),
                nssBeneficiaireFormatte, message, logsList);
    }

    private void isDateDebutAnterieurDateFin(String dateDebut, String dateFin) {
        if (!JadeStringUtil.isBlankOrZero(dateDebut) && !JadeStringUtil.isBlankOrZero(dateFin)) {
            try {
                JADate jaDateDebut = new JADate(dateDebut);
                JADate jaDateFin = new JADate(dateFin);

                if (cal.compare(jaDateDebut, jaDateFin) == JACalendar.COMPARE_FIRSTUPPER) {
                    String message = BSessionUtil.getSessionFromThreadContext().getLabel(
                            "ERREUR_RF_DEM_S_DATE_FIN_ANTERIEUR_DATE_DEBUT_TRAITEMENT");
                    logErreurs("RFImportDemandesCmsCreationHandler.isDateDebutAnterieurDateFin()", message, true,
                            RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
                }

            } catch (JAException e) {
                logErreurs("RFImportDemandesCmsCreationHandler.isDateDebutAnterieurDateFin()", e.getMessage(), true,
                        RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            }
        }

    }

    private String retrieveAndCheckDateFormattee(String date, String champ) {

        try {
            return PRDateFormater.convertDate_AAMMJJ_to_JJxMMxAAAA(date);
        } catch (JAException e) {
            String message = MessageFormat
                    .format(BSessionUtil.getSessionFromThreadContext().getLabel(
                            "RF_IMPORT_TMR_MAUVAIS_FORMAT_DATE_OU_VIDE"), champ);
            logErreurs("RFImportDemandesCmsCreationHandler.retrieveAndCheckDateFormattee()", message, true,
                    RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            return null;
        }
    }

    private String retrieveAndCheckIdDossier() {

        try {

            nssBeneficiaireFormatte = retrieveAndCheckNssBeneficiaireFormatte();

            if (!JadeStringUtil.isBlankOrZero(nssBeneficiaireFormatte)) {

                // Recherche, création du dossier
                PRTiersWrapper prTieWra = PRTiersHelper.getTiers(BSessionUtil.getSessionFromThreadContext(),
                        nssBeneficiaireFormatte);
                if (prTieWra != null) {
                    RFPrDemandeJointDossier rfPrDemJoiDos = RFUtils.getDossierJointPrDemande(prTieWra.getIdTiers(),
                            BSessionUtil.getSessionFromThreadContext());
                    if (null != rfPrDemJoiDos) {
                        return rfPrDemJoiDos.getIdDossier();
                    } else {
                        // Création du dossier
                        return RFUtils.ajouterDossier(prTieWra.getIdTiers(), entityData.getGestionnaire(), BSessionUtil
                                .getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                                .getCurrentThreadTransaction());
                    }
                } else {
                    String message = MessageFormat.format(
                            BSessionUtil.getSessionFromThreadContext().getLabel(
                                    "RF_IMPORT_TMR_PROCESS_TIERS_INEXISTANT_WEB_TIERS"), nssBeneficiaireFormatte);
                    logErreurs("RFImportDemandesCmsCreationHandler.retrieveIdDossier()", message, true,
                            RFCodeTraitementDemandeTmrCleEnum.NSS_PAS_TROUVE.getCode());
                    return null;
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            String message = MessageFormat.format(
                    BSessionUtil.getSessionFromThreadContext().getLabel(
                            "RF_IMPORT_TMR_PROCESS_TIERS_INEXISTANT_WEB_TIERS"), nssBeneficiaireFormatte);
            logErreurs("RFImportDemandesCmsCreationHandler.retrieveIdDossier()", message
                    + RFImportDemandesCmsCreationHandler.PARENTHESE_OUVERTE + e.getMessage()
                    + RFImportDemandesCmsCreationHandler.PARENTHESE_FERMEE, true,
                    RFCodeTraitementDemandeTmrCleEnum.NSS_PAS_TROUVE.getCode());
            return null;
        }
    }

    private String retrieveAndCheckIdTiersEquipe() {
        if (!JadeStringUtil.isBlankOrZero(entityData.getNumeroEquipe())) {
            return getAdministrationEquipeParCode(entityData.getNumeroEquipe());
        } else {
            logErreurs("RFImportDemandesCmsCreationHandler.retrieveAndCheckIdTiersEquipe()", BSessionUtil
                    .getSessionFromThreadContext().getLabel("LABEL_RF_IMPORT_TMR_NUMERO_EQUIPE_VIDE_FR"), true,
                    RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            return null;
        }
    }

    private String retrieveAndCheckMontantSurSeptPositionsFormatte() {

        if (hasDateSeptPositions()) {

            StringBuffer montant = new StringBuffer();
            montant.append(entityData.getMontantDemande().substring(0, 5));
            montant.append(RFImportDemandesCmsCreationHandler.POINT);
            montant.append(entityData.getMontantDemande().substring(5, 7));

            return new BigDecimal(montant.toString()).toString();
        } else {
            logErreurs("RFImportDemandesCmsCreationHandler.getMontantSurSeptPositionsFormatte()", BSessionUtil
                    .getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_ERREUR_MONTANT"), true,
                    RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            return null;
        }
    }

    /**
     * 
     * Retourne le nss formatte
     * 
     * @return true si nss valide, false si non valide
     */
    private String retrieveAndCheckNssBeneficiaireFormatte() {

        StringBuffer nssBeneficiaireFormatte = RFImportDemandesCmsCreationHandler.formatNssFromEntity(entityData
                .getNssBeneficiaire());

        if (nssBeneficiaireFormatte.length() > 0) {

            if (RFUtils.isNNS(nssBeneficiaireFormatte.toString())) {
                return nssBeneficiaireFormatte.toString();
            } else {
                logErreurs("RFImportDemandesCmsCreationHandler.getNssBeneficiaireFormatte()", BSessionUtil
                        .getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_NSS_BENEFICIAIRE_NON_VALIDE"), true,
                        RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
                return "";
            }

        } else {
            logErreurs("RFImportDemandesCmsCreationHandler.getNssBeneficiaireFormatte()", BSessionUtil
                    .getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_NSS_BENEFICIAIRE_NON_VALIDE"), true,
                    RFCodeTraitementDemandeTmrCleEnum.DONNEES_INCOHERENTES.getCode());
            return "";
        }
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        if (RFImportDemandesCmsCreationHandler.isPasPremierNiDerniereLigne(entityData)) {

            // On veut récuperer toutes les erreurs de la ligne -> on ne retourne pas d'exceptions
            String idDossier = retrieveAndCheckIdDossier();
            String dateDebutTraitementFormatte = retrieveAndCheckDateFormattee(entityData.getDateDeDebutTraitement(),
                    DATE_DEBUT_TRAITEMENT);
            String dateFinTraitementFormattee = retrieveAndCheckDateFormattee(entityData.getDateDeFinTraitement(),
                    DATE_FIN_TRAITEMENT);
            if (!JadeStringUtil.isBlankOrZero(dateDebutTraitementFormatte)
                    && !JadeStringUtil.isBlankOrZero(dateFinTraitementFormattee)) {
                isDateDebutAnterieurDateFin(dateDebutTraitementFormatte, dateFinTraitementFormattee);
            }

            String dateFactureFormattee = retrieveAndCheckDateFormattee(entityData.getDateFacture(), DATE_FACTURE);
            String montantAPayerFormatte = retrieveAndCheckMontantSurSeptPositionsFormatte();
            String idTiersEquipe = retrieveAndCheckIdTiersEquipe();

            if (!(hasErrors || JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR))) {
                idDemande = addDemandeRFM(dateDebutTraitementFormatte, dateFinTraitementFormattee,
                        dateFactureFormattee, montantAPayerFormatte, idDossier, idTiersEquipe);
            }

            setDataToSave(idDemande);
        }
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {

        if (entity != null) {
            this.entity = entity;

            Gson gson = new Gson();
            entityData = gson.fromJson(this.entity.getValue1(), RFImportDemandesCmsData.class);
        } else {
            JadeThread.logError("RFImportDemandesCmsCreationHandler.setCurrentEntity()", "Entity is null");
        }
    }

    @Override
    public void setData(Map<RFProcessImportationTmrEnum, String> hashMap) {
        // TODO Auto-generated method stub

    }

    private void setDataToSave(String value) {
        String messagesErreurs = buildMessagesErreursToSave().toString();
        if (!JadeStringUtil.isBlankOrZero(messagesErreurs)) {
            dataToSave.put(RFProcessImportationTmrEnum.MESSAGES_ERREUR_IMPORTATION, messagesErreurs);
        }

        if (!JadeStringUtil.isBlankOrZero(value)) {
            dataToSave.put(RFProcessImportationTmrEnum.ID_DEMANDE, value);
        }
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

}
