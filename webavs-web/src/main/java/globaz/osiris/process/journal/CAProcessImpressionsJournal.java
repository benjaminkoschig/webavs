/*
 * Créé le 7 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.process.journal;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAPaiementBVR;
import globaz.osiris.print.CAXmlmlListJournalSuspens;
import globaz.osiris.print.itext.list.CAIListJournalEcritures_Doc;
import globaz.osiris.print.itext.list.CAIListPaiementsEtrangers_Doc;
import globaz.osiris.print.list.CAListOperationsSuspens;
import globaz.osiris.utils.CAOsirisContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * @author jts 7 avr. 05 10:46:52
 */
public class CAProcessImpressionsJournal extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String fromIdExterneRole = "";
    private String idJournal = new String();
    private String liste = new String();
    private int showDetail = 0;
    private String toIdExterneRole = "";
    private String typeImpression = "pdf";
    private CAXmlmlListJournalSuspens xmlml = null;

    public CAProcessImpressionsJournal() {
        super();
    }

    public CAProcessImpressionsJournal(BProcess parent) {
        super(parent);
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
    protected boolean _executeProcess() {

        try {

            if (getListe().equals("ecritures")) {
                if (!isOnError()) {
                    setState(getSession().getLabel("6110"));
                    super.setSendCompletionMail(true);
                    CAIListJournalEcritures_Doc report = new CAIListJournalEcritures_Doc(this);
                    report.setShowDetail(getShowDetail());
                    report.setIdJournal(getIdJournal());
                    report.setFromIdExterneRole(getFromIdExterneRole());
                    report.setToIdExterneRole(getToIdExterneRole());
                    report.executeProcess();
                }
            } else if (getListe().equals("paiementEtranger")) {
                if (!isOnError()) {
                    setState(getSession().getLabel("6115"));
                    super.setSendCompletionMail(true);
                    CAIListPaiementsEtrangers_Doc report = new CAIListPaiementsEtrangers_Doc(this);
                    report.setShowDetail(getShowDetail());
                    report.setIdJournal(getIdJournal());
                    report.executeProcess();
                }
            } else if (getListe().equals("suspens")) {
                createDocumentListOperationSuspens();
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;

        }
        return !isOnError();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    private void createDataForExcel() throws Exception {

        FWCurrency sumMontantTotal = new FWCurrency();
        xmlml = new CAXmlmlListJournalSuspens();

        CAOperationManager manager = new CAOperationManager();
        manager.setSession(getSession());
        manager.setForIdJournal(getIdJournal());

        ArrayList<String> etat = new ArrayList<String>();
        etat.add(APIOperation.ETAT_ERREUR);
        etat.add(APIOperation.ETAT_ERREUR_VERSEMENT);
        manager.setForEtatIn(etat);

        manager.setVueOperationCpteAnnexe("true");
        manager.setApercuJournal("false");
        manager.setForSelectionTri("1000");

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        Object[] obj_listOpe = manager.getContainer().toArray();

        // Pour chaque ligne de la requete
        for (Object obj_ope : obj_listOpe) {
            CAOperation operation = (CAOperation) obj_ope;

            String descCompteAnnexe = "";
            String nom = "";
            if (operation.getCompteAnnexe() != null) {
                descCompteAnnexe = operation.getCompteAnnexe().getIdExterneRole();
                nom = operation.getCompteAnnexe().getTiers().getNom();
            }

            String idExterneSection = "";
            if (operation.getSection() != null) {
                idExterneSection = operation.getSection().getIdExterne();
            }

            String montant = operation.getMontant();
            sumMontantTotal.add(operation.getMontant());

            String refBvr = "";
            if (operation.getIdTypeOperation().equals(APIOperation.CAPAIEMENTBVR)) {
                CAPaiementBVR oper = (CAPaiementBVR) operation.getOperationFromType(getTransaction());
                refBvr = oper.getReferenceBVR();
            }

            String messages = "";
            if (operation.getLog() != null) {
                Enumeration<FWMessage> e = operation.getLog().getMessagesToEnumeration();
                while (e.hasMoreElements()) {
                    FWMessage msg = e.nextElement();
                    messages += msg.getMessageText() + "\r\n";
                }
            }

            xmlml.createLigne(nom, descCompteAnnexe, idExterneSection, montant, refBvr, messages);
        }

        xmlml.setSumMontant(sumMontantTotal.toString());
        xmlml.setNumeroJournal(getIdJournal());
    }

    private void createDocumentListOperationSuspens() throws Exception {
        setState(getSession().getLabel("LIST_OPERATIONS_SUSPENS"));
        setSendCompletionMail(true);

        if ("pdf".equals(getTypeImpression())) {
            createPdf();
        } else {
            // Création des données
            createDataForExcel();

            // Récupération des labels
            retrieveLabels();

            // Création du fichier excel
            createListExcel();
        }

    }

    private void createListExcel() throws Exception {
        CAOsirisContainer container = xmlml.loadResults();

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + CAApplication.DEFAULT_OSIRIS_ROOT
                + "/model/excelml/" + CAXmlmlListJournalSuspens.XLS_DOC_NAME + "Modele.xml";

        String xlsDocPath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CAXmlmlListJournalSuspens.XLS_DOC_NAME + ".xml");

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CAApplication.DEFAULT_APPLICATION_OSIRIS);
        docInfoExcel.setDocumentTitle(CAXmlmlListJournalSuspens.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CAXmlmlListJournalSuspens.NUMERO_INFOROM);

        xmlml.setCompanyName(FWIImportProperties.getInstance().getProperty(docInfoExcel,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

        xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, container);

        this.registerAttachedDocument(docInfoExcel, xlsDocPath);
    }

    private void createPdf() throws Exception {

        CAListOperationsSuspens report = new CAListOperationsSuspens(getSession());
        report.setForIdJournal(getIdJournal());
        report.setEMailAddress(getEMailAddress());
        report.executeProcess();

        if (!JadeStringUtil.isBlank(report.getFileName())) {
            JadePublishDocumentInfo info = createDocumentInfo();
            // Envoie un e-mail avec les pdf fusionnés
            info.setPublishDocument(true);
            info.setArchiveDocument(false);
            info.setDocumentTypeNumber(CAListOperationsSuspens.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(info, report.getFileName());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (getListe().equals("paiementEtranger")) {
            if (getMemoryLog().hasErrors()) {
                return getSession().getLabel("5510");
            } else {
                return getSession().getLabel("5509");
            }
        } else if (getListe().equals("suspens")) {
            return getSession().getLabel("LIST_OPERATIONS_SUSPENS");
        } else {
            if (getMemoryLog().hasErrors()) {
                return getSession().getLabel("5506");
            } else {
                return getSession().getLabel("5505");
            }
        }
    }

    /**
     * @return the fromIdExterneRole
     */
    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    /**
     * @return
     */
    public String getIdJournal() {
        return idJournal;
    }

    public CAJournal getJournal() {
        CAJournal journal = null;

        // Si pas déjà chargé
        if (journal == null) {
            try {
                journal = new CAJournal();
                journal.setSession(getSession());
                journal.setIdJournal(getIdJournal());
                journal.retrieve(getTransaction());
                if (journal.isNew()) {
                    JadeLogger.fatal(this, "5157 : " + getIdJournal());
                    journal = null;
                }
            } catch (Exception e) {
                JadeLogger.fatal(this, e);
                journal = null;
            }
        }
        return journal;
    }

    /**
     * @return
     */
    public String getListe() {
        return liste;
    }

    /**
     * @return
     */
    public int getShowDetail() {
        return showDetail;
    }

    /**
     * @return the toIdExterneRole
     */
    public String getToIdExterneRole() {
        return toIdExterneRole;
    }

    public String getTypeImpression() {
        return typeImpression;
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

    private void retrieveLabels() {
        xmlml.setTitle(getSession().getLabel("LIST_OPERATIONS_SUSPENS"));
        xmlml.setLabelDescCompteAnnexe(getSession().getLabel("COMPTEANNEXE"));
        xmlml.setLabelNom(getSession().getLabel("NOM"));
        xmlml.setLabelIdExterneSection(getSession().getLabel("SECTION"));
        xmlml.setLabelMontant(getSession().getLabel("MONTANT"));
        xmlml.setLabelNumeroJournal(getSession().getLabel("NOJOURNAL"));
        xmlml.setLabelRefBvr(getSession().getLabel("LIST_OPERATIONS_SUSPENS_REFERENCE"));
        xmlml.setLabelMessage(getSession().getLabel("LIST_OPERATIONS_SUSPENS_ERREURS"));
        xmlml.setLabelSumMontant(getSession().getLabel("TOTAL"));
        xmlml.setLabelDateDoc(getSession().getLabel("DATE"));
    }

    /**
     * @param fromIdExterneRole
     *            the fromIdExterneRole to set
     */
    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    /**
     * @param string
     */
    public void setIdJournal(String string) {
        idJournal = string;
    }

    /**
     * @param string
     */
    public void setListe(String string) {
        liste = string;
    }

    /**
     * @param i
     */
    public void setShowDetail(int i) {
        showDetail = i;
    }

    /**
     * @param toIdExterneRole
     *            the toIdExterneRole to set
     */
    public void setToIdExterneRole(String toIdExterneRole) {
        this.toIdExterneRole = toIdExterneRole;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
