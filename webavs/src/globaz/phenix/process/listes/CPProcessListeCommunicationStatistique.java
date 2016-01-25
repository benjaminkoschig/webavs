package globaz.phenix.process.listes;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.listes.excel.CPListeCommunicationStatistique;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public abstract class CPProcessListeCommunicationStatistique extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String fromAnnee = "";
    private String fromNumAffilie = "";
    private java.lang.String genreAffilie = "";
    private java.lang.String toAnnee = "";
    private String toNumAffilie = "";

    /**
     * Constructor for CPProcessImprimerListDecision.
     */
    public CPProcessListeCommunicationStatistique() {
        super();
    }

    /**
     * Constructor for CPProcessImprimerListDecision.
     */
    public CPProcessListeCommunicationStatistique(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // Création du document
            CPListeCommunicationStatistique excelDoc = new CPListeCommunicationStatistique(getSession(),
                    getFromNumAffilie(), getToNumAffilie(), getFromAnnee(), getToAnnee(), getGenreAffilie());
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheet(getTransaction());
            if (!getTransaction().hasErrors()) {
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setDocumentType("0268CCP");
                docInfo.setDocumentTypeNumber("");
                this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
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

    @Override
    protected String getEMailObject() {
        if ((isAborted() || getSession().hasErrors())) {
            return getSession().getLabel("SUJET_EMAIL_LISTE_STATCOMFIS_PASOK");
        } else {
            return getSession().getLabel("SUJET_EMAIL_LISTE_STATCOMFIS_OK");
        }
    }

    /**
     * Returns the fromAnnee.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * Returns the fromNumAffilie.
     * 
     * @return String
     */
    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * Returns the genreAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    public java.lang.String getToAnnee() {
        return toAnnee;
    }

    /**
     * Returns the toNumAffilie.
     * 
     * @return String
     */
    public String getToNumAffilie() {
        return toNumAffilie;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Sets the fromAnnee.
     * 
     * @param fromAnnee
     *            The fromAnnee to set
     */
    public void setFromAnnee(java.lang.String anneeDecision) {
        fromAnnee = anneeDecision;
    }

    /**
     * Sets the fromNumAffilie.
     * 
     * @param fromNumAffilie
     *            The fromNumAffilie to set
     */
    public void setFromNumAffilie(String fromAffilieDebut) {
        fromNumAffilie = fromAffilieDebut;
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

    public void setToAnnee(java.lang.String toFinPeriode) {
        toAnnee = toFinPeriode;
    }

    /**
     * Sets the toNumAffilie.
     * 
     * @param toNumAffilie
     *            The toNumAffilie to set
     */
    public void setToNumAffilie(String fromAffilieFin) {
        toNumAffilie = fromAffilieFin;
    }

}
