package globaz.osiris.externe;

import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIMessageLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWPKProvider;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIJournal;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreRecouvrement;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.print.itext.list.CAIListJournalEcritures_Doc;
import globaz.osiris.process.CAIProcessListOrdreGroupe;
import globaz.osiris.process.CAProcessAttacherOrdre;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import globaz.osiris.process.journal.CAProcessComptabiliserJournal;
import globaz.osiris.process.journal.CAUtilsJournal;

/**
 * Cette classe encapsule le processus de mise en compte Date de cr�ation : (15.01.2002 13:23:15)
 * 
 * @author: Administrator
 */
/**
 * @author SCH
 */
public final class CAGestionComptabiliteExterne implements APIGestionComptabiliteExterne {
    private String eMailAddress = null;
    private boolean imprimerJournal = false;
    private boolean imprimerOrdreGroupe = false;

    private CAJournal journal = new CAJournal();

    private BIMessageLog log = new FWMemoryLog();

    private BProcess process = null;
    private boolean sendCompletionMail = true;

    private BSession session = null;
    private BTransaction transaction = null;

    /**
     * Constructeur.
     */
    public CAGestionComptabiliteExterne() {
        super();

    }

    /**
     * Constructeur.
     * 
     * @param processParent
     */
    public CAGestionComptabiliteExterne(BProcess processParent) {
        this();

        setProcess(processParent);
        setMessageLog(processParent.getMemoryLog());
        setTransaction(processParent.getTransaction());
        setSendCompletionMail(false);
        setEMailAddress(processParent.getEMailAddress());
    }

    /**
     * Ajout d'une op�ration au journal.
     */
    @Override
    public void addOperation(APIOperation oper) {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{addOperation}", FWMessage.FATAL, this.getClass().getName());
            return;
        }

        if (oper == null) {
            log.logMessage("5012", "{addOperation}", FWMessage.FATAL, this.getClass().getName());
            return;
        }

