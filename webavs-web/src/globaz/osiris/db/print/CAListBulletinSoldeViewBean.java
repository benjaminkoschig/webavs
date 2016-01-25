package globaz.osiris.db.print;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.application.FAApplication;
import globaz.osiris.db.bulletinSolde.CABulletinSolde;
import globaz.osiris.db.bulletinSolde.CABulletinSoldeManager;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;

public class CAListBulletinSoldeViewBean extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String LIST_BULLETIN_DE_SOLDE = "listBulletinDeSolde";

    private String documentDate = null;
    private String forSelectionRole = null;
    private String forSelectionTriCompteAnnexe = null;
    private String fromDate = null;
    private String fromNoCompteAnnexe = null;
    FWIImportManager importManager = null;
    private String untilDate = null;

    private String untilNoCompteAnnexe = null;

    public CAListBulletinSoldeViewBean() {
        // TODO Auto-generated constructor stub
    }

    public CAListBulletinSoldeViewBean(BProcess parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    public CAListBulletinSoldeViewBean(BSession session) {
        super(session);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            // Create datasource
            buildDocument();

            return fusionneDocuments();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }
    }

    protected void buildDocument() {
        CABulletinSoldeManager manager = new CABulletinSoldeManager();
        try {
            manager.setSession(getSession());
            manager.setForSelectionRole(getForSelectionRole());
            manager.setFromNoCompteAnnexe(getFromNoCompteAnnexe());
            manager.setUntilNoCompteAnnexe(getUntilNoCompteAnnexe());
            manager.setForSelectionTriCompteAnnexe(getForSelectionTriCompteAnnexe());
            manager.setFromDate(getFromDate());
            manager.setUntilDate(getUntilDate());
            manager.find();

            BSession sessionMusca = new BSession("MUSCA");
            getSession().connectSession(sessionMusca);
            String montantMinimeNeg = sessionMusca.getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
            String montantMinimePos = sessionMusca.getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);

            setProgressScaleValue(manager.size());

            if (!manager.isEmpty()) {
                importManager = new FWIImportManager();

                for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
                    CABulletinSolde bulletin = (CABulletinSolde) manager.getEntity(i);
                    CAImpressionBulletinsSoldes_Doc doc = new CAImpressionBulletinsSoldes_Doc();
                    doc.setParent(this);
                    doc.setIdSection(bulletin.getIdSection());
                    doc.setMontantMinimePos(montantMinimePos);
                    doc.setMontantMinimeNeg(montantMinimeNeg);
                    doc.setNumeroReferenceInforom(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE);
                    doc.setSession(getSession());
                    doc.executeProcess();

                    incProgressCounter();
                }
            }
        } catch (FWIException e) {
            getMemoryLog().logMessage(null, e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.fatal(this, e);
        } catch (Exception e2) {
            getMemoryLog().logMessage(null, e2.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.fatal(this, e2);
        }
    }

    /**
     * @return
     * @throws Exception
     */
    private boolean fusionneDocuments() throws Exception {
        if (isAborted()) {
            return false;
        }

        JadePublishDocumentInfo info = createDocumentInfo();
        // Envoie un e-mail avec les pdf fusionnés
        info.setPublishDocument(true);
        info.setArchiveDocument(false);
        info.setDocumentTypeNumber(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE);

        this.mergePDF(info, true, 500, false, null);

        return true;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_BULLETIN_SOLDE_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_BULLETIN_SOLDE_EMAIL_OK");
        }
    }

    /**
     * Return la date actuelle. Date default de la fonction "Impression de la liste des extraits des comptes annexe".
     * 
     * @return
     */
    public String getFormatedDateToday() {
        return JACalendar.todayJJsMMsAAAA();
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public String getForSelectionTriCompteAnnexe() {
        return forSelectionTriCompteAnnexe;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getFromNoCompteAnnexe() {
        return fromNoCompteAnnexe;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public String getUntilNoCompteAnnexe() {
        return untilNoCompteAnnexe;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    public void setForSelectionTriCompteAnnexe(String forSelectionTriCompteAnnexe) {
        this.forSelectionTriCompteAnnexe = forSelectionTriCompteAnnexe;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setFromNoCompteAnnexe(String fromNoCompteAnnexe) {
        this.fromNoCompteAnnexe = fromNoCompteAnnexe;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

    public void setUntilNoCompteAnnexe(String untilNoCompteAnnexe) {
        this.untilNoCompteAnnexe = untilNoCompteAnnexe;
    }

}
