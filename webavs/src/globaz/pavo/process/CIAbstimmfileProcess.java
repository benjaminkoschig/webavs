package globaz.pavo.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

public class CIAbstimmfileProcess extends BProcess {

    private static final long serialVersionUID = -2589061912864685429L;
    private Boolean allAffilie = true;
    private String anneeDebut = "";
    private String anneeFin = "";
    private String Email = "";
    private String montant = "";
    private CIAbstimmfileDocument.Operator montantOperator = CIAbstimmfileDocument.Operator.egal;
    private String numeorTo = "";
    private String numeroFrom = "";

    /**
     * Constructor for CIAbstimmfileProcess.
     */
    public CIAbstimmfileProcess() {
        super();
    }

    /**
     * Constructor for CIAbstimmfileProcess.
     * 
     * @param parent
     */
    public CIAbstimmfileProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Constructor for CIAbstimmfileProcess.
     * 
     * @param session
     */
    public CIAbstimmfileProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        CIAbstimmfileDocument excelDoc = new CIAbstimmfileDocument("AbstimFile", anneeDebut, anneeFin, getSession());
        // excelDoc.setSession(getSession());
        excelDoc.setProcess(this);
        excelDoc.setAllAffilie(getAllAffilie());
        excelDoc.setMontant(getMontant());
        excelDoc.setMontantOperator(getMontantOperator());
        excelDoc.setNumeroFrom(getNumeroFrom());
        excelDoc.setNumeroTo(getNumeorTo());
        excelDoc.populateSheet(getSession());

        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setDocumentType("0212CCI");
        docInfo.setDocumentTypeNumber("");
        this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
        return true;

    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (JadeStringUtil.isBlank(getEMailAddress()) || JadeStringUtil.isBlank(anneeDebut)
                || JadeStringUtil.isBlank(anneeFin)) {
            this._addError(getTransaction(), "Données non valides");
            abort();
            return;
        }
    }

    /**
     * @return the allAffilie
     */
    public Boolean getAllAffilie() {
        return allAffilie;
    }

    /**
     * Returns the anneeDebut.
     * 
     * @return String
     */
    public String getAnneeDebut() {
        return anneeDebut;
    }

    /**
     * Returns the anneeFin.
     * 
     * @return String
     */
    public String getAnneeFin() {
        return anneeFin;
    }

    /**
     * @return
     */
    public String getEmail() {
        return Email;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return "L'importation d'Abtimmfile a échoué";
        } else {
            return "L'importation d'Abtimmfile s'est effectuée avec succès.";
        }
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the montantOperator
     */
    public CIAbstimmfileDocument.Operator getMontantOperator() {
        return montantOperator;
    }

    /**
     * @return the numeorTo
     */
    public String getNumeorTo() {
        return numeorTo;
    }

    /**
     * @return the numeroFrom
     */
    public String getNumeroFrom() {
        return numeroFrom;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param allAffilie
     *            the allAffilie to set
     */
    public void setAllAffilie(Boolean allAffilie) {
        this.allAffilie = allAffilie;
    }

    /**
     * Sets the anneeDebut.
     * 
     * @param anneeDebut
     *            The anneeDebut to set
     */
    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    /**
     * Sets the anneeFin.
     * 
     * @param anneeFin
     *            The anneeFin to set
     */
    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    /**
     * @param string
     */
    public void setEmail(String string) {
        Email = string;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param montantOperator
     *            the montantOperator to set
     */
    public void setMontantOperator(CIAbstimmfileDocument.Operator montantOperator) {
        this.montantOperator = montantOperator;
    }

    /**
     * @param numeorTo
     *            the numeorTo to set
     */
    public void setNumeorTo(String numeorTo) {
        this.numeorTo = numeorTo;
    }

    /**
     * @param numeroFrom
     *            the numeroFrom to set
     */
    public void setNumeroFrom(String numeroFrom) {
        this.numeroFrom = numeroFrom;
    }

}
