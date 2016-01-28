package globaz.phenix.process.listes;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationCalculManager;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.listes.excel.CPListeConcordanceCotPersCI;

/**
 * Process de g�naration d'une liste excel pour les concordances entre Cot.Pers. et CI Date de cr�ation : (17.06.2007
 * 08:34:14)
 * 
 * @author: jpa
 */
public class CPProcessListeConcordanceCotPersCI extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Date d'impression
    private String dateImpression = "";
    // Num�rod de passage de facturation
    private String forIdPassage = "";
    // Num�ro d'affili� de d�part
    private String fromAffilieDebut = "";
    // Num�ro d'affili� de fin
    private String fromAffilieFin = "";
    // Ann�e de d�but de la recherche
    private String fromAnneeDecision = "";
    private String fromDiffAdmise = "";
    // P�riode de facturation d�but
    private String fromPeriodeFacturation = "";
    // Ann�e de fin de la recherche
    private String toAnneeDecision = "";
    private String toDiffAdmise = "";
    // P�riode de facturation fin
    private String toPeriodeFacturation = "";

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     */
    public CPProcessListeConcordanceCotPersCI() {
        super();
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessListeConcordanceCotPersCI(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public CPProcessListeConcordanceCotPersCI(BSession session) {
        super(session);
    }

    /**
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            CPDecisionAffiliationCalculManager manager = new CPDecisionAffiliationCalculManager();
            manager.setSession(getSession());

            AFParticulariteAffiliationManager mgrParticulariteAffiliation = new AFParticulariteAffiliationManager();
            mgrParticulariteAffiliation.setSession(getSession());
            if (!JadeStringUtil.isEmpty(getFromAnneeDecision())) {
                mgrParticulariteAffiliation.setDateDebutLessOrEqual("01.01." + getFromAnneeDecision());
            }

            if (!JadeStringUtil.isEmpty(getToAnneeDecision())) {
                mgrParticulariteAffiliation.setDateFinGreatOrEqual("31.12." + getToAnneeDecision());
            }

            manager.setNotInIdAffiliation(mgrParticulariteAffiliation.idAffCotPersAutreAgenceOuverte());

            manager.setFromAnneeDecision(getFromAnneeDecision());
            manager.setTillAnneeDecision(getToAnneeDecision());
            manager.setFromNoAffilie(getFromAffilieDebut());
            manager.setTillNoAffilie(getFromAffilieFin());
            manager.setFromPeriodeFacturation(getFromPeriodeFacturation());
            manager.setTillPeriodeFacturation(getToPeriodeFacturation());
            manager.setForIdPassage(getForIdPassage());
            manager.setIsActiveOrRadie(Boolean.TRUE);
            manager.setForIdDonneesCalcul(CPDonneesCalcul.CS_REV_CI);
            manager.setForExceptTypeDecision(CPDecision.CS_REMISE);
            manager.setForExceptSpecification(CPDecision.CS_SALARIE_DISPENSE);
            manager.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_PB_COMPTABILISATION + ", "
                    + CPDecision.CS_REPRISE + ", " + CPDecision.CS_SORTIE);
            // !!! Mettre en premier l'ordre par idTiers � cause des affili�s
            // qui changent de n�
            manager.orderByIdTiers();
            manager.orderByAnnee();
            manager.orderByNoAffilie();
            manager.changeManagerSize(0);
            // manager.find();
            /*
             * CPDecision entity = null; for(int i=0;i<manager.size();i++) { entity = (CPDecision)manager.getEntity(i);
             * }
             */

            // On regarde que le nombre n'est pas trop grand
            /*
             * if(manager.getCount()>1){ _addError(getTransaction(), "Erreur affiner"); return false; }
             */

            // Cr�ation du document
            CPListeConcordanceCotPersCI excelDoc = new CPListeConcordanceCotPersCI(getSession(),
                    getFromAnneeDecision(), getToAnneeDecision(), getFromAffilieDebut(), getFromAffilieFin(),
                    getFromPeriodeFacturation(), getToPeriodeFacturation(), getForIdPassage());
            excelDoc.setSession(getSession());
            excelDoc.setProcessAppelant(this);
            excelDoc.setFromDiffAdmise(getFromDiffAdmise());
            excelDoc.setToDiffAdmise(getToDiffAdmise());
            excelDoc.populateSheet(manager, getTransaction());
            excelDoc.toString();
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0120CCP");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires) On va compter le nombre d'inscriptions
     */
    @Override
    protected void _validate() throws Exception {
        // Contr�le du mail
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        // Si aucun crit�re de s�lection => erreur
        if (JadeStringUtil.isEmpty(getFromAnneeDecision()) && JadeStringUtil.isEmpty(getToAnneeDecision())
                && JadeStringUtil.isEmpty(getFromAffilieDebut()) && JadeStringUtil.isEmpty(getFromAffilieFin())
                && JadeStringUtil.isEmpty(getFromPeriodeFacturation())
                && JadeStringUtil.isEmpty(getToPeriodeFacturation()) && JadeStringUtil.isEmpty(getForIdPassage())) {
            getSession().addError(getSession().getLabel("SELECTION_INCOMPLETE"));
        }

        if (!JadeStringUtil.isEmpty(getForIdPassage())
                && (!JadeStringUtil.isBlankOrZero(getFromPeriodeFacturation()) || !JadeStringUtil
                        .isBlankOrZero(getToPeriodeFacturation()))) {
            getSession().addError(getSession().getLabel("SELECTION_INCORRECT"));
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * Return le sujet de l'email Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SUJET_EMAIL_LISTE_CI_CP_PASOK");
        } else {
            return getSession().getLabel("SUJET_EMAIL_LISTE_CI_CP_OK");
        }
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    /**
     * Returns le num�ro d'affili� de d�part
     * 
     * @return String
     */
    public String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    /**
     * Returns le num�ro d'affili� de fin.
     * 
     * @return String
     */
    public String getFromAffilieFin() {
        return fromAffilieFin;
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    public String getFromDiffAdmise() {
        return fromDiffAdmise;
    }

    public String getFromPeriodeFacturation() {
        return fromPeriodeFacturation;
    }

    /**
     * Returns the toAnneeDecision.
     * 
     * @return String
     */
    public String getToAnneeDecision() {
        return toAnneeDecision;
    }

    public String getToDiffAdmise() {
        return toDiffAdmise;
    }

    public String getToPeriodeFacturation() {
        return toPeriodeFacturation;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    /**
     * Sets the genreDecision.
     * 
     * @param num�ro
     *            d'affili� de d�part
     */
    public void setFromAffilieDebut(String fromNumAffilie) {
        fromAffilieDebut = fromNumAffilie;
    }

    /**
     * Sets the genreDecision.
     * 
     * @param NumAffilie
     *            le num�ro d'affili� de fin
     */
    public void setFromAffilieFin(String toNumAffilie) {
        fromAffilieFin = toNumAffilie;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setFromAnneeDecision(String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
    }

    public void setFromDiffAdmise(String fromDiffAdmise) {
        this.fromDiffAdmise = fromDiffAdmise;
    }

    public void setFromPeriodeFacturation(String fromPeriodeFacturation) {
        this.fromPeriodeFacturation = fromPeriodeFacturation;
    }

    /**
     * Sets the toAnneeDecision.
     * 
     * @param toAnneeDecision
     *            The toAnneeDecision to set
     */
    public void setToAnneeDecision(String toAnneeDecision) {
        this.toAnneeDecision = toAnneeDecision;
    }

    public void setToDiffAdmise(String toDiffAdmise) {
        this.toDiffAdmise = toDiffAdmise;
    }

    public void setToPeriodeFacturation(String toPeriodeFacturation) {
        this.toPeriodeFacturation = toPeriodeFacturation;
    }

}
