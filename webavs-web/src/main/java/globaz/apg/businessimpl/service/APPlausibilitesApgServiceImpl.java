package globaz.apg.businessimpl.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import globaz.apg.application.APApplication;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.business.service.APPlausibilitesApgService;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.annonces.APAnnonceAPGManager;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.enums.APAllPlausibiliteRules;
import globaz.apg.enums.APBreakableRules;
import globaz.apg.enums.APStepSendAnnonceRules;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.exceptions.APPlausibilitesException;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APRuleFactoryException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.pojo.APErreurValidationPeriode;
import globaz.apg.pojo.APValidationPrestationAPGContainer;
import globaz.apg.pojo.ViolatedRule;
import globaz.apg.properties.APParameter;
import globaz.apg.rapg.rules.Rule;
import globaz.apg.rapg.rules.RulesFactory;
import globaz.apg.utils.APGUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.utils.PRDateUtils;

/**
 * @author dde
 */
public class APPlausibilitesApgServiceImpl implements APPlausibilitesApgService {

    private static final String PREFIX_LABEL = "APG_RULE_";

    /**
     * Méthode commune de check des plausibilités
     *
     * @param rules
     * @param champsAnnonce
     * @return
     * @throws APPlausibilitesException
     */
    private ArrayList<ViolatedRule> check(BSession session, List<String> rules, APChampsAnnonce annonce)
            throws APPlausibilitesException {
        ArrayList<ViolatedRule> listErrors = new ArrayList<ViolatedRule>();

        String code = null;
        Rule rule = null;
        try {
            for (String r : rules) {
                code = r.substring(r.indexOf("_") + 1);
                rule = RulesFactory.getRule(code, session);
                try {
                    if (!isRuleInSkipListFerciab(rule, annonce) && !rule.check(annonce)) {
                        if (ruleConcernePlageValeurs(rule)) {
                            listErrors.add(getViolatedRuleDetail(session, rule.getErrorCode(), annonce));
                        } else {
                            listErrors.add(getViolatedRuleDetail(session, rule.getErrorCode()));
                        }
                    }
                }
                // Survient dans le cas ou un champs contrôlé par une règle est invalid
                catch (IllegalArgumentException e) {
                    StringBuilder message = new StringBuilder();
                    String mes1 = session.getLabel("EXECUTION_REGLE_IMPOSSIBLE_EXCEPTION");
                    mes1 = mes1.replace("{0}", rule.getErrorCode());
                    message.append(getViolatedRuleDetail(session, rule.getErrorCode()).getErrorMessage());
                    message.append("</br><strong>");
                    message.append(mes1);
                    message.append("</strong>");
                    message.append(" : </br>");
                    message.append(e.toString());
                    ViolatedRule violatedRule = new ViolatedRule(rule.getErrorCode(), message.toString(),
                            rule.isBreakable());
                    violatedRule.setFatalErrorThrown(true);
                    listErrors.add(violatedRule);
                }
                // Survient en cas d'erreur interne à la règle lors de son l'exection
                catch (APRuleExecutionException e) {
                    String message = getViolatedRuleDetail(session, rule.getErrorCode()).getErrorMessage();
                    message += "</br><strong>" + e.toString() + "</strong>";
                    ViolatedRule violatedRule = new ViolatedRule(rule.getErrorCode(), message, rule.isBreakable());
                    violatedRule.setFatalErrorThrown(true);
                    listErrors.add(violatedRule);
                }
            }
        }

        // Survient dans le cas ou une erreur survient lors de la création d'une instance de règle
        catch (APRuleFactoryException e) {
            throw new APPlausibilitesException(
                    "Exception pendant l'instantiation de la règle [" + code + "] : " + e.toString(), e);
        } catch (Throwable t) {
            JadeLogger.error(this, t);
        }
        return listErrors;
    }

