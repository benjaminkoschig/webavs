package globaz.phenix.process.listes;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationTiersManager;
import globaz.phenix.listes.excel.CPListeDecisions;

public abstract class CPProcessListeDecisionsPrincipale extends BProcess {

    private static final long serialVersionUID = 1L;
    private java.lang.String anneeDecision = "";
    private String fromAffilieDebut = "";
    private String fromAffilieFin = "";
    private java.lang.String fromDebutPeriode = "";
    private java.lang.String genreAffilie = "";
    private java.lang.String idPassage = "";
    private Boolean isActive = Boolean.FALSE;
    private java.lang.String toFinPeriode = "";

    private java.lang.String typeDecision = "";

    public CPProcessListeDecisionsPrincipale() {
        super();
    }

    public CPProcessListeDecisionsPrincipale(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CPDecisionAffiliationTiersManager manager = new CPDecisionAffiliationTiersManager();
            manager.setSession(getSession());
            manager.setForAnneeDecision(getAnneeDecision());
            manager.setFromAffilie(getFromAffilieDebut());
            manager.setToNumAffilie(getFromAffilieFin());
            // Si liste des décisions comptabilisées (test sur période renseigné dans ce cas)
            // => si indépendant de sélectionné, il faut prendre tous les catégories d'indépendant soit (rentier,
            // agriculteur etc...)
            // => idem pour non actif ou il faut prendre non actif et étudiant
            if (!JadeStringUtil.isBlankOrZero(getFromDebutPeriode())) {
                // Liste des décisions comptabilisées dans une période
                if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getGenreAffilie())) {
                    manager.setWantIndependant(Boolean.TRUE);
                } else if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())) {
                    manager.setWantNonActif(Boolean.TRUE);
                }
            }
            manager.setForGenreAffilie(getGenreAffilie());
            manager.setForIdPassage(getIdPassage());
            manager.setFromDateFacturation(getFromDebutPeriode());
            manager.setToDateFacturation(getToFinPeriode());
            manager.setForTypeDecision(getTypeDecision());
            manager.setSelectMaxDateInformation(Boolean.FALSE);
            manager.setIsActive(getIsActive());
            manager.orderByNumAffilie();
            manager.orderByAnnee();
            // Création du document
            CPListeDecisions excelDoc = new CPListeDecisions(getSession(), getIdPassage(), manager);
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheet(manager, getTransaction());
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            if (!JadeStringUtil.isBlankOrZero(getFromDebutPeriode())) {
                docInfo.setDocumentType("0267CCP ");
            } else {
                docInfo.setDocumentType("0081CCP");
            }
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("IMPRESSION_LISTE_DECISION_COMPTABILISER_ERREUR"));

            String messageInformation = CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        // Contrôle du mail
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        super._validate();
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Returns the anneeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getAnneeDecision() {
        return anneeDecision;
    }

    @Override
    protected String getEMailObject() {
        if ((isAborted() || getSession().hasErrors())) {
            if (JadeStringUtil.isEmpty(getFromDebutPeriode())) {
                return getSession().getLabel("SUJET_EMAIL_PASOK_LISTE_DECISION");
            } else {
                return getSession().getLabel("PASOK_LISTEDECISIONPERIODE");
            }
        } else {
            if (JadeStringUtil.isEmpty(getFromDebutPeriode())) {
                return getSession().getLabel("SUJET_EMAIL_OK_LISTE_DECISION");
            } else {
                return getSession().getLabel("LISTDECISIONPERIODE");
            }
        }
    }

    /**
     * Returns the fromAffilieDebut.
     * 
     * @return String
     */
    public String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    /**
     * Returns the fromAffilieFin.
     * 
     * @return String
     */
    public String getFromAffilieFin() {
        return fromAffilieFin;
    }

    public java.lang.String getFromDebutPeriode() {
        return fromDebutPeriode;
    }

    /**
     * Returns the genreAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * Returns the idPassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public java.lang.String getToFinPeriode() {
        return toFinPeriode;
    }

    /**
     * Returns the typeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeDecision() {
        return typeDecision;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Sets the anneeDecision.
     * 
     * @param anneeDecision
     *            The anneeDecision to set
     */
    public void setAnneeDecision(java.lang.String anneeDecision) {
        this.anneeDecision = anneeDecision;
    }

    /**
     * Sets the fromAffilieDebut.
     * 
     * @param fromAffilieDebut
     *            The fromAffilieDebut to set
     */
    public void setFromAffilieDebut(String fromAffilieDebut) {
        this.fromAffilieDebut = fromAffilieDebut;
    }

    /**
     * Sets the fromAffilieFin.
     * 
     * @param fromAffilieFin
     *            The fromAffilieFin to set
     */
    public void setFromAffilieFin(String fromAffilieFin) {
        this.fromAffilieFin = fromAffilieFin;
    }

    public void setFromDebutPeriode(java.lang.String fromDebutPeriode) {
        this.fromDebutPeriode = fromDebutPeriode;
    }

    /**
     * Sets the genreAffilie.
     * 
     * @param genreAffilie
     *            The genreAffilie to set
     */
    public void setGenreAffilie(java.lang.String genreAffilie) {
        this.genreAffilie = genreAffilie;
    }

    /**
     * Sets the idPassage.
     * 
     * @param idPassage
     *            The idPassage to set
     */
    public void setIdPassage(java.lang.String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setToFinPeriode(java.lang.String toFinPeriode) {
        this.toFinPeriode = toFinPeriode;
    }

    /**
     * Sets the typeDecision.
     * 
     * @param typeDecision
     *            The typeDecision to set
     */
    public void setTypeDecision(java.lang.String typeDecision) {
        this.typeDecision = typeDecision;
    }

}
