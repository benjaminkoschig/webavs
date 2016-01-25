package globaz.phenix.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationDifferente;
import globaz.phenix.db.principale.CPCotisationDifferenteManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;

/**
 * Insérez la description du type ici. Date de création : (18.11.2004 14:00:00)
 * 
 * @author: acr
 */
public class CPProcessMiseAjourAffiliation extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateFinTrimestre = "";
    private Boolean decisionsValideesPassageComptabilise = new Boolean(false);
    private int nbEnregistrementMiseAjour = 0;

    public CPProcessMiseAjourAffiliation() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessMiseAjourAffiliation(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessMiseAjourAffiliation(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        boolean resultat = true;
        BStatement statement = null;
        CPCotisationDifferente cotisationDifferente = null;
        CPCotisationDifferenteManager cotisationDifferenteManager = null;
        try {
            String trimestrePrecedent = "";
            JADate date = JACalendar.today();
            int annee = date.getYear();
            int mois = date.getMonth();
            int moisPrecedent = 0;
            int anneePrecedente = 0;
            if ((mois >= 1) && (mois <= 3)) {
                anneePrecedente = annee - 1;
                moisPrecedent = 12;
            } else {
                moisPrecedent = mois - 3;
                anneePrecedente = annee;
            }
            // Trimestre précédent
            if ((moisPrecedent >= 1) && (moisPrecedent <= 3)) {
                trimestrePrecedent = "31.03." + anneePrecedente;
            }
            if ((moisPrecedent >= 4) && (moisPrecedent <= 6)) {
                trimestrePrecedent = "30.06." + anneePrecedente;
            }
            if ((moisPrecedent >= 7) && (moisPrecedent <= 9)) {
                trimestrePrecedent = "30.09." + anneePrecedente;
            }
            if ((moisPrecedent >= 10) && (moisPrecedent <= 12)) {
                trimestrePrecedent = "31.12." + anneePrecedente;
            }
            cotisationDifferenteManager = new CPCotisationDifferenteManager();
            cotisationDifferenteManager.setSession(getSession());
            cotisationDifferenteManager.setFromDateFinAffiliation(trimestrePrecedent);
            cotisationDifferenteManager.setToDateDebutDecision(dateFinTrimestre);
            cotisationDifferenteManager.setForAnneeDecisionCP(JACalendar.today().getYear() + "");
            cotisationDifferenteManager
                    .setForDecisionsValideesPassageComptabilise(decisionsValideesPassageComptabilise);
            cotisationDifferenteManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            cotisationDifferenteManager.find(getTransaction());
            // Pour information: indique le nombre d'annonces à charger
            nbEnregistrementMiseAjour = cotisationDifferenteManager.getCount();
            setProgressScaleValue(nbEnregistrementMiseAjour);
            statement = cotisationDifferenteManager.cursorOpen(getTransaction());
            while (((cotisationDifferente = (CPCotisationDifferente) cotisationDifferenteManager
                    .cursorReadNext(statement)) != null) && (!cotisationDifferente.isNew())) {
                // Lecture de l'affilation
                AFAffiliation affiliation = new AFAffiliation();
                affiliation.setSession(getSession());
                affiliation.setAffiliationId(cotisationDifferente.getIdAffilie());
                affiliation.retrieve(getTransaction());
                if ((affiliation == null) || affiliation.isNew()) {
                    throw new Exception("L'affiliation " + affiliation.getAffiliationId() + " n'existe pas!");
                }
                CPDecision decision = new CPDecision();
                decision.setSession(getSession());
                decision.setIdDecision(cotisationDifferente.getIdDecision());
                decision.retrieve(getTransaction());
                if ((decision != null) && !decision.isNew()) {
                    if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equals(cotisationDifferente.getTypeAssurance())) {
                        miseAjourMontantsAffiliationAVS(affiliation, decision);
                    } else if (CodeSystem.TYPE_ASS_FRAIS_ADMIN.equals(cotisationDifferente.getTypeAssurance())) {
                        miseAjourMontantsAffiliationFraisAdmin(affiliation, decision);
                    } else if (CodeSystem.TYPE_ASS_COTISATION_AF.equals(cotisationDifferente.getTypeAssurance())) {
                        miseAjourMontantsAffiliationAF(affiliation, decision);
                    } else if (CodeSystem.TYPE_ASS_MATERNITE.equals(cotisationDifferente.getTypeAssurance())) {
                        miseAjourMontantsAffiliationMaternite(affiliation, decision);
                    } else if (CodeSystem.TYPE_ASS_COTISATION_AC.equals(cotisationDifferente.getTypeAssurance())) {
                        miseAjourMontantsAffiliationAssuranceChomage(affiliation, decision);
                    } else if (CodeSystem.TYPE_ASS_COTISATION_AC2.equals(cotisationDifferente.getTypeAssurance())) {
                        miseAjourMontantsAffiliationAssuranceChomage2(affiliation, decision);
                    } else if (CodeSystem.TYPE_ASS_AFI.equals(cotisationDifferente.getTypeAssurance())) {
                        miseAjourMontantsAffiliationAFI(affiliation, decision);
                    }
                }
                // committer
                if (!getTransaction().hasErrors() && !isAborted()) {
                    try {
                        getTransaction().commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                incProgressCounter();
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            resultat = false;
        } finally {
            try {
                cotisationDifferenteManager.cursorClose(statement);
                statement = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * @return
     */
    public String getDateFinTrimestre() {
        return dateFinTrimestre;
    }

    /**
     * @return
     */
    public Boolean getDecisionsValideesPassageComptabilise() {
        return decisionsValideesPassageComptabilise;
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (!isAborted() && !isOnError()) {
            if (nbEnregistrementMiseAjour == 0) {
                return "Mise à jour des montants de l'affiliation: Aucune ligne modifiée";
            } else {
                return "Mise à jour des montants de l'affiliation: " + nbEnregistrementMiseAjour
                        + " ligne(s) modifiée(s)";
            }
        } else {
            return "La mise à jour de l'affiliation a échoué!";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void miseAjourAFCotisation(CPDecision decision, AFCotisation cotisationAfAvs) throws Exception {
        CPCotisation cotisationCpAvs = new CPCotisation();
        cotisationCpAvs.setSession(getSession());
        cotisationCpAvs.setAlternateKey(1);
        cotisationCpAvs.setIdCotiAffiliation(cotisationAfAvs.getCotisationId());
        cotisationCpAvs.setIdDecision(decision.getIdDecision());
        cotisationCpAvs.retrieve(getTransaction());
        if ((cotisationCpAvs != null) && !cotisationCpAvs.isNew()) {
            // Remise à zéro des montants si salarié dispensé
            if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(decision.getSpecification())
                    || CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())
                    || AFParticulariteAffiliation.existeParticularite(getSession(), decision.getIdAffiliation(),
                            CodeSystem.PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE, decision.getDebutDecision())) {
                cotisationAfAvs.setMontantAnnuel("0");
                cotisationAfAvs.setMontantSemestriel("0");
                cotisationAfAvs.setMontantTrimestriel("0");
                cotisationAfAvs.setMontantMensuel("0");
            } else {
                cotisationAfAvs.setMontantAnnuel(cotisationCpAvs.getMontantAnnuel());
                cotisationAfAvs.setMontantSemestriel(cotisationCpAvs.getMontantSemestriel());
                cotisationAfAvs.setMontantTrimestriel(cotisationCpAvs.getMontantTrimestriel());
                cotisationAfAvs.setMontantMensuel(cotisationCpAvs.getMontantMensuel());
            }
            cotisationAfAvs.setAnneeDecision(decision.getAnneeDecision());
            cotisationAfAvs.update(getTransaction());
        }
    }

    public void miseAjourMontantsAffiliationAF(AFAffiliation affiliation, CPDecision decision) throws Exception {
        String dateReference = decision.getFinDecision();
        if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                && BSessionUtil
                        .compareDateFirstLower(getSession(), affiliation.getDateFin(), decision.getFinDecision())) {
            dateReference = affiliation.getDateFin();
        }
        AFCotisation cotisationAfAvs = affiliation._cotisationSansTestFinAffiliation(getTransaction(),
                affiliation.getAffiliationId(), CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AF,
                dateReference);
        if ((cotisationAfAvs == null) || cotisationAfAvs.isNew()) {
            throw new Exception("Affilié " + affiliation.getAffilieNumero() + " - La cotisation pour l'assurance "
                    + getSession().getCodeLibelle(CodeSystem.TYPE_ASS_COTISATION_AF) + " n'existe pas!");
        }
        miseAjourAFCotisation(decision, cotisationAfAvs);
    }

    public void miseAjourMontantsAffiliationAFI(AFAffiliation affiliation, CPDecision decision) throws Exception {
        String dateReference = decision.getFinDecision();
        if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                && BSessionUtil
                        .compareDateFirstLower(getSession(), affiliation.getDateFin(), decision.getFinDecision())) {
            dateReference = affiliation.getDateFin();
        }
        AFCotisation cotisationAfAvs = affiliation._cotisationSansTestFinAffiliation(getTransaction(),
                affiliation.getAffiliationId(), CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_AFI, dateReference);
        if ((cotisationAfAvs == null) || cotisationAfAvs.isNew()) {
            throw new Exception("Affilié " + affiliation.getAffilieNumero() + " - La cotisation pour l'assurance "
                    + getSession().getCodeLibelle(CodeSystem.TYPE_ASS_AFI) + " n'existe pas!");
        }
        miseAjourAFCotisation(decision, cotisationAfAvs);
    }

    public void miseAjourMontantsAffiliationAssuranceChomage(AFAffiliation affiliation, CPDecision decision)
            throws Exception {
        String dateReference = decision.getFinDecision();
        if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                && BSessionUtil
                        .compareDateFirstLower(getSession(), affiliation.getDateFin(), decision.getFinDecision())) {
            dateReference = affiliation.getDateFin();
        }
        AFCotisation cotisationAfAvs = affiliation._cotisationSansTestFinAffiliation(getTransaction(),
                affiliation.getAffiliationId(), CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC,
                dateReference);
        if ((cotisationAfAvs == null) || cotisationAfAvs.isNew()) {
            throw new Exception("Affilié " + affiliation.getAffilieNumero() + " - La cotisation pour l'assurance "
                    + getSession().getCodeLibelle(CodeSystem.TYPE_ASS_COTISATION_AC) + " n'existe pas!");
        }
        miseAjourAFCotisation(decision, cotisationAfAvs);
    }

    public void miseAjourMontantsAffiliationAssuranceChomage2(AFAffiliation affiliation, CPDecision decision)
            throws Exception {
        String dateReference = decision.getFinDecision();
        if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                && BSessionUtil
                        .compareDateFirstLower(getSession(), affiliation.getDateFin(), decision.getFinDecision())) {
            dateReference = affiliation.getDateFin();
        }
        AFCotisation cotisationAfAvs = affiliation._cotisationSansTestFinAffiliation(getTransaction(),
                affiliation.getAffiliationId(), CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC2,
                dateReference);
        if ((cotisationAfAvs == null) || cotisationAfAvs.isNew()) {
            throw new Exception("Affilié " + affiliation.getAffilieNumero() + " - La cotisation pour l'assurance "
                    + getSession().getCodeLibelle(CodeSystem.TYPE_ASS_COTISATION_AC2) + " n'existe pas!");
        }
        miseAjourAFCotisation(decision, cotisationAfAvs);
    }

    public void miseAjourMontantsAffiliationAVS(AFAffiliation affiliation, CPDecision decision) throws Exception {
        String dateReference = decision.getFinDecision();
        if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                && BSessionUtil
                        .compareDateFirstLower(getSession(), affiliation.getDateFin(), decision.getFinDecision())) {
            dateReference = affiliation.getDateFin();
        }
        AFCotisation cotisationAfAvs = affiliation._cotisationSansTestFinAffiliation(getTransaction(),
                affiliation.getAffiliationId(), CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AVS_AI,
                dateReference);
        if ((cotisationAfAvs == null) || cotisationAfAvs.isNew()) {
            throw new Exception("Affilié " + affiliation.getAffilieNumero() + " - La cotisation pour l'assurance "
                    + getSession().getCodeLibelle(CodeSystem.TYPE_ASS_COTISATION_AVS_AI) + " n'existe pas!");
        }
        miseAjourAFCotisation(decision, cotisationAfAvs);
    }

    public void miseAjourMontantsAffiliationFraisAdmin(AFAffiliation affiliation, CPDecision decision) throws Exception {
        String dateReference = decision.getFinDecision();
        if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                && BSessionUtil
                        .compareDateFirstLower(getSession(), affiliation.getDateFin(), decision.getFinDecision())) {
            dateReference = affiliation.getDateFin();
        }
        AFCotisation cotisationAfFraisAdmin = affiliation._cotisationSansTestFinAffiliation(getTransaction(),
                affiliation.getAffiliationId(), CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_FRAIS_ADMIN,
                dateReference);
        if ((cotisationAfFraisAdmin == null) || cotisationAfFraisAdmin.isNew()) {
            throw new Exception("Affilié " + affiliation.getAffilieNumero() + " - La cotisation pour l'assurance "
                    + getSession().getCodeLibelle(CodeSystem.TYPE_ASS_FRAIS_ADMIN) + " n'existe pas!");
        }
        miseAjourAFCotisation(decision, cotisationAfFraisAdmin);
    }

    public void miseAjourMontantsAffiliationMaternite(AFAffiliation affiliation, CPDecision decision) throws Exception {
        String dateReference = decision.getFinDecision();
        if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                && BSessionUtil
                        .compareDateFirstLower(getSession(), affiliation.getDateFin(), decision.getFinDecision())) {
            dateReference = affiliation.getDateFin();
        }
        AFCotisation cotisationAfAvs = affiliation._cotisationSansTestFinAffiliation(getTransaction(),
                affiliation.getAffiliationId(), CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_MATERNITE,
                dateReference);
        if ((cotisationAfAvs == null) || cotisationAfAvs.isNew()) {
            throw new Exception("Affilié " + affiliation.getAffilieNumero() + " - La cotisation pour l'assurance "
                    + getSession().getCodeLibelle(CodeSystem.TYPE_ASS_MATERNITE) + " n'existe pas!");
        }
        miseAjourAFCotisation(decision, cotisationAfAvs);
    }

    public String rechercheDateFinTrimestre() {
        String dateFinTrimestre = "";
        int mois = JACalendar.today().getMonth();
        switch (mois) {
            case 1:
                dateFinTrimestre = "31.03." + JACalendar.today().getYear();
                break;
            case 2:
                dateFinTrimestre = "31.03." + JACalendar.today().getYear();
                break;
            case 3:
                dateFinTrimestre = "31.03." + JACalendar.today().getYear();
                break;
            case 4:
                dateFinTrimestre = "30.06." + JACalendar.today().getYear();
                break;
            case 5:
                dateFinTrimestre = "30.06." + JACalendar.today().getYear();
                break;
            case 6:
                dateFinTrimestre = "30.06." + JACalendar.today().getYear();
                break;
            case 7:
                dateFinTrimestre = "30.09." + JACalendar.today().getYear();
                break;
            case 8:
                dateFinTrimestre = "30.09." + JACalendar.today().getYear();
                break;
            case 9:
                dateFinTrimestre = "30.09." + JACalendar.today().getYear();
                break;
            case 10:
                dateFinTrimestre = "31.12." + JACalendar.today().getYear();
                break;
            case 11:
                dateFinTrimestre = "31.12." + JACalendar.today().getYear();
                break;
            case 12:
                dateFinTrimestre = "31.12." + JACalendar.today().getYear();
                break;
            default:
                dateFinTrimestre = "0";
        }
        return dateFinTrimestre;
    }

    public CPDecision rechercheDerniereDecision(AFAffiliation affiliation) throws Exception {
        CPDecision decision = null;
        String dateFinTrimestre = rechercheDateFinTrimestre();
        // Lecture de la dernière décision qui mettra à jour les montants de
        // l'affilation
        // La décision doit concerner l'année en cours. Si aucune décision pour
        // l'année en cours,
        // ça ne sert à rien de mettre les montants de l'affiliation à jour.
        CPDecisionManager decisionMng = new CPDecisionManager();
        decisionMng.setSession(getSession());
        decisionMng.setForIdAffiliation(affiliation.getAffiliationId());
        decisionMng.setForIdTiers(affiliation.getIdTiers());
        decisionMng.setForAnneeDecision(JACalendar.today().getYear() + "");
        decisionMng.setToDateDebutDecision(dateFinTrimestre);
        decisionMng.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_PB_COMPTABILISATION + ", "
                + CPDecision.CS_REPRISE);
        decisionMng.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
        decisionMng.setOrder("IADINF DESC, IAIDEC DESC");
        decisionMng.find(getTransaction());
        if (!decisionMng.isEmpty()) {
            return (CPDecision) decisionMng.getEntity(0);
        }
        if (decision == null) {
            getMemoryLog().logMessage(
                    "L'affiliation (" + affiliation.getDateDebut() + " - " + affiliation.getDateFin()
                            + ") n'a pas été mise à jour car il n'existe aucune décision "
                            + JACalendar.today().getYear() + ".", FWMessage.INFORMATION, "Mise à jour affiliation");
        }
        return decision;
    }

    /**
     * @param string
     */
    public void setDateFinTrimestre(String string) {
        dateFinTrimestre = string;
    }

    /**
     * @param boolean1
     */
    public void setDecisionsValideesPassageComptabilise(Boolean boolean1) {
        decisionsValideesPassageComptabilise = boolean1;
    }

}
