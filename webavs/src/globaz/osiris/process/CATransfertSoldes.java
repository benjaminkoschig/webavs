package globaz.osiris.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAJournalManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.external.IntRole;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author dda
 */
public class CATransfertSoldes extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String EMAIL_SUBJECT_TRANSFERT_SOLDES_ERROR = "TRANSFERT_SOLDES_ERROR";

    private static final String EMAIL_SUBJECT_TRANSFERT_SOLDES_OK = "TRANSFERT_SOLDES_OK";
    private static final String LABEL_AUCUNES_SECTIONS_TRANSFERER = "AUCUNES_SECTIONS_TRANSFERER";
    private static final String LABEL_COMPTE_ANNEXE_DESTINATION = "COMPTE_ANNEXE_DESTINATION";
    private static final String LABEL_COMPTES_ANNEXES_DIFFERENTS = "COMPTES_ANNEXES_DIFFERENTS";
    private static final String LABEL_IMPOSSIBLE_CREER_SECTION = "IMPOSSIBLE_CREER_SECTION";
    private static final String LABEL_LIBELLE_ECRITURE_DEST_TRANSFERT_SOLDE = "LIBELLE_ECRITURE_DEST_TRANSFERT_SOLDE";
    private static final String LABEL_LIBELLE_ECRITURE_REF_TRANSFERT_SOLDE = "LIBELLE_ECRITURE_REF_TRANSFERT_SOLDE";
    private static final String LABEL_MONTANT_INFERIEUR_SOLDE_COMPTE_ANNEXE = "MONTANT_INFERIEUR_SOLDE_COMPTE_ANNEXE";
    private static final String LABEL_MONTANT_MEME_SIGNE = "MONTANT_MEME_SIGNE";
    private static final String LABEL_MONTANT_NON_NULL = "7385";
    private static final String LABEL_MONTANT_NON_RENSEIGNE = "5129";
    private static final String LABEL_MONTANT_SUPERIEUR_SOLDE_COMPTE_ANNEXE = "MONTANT_SUPERIEUR_SOLDE_COMPTE_ANNEXE";
    private static final String LABEL_MOTIF_NON_RENSEIGNE = "MOTIF_NON_RENSEIGNE";

    private static final String LABEL_TIERS_NON_RENSEIGNE = "5045";
    private static final String SECTION_ID_EXTERNE_RIGHT_PART = "00000";

    private String forSelectionRole = null;
    private String idExterneDestinationCompteAnnexe = "";

    private String idExterneDestinationSection = "";
    private String idSourceCompteAnnexe = null;

    private String idTiersDestinationCompteAnnexe = null;
    private String idTypeDestinationSection = null;

    private CAJournal journal = null;

    private String motifJournal = new String();

    private Boolean quittancer = new Boolean(false);
    private boolean sendEmail = true;
    private String soldeMaxToTransfer = null;
    private CACompteAnnexe sourceCompteAnnexe = null;
    private boolean useJournalJournalier = true;

    /**
     * Constructor for CATransfertSoldes.
     */
    public CATransfertSoldes() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CATransfertSoldes.
     * 
     * @param parent
     */
    public CATransfertSoldes(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CATransfertSoldes.
     * 
     * @param session
     */
    public CATransfertSoldes(BSession session) throws Exception {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        if (!validateSolde()) {
            getMemoryLog().logMessage(getSession().getLabel(CATransfertSoldes.LABEL_MONTANT_NON_NULL), FWMessage.FATAL,
                    this.getClass().getName());
            return false;
        }

        if (sourceIdEqualsDestinationId()) {
            getMemoryLog().logMessage(getSession().getLabel(CATransfertSoldes.LABEL_COMPTES_ANNEXES_DIFFERENTS),
                    FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        if (isQuittancer().booleanValue() && JadeStringUtil.isBlank(getIdTiersDestinationCompteAnnexe())) {
            setIdTiersDestCompteAnnexeFromRole();
        }

        if (!isUseJournalJournalier() && JadeStringUtil.isBlank(getMotifJournal())) {
            getMemoryLog().logMessage(getSession().getLabel(CATransfertSoldes.LABEL_MOTIF_NON_RENSEIGNE),
                    FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        if (JadeStringUtil.isBlank(getIdExterneDestinationCompteAnnexe())) {
            getMemoryLog().logMessage(getSession().getLabel(CATransfertSoldes.LABEL_COMPTE_ANNEXE_DESTINATION),
                    FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        try {
            CACompteAnnexe destinationCompteAnnexe = getDestinationCompteAnnexe();

            FWCurrency soldeTotal = soldeSourceSections();

            transfertSolde(destinationCompteAnnexe, soldeTotal);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(isSendEmail());
        setSendMailOnError(isSendEmail());

        if (!validateSolde()) {
            this._addError(getTransaction(), getSession().getLabel(CATransfertSoldes.LABEL_MONTANT_NON_NULL));
        }

        if (sourceIdEqualsDestinationId()) {
            this._addError(getTransaction(), getSession().getLabel(CATransfertSoldes.LABEL_COMPTES_ANNEXES_DIFFERENTS));
        }

        if (JadeStringUtil.isBlank(getSoldeMaxToTransfer())) {
            this._addError(getTransaction(), getSession().getLabel(CATransfertSoldes.LABEL_MONTANT_NON_RENSEIGNE));
        }

        if (!validateSigne()) {
            this._addError(getTransaction(), getSession().getLabel(CATransfertSoldes.LABEL_MONTANT_MEME_SIGNE));
        }

        if (!validateSoldeMaxToTransfer()) {
            if ((new FWCurrency(getSourceCompteAnnexe().getSolde())).isNegative()) {
                this._addError(getTransaction(),
                        getSession().getLabel(CATransfertSoldes.LABEL_MONTANT_INFERIEUR_SOLDE_COMPTE_ANNEXE));
            } else {
                this._addError(getTransaction(),
                        getSession().getLabel(CATransfertSoldes.LABEL_MONTANT_SUPERIEUR_SOLDE_COMPTE_ANNEXE));
            }
        }

        if (isQuittancer().booleanValue() && JadeStringUtil.isBlank(getIdTiersDestinationCompteAnnexe())) {
            setIdTiersDestCompteAnnexeFromRole();
        }

        if (!isUseJournalJournalier() && JadeStringUtil.isBlank(getMotifJournal())) {
            this._addError(getTransaction(), getSession().getLabel(CATransfertSoldes.LABEL_MOTIF_NON_RENSEIGNE));
        }
    }

    /**
     * Créer un journal spécial pour le transfert de solde (utilisé par exemple pour le process de changmenet de nom de
     * PHENIX, spécialité CS).
     * 
     * @return
     * @throws Exception
     */
    private CAJournal createJournal() throws Exception {
        CAJournal journal = new CAJournal();
        journal.setSession(getSession());

        journal.setLibelle(getMotifJournal());
        journal.setDateValeurCG(JACalendar.todayJJsMMsAAAA());
        journal.setDate(JACalendar.todayJJsMMsAAAA());

        journal.add(getTransaction());

        return journal;
    }

    /**
     * Retourne le compte annexe de destination. <br/>
     * Si le mode quittancer est actif et que le compte de destination ne peut-être résolu alors le compte annexe de
     * destination sera créé.
     * 
     * @return
     * @throws Exception
     */
    private CACompteAnnexe getDestinationCompteAnnexe() throws Exception {
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(getSession());
        manager.setForIdExterneRole(getIdExterneDestinationCompteAnnexe());

        manager.setForSelectionRole(getForSelectionRole());

        manager.find(getTransaction());

        if (manager.hasErrors()) {
            throw new Exception(getSession().getLabel(CATransfertSoldes.LABEL_COMPTE_ANNEXE_DESTINATION));
        }

        if (manager.isEmpty()) {
            if (isQuittancer().booleanValue()) {
                CACompteAnnexe newDestinationCompteAnnexe = new CACompteAnnexe();
                newDestinationCompteAnnexe.setSession(getSession());

                newDestinationCompteAnnexe.setIdExterneRole(getIdExterneDestinationCompteAnnexe());
                newDestinationCompteAnnexe.setIdRole(getForSelectionRole());
                newDestinationCompteAnnexe.setIdJournal(getJournal().getIdJournal());
                newDestinationCompteAnnexe.setIdTiers(getIdTiersDestinationCompteAnnexe());

                newDestinationCompteAnnexe.add(getTransaction());

                if (newDestinationCompteAnnexe.hasErrors()) {
                    throw new Exception(newDestinationCompteAnnexe.getErrors().toString());
                }

                return newDestinationCompteAnnexe;
            } else {
                throw new Exception(getSession().getLabel(CATransfertSoldes.LABEL_COMPTE_ANNEXE_DESTINATION));
            }
        } else {
            return (CACompteAnnexe) manager.getFirstEntity();
        }
    }

    /**
     * Retourne la dernière section ouverte de l'année en cours (exemple 200500000). Si cette dernière n'éxiste pas =>
     * elle sera créée.
     * 
     * @param idCompteAnnexe
     * @return
     * @throws Exception
     */
    private CASection getDestinationSection(String idCompteAnnexe) throws Exception {
        CASectionManager manager = new CASectionManager();
        manager.setSession(getSession());
        manager.setForIdCompteAnnexe(idCompteAnnexe);

        if (!JadeStringUtil.isBlank(getIdExterneDestinationSection())) {
            manager.setForIdExterne(getIdExterneDestinationSection());
        } else {
            manager.setForIdExterne("" + JACalendar.today().getYear() + CATransfertSoldes.SECTION_ID_EXTERNE_RIGHT_PART);
        }

        manager.setForIdTypeSection(getIdTypeDestinationSection());

        manager.find(getTransaction());

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        CASection section = null;
        if (manager.isEmpty()) {
            if (isQuittancer().booleanValue()) {
                section = new CASection();
                section.setSession(getSession());

                section.setIdCompteAnnexe(idCompteAnnexe);

                if (!JadeStringUtil.isBlank(getIdExterneDestinationSection())) {
                    section.setIdExterne(getIdExterneDestinationSection());
                } else {
                    section.setIdExterne("" + JACalendar.today().getYear()
                            + CATransfertSoldes.SECTION_ID_EXTERNE_RIGHT_PART);
                }

                section.setIdJournal(getJournal().getIdJournal());
                section.setIdTypeSection(getIdTypeDestinationSection());
                section.setDateSection("0101" + JACalendar.today().getYear());

                section.add(getTransaction());

                if (section.hasErrors()) {
                    throw new Exception(getSession().getLabel(CATransfertSoldes.LABEL_IMPOSSIBLE_CREER_SECTION)
                            + " (idCptAnnexe = " + idCompteAnnexe + ")");
                }
            } else {
                throw new Exception(getSession().getLabel(CATransfertSoldes.LABEL_IMPOSSIBLE_CREER_SECTION) + ", "
                        + getSession().getLabel("QUITTANCE_OBLIGATOIRE"));
            }
        } else {
            section = (CASection) manager.getFirstEntity();
        }

        return section;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel(CATransfertSoldes.EMAIL_SUBJECT_TRANSFERT_SOLDES_ERROR);
        } else {
            return getSession().getLabel(CATransfertSoldes.EMAIL_SUBJECT_TRANSFERT_SOLDES_OK);
        }
    }

    /**
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return
     */
    public String getIdExterneDestinationCompteAnnexe() {
        return idExterneDestinationCompteAnnexe;
    }

    public String getIdExterneDestinationSection() {
        return idExterneDestinationSection;
    }

    /**
     * @return
     */
    public String getIdSourceCompteAnnexe() {
        return idSourceCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdTiersDestinationCompteAnnexe() {
        return idTiersDestinationCompteAnnexe;
    }

    public String getIdTypeDestinationSection() {
        return idTypeDestinationSection;
    }

    /**
     * Retourne le journal ouvert du jour. Si aucun journal du jour est ouvert on en créé un.
     * 
     * @return
     * @throws Exception
     */
    public CAJournal getJournal() throws Exception {
        if (journal == null) {
            CAJournalManager manager = new CAJournalManager();
            manager.setSession(getSession());
            manager.setForTypeJournal(CAJournal.TYPE_JOURNALIER);
            manager.setForProprietaire(getSession().getUserName());
            manager.setForEtat(CAJournal.OUVERT);
            manager.setForDateValeurCG(JACalendar.today().toStr("."));
            manager.find(getTransaction());

            if (!manager.isEmpty()) {
                journal = (CAJournal) manager.getEntity(0);
            } else {
                if (isUseJournalJournalier()) {
                    journal = CAJournal.fetchJournalJournalier(getSession(), getTransaction());
                } else {
                    journal = createJournal();
                }
            }
        }
        return journal;
    }

    /**
     * @return
     */
    public String getMotifJournal() {
        return motifJournal;
    }

    /**
     * @return
     */
    public Boolean getQuittancer() {
        return isQuittancer();
    }

    /**
     * Retourne les sections à transférer.
     * 
     * @param positiveSections
     *            True pour rechercher uniquement les sections dont le solde > 0.
     * @return
     * @throws Exception
     */
    private Iterator getSections(boolean positiveSections) throws Exception {
        CASectionManager manager = new CASectionManager();
        manager.setSession(getSession());
        manager.setForIdCompteAnnexe(getSourceCompteAnnexe().getIdCompteAnnexe());
        manager.setOrderBy(CASectionManager.ORDER_DATE);

        if (positiveSections) {
            manager.setForSelectionSections("3");
        } else {
            manager.setForSelectionSections("4");
        }

        manager.find(getTransaction());

        if (manager.hasErrors()) {
            throw new Exception(getSession().getLabel(CATransfertSoldes.LABEL_AUCUNES_SECTIONS_TRANSFERER));
        }

        Vector result = new Vector();
        for (int i = 0; i < manager.size(); i++) {
            result.add(manager.getEntity(i));
        }

        if (result.isEmpty()) {
            result.add(getDestinationSection(getSourceCompteAnnexe().getIdCompteAnnexe()));
        }

        return result.iterator();
    }

    /**
     * @return
     */
    public String getSoldeMaxToTransfer() {
        return soldeMaxToTransfer;
    }

    /**
     * @return
     */
    public CACompteAnnexe getSourceCompteAnnexe() {
        if (sourceCompteAnnexe == null) {
            sourceCompteAnnexe = new CACompteAnnexe();
            sourceCompteAnnexe.setSession(getSession());
            sourceCompteAnnexe.setIdCompteAnnexe(getIdSourceCompteAnnexe());

            try {
                sourceCompteAnnexe.retrieve();
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            }
        }

        return sourceCompteAnnexe;
    }

    /**
     * @return
     */
    public Boolean isQuittancer() {
        return quittancer;
    }

    /**
     * @return
     */
    public boolean isSendEmail() {
        return sendEmail;
    }

    /**
     * @return
     */
    public boolean isUseJournalJournalier() {
        return useJournalJournalier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     */
    public void setIdExterneDestinationCompteAnnexe(String s) {
        idExterneDestinationCompteAnnexe = s;
    }

    public void setIdExterneDestinationSection(String idExterneDestinationSection) {
        this.idExterneDestinationSection = idExterneDestinationSection;
    }

    /**
     * @param string
     */
    public void setIdSourceCompteAnnexe(String string) {
        idSourceCompteAnnexe = string;
    }

    private void setIdTiersDestCompteAnnexeFromRole() throws Exception {
        CAApplication currentApplication = CAApplication.getApplicationOsiris();
        IntRole role = (IntRole) globaz.globall.db.GlobazServer.getCurrentSystem()
                .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                .getImplementationFor(getSession(), IntRole.class);
        role.retrieve(getForSelectionRole(), getIdExterneDestinationCompteAnnexe());

        if (role.isNew()) {
            this._addError(getTransaction(), getSession().getLabel(CATransfertSoldes.LABEL_TIERS_NON_RENSEIGNE));
            throw new Exception(getSession().getLabel(CATransfertSoldes.LABEL_TIERS_NON_RENSEIGNE));
        }

        setIdTiersDestinationCompteAnnexe(role.getIdTiers());
    }

    /**
     * @param string
     */
    public void setIdTiersDestinationCompteAnnexe(String s) {
        idTiersDestinationCompteAnnexe = s;
    }

    public void setIdTypeDestinationSection(String idTypeDestinationSection) {
        this.idTypeDestinationSection = idTypeDestinationSection;
    }

    /**
     * @param string
     */
    public void setMotifJournal(String s) {
        motifJournal = s;
    }

    /**
     * @param b
     */
    public void setQuittancer(Boolean b) {
        quittancer = b;
    }

    /**
     * @param b
     */
    public void setSendEmail(boolean b) {
        sendEmail = b;
    }

    /**
     * @param string
     */
    public void setSoldeMaxToTransfer(String s) {
        soldeMaxToTransfer = s;
    }

    /**
     * @param b
     */
    public void setUseJournalJournalier(boolean b) {
        useJournalJournalier = b;
    }

    /**
     * Solder les sections du comptes sources.
     * 
     * @return Le montant total à transférer sur le compte de destination.
     * @throws Exception
     */
    private FWCurrency soldeSourceSections() throws Exception {
        if (JadeStringUtil.isBlank(getSoldeMaxToTransfer())) {
            throw new Exception(getSession().getLabel(CATransfertSoldes.LABEL_MONTANT_NON_RENSEIGNE));
        }

        FWCurrency soldeMax = new FWCurrency(getSoldeMaxToTransfer());

        if (!validateSigne()) {
            throw new Exception(getSession().getLabel(CATransfertSoldes.LABEL_MONTANT_MEME_SIGNE));
        }

        if (!validateSoldeMaxToTransfer()) {
            if ((new FWCurrency(getSourceCompteAnnexe().getSolde())).isNegative()) {
                throw new Exception(getSession()
                        .getLabel(CATransfertSoldes.LABEL_MONTANT_INFERIEUR_SOLDE_COMPTE_ANNEXE));
            } else {
                throw new Exception(getSession()
                        .getLabel(CATransfertSoldes.LABEL_MONTANT_SUPERIEUR_SOLDE_COMPTE_ANNEXE));
            }
        }

        if (soldeMax.isPositive()) {
            soldeSourceSectionsPositive();
            soldeMax.negate();
        } else {
            soldeSourceSectionsNegative();
            soldeMax.abs();
        }

        return soldeMax;
    }

    /**
     * Solder les sections < 0 du compte sources.
     * 
     * @throws Exception
     */
    private void soldeSourceSectionsNegative() throws Exception {
        FWCurrency soldeMax = new FWCurrency(getSoldeMaxToTransfer());

        Iterator it = getSections(false);

        String libelle = getSession().getLabel(CATransfertSoldes.LABEL_LIBELLE_ECRITURE_DEST_TRANSFERT_SOLDE) + " "
                + getIdExterneDestinationCompteAnnexe();

        while ((it.hasNext()) && (!soldeMax.isZero())) {
            CASection section = (CASection) it.next();
            FWCurrency sectionSolde = section.getSoldeToCurrency();

            if ((sectionSolde.compareTo(soldeMax) > 1) && (it.hasNext())) {
                section.addEcriture(getTransaction(), getSourceCompteAnnexe(), getJournal(), sectionSolde, libelle,
                        APIReferenceRubrique.COMPENSATION_TRANSFERT_DE_SOLDE);
                soldeMax.sub(sectionSolde);
            } else {
                section.addEcriture(getTransaction(), getSourceCompteAnnexe(), getJournal(), soldeMax, libelle,
                        APIReferenceRubrique.COMPENSATION_TRANSFERT_DE_SOLDE);
                soldeMax.sub(soldeMax);
            }
        }
    }

    /**
     * Solder les sections > 0 du compte sources.
     * 
     * @throws Exception
     */
    private void soldeSourceSectionsPositive() throws Exception {
        FWCurrency soldeMax = new FWCurrency(getSoldeMaxToTransfer());

        Iterator it = getSections(true);

        String libelle = getSession().getLabel(CATransfertSoldes.LABEL_LIBELLE_ECRITURE_DEST_TRANSFERT_SOLDE) + " "
                + getIdExterneDestinationCompteAnnexe();

        while ((it.hasNext()) && (!soldeMax.isZero())) {
            CASection section = (CASection) it.next();
            FWCurrency sectionSolde = section.getSoldeToCurrency();

            if ((sectionSolde.compareTo(soldeMax) < 1) && (it.hasNext())) {
                section.addEcriture(getTransaction(), getSourceCompteAnnexe(), getJournal(), sectionSolde, libelle,
                        APIReferenceRubrique.COMPENSATION_TRANSFERT_DE_SOLDE);
                soldeMax.sub(sectionSolde);
            } else {
                section.addEcriture(getTransaction(), getSourceCompteAnnexe(), getJournal(), soldeMax, libelle,
                        APIReferenceRubrique.COMPENSATION_TRANSFERT_DE_SOLDE);
                soldeMax.sub(soldeMax);
            }
        }
    }

    /**
     * Le compte annexe source doit être différent du compte source.
     * 
     * @return
     */
    private boolean sourceIdEqualsDestinationId() {
        if (getSourceCompteAnnexe().getIdExterneRole().equals(getIdExterneDestinationCompteAnnexe())
                && getSourceCompteAnnexe().getIdRole().equals(getForSelectionRole())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Transfert le solde total vers le compte de destination.
     * 
     * @param destinationCompteAnnexe
     * @param soldeTotal
     * @throws Exception
     */
    private void transfertSolde(CACompteAnnexe destinationCompteAnnexe, FWCurrency soldeTotal) throws Exception {
        String libelle = getSession().getLabel(CATransfertSoldes.LABEL_LIBELLE_ECRITURE_REF_TRANSFERT_SOLDE) + " "
                + getSourceCompteAnnexe().getIdExterneRole();

        CASection destinationSection = getDestinationSection(destinationCompteAnnexe.getIdCompteAnnexe());
        destinationSection.addEcriture(getTransaction(), destinationCompteAnnexe, getJournal(), soldeTotal, libelle,
                APIReferenceRubrique.COMPENSATION_TRANSFERT_DE_SOLDE);
    }

    /**
     * Le montant a transférer doit être de même signe que le montant du compte annexe source.
     * 
     * @return True si condition remplie.
     */
    private boolean validateSigne() {
        FWCurrency soldeToTransfer = new FWCurrency(getSoldeMaxToTransfer());
        FWCurrency soldeSourceCompteAnnexe = new FWCurrency(getSourceCompteAnnexe().getSolde());

        if ((soldeToTransfer.isPositive() && soldeSourceCompteAnnexe.isPositive())
                || (soldeToTransfer.isNegative() && soldeSourceCompteAnnexe.isNegative())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Le solde du compte annexe source ne doit pas être nul.
     * 
     * @return
     */
    private boolean validateSolde() {
        FWCurrency solde = new FWCurrency(getSourceCompteAnnexe().getSolde());

        if (solde.isZero()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Le montant a transférer (abs) doit être plus petit ou égal au montant du compte annexe source (abs).
     * 
     * @return True si condition remplie.
     */
    private boolean validateSoldeMaxToTransfer() {
        FWCurrency soldeToTransfer = new FWCurrency(getSoldeMaxToTransfer());
        FWCurrency soldeSourceCompteAnnexe = new FWCurrency(getSourceCompteAnnexe().getSolde());

        soldeToTransfer.abs();
        soldeSourceCompteAnnexe.abs();

        if (soldeToTransfer.compareTo(soldeSourceCompteAnnexe) < 1) {
            return true;
        } else {
            return false;
        }
    }
}
