package globaz.naos.db.rentier;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 08:34:14)
 * 
 * @author: ado
 */
public class AFExport extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean changementPeriodicite = false;

    protected String dateDebut = "";
    protected String dateFin = "";

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     */
    public AFExport() {
        super();
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public AFExport(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public AFExport(globaz.globall.db.BSession session) {
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

            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(getSession());
            manager.setForDateFinGreater(dateFin);
            manager.setForTypeFacturation("PERSONNEL");
            manager.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_NON_ACTIF });
            manager.forIsTraitement(false);
            manager.find(BManager.SIZE_NOLIMIT);
            RentierDocument excelDoc = new RentierDocument("Rentier " + dateDebut + "-" + dateFin);
            excelDoc.populateSheet(manager, dateDebut, dateFin, getSession(), getMemoryLog(), getTransaction(),
                    changementPeriodicite);
            if (!getTransaction().hasErrors()) {
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setDocumentType("0304CAF");
                docInfo.setDocumentTypeNumber("");
                this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return false;
        }
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    // *******************************************************
    // Getter
    // *******************************************************
    public boolean getChangementPeriodicite() {
        return changementPeriodicite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        String obj = "";
        if (getSession().hasErrors()) {
            obj = getSession().getLabel("RADIER_RENTIERS_ERREUR");
        } else {
            obj = getSession().getLabel("RADIER_RENTIERS_OK");
        }
        return obj;
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process.
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    // *******************************************************
    // Setter
    // *******************************************************
    public void setChangementPeriodicite(boolean changementPeriodicite) {
        this.changementPeriodicite = changementPeriodicite;
    }

}