        try {
            CAOperation operation = (CAOperation) oper;

            // La cr�er (l'op�ration before call a d�j� �t� effectu�e, on ne
            // l'effectue pas une deuxi�me fois)
            operation.wantCallMethodBefore(false);

            operation.add(transaction);

            operation.wantCallMethodBefore(true);

            if (operation.isNew() || operation.hasErrors()) {
                log.logStringBuffer(transaction.getErrors(), "CAOperation");
                log.logMessage("5004", null, FWMessage.FATAL, this.getClass().getName());
                return;
            }
        } catch (Exception e) {
            log.logMessage("5002", "Error in addOperation - " + e.getMessage(), FWMessage.FATAL, this.getClass()
                    .getName());
            return;
        }
    }

    /**
     * Cl�ture du process. Cette m�thode doit �tre appel�e � la fin du processus de comptabilisation. Elle assume les
     * fonctions suivantes :
     * <p>
     * <ul>
     * v�rification du bon d�roulement du processus
     * <ul>
     * sauvegarde du log des messages
     * <ul>
     * d�clencher la mise en compte s'il n'y a pas d'erreurs
     * <ul>
     * communiquer le log des messages et le r�sultat de la comptabilisatoin par e-mail � l'adresse fournie Date de
     * cr�ation : (16.01.2002 08:40:25)
     */
    @Override
    public void comptabiliser() {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            return;
        }

        if (!hasFatalErrors()) {
            try {
                journal.retrieve(transaction);
            } catch (Exception e) {
                log.logMessage("5002", "Erreur in retrieve - " + e.getMessage(), FWMessage.FATAL, this.getClass()
                        .getName());
            }

            if (isJournalEmpty()) {
                log.logMessage("5005", null, FWMessage.AVERTISSEMENT, this.getClass().getName());
                journal.setEtat(CAJournal.ANNULE);
            } else {
                comptabiliserJournal();
            }

            // Mettre � jour le journal
            if (!hasFatalErrors()) {
                try {
                    journal.update(transaction);

                    if (journal.hasErrors()) {
                        log.logStringBuffer(transaction.getErrors(), CAJournal.class.getName());
                        log.logMessage("5004", null, FWMessage.FATAL, this.getClass().getName());
                    }
                } catch (Exception e) {
                    log.logMessage("5002", "Error in journal.update - " + e.getMessage(), FWMessage.FATAL, this
                            .getClass().getName());
                }
            }
        }

        // Imprimer le journal si n�cessaire
        if (getImprimerJournal()) {
            imprimerJournal();
        }

        if (sendCompletionMail) {
            sendFinalEmail();
        }
    }

    /**
     * Effectue la comptabilisation du journal.
     */
    private void comptabiliserJournal() {
        if (!hasFatalErrors() && !journal.hasErrors()) {
            if (!new CAComptabiliserJournal().comptabiliser(getProcess(), journal)) {
                log.logMessage("5008", null, FWMessage.ERREUR, this.getClass().getName());
            }

            if (journal.hasErrors()) {
                log.logStringBuffer(transaction.getErrors(), CAJournal.class.getName());
                log.logMessage("5004", null, FWMessage.FATAL, this.getClass().getName());
            }
        }
    }

    /**
     * Cr�ation d'un compte annexe.
     * 
     * @param idTiers
     * @param idRole
     * @param idExterneRole
     * @return
     */
    private APICompteAnnexe createCompteAnnexe(String idTiers, String idRole, String idExterneRole) {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{getCompteAnnexeByRole}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        if (JadeStringUtil.isIntegerEmpty(idRole) || JadeStringUtil.isBlank(idExterneRole)
                || JadeStringUtil.isIntegerEmpty(idTiers)) {
            log.logMessage("5012", "{getCompteAnnexeByRole}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdJournal(journal.getIdJournal());
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);

        try {
            compteAnnexe.add(transaction);

            if (compteAnnexe.isNew() || compteAnnexe.hasErrors()) {
                log.logStringBuffer(transaction.getErrors(), this.getClass().getName());
                log.logMessage("5004", null, FWMessage.FATAL, "CACompteAnnexe");
            }
        } catch (Exception e) {
            log.logMessage("5002", "Error in createCompteAnnexe - " + e.getMessage(), FWMessage.FATAL, this.getClass()
                    .getName());
            return null;
        }

        return compteAnnexe;

    }

    /**
     * Cr�er une �criture attach� au journal (sans ajout en BD).
     */
    @Override
    public APIEcriture createEcriture() {
        return this.createEcriture(null);
    }

    @Override
    public APIEcriture createEcriture(String idEcriture) {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{createEcriture}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        CAEcriture ecriture = new CAEcriture();

        if (idEcriture != null) {
            ecriture.setIdOperation(idEcriture);
            ecriture.useOptimisation(true);
        }
        ecriture.setSession(getSession());
        ecriture.setIdJournal(journal.getIdJournal());

        try {
            ecriture.beforeAdd(transaction);
        } catch (Exception e) {
            log.logMessage("5002", "Error in createEcriture - " + e.getMessage(), FWMessage.FATAL, this.getClass()
                    .getName());
            return null;
        }

        return ecriture;
    }

    /**
     * Cr�ation du journal de comptabilisation.
     */
    @Override
    public APIJournal createJournal() {
        // Sortir si erreur
        if (hasFatalErrors()) {
            return null;
        }

        if (!journal.isNew()) {
            log.logMessage("5001", "{createJournal}", FWMessage.FATAL);
            return null;
        }

        if (transaction == null) {
            log.logMessage("5003", null, FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        try {
            journal.setTypeJournal(CAJournal.TYPE_AUTOMATIQUE);
            journal.add(transaction);

            journal.setMemoryLog((FWMemoryLog) getMessageLog());

            if (journal.isNew() || journal.hasErrors()) {
                log.logStringBuffer(transaction.getErrors(), this.getClass().getName());
                log.logMessage("5004", null, FWMessage.FATAL, this.getClass().getName());
                return null;
            }
        } catch (Exception e) {
            log.logMessage("5002", "Error in createJournal - " + e.getMessage(), FWMessage.FATAL, this.getClass()
                    .getName());
            return null;
        }

        return journal;
    }

    /**
     * Cr�er un ordre de recouvrement attach� au journal (sans ajout en BD).
     */
    @Override
    public APIOperationOrdreRecouvrement createOperationOrdreRecouvrement() {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{createOperationOrdreRecouvrement}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        CAOperationOrdreRecouvrement recouvrement = new CAOperationOrdreRecouvrement();
        recouvrement.setSession(getSession());

        recouvrement.setIdJournal(journal.getIdJournal());
        try {
            recouvrement.beforeAdd(transaction);
        } catch (Exception e) {
            log.logMessage("5002", "Error in createOperationOrdreRecouvrement - " + e.getMessage(), FWMessage.FATAL,
                    this.getClass().getName());
            return null;
        }

        return recouvrement;
    }

    /**
     * Cr�er un ordre de versement attach� au journal (sans ajout en BD).
     */
    @Override
    public APIOperationOrdreVersement createOperationOrdreVersement() {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{createOperationOrdreVersement}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        CAOperationOrdreVersement versement = new CAOperationOrdreVersement();
        versement.setSession(getSession());

        versement.setIdJournal(journal.getIdJournal());
        try {
            versement.beforeAdd(transaction);
        } catch (Exception e) {
            log.logMessage("5002", "Error in createOperationOrdreVersement - " + e.getMessage(), FWMessage.FATAL, this
                    .getClass().getName());
            return null;
        }

        return versement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIGestionComptabiliteExterne#createSection(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public APISection createSection(String idCompteAnnexe, String idTypeSection, String idExterne, String domaine,
            String typeAdresse) {
        return this.createSection(idCompteAnnexe, idTypeSection, idExterne, domaine, typeAdresse, Boolean.FALSE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIGestionComptabiliteExterne#createSection(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public APISection createSection(String idCompteAnnexe, String idTypeSection, String idExterne, String domaine,
            String typeAdresse, Boolean nonImprimable) {
        return this.createSection(null, idCompteAnnexe, idTypeSection, idExterne, domaine, typeAdresse, nonImprimable,
                null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIGestionComptabiliteExterne#createSection(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public APISection createSection(String idSection, String idCompteAnnexe, String idTypeSection, String idExterne,
            String domaine, String typeAdresse, Boolean nonImprimable, String idCaisseProf) {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{createSection}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        if (JadeStringUtil.isIntegerEmpty(idExterne) || JadeStringUtil.isIntegerEmpty(idTypeSection)
                || JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            log.logMessage("5012", "{getSectionByIdExterne}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        CASection section = new CASection();
        section.setSession(getSession());

        if (idSection != null) {
            section.setIdSection(idSection);
        }

        // Site c'est une section de type retours, bloquer le mode de compensation
        // Suite au BZ 7697 afin de bloquer quelques mauvaises manipulations des utilisateurs
        if (APISection.ID_TYPE_SECTION_RETOUR.equals(idTypeSection)) {
            section.setIdModeCompensation(APISection.MODE_BLOQUER_COMPENSATION);
        }

        section.setIdJournal(journal.getIdJournal());
        section.setIdTypeSection(idTypeSection);
        section.setIdCompteAnnexe(idCompteAnnexe);
        section.setIdExterne(idExterne);
        section.setDateSection(journal.getDateValeurCG());
        section.setDomaine(domaine);
        if (!JadeStringUtil.isBlankOrZero(idCaisseProf)) {
            section.setIdCaisseProfessionnelle(idCaisseProf);
        }
        section.setTypeAdresse(typeAdresse);
        section.setNonImprimable(nonImprimable);

        try {
            section.add(transaction);

            if (section.isNew() || section.hasErrors()) {
                log.logStringBuffer(transaction.getErrors(), this.getClass().getName());
                log.logMessage("5004", null, FWMessage.FATAL, this.getClass().getName());
            }
        } catch (Exception e) {
            log.logMessage("5002", "Error in createSection - " + e.getMessage(), FWMessage.FATAL, this.getClass()
                    .getName());
            return null;
        }
        return section;
    }

    /**
     * Retrouve un compte annexe gr�ce � son idCompteAnnexe.
     */
    @Override
    public APICompteAnnexe getCompteAnnexeById(String idCompteAnnexe) {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{getCompteAnnexeById}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        if (JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            log.logMessage("5012", "{getCompteAnnexeById}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);

        try {
            compteAnnexe.retrieve(transaction);
            if (compteAnnexe.isNew() || compteAnnexe.hasErrors()) {
                log.logStringBuffer(transaction.getErrors(), this.getClass().getName());
                log.logMessage("5004", null, FWMessage.FATAL, this.getClass().getName());
                return null;
            }
        } catch (Exception e) {
            log.logMessage("5002", "Error in getCompteAnnexeById - " + e.getMessage(), FWMessage.FATAL, this.getClass()
                    .getName());
            return null;
        }

        return compteAnnexe;
    }

    /**
     * Retourne un compte annexe qui correspond au r�le. Si le compte annexe n'existe pas, on ouvre un nouveau compte.
     */
    @Override
    public APICompteAnnexe getCompteAnnexeByRole(String idTiers, String idRole, String idExterneRole) {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{getCompteAnnexeByRole}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        // V�rifier les param�tres
        if ((idRole == null) || (idExterneRole == null) || (idTiers == null)) {
            log.logMessage("5012", "{getCompteAnnexeByRole}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }
        if (JadeStringUtil.isIntegerEmpty(idRole) || JadeStringUtil.isBlank(idExterneRole)
                || JadeStringUtil.isIntegerEmpty(idTiers)) {
            log.logMessage("5012", "{getCompteAnnexeByRole}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);

        try {
            compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compteAnnexe.retrieve(transaction);

            compteAnnexe.setAlternateKey(0);
            if (compteAnnexe.isNew()) {
                return createCompteAnnexe(idTiers, idRole, idExterneRole);
            }
        } catch (Exception e) {
            log.logMessage("5002", "Error in getCompteAnnexeByRole - " + e.getMessage(), FWMessage.FATAL, this
                    .getClass().getName());
            compteAnnexe = null;
        }

        return compteAnnexe;
    }

    /**
     * Doit-on imprimer le journal lors de la comptabilisation ?
     * 
     * @return
     */
    private boolean getImprimerJournal() {
        return imprimerJournal;
    }

    /**
     * Doit-on imprimer l'ordre group� lors de la comptabilisation ?
     * 
     * @return
     */
    private boolean getImprimerOrdreGroupe() {
        return imprimerOrdreGroupe;
    }

    /**
     * Retourne le journal actuellement en cours.
     */
    @Override
    public APIJournal getJournal() {
        if (!journal.isOuvert() && !journal.isTraitement()) {
            log.logMessage("5001", "{getJournal}", FWMessage.FATAL);
            return null;
        }

        return journal;
    }

    /**
     * Retourne le log.
     * 
     * @return
     */
    @Override
    public BIMessageLog getMessageLog() {
        if (log == null) {
            log = new FWMemoryLog();
        }

        return log;
    }

    /**
     * Retourne le process.
     * 
     * @return BProcess
     */
    private BProcess getProcess() {
        if (process == null) {
            process = new CAProcessComptabiliserJournal();
            process.setControleTransaction(false);
            process.setTransaction(transaction);
        }

        process.setSession(getSession());
        return process;
    }

    /**
     * Retrouve une section gr�ce � son idexterne. Si section non r�solue => cr�ation nouvelle section.
     * 
     * @return APISection
     */
    @Override
    public APISection getSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne) {
        return this.getSectionByIdExterne(idCompteAnnexe, idTypeSection, idExterne, null, null);
    }

    /**
     * Retrouve une section gr�ce � son idexterne. Si section non r�solue => cr�ation nouvelle section.
     * 
     * @return APISection
     */
    @Override
    public APISection getSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne,
            String domaine, String typeAdresse) {
        return this.getSectionByIdExterne(idCompteAnnexe, idTypeSection, idExterne, domaine, typeAdresse,
                Boolean.FALSE, null);
    }

    /**
     * Retrouve une section gr�ce � son idexterne. Si section non r�solue => cr�ation nouvelle section. <br/>
     * domaine et typeAdresse utilis� par Facturation dans le but de sauvegarder l'adresse sp�cifique utilit� dans Naos.
     * 
     * @return APISection
     */
    @Override
    public APISection getSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne,
            String domaine, String typeAdresse, Boolean nonImprimable, FWPKProvider pkProvider) {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            log.logMessage("5001", "{getSectionByIdExterne}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        if (JadeStringUtil.isIntegerEmpty(idExterne) || JadeStringUtil.isIntegerEmpty(idTypeSection)
                || JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            log.logMessage("5012", "{getSectionByIdExterne}", FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        CASectionManager mgr = new CASectionManager();
        mgr.setSession(getSession());
        mgr.setForIdCompteAnnexe(idCompteAnnexe);
        mgr.setForIdExterne(idExterne);
        mgr.setForIdTypeSection(idTypeSection);
        try {
            int nb = mgr.getCount(transaction);
            if (mgr.hasErrors()) {
                log.logStringBuffer(transaction.getErrors(), this.getClass().getName());
                log.logMessage("5004", null, FWMessage.FATAL, this.getClass().getName());
                return null;
            }
            if (nb > 0) {
                if (nb > 1) {
                    log.logMessage("5043", null, FWMessage.FATAL, this.getClass().getName());
                    return null;
                } else {
                    mgr.find(transaction, 1);
                    return (CASection) mgr.getFirstEntity();
                }
            } else {
                String idSection = null;
                if (pkProvider != null) {
                    try {
                        idSection = pkProvider.getNextPrimaryKey();
                    } catch (Exception e) {
                        idSection = null;
                        JadeCodingUtil.catchException(this, "getSectionByIdExterne", e);
                    }
                }
                return this.createSection(idSection, idCompteAnnexe, idTypeSection, idExterne, domaine, typeAdresse,
                        nonImprimable, null);
            }
        } catch (Exception e) {
            log.logMessage("5002", "Error in getSectionByIdExterne - " + e.getMessage(), FWMessage.FATAL, this
                    .getClass().getName());
            return null;
        }
    }

    /**
     * Retourne la session en cours.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    /**
     * Retourne vrai s'il y a au moins une erreur fatale.
     * 
     * @return boolean
     */
    @Override
    public boolean hasFatalErrors() {
        return log.getErrorLevel().equals(FWMessage.FATAL);
    }

    /**
     * Imprimer le journal.
     * 
     * @author S�bastien Chappatte Cette m�thode permet d'imprimer un journal en utilisant IText
     */
    public void imprimerJournal() {
        try {
            CAIListJournalEcritures_Doc proc = null;

            if (getProcess() == null) {
                proc = new CAIListJournalEcritures_Doc();
                proc.setControleTransaction(true);
                proc.setEMailAddress(eMailAddress);
            } else {
                proc = new CAIListJournalEcritures_Doc(getProcess());
            }

            proc.setIdJournal(journal.getIdJournal());

            proc.executeProcess();
        } catch (Exception e) {
            log.logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }
    }

    /**
     * Imprimer l'ordre group�.
     * 
     * @author SCO
     */
    public void imprimerOrdreGroupe(String idOrdreGroupe) {
        try {
            CAIProcessListOrdreGroupe proc = new CAIProcessListOrdreGroupe();
            proc.setControleTransaction(false);
            proc.setTransaction(transaction);
            proc.setEMailAddress(eMailAddress);
            proc.setIdOrdreGroupe(idOrdreGroupe);
            proc.setSession(getSession());
            proc.executeProcess();

        } catch (Exception e) {
            log.logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }
    }

    /**
     * Le journal contient-il des op�rations ?
     * 
     * @return
     * @throws Exception
     */
    public boolean isJournalEmpty() {
        CAOperationManager manager = new CAOperationManager();
        manager.setSession(getSession());

        manager.setForIdJournal(journal.getIdJournal());

        try {
            manager.find();
        } catch (Exception e) {
            log.logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return manager.isEmpty();
    }

    /**
     * @see APIGestionComptabiliteExterne#isPeriodeComptableOuverte(String)
     */
    @Override
    public boolean isPeriodeComptableOuverte(String dateValeurCG) {
        return new CAUtilsJournal().isPeriodeComptableOuverte(session, transaction, dateValeurCG);
    }

    /**
     * Cr�ation de l'ordre group� correspondant au journal.<BR>
     * <BR>
     * - Cr�ation d'un ordre group� avec les ordres du journal sp�cifi�<BR>
     * - Pr�paration de l'ordre group�<BR>
     * <BR>
     * 
     * @param idOrganeExecution
     * @param numeroOG
     * @param dateEcheance
     * @param typeOrdre
     * @param natureOrdre
     * @paran libelleOG
     */
    @Override
    public void preparerOrdreGroupe(String idOrganeExecution, String numeroOG, String dateEcheance, String typeOrdre,
            String natureOrdre, String libelleOG) {

        boolean continu = true;

        // V�rification d'usage
        // ------------------------------------------------------------------------
        // - Validit� de l'organe d'execution
        if (JadeStringUtil.isIntegerEmpty(idOrganeExecution)) {
            log.logMessage("POG_ORGANE_EXECUTION", "{idOrganeExecution}", FWMessage.FATAL, this.getClass().getName());
            continu = false;
        } else {

            CAOrganeExecution organeExecution = new CAOrganeExecution();
            organeExecution.setSession(getSession());
            organeExecution.setIdOrganeExecution(idOrganeExecution);

            try {
                organeExecution.retrieve(transaction);
                if (organeExecution.isNew() || organeExecution.hasErrors()) {
                    log.logStringBuffer(transaction.getErrors(), this.getClass().getName());
                    log.logMessage("POG_RETRIEVE_ORGANE_EXECUTION", null, FWMessage.FATAL, this.getClass().getName());
                    continu = false;
                }
            } catch (Exception e) {
                log.logMessage("5002", "Error in organeExecution.retrieve - " + e.getMessage(), FWMessage.FATAL, this
                        .getClass().getName());
                continu = false;
            }
        }

        // V�rifie qu'une date est pr�sente
        if (JAUtil.isDateEmpty(dateEcheance)) {
            log.logMessage("POG_DATE_ECHEANCE", "{dateEcheance}", FWMessage.FATAL, this.getClass().getName());
            continu = false;
        }

        // V�rifie sa validit�
        try {
            BSessionUtil.checkDateGregorian(session, dateEcheance);
        } catch (Exception jae) {
            log.logMessage("POG_DATE_ECHEANCE_FORMAT", "{checkDateGregorian}", FWMessage.FATAL, this.getClass()
                    .getName());
            continu = false;
        }

        // - Pr�sence d'un journal
        if (journal == null) {
            log.logMessage("POG_JOURNAL", "{journal}", FWMessage.FATAL, this.getClass().getName());
            continu = false;
        }

        // - Etat du journal
        if (!journal.isComptabilise()) {
            log.logMessage("POG_JOURNAL_ETAT", FWMessage.FATAL, this.getClass().getName());
            continu = false;
        }

        // Si erreur, on envoi un mail et on sort de la m�thode
        if (!continu) {
            // Envoi du mail de fin
            if (sendCompletionMail) {
                this.sendFinalPreparerEmail();
            }
            return;
        }
        log.logMessage("POG_PARAM_VALIDE", null, FWMessage.INFORMATION, this.getClass().getName());

        // Cr�ation de l'ordre group�
        // La description de l'ordre group� est compos� du num�ro du journal
        // suivi
        // de son libell�.
        // ------------------------------------------------------------------------
        CAOrdreGroupe ordreGroupe = new CAOrdreGroupe();
        ordreGroupe.setSession(getSession());
        ordreGroupe.setIdOrganeExecution(idOrganeExecution);
        ordreGroupe.setNumeroOG(numeroOG);
        ordreGroupe.setDateEcheance(dateEcheance);
        ordreGroupe.setTypeOrdreGroupe(typeOrdre);
        ordreGroupe.setNatureOrdresLivres(natureOrdre);
        if (!JadeStringUtil.isEmpty(libelleOG)) {

            // BUG 5038, libelle trop long
            if (libelleOG.length() > 49) {
                libelleOG = libelleOG.substring(0, 49);
            }

            ordreGroupe.setMotif(libelleOG);
        } else {
            ordreGroupe.setMotif(journal.getIdJournal() + " " + journal.getLibelle());
        }

        try {
            ordreGroupe.add(transaction);

            if (ordreGroupe.hasErrors()) {
                log.logStringBuffer(transaction.getErrors(), CAOrdreGroupe.class.getName());
                log.logMessage("POG_ADD_ORDRE_GROUPE", null, FWMessage.FATAL, this.getClass().getName());
                continu = false;
            }

        } catch (Exception e) {
            log.logMessage("5002", "Error in ordreGroupe.add - " + e.getMessage(), FWMessage.FATAL, this.getClass()
                    .getName());
            continu = false;
        }

        // Si erreur, on envoi un mail et on sort de la m�thode
        if (!continu) {
            // Envoi du mail de fin
            if (sendCompletionMail) {
                this.sendFinalPreparerEmail();
            }
            return;
        }
        log.logMessage("POG_CREATION_OG_SUCCES", null, FWMessage.INFORMATION, this.getClass().getName());

        // Lancer la preparation.
        // ------------------------------------------------------------------------
        CAProcessAttacherOrdre processAttacheOrdre = new CAProcessAttacherOrdre();
        processAttacheOrdre.setControleTransaction(false);
        processAttacheOrdre.setTransaction(transaction);
        processAttacheOrdre.setSession(getSession());
        processAttacheOrdre.setEMailAddress(eMailAddress);
        processAttacheOrdre.setSendCompletionMail(sendCompletionMail);
        processAttacheOrdre.setIdOrdreGroupe(ordreGroupe.getIdOrdreGroupe());
        processAttacheOrdre.setIdJournalSource(journal.getIdJournal());

        try {
            processAttacheOrdre.executeProcess();
        } catch (Exception e) {
            log.logMessage("5002", "Error in processAttacheOrdre.executeProcess() - " + e.getMessage(),
                    FWMessage.FATAL, this.getClass().getName());
            continu = false;
        }

        // Si erreur, on envoi un mail et on sort de la m�thode
        if (!continu) {
            // Envoi du mail de fin
            if (sendCompletionMail) {
                this.sendFinalPreparerEmail();
            }
            return;
        }
        log.logMessage("POG_PREPRATION_OG_SUCCES", null, FWMessage.INFORMATION, this.getClass().getName());

        // Impression de l'ordre group� si necessaire
        // ------------------------------------------------------------------------
        if (getImprimerOrdreGroupe()) {
            imprimerOrdreGroupe(ordreGroupe.getIdOrdreGroupe());
        }

        // Envoi du mail de fin
        if (sendCompletionMail) {
            this.sendFinalPreparerEmail(ordreGroupe.getIdOrdreGroupe(), ordreGroupe.getMotif());
        }
    }

    /**
     * Envois un email contenant le traitement effectu� � l'adresse eMailAddress.
     * 
     * @see eMailAddress
     */
    private void sendFinalEmail() {
        // D�terminer l'objet du message en fonction du code erreur
        String obj;
        if (hasFatalErrors() || journal.isErreur()) {
            obj = getSession().getLabel("5007");
        } else {
            obj = getSession().getLabel("5006");
        }

        obj = obj + " [" + journal.getIdJournal() + "-" + journal.getLibelle() + "]";

        try {
            JadeSmtpClient.getInstance().sendMail(eMailAddress, obj, log.getMessagesInString(), null);
        } catch (Exception e) {
            getMessageLog().printToConsole();
        }
    }

    /**
     * Envois un email contenant le traitement effectu� lors de la cr�ation + pr�paration de l'ordre groupe � l'adresse
     * eMailAddress.
     * 
     * @see eMailAddress
     */
    private void sendFinalPreparerEmail() {
        this.sendFinalPreparerEmail(null, null);
    }

    /**
     * Envois un email contenant le traitement effectu� lors de la cr�ation + pr�paration de l'ordre groupe � l'adresse
     * eMailAddress.
     * 
     * @see eMailAddress
     */
    private void sendFinalPreparerEmail(String idOrdreGroupe, String libelleOrdreGroupe) {
        // D�terminer l'objet du message en fonction du code erreur
        String obj;
        if (hasFatalErrors() || log.hasErrors()) {
            obj = getSession().getLabel("POG_ERREUR");
            if (idOrdreGroupe != null) {
                obj = obj + " [" + idOrdreGroupe + "-" + libelleOrdreGroupe + "]";
            }
        } else {
            obj = getSession().getLabel("POG_SUCCES");
        }

        try {
            JadeSmtpClient.getInstance().sendMail(eMailAddress, obj, log.getMessagesInString(), null);
        } catch (Exception e) {
            getMessageLog().printToConsole();
        }
    }

    /**
     * Set la date de valeur du journal.
     * 
     * @param param String
     */
    @Override
    public void setDateValeur(String dateValeur) {
        if (!journal.isNew()) {
            log.logMessage("5001", "{setDateValeurCG}", FWMessage.FATAL);
        } else {
            journal.setDateValeurCG(dateValeur);
        }
    }

    /**
     * L'email pour la comptabilisation.
     * 
     * @param address String
     */
    @Override
    public void setEMailAddress(String address) {
        eMailAddress = address;
    }

    /**
     * Set le libell� du journal.
     * 
     * @param newLibelle String
     */
    @Override
    public void setLibelle(String newLibelle) {
        if (!journal.isNew()) {
            log.logMessage("5001", "{setLibelle}", FWMessage.FATAL);
        } else {
            journal.setLibelle(newLibelle);
        }
    }

    /**
     * Set le logs.
     * 
     * @param BIMessageLog messageLog
     */
    @Override
    public void setMessageLog(BIMessageLog messageLog) {
        log = messageLog;
    }

    /**
     * Set le process parent.
     * 
     * @param newProcess BProcess
     */
    @Override
    public void setProcess(BProcess newProcess) {
        process = newProcess;
    }

    /**
     * Doit-on envoy� les emails ?
     * 
     * @param value boolean
     */
    @Override
    public void setSendCompletionMail(boolean value) {
        sendCompletionMail = value;
    }

    /**
     * Set la session.
     * 
     * @param param globaz.globall.db.BTransaction
     */
    @Override
    public void setSession(BSession newSession) {
        session = newSession;
    }

    /**
     * Set la transaction.
     * 
     * @param param globaz.globall.db.BTransaction
     */
    @Override
    public void setTransaction(BITransaction t) {
        if (!journal.isNew()) {
            log.logMessage("5001", "{setTransaction}", FWMessage.FATAL);
        } else {
            transaction = (BTransaction) t;
            journal.setSession(getSession());
        }
    }

}
