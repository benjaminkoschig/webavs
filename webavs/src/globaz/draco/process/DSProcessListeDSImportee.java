package globaz.draco.process;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.print.list.DSListeDSImportee;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 08:34:14)
 * 
 * @author: ado
 */
public class DSProcessListeDSImportee extends BProcess {

    private static final long serialVersionUID = -3774897347416015992L;
    private String anneeDeclaration;
    private String periodeReferenceDebut;
    private String periodeReferenceFin;
    private String provenance;

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     */
    public DSProcessListeDSImportee() {
        super();
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param parent
     *            BProcess
     */
    public DSProcessListeDSImportee(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public DSProcessListeDSImportee(BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            DSDeclarationListViewBean manager = new DSDeclarationListViewBean();
            manager.setSession(getSession());
            manager.setForAnnee(getAnneeDeclaration());
            manager.setForDateRetourEffLowerOrEquals(getPeriodeReferenceFin());
            manager.setForDateRetourEffGreaterOrEquals(getPeriodeReferenceDebut());
            manager.setForProvenance(getProvenance());
            manager.setForSelectionTri("3"); // Affilié, année
            // Création du document
            DSListeDSImportee excelDoc = new DSListeDSImportee(getSession(), manager);
            excelDoc.setSession(getSession());
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheet(manager, getTransaction());
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0311CDS");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        // Contrôle du mail
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError(getSession().getLabel("MSG_EMAILADDRESS_VIDE"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("MSG_PROCESS_LISTEIMPORT_ECHEC");
        } else {
            return getSession().getLabel("MSG_PROCESS_LISTEIMPORT_SUCCES");
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public String getPeriodeReferenceDebut() {
        return periodeReferenceDebut;
    }

    public void setPeriodeReferenceDebut(String dateReference) {
        periodeReferenceDebut = dateReference;
    }

    public String getAnneeDeclaration() {
        return anneeDeclaration;
    }

    public void setAnneeDeclaration(String anneeDeclaration) {
        this.anneeDeclaration = anneeDeclaration;
    }

    public String getPeriodeReferenceFin() {
        return periodeReferenceFin;
    }

    public void setPeriodeReferenceFin(String periodeFin) {
        periodeReferenceFin = periodeFin;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }
}
