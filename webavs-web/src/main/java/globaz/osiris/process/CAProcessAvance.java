package globaz.osiris.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.avance.CAAvanceEcheance;
import globaz.osiris.db.avance.CAAvanceListViewBean;
import globaz.osiris.db.avance.CAAvanceViewBean;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationOrdreVersementAvance;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;

/**
 * Process utilisé pour la création des Avances par Ordre de versement (VA) aux affiliés.
 * 
 * @author dda
 */
public class CAProcessAvance extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_EXECUTION_DES_AVANCES_ERROR = "EXECUTION_DES_AVANCES_ERROR";
    private static final String LABEL_EXECUTION_DES_AVANCES_OK = "EXECUTION_DES_AVANCES_OK";
    private static final String LABEL_EXECUTION_RESULT = "EXECUTION_RESULT";
    private static final String LABEL_MOTIF_NON_RENSEIGNE = "MOTIF_NON_RENSEIGNE";
    private static final String LABEL_TYPE_ECHEANCE_NON_RENSEIGNE = "TYPE_ECHEANCE_NON_RENSEIGNE";

    private String executeForDate;
    private String forEcheanceType;
    private String forIdModeRecouvrement;
    private String forSelectionRole;
    private String idOrganeExecution;
    private CAJournal journal = null;

    private String motif;

    /**
     * Constructor for CAProcessAvance.
     */
    public CAProcessAvance() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessAvance.
     * 
     * @param parent
     */
    public CAProcessAvance(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessAvance.
     * 
     * @param session
     */
    public CAProcessAvance(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {
            checkExecuteForDate();
            checkEcheanceType();

            if (JadeStringUtil.isBlank(getMotif())) {
                throw new Exception(getSession().getLabel(CAProcessAvance.LABEL_MOTIF_NON_RENSEIGNE));
            }

            int countProcessed = executeAvances();

            getMemoryLog().logMessage(
                    FWMessageFormat.format(getSession().getLabel(CAProcessAvance.LABEL_EXECUTION_RESULT), ""
                            + countProcessed), FWMessage.INFORMATION, this.getClass().getName());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        try {
            checkExecuteForDate();
            checkEcheanceType();
        } catch (Exception e) {
            this._addError(e.getMessage());
        }

        if (JadeStringUtil.isBlank(getMotif())) {
            this._addError(getSession().getLabel(CAProcessAvance.LABEL_MOTIF_NON_RENSEIGNE));
        }
    }

    /**
     * Créer un ordre de versement valant le montant de l'avance pré-calculé.
     * 
     * @param avance
     * @param montant
     * @throws Exception
     */
    private void addOrdreVersement(CAAvanceViewBean avance, FWCurrency montant) throws Exception {
        // créé l'ordre versement
        CAOperationOrdreVersementAvance versement = new CAOperationOrdreVersementAvance();
        versement.setSession(getSession());

        versement.setIdCompteAnnexe(avance.getIdCompteAnnexe());
        versement.setDate(getExecuteForDate());
        versement.setMontant(montant.toString());

        String idJournal = getJournal();

        versement.setIdJournal(idJournal);
        versement.setIdSection(getIdDestinationSection(avance.getIdCompteAnnexe(), idJournal,avance.getIdModeRecouvrement()));
        versement.setMotif(avance.getLibelle());

        if (avance.getIdModeRecouvrement().equals(CAPlanRecouvrement.CS_AVANCE_APG)) {
            versement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_APG);
        } else if (avance.getIdModeRecouvrement().equals(CAPlanRecouvrement.CS_AVANCE_RENTES)) {
            versement.setNatureOrdre(CAOrdreGroupe.NATURE_RENTES_AVS_AI);
            versement.setMotif(avance.getIdExterneRole() + " " + avance.getLibelle());
        } else if (avance.getIdModeRecouvrement().equals(CAPlanRecouvrement.CS_AVANCE_IJAI)) {
            versement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_IJAI);
        } else if (avance.getIdModeRecouvrement().equals(CAPlanRecouvrement.CS_AVANCE_PTRA)) {
            versement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_PRESTATION_TRANSITOIRE);
        }  else {
            versement.setNatureOrdre(CAOrdreGroupe.ORDRESTOUS);
        }

        versement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);

        if (!JadeStringUtil.isBlank(getIdOrganeExecution())) {
            versement.setIdOrganeExecution(getIdOrganeExecution());
        }

        versement.add(getTransaction());
    }

    /**
     * Contrôle du type d'échéance.
     * 
     * @throws Exception
     */
    private void checkEcheanceType() throws Exception {
        if (JadeStringUtil.isBlank(getForEcheanceType())) {
            throw new Exception(getSession().getLabel(CAProcessAvance.LABEL_TYPE_ECHEANCE_NON_RENSEIGNE));
        } else if (!getForEcheanceType().equals(CAPlanRecouvrement.CS_ECHEANCE_MENSUELLE)
                && !getForEcheanceType().equals(CAPlanRecouvrement.CS_ECHEANCE_TRIMESTRIELLE)
                && !getForEcheanceType().equals(CAPlanRecouvrement.CS_ECHEANCE_ANNUELLE)) {
            throw new Exception(getSession().getLabel(CAProcessAvance.LABEL_TYPE_ECHEANCE_NON_RENSEIGNE));
        }
    }

    /**
     * Contrôle de la date d'éxécution.
     * 
     * @throws Exception
     */
    private void checkExecuteForDate() throws Exception {
        if (JadeStringUtil.isBlank(getExecuteForDate())) {
            throw new Exception(getSession().getLabel("5327"));
        }

        new JADate(getExecuteForDate());
    }

    /**
     * Exécution, création, des avances. <br>
     * 1. Recherche des avances. <br>
     * 2. Contrôle de la date d'éxécution. <br>
     * 3. Calcul du montant à avancer. <br>
     * 4. Ajout d'une ordre de versement VA. <br>
     * 5. Mise à jour de l'échéance de l'avance.
     * 
     * @return Le nombre d'avances créées.
     * @throws Exception
     */
    private int executeAvances() throws Exception {
        int countProcessed = 0;

        CAAvanceListViewBean manager = new CAAvanceListViewBean();
        setAvanceManagerProperties(manager);

        manager.find();

        for (int i = 0; i < manager.size(); i++) {
            CAAvanceViewBean avance = (CAAvanceViewBean) manager.get(i);
            CAAvanceEcheance echeance = avance.getEcheance(getTransaction());

            if (!isLastExecutionDateOver(echeance)) {
                if (!JadeStringUtil.isDecimalEmpty(avance.getPlafond())) {
                    if (!JadeStringUtil.isBlank(echeance.getDateExigibilite())) {
                        if (isEndDateNotOver(echeance)) {
                            FWCurrency montant = getMontantPlafondDependent(avance, echeance);
                            if (!montant.isZero()) {
                                addOrdreVersement(avance, montant);
                                updateEcheance(echeance, montant);

                                countProcessed++;
                            }
                        }
                    } else {
                        FWCurrency montant = getMontantPlafondDependent(avance, echeance);
                        if (!montant.isZero()) {
                            addOrdreVersement(avance, montant);
                            updateEcheance(echeance, montant);

                            countProcessed++;
                        }
                    }
                } else {
                    if (!JadeStringUtil.isBlank(echeance.getDateExigibilite())) {
                        if (isEndDateNotOver(echeance)) {
                            FWCurrency montant = getAcompte(avance, echeance);
                            if (!montant.isZero()) {
                                addOrdreVersement(avance, montant);
                                updateEcheance(echeance, montant);

                                countProcessed++;
                            }
                        }
                    }
                }
            }
        }

        return countProcessed;
    }

    /**
     * Retourne l'acompte. Si rien na encore été versé à l'affilié alors la méthode retourne le premier acompte.
     * 
     * @param avance
     * @param echeance
     * @return
     */
    private FWCurrency getAcompte(CAAvanceViewBean avance, CAAvanceEcheance echeance) {
        if (!JadeStringUtil.isDecimalEmpty(avance.getPremierAcompte())
                && JadeStringUtil.isDecimalEmpty(echeance.getMontantPaye())) {
            return new FWCurrency(avance.getPremierAcompte());
        } else {
            return new FWCurrency(avance.getAcompte());
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors()) {
            return getSession().getLabel(CAProcessAvance.LABEL_EXECUTION_DES_AVANCES_ERROR);
        } else {
            return getSession().getLabel(CAProcessAvance.LABEL_EXECUTION_DES_AVANCES_OK);
        }
    }

    /**
     * @return
     */
    public String getExecuteForDate() {
        return executeForDate;
    }

    public JADate getExecuteForDateAsJADate() throws JAException {
        return new JADate(getExecuteForDate());
    }

    /**
     * @return
     */
    public String getForEcheanceType() {
        return forEcheanceType;
    }

    /**
     * @return
     */
    public String getForIdModeRecouvrement() {
        return forIdModeRecouvrement;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Retourne l'id de la section de destination pour l'ordre de versement. <br>
     * Si la section n'éxiste pas cette dernière sera créée.
     * 
     * @param idCompteAnnexe
     * @param idJournal
     * @return L'id de la section de destination.
     * @throws Exception
     */
    private String getIdDestinationSection(String idCompteAnnexe, String idJournal,String modeRecouvrement) throws Exception {
        CASectionManager manager = new CASectionManager();
        manager.setSession(getSession());
        manager.setForIdCompteAnnexe(idCompteAnnexe);
        manager.setForIdExterne("" + JACalendar.today().getYear() + APISection.CATEGORIE_SECTION_AVANCE + "000");
        if(modeRecouvrement.equals(CAPlanRecouvrement.CS_AVANCE_PTRA)){
            manager.setForIdExterne("" + JACalendar.today().getYear()+APISection.CATEGORIE_SECTION_AVANCE_PTRA+ "000");
        }else{
            manager.setForIdExterne("" + JACalendar.today().getYear() + APISection.CATEGORIE_SECTION_AVANCE + "000");
        }

        manager.find(getTransaction());

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        CASection section = null;
        if (manager.isEmpty()) {
            section = new CASection();
            section.setSession(getSession());

            section.setIdCompteAnnexe(idCompteAnnexe);
            if(modeRecouvrement.equals(CAPlanRecouvrement.CS_AVANCE_PTRA)){
                section.setIdExterne("" + JACalendar.today().getYear()+APISection.CATEGORIE_SECTION_AVANCE_PTRA+ "000");
            }else{
                section.setIdExterne("" + JACalendar.today().getYear() + APISection.CATEGORIE_SECTION_AVANCE + "000");
            }
            section.setIdJournal(idJournal);
            if(modeRecouvrement.equals(CAPlanRecouvrement.CS_AVANCE_PTRA)){
                section.setIdTypeSection(APISection.ID_TYPE_SECTION_AVANCE_PTRA);
            }else{
                section.setIdTypeSection(APISection.ID_TYPE_SECTION_AVANCES);
            }
            section.setDateSection(JACalendar.today().toString());

            section.add(getTransaction());

            if (section.hasErrors()) {
                throw new Exception(getSession().getLabel("IMPOSSIBLE_CREER_SECTION") + " (idCptAnnexe = "
                        + idCompteAnnexe + ")");
            }
        } else {
            section = (CASection) manager.getFirstEntity();
        }

        return section.getIdSection();
    }

    /**
     * @return
     */
    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    /**
     * Retourne l'idJournal du journal ou les ordres de versements à créer seront ajoutés. <br>
     * Si le journal n'éxiste pas un nouveau journal sera créé.
     * 
     * @return
     * @throws Exception
     */
    private String getJournal() throws Exception {
        if (journal == null) {
            journal = new CAJournal();
            journal.setSession(getSession());

            journal.setLibelle(getMotif());
            journal.setDateValeurCG(getExecuteForDate());
            journal.setDate(JACalendar.todayJJsMMsAAAA());

            journal.add(getTransaction());
        }

        return journal.getIdJournal();
    }

    /**
     * Retourne le montant à avancer. Montant dépendant du Plafond.
     * 
     * @param avance
     * @param echeance
     * @return Le montant (égal à lacompte) ou (égal à la différence restante à avancer)
     */
    private FWCurrency getMontantPlafondDependent(CAAvanceViewBean avance, CAAvanceEcheance echeance) {
        FWCurrency result = null;

        FWCurrency montantAvancer = new FWCurrency(echeance.getMontantPaye());
        montantAvancer.abs();
        FWCurrency montantTotalAvance = new FWCurrency(avance.getPlafond());

        montantAvancer.add(echeance.getMontant());

        if (montantAvancer.compareTo(montantTotalAvance) != 1) {
            result = getAcompte(avance, echeance);
        } else {
            result = montantTotalAvance;
            FWCurrency montantDejaAvance = new FWCurrency(echeance.getMontantPaye());
            montantDejaAvance.abs();
            result.sub(montantDejaAvance);
        }

        return result;
    }

    /**
     * @return
     */
    public String getMotif() {
        return motif;
    }

    /**
     * La date de fin est-elle plus grande que la date de traitement.
     * 
     * @param echeance
     * @return True si la date de fin est plus grande que la date de traitement.
     * @throws Exception
     */
    private boolean isEndDateNotOver(CAAvanceEcheance echeance) throws Exception {
        JADate endDate = new JADate(echeance.getDateExigibilite());

        JACalendar cal = new JACalendarGregorian();

        return cal.compare(getExecuteForDateAsJADate(), endDate) == JACalendar.COMPARE_SECONDUPPER;
    }

    /**
     * La dernière date d'éxécution est-elle plus grande que ... ?
     * 
     * @param echeance
     * @param addMonth
     * @return True si la dernière date d'éxécution est plus grande que ...
     * @throws Exception
     */
    private boolean isLastExecutionDateOver(CAAvanceEcheance echeance) throws Exception {
        JACalendar cal = new JACalendarGregorian();
        JADate tmp = new JADate();

        if (JadeStringUtil.isBlank(echeance.getDateEffective()) && !JadeStringUtil.isBlank(echeance.getDateRappel())) {
            JADate firstDateExecution = new JADate(echeance.getDateRappel()); // Date
            // rappel
            // -->
            // 1ère
            // date
            // d'échéance
            return cal.compare(getExecuteForDateAsJADate(), firstDateExecution) == JACalendar.COMPARE_SECONDUPPER;
        }

        JADate lastExecutionDate = new JADate(echeance.getDateEffective()); // Date
        // effective
        // -->
        // date
        // dernière
        // exécution

        if (getForEcheanceType().equals(CAPlanRecouvrement.CS_ECHEANCE_MENSUELLE)) {
            tmp = cal.addMonths(lastExecutionDate, 1);
        } else if (getForEcheanceType().equals(CAPlanRecouvrement.CS_ECHEANCE_TRIMESTRIELLE)) {
            tmp = cal.addMonths(lastExecutionDate, 3);
        } else if (getForEcheanceType().equals(CAPlanRecouvrement.CS_ECHEANCE_ANNUELLE)) {
            tmp = cal.addMonths(lastExecutionDate, 12);
        } else {
            throw new Exception(getSession().getLabel(CAProcessAvance.LABEL_TYPE_ECHEANCE_NON_RENSEIGNE));
        }

        return cal.compare(getExecuteForDateAsJADate(), tmp) == JACalendar.COMPARE_SECONDUPPER;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Set les paramètres de recherche pour le manager d'avances.
     * 
     * @param manager
     */
    private void setAvanceManagerProperties(CAAvanceListViewBean manager) {
        manager.setSession(getSession());
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        manager.setForSelectionRole(getForSelectionRole());
        manager.setForIdEtat(CAPlanRecouvrement.CS_ACTIF);

        if (!JadeStringUtil.isBlank(getForIdModeRecouvrement())) {
            manager.setForIdModeRecouvrement(getForIdModeRecouvrement());
        }

        manager.setForIdTypeEcheance(getForEcheanceType());
    }

    /**
     * @param string
     */
    public void setExecuteForDate(String s) {
        executeForDate = s;
    }

    /**
     * @param string
     */
    public void setForEcheanceType(String string) {
        forEcheanceType = string;
    }

    /**
     * @param string
     */
    public void setForIdModeRecouvrement(String s) {
        forIdModeRecouvrement = s;
    }

    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     */
    public void setIdOrganeExecution(String s) {
        idOrganeExecution = s;
    }

    /**
     * @param string
     */
    public void setMotif(String string) {
        motif = string;
    }

    /**
     * Mise à jour de l'échéance de l'avance. <br>
     * Respectivement update du montant et de la date d'éxécution.
     * 
     * @param echeance
     * @param montant
     * @throws Exception
     */
    private void updateEcheance(CAAvanceEcheance echeance, FWCurrency montant) throws Exception {
        FWCurrency montantAvancer;
        echeance.setDateEffective(getExecuteForDate());
        montantAvancer = new FWCurrency(echeance.getMontantPaye());
        montantAvancer.abs();
        montantAvancer.add(montant);
        echeance.setMontant(montant.toString());
        echeance.setMontantPaye(montantAvancer.toString());

        echeance.update(getTransaction());
    }

}