    private boolean isRuleInSkipListFerciab(Rule rule, APChampsAnnonce annonce) {
        String hasComplement = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);
        if (!"true".equals(hasComplement)) {
            return false;
        } else if (APGUtils.isTypeAnnonceJourIsole(annonce.getServiceType()) && ruleInSkipList(rule)) {
            return true;
        }
        return false;
    }

    private boolean ruleInSkipList(Rule rule) {
        return APAllPlausibiliteRules.R_307.getCodeAsString().equals(rule.getErrorCode())
                || APAllPlausibiliteRules.R_321.getCodeAsString().equals(rule.getErrorCode());
    }

    private boolean ruleConcernePlageValeurs(Rule rule) {
        return APAllPlausibiliteRules.R_54.getCodeAsString().equals(rule.getErrorCode())
                || APAllPlausibiliteRules.R_55.getCodeAsString().equals(rule.getErrorCode())
                || APAllPlausibiliteRules.R_56.getCodeAsString().equals(rule.getErrorCode())
                || APAllPlausibiliteRules.R_57.getCodeAsString().equals(rule.getErrorCode())
                || APAllPlausibiliteRules.R_58.getCodeAsString().equals(rule.getErrorCode())
                || APAllPlausibiliteRules.R_59.getCodeAsString().equals(rule.getErrorCode())
                || APAllPlausibiliteRules.R_60.getCodeAsString().equals(rule.getErrorCode());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.business.services.plausibilites.PlausibilitesApgService#checkPlausisXSD(ch.globaz.apg.business.
     * models.annonces.APChampsAnnonce)
     */
    @Override
    public List<ViolatedRule> checkAnnonce(BSession session, APValidationPrestationAPGContainer container,
            PRTiersWrapper tiers) throws APPlausibilitesException {
        ArrayList<String> rules = new ArrayList<String>();
        for (APAllPlausibiliteRules rule : APAllPlausibiliteRules.values()) {
            rules.add(rule.toString());
        }
        APChampsAnnonce champsAnnonce = container.getAnnonce().toChampsAnnonce();
        champsAnnonce.setIdDroit(container.getDroit().getIdDroit());
        champsAnnonce.setInsurantBirthDate(tiers.getDateNaissance());
        champsAnnonce.setInsurantSexe(tiers.getSexe());
        List<ViolatedRule> validationErrors = check(session, rules, champsAnnonce);
        container.setValidationErrors(validationErrors);
        return validationErrors;
    }

    private List<String> checkDateComptableRule209(APAnnonceAPG annonceInitiale, APAnnonceAPG annonceCorrective,
            List<String> errorList) {

        // getAccountingMonth retourne mm.aaaa
        String dateComptableAnnonceCorrective = "01." + annonceCorrective.getAccountingMonth();
        String dateComptableAnnonceCorrigee = "01." + annonceInitiale.getAccountingMonth();

        switch (PRDateUtils.compare(dateComptableAnnonceCorrigee, dateComptableAnnonceCorrective)) {
            case EQUALS:
            case AFTER:
                // La date comptable de l'annonce corrective est >= à la date comptable de l'annonce corrigée --> ok
                break;
            case BEFORE:
                // La date comptable de l'annonce corrective est < à la date comptable de l'annonce corrigée --> ko
                errorList.add("XSD_MOIS_COMPTABLE_ANNONCE_CORRECTIVE_AVANT_MOIS_COMPTABLE_ANNONCE_CORRIGE");
                break;

            case INCOMPARABLE:
                // Cas anormal.... erreur de format de date ?!?
                errorList.add("XSD_ERREUR_LORS_VALIDATION_PLAUSIBILITE209");
                break;
        }

        return errorList;
    }

    @Override
    public List<String> checkPlausisXSD(APChampsAnnonce champsAnnonce, BSession session)
            throws APPlausibilitesException {
        List<String> errorList = new ArrayList<String>();

        // Contrôles fais sur tous les types de message
        if (JadeStringUtil.isEmpty(champsAnnonce.getSenderId())) {
            errorList.add("XSD_MANDATORY_SENDERID");
        }
        if (JadeStringUtil.isEmpty(champsAnnonce.getRecipientId())) {
            errorList.add("XSD_MANDATORY_RECIPIENTID");
        }
        if (JadeStringUtil.isEmpty(champsAnnonce.getMessageId())) {
            errorList.add("XSD_MANDATORY_MESSAGEID");
        }
        if (JadeStringUtil.isEmpty(champsAnnonce.getMessageType())
                || !APAnnoncesRapgService.messageType.equals(champsAnnonce.getMessageType())) {
            errorList.add("XSD_MANDATORY_MESSAGETYPE");
        }

        if (JadeStringUtil.isEmpty(champsAnnonce.getEventDate())) {
            errorList.add("XSD_MANDATORY_EVENTDATE");
        }
        if (JadeStringUtil.isEmpty(champsAnnonce.getAction())) {
            errorList.add("XSD_MANDATORY_ACTION");
        }
        if (JadeStringUtil.isEmpty(champsAnnonce.getDeliveryOfficeBranch())
                || JadeStringUtil.isEmpty(champsAnnonce.getDeliveryOfficeOfficeIdentifier())) {
            errorList.add("XSD_MANDATORY_DELIVERYOFFICE");
        }

        if (APAnnoncesRapgService.subMessageType1.equals(champsAnnonce.getSubMessageType())) {
            // Contrôle du sous-type de message
            if (!APAnnoncesRapgService.subMessageType1.equals(champsAnnonce.getSubMessageType())) {
                errorList.add("XSD_MANDATORY_SUBMESSAGETYPE");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getInsurant())) {
                errorList.add("XSD_MANDATORY_INSURANT");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getInsurantMaritalStatus())) {
                errorList.add("XSD_MANDATORY_INSURANTMARITALSTATUS");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getAccountingMonth())) {
                errorList.add("XSD_MANDATORY_ACCOUNTINGMONTH");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getServiceType())) {
                errorList.add("XSD_MANDATORY_SERVICETYPE");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getActivityBeforeService())) {
                errorList.add("XSD_MANDATORY_ACTIVITYBEFORESERVICE");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getAverageDailyIncome())) {
                errorList.add("XSD_MANDATORY_AVERAGEDAILYINCOME");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getStartOfPeriod())) {
                errorList.add("XSD_MANDATORY_STARTOFPERIOD");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getEndOfPeriod())) {
                errorList.add("XSD_MANDATORY_ENDOFPERIOD");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getNumberOfDays())) {
                errorList.add("XSD_MANDATORY_NUMBEROFDAYS");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getBasicDailyAmount())) {
                errorList.add("XSD_MANDATORY_BASICDAILYAMOUNT");
            }
            if (champsAnnonce.getDailyIndemnityGuaranteeAI() == null) {
                errorList.add("XSD_MANDATORY_DAILYINDEMNITYGUARANTEEAI");
            }
            if (champsAnnonce.getAllowanceFarm() == null) {
                errorList.add("XSD_MANDATORY_ALLOWANCEFARM");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getAllowanceCareExpenses())) {
                errorList.add("XSD_MANDATORY_ALLOWANCECAREEXPENSES");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getTotalAPG())) {
                errorList.add("XSD_MANDATORY_TOTALAPG");
            }
            if (JadeStringUtil.isEmpty(champsAnnonce.getPaymentMethod())) {
                errorList.add("XSD_MANDATORY_PAYMENTMETHOD");
            }

            checkRule208(champsAnnonce, errorList);

        } else if (isAnnonceDeType3Ou4(champsAnnonce)) {

            if (champsAnnonce.getMessageId().equals("109794")) {
                System.out.println();
            }

            if (JadeStringUtil.isEmpty(champsAnnonce.getTimeStamp())) {
                errorList.add("XSD_MANDATORY_TIMESTAMP");
            }
            tryToCheckRule209(champsAnnonce, session, errorList);
        }
        return errorList;
    }

    /**
     * Test lié à la plausibilité 208 :</br>
     * empêcher l'envoi des annonces si le mois du message date est < que
     * l'accountingMonth si on paie le 08.08.2012, envoi auto annonce -> accountingMonth = 08.2012, messageDate =
     * 08.2012
     *
     * @param champsAnnonce
     * @param errorList
     */
    private void checkRule208(final APChampsAnnonce champsAnnonce, final List<String> errorList) {
        String messageDate = champsAnnonce.getMessageDate();
        String accountingMonth = champsAnnonce.getAccountingMonth();
        if (!JadeStringUtil.isEmpty(accountingMonth)) {
            String accountingMonthPourTest = JadeDateUtil.getFirstDateOfMonth(accountingMonth);
            switch (PRDateUtils.compare(messageDate, accountingMonthPourTest)) {
                case EQUALS:
                case BEFORE:
                    // l'accountingMonth est antérieur (avant) le messageDate -> ok
                    // l'accountingMonth est égal au messageDate -> ok
                    break;
                case AFTER:
                    // l'accountingMonth est postérieur (après) le messageDate -> KO
                    errorList.add("XSD_PAS_PAIEMENT_ANTICIPE");
                    break;
                case INCOMPARABLE:
                    // Cas anormal.... erreur de format de date ?!?
                    errorList.add("XSD_ERREUR_LORS_VALIDATION_PLAUSIBILITE208");
                    break;
            }

        }
    }

    /**
     * Le mois comptable d'une annonce de type 3 (Correction) ou de type 4 (Restitution) ne peut pas être inférieur au
     * mois comptable de l'annonce initiale (Plausibilité 209)
     *
     * @param champsAnnonce
     * @param session
     * @param errorList
     * @throws Exception
     */
    private void tryToCheckRule209(final APChampsAnnonce champsAnnonce, BSession session,
            final List<String> errorList) {
        if (isAnnonceDeType3Ou4(champsAnnonce)) {
            if (isAccountingMonthSet(champsAnnonce)) {
                doCheckRule209(champsAnnonce, session, errorList);
            }
        }
    }

    private void doCheckRule209(final APChampsAnnonce champsAnnonce, final BSession session,
            final List<String> errorList) {

        boolean canCheckAccountingMonth = true;
        APAnnonceAPG annonceCorrective = null;
        APAnnonceAPGManager manager = null;
        try {
            annonceCorrective = new APAnnonceAPG();
            annonceCorrective.setSession(session);
            annonceCorrective.setId(champsAnnonce.getMessageId());
            annonceCorrective.retrieve();
            if (annonceCorrective.isNew()) {
                throw new APEntityNotFoundException(APAnnonceAPG.class, champsAnnonce.getMessageId());
            }

            manager = new APAnnonceAPGManager();
            manager.setSession(session);
            manager.setForBPID(champsAnnonce.getBusinessProcessId());
            manager.setForSubMessageType(APAnnoncesRapgService.subMessageType1);
            manager.find();

        } catch (Exception exception) {
            canCheckAccountingMonth = false;
        }

        if (canCheckAccountingMonth && (manager.getContainer().size() == 1)) {
            APAnnonceAPG annonceInitial = (APAnnonceAPG) manager.getFirstEntity();
            checkDateComptableRule209(annonceInitial, annonceCorrective, errorList);
        }
    }

    /**
     * @param champsAnnonce
     * @return
     */
    private boolean isAccountingMonthSet(final APChampsAnnonce champsAnnonce) {
        return !JadeStringUtil.isEmpty(champsAnnonce.getAccountingMonth())
                && JadeDateUtil.isGlobazDate("01." + champsAnnonce.getAccountingMonth());
    }

    /**
     * @param champsAnnonce
     * @return
     */
    private boolean isAnnonceDeType3Ou4(final APChampsAnnonce champsAnnonce) {
        return APAnnoncesRapgService.subMessageType3.equals(champsAnnonce.getSubMessageType())
                || APAnnoncesRapgService.subMessageType4.equals(champsAnnonce.getSubMessageType());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.business.services.plausibilites.PlausibilitesApgService#checkStepSendAnnonce(ch.globaz.apg.business
     * .models.plausibilites.APChampsAnnonce)
     */
    @Override
    public List<ViolatedRule> checkStepSendAnnonce(BSession session, APChampsAnnonce champsAnnonce)
            throws APPlausibilitesException {
        ArrayList<String> rules = new ArrayList<String>();
        for (APStepSendAnnonceRules rule : APStepSendAnnonceRules.values()) {
            rules.add(rule.toString());
        }
        return check(session, rules, champsAnnonce);
    }

    /**
     * Contrôle qu'une prestation existe pour chaque périodes
     *
     * @param droit
     * @param periodes
     * @param prestations
     * @return
     */
    @Override
    public List<APErreurValidationPeriode> controllerPrestationEnFonctionPeriodes(BSession session, APDroitLAPG droit,
            List<APPeriodeAPG> periodesAPG, List<APPrestation> prestations) {
        List<APErreurValidationPeriode> resultsString = new ArrayList<APErreurValidationPeriode>();
        boolean[] results = new boolean[periodesAPG.size()];

        List<PRPeriode> periodes = new ArrayList<PRPeriode>();
        for (APPeriodeAPG p : periodesAPG) {
            PRPeriode per = new PRPeriode(p.getDateDebutPeriode(), p.getDateFinPeriode());
            periodes.add(per);
        }

        for (int ctr = 0; ctr < periodes.size(); ctr++) {
            PRPeriode periode = periodes.get(ctr);
            try {
                for (APPrestation prestation : prestations) {
                    if (PRDateUtils.isDateDansLaPeriode(periode, prestation.getDateDebut())
                            && PRDateUtils.isDateDansLaPeriode(periode, prestation.getDateFin())) {
                        results[ctr] = true;
                    }
                }
            } catch (Exception exception) {
                String message = session.getLabel("VALIDATION_PRESTATION_EXCEPTION_ANALYSE_DATES_PRESTATION");
                message = message.replace("{0}", periode.getDateDeDebut());
                message = message.replace("{1}", periode.getDateDeFin());
                resultsString.add(new APErreurValidationPeriode(periodesAPG.get(ctr), message));
            }
        }
        for (int ctr = 0; ctr < results.length; ctr++) {
            if (!results[ctr]) {
                String message = session.getLabel("VALIDATION_PRESTATION_AUCUNE_PRESTATION_POUR_PERIODE");
                message = message.replace("{0}", periodes.get(ctr).getDateDeDebut());
                message = message.replace("{1}", periodes.get(ctr).getDateDeFin());
                resultsString.add(new APErreurValidationPeriode(periodesAPG.get(ctr), message));
            }
        }
        return resultsString;
    }

    private String getRuleMessage(BSession session, String code) {

        return session.getLabel(APPlausibilitesApgServiceImpl.PREFIX_LABEL + code);
    }

    private String getRuleMessage(BSession session, String code, APChampsAnnonce annonce)
            throws APRuleExecutionException {
        String message = null;
        try {
            message = session.getLabel(APPlausibilitesApgServiceImpl.PREFIX_LABEL + code);
            String parametre = null;
            if (APAllPlausibiliteRules.R_54.getCodeAsString().equals(code)) {
                parametre = APParameter.NOMBRE_JOURS_ISOLES_DEMENAGEMENT.getParameterName();
            } else if (APAllPlausibiliteRules.R_55.getCodeAsString().equals(code)) {
                parametre = APParameter.NOMBRE_JOURS_ISOLES_NAISSANCE.getParameterName();
            } else if (APAllPlausibiliteRules.R_56.getCodeAsString().equals(code)) {
                parametre = APParameter.NOMBRE_JOURS_ISOLES_MARIAGE_LPART.getParameterName();
            } else if (APAllPlausibiliteRules.R_57.getCodeAsString().equals(code)) {
                parametre = APParameter.NOMBRE_JOURS_ISOLES_DECES.getParameterName();
            } else if (APAllPlausibiliteRules.R_58.getCodeAsString().equals(code)) {
                parametre = APParameter.NOMBRE_JOURS_ISOLES_INSPECTION_RECRUTEMENT_LIBERATION.getParameterName();
            } else if (APAllPlausibiliteRules.R_59.getCodeAsString().equals(code)) {
                parametre = APParameter.NOMBRE_JOURS_ISOLES_CONGE_JEUNESSE.getParameterName();
            } else if (APAllPlausibiliteRules.R_60.getCodeAsString().equals(code)) {
                parametre = APParameter.NOMBRE_JOURS_ISOLES_DECES_DEMI_JOUR.getParameterName();
            }
            if (parametre != null) {
                BigDecimal valPlage = new BigDecimal(FWFindParameter.findParameter(
                        session.getCurrentThreadTransaction(), "1", parametre, annonce.getStartOfPeriod(), "", 0));
                message = message.replace("{0}", valPlage.toString());
            }
        } catch (Exception e) {
            throw new APRuleExecutionException(e);
        }
        return message;
    }

    private ViolatedRule getViolatedRuleDetail(BSession session, String code) {
        return new ViolatedRule(code, getRuleMessage(session, code), isRuleBreakable(code));
    }

    private ViolatedRule getViolatedRuleDetail(BSession session, String code, APChampsAnnonce annonce)
            throws APRuleExecutionException {
        return new ViolatedRule(code, getRuleMessage(session, code, annonce), isRuleBreakable(code));
    }

    private Boolean isRuleBreakable(String code) {

        Boolean trouve = Boolean.FALSE;
        APBreakableRules[] rules = APBreakableRules.values();
        for (int i = 0; i < rules.length; i++) {
            String r = rules[i].toString();
            String codeR = r.substring(r.indexOf("_") + 1);
            if (code.equals(codeR)) {
                trouve = Boolean.TRUE;
            }
        }

        return trouve;
    }

    @Override
    public List<String> controllerPrestationsJoursIsoles(BSession session, List<APPrestation> prestations,
            APDroitLAPG droit) {
        List<String> resultsString = new ArrayList<String>();
        try {
            if (prestations.isEmpty()) {
                resultsString.add(session.getLabel("VALIDATION_PRESTATION_EXCEPTION_ASSURANCE_CIAB_EMPTY_EMPLOYEUR"));
            } else if (prestationVerseeAssure(session, droit)) {
                resultsString.add(session.getLabel("VALIDATION_PRESTATION_EXCEPTION_PRESTATION_CIAB_VERSEE_ASSURE"));
            }
        } catch (Exception e) {
            resultsString.add(session.getLabel("VALIDATION_PRESTATION_EXCEPTION_SITUATION_PROF_NON_TROUVEE"));
        }
        return resultsString;
    }

    private boolean prestationVerseeAssure(BSession session, APDroitLAPG droit) throws Exception {
        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();
        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction());

        // pour chaque situation professionnelle
        for (int idSitPro = 0; idSitPro < mgr.size(); ++idSitPro) {
            APSituationProfessionnelle sitPro = (APSituationProfessionnelle) mgr.get(idSitPro);
            if (!sitPro.getIsVersementEmployeur()) {
                return true;
            }
        }
        return false;
    }
}
