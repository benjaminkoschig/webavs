package globaz.osiris.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAPaiementBVR;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * @author dda
 */
public class CAListOperationsSuspens extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final int MAX_LENGTH_NOM_COMPTEANNEXE = 30;

    public static final String NUMERO_REFERENCE_INFOROM = "0188GCA";

    private String forIdJournal;

    private boolean printFirstPageInfos = true;

    private FWCurrency sumMontantTotal = new FWCurrency();

    public CAListOperationsSuspens() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS), "CA", "GLOBAZ", "Liste des suspens du journal",
                new CAOperationManager(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    public CAListOperationsSuspens(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "CA", "GLOBAZ", "Liste des suspens du journal", new CAOperationManager(),
                CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     * @see globaz.framework.printing.FWAbstractDocumentList#_beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        CAOperationManager manager = (CAOperationManager) _getManager();
        manager.setSession(getSession());

        manager.setForIdJournal(getForIdJournal());

        ArrayList etat = new ArrayList();
        etat.add(APIOperation.ETAT_ERREUR);
        etat.add(APIOperation.ETAT_ERREUR_VERSEMENT);
        manager.setForEtatIn(etat);

        manager.setVueOperationCpteAnnexe("true");
        manager.setApercuJournal("false");
        manager.setForSelectionTri("1000");

        _setDocumentTitle(getSession().getLabel("LIST_OPERATIONS_SUSPENS"));
        getDocumentInfo().setTemplateName("");
        getDocumentInfo().setDocumentTypeNumber(CAListOperationsSuspens.NUMERO_REFERENCE_INFOROM);
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

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

    /**
     * Ajoute les informations de header sur la première page.
     * 
     * @throws FWIException
     */
    private void addFirstPageInfos() throws FWIException {
        if (!_getReport().isOpen()) {
            _getReport().open();
        }

        this._addLine(getFontCell(), getSession().getLabel("NOJOURNAL") + " : " + getForIdJournal(), null, null, null,
                null);

        this._addLine(getFontCell(), "", null, null, null, null);
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#addRow(globaz.globall.db.BEntity)
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        if (printFirstPageInfos) {
            addFirstPageInfos();
            printFirstPageInfos = false;
        }

        CAOperation operation = (CAOperation) entity;

        if (operation.getCompteAnnexe() != null) {
            _addCell(operation.getCompteAnnexe().getCARole().getDescription() + " "
                    + operation.getCompteAnnexe().getIdExterneRole());

            if (!JadeStringUtil.isBlank(operation.getCompteAnnexe().getTiers().getNom())
                    && (operation.getCompteAnnexe().getTiers().getNom().length() > CAListOperationsSuspens.MAX_LENGTH_NOM_COMPTEANNEXE)) {
                _addCell(operation.getCompteAnnexe().getTiers().getNom()
                        .substring(0, CAListOperationsSuspens.MAX_LENGTH_NOM_COMPTEANNEXE)
                        + "...");
            } else {
                _addCell(operation.getCompteAnnexe().getTiers().getNom());
            }
        } else {
            _addCell("");
            _addCell("");
        }

        if (operation.getSection() != null) {
            _addCell(operation.getSection().getIdExterne());
        } else {
            _addCell("");
        }

        _addCell(JANumberFormatter.format(operation.getMontant(), 0, 2, JANumberFormatter.NEAR));
        sumMontantTotal.add(operation.getMontant());

        if (operation.getIdTypeOperation().equals(APIOperation.CAPAIEMENTBVR)) {
            CAPaiementBVR oper = (CAPaiementBVR) operation.getOperationFromType(getTransaction());
            _addCell(oper.getReferenceBVR());
        } else {
            _addCell("");
        }

        String messages = "";
        if (operation.getLog() != null) {
            Enumeration e = operation.getLog().getMessagesToEnumeration();
            while (e.hasMoreElements()) {
                FWMessage msg = (FWMessage) e.nextElement();
                messages += msg.getMessageText() + "\r\n";
            }
        }

        _addCell(messages);
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#initializeTable()
     */
    @Override
    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel("COMPTEANNEXE"), 10);
        this._addColumnLeft(getSession().getLabel("NOM"), 12);
        this._addColumnLeft(getSession().getLabel("SECTION"), 6);
        this._addColumnRight(getSession().getLabel("MONTANT"), 6);
        this._addColumnLeft(getSession().getLabel("LIST_OPERATIONS_SUSPENS_REFERENCE"), 15);
        this._addColumnLeft(getSession().getLabel("LIST_OPERATIONS_SUSPENS_ERREURS"), 25);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#summary()
     */
    @Override
    protected void summary() throws FWIException {
        _addCell("");
        _addCell("");
        _addCell(getSession().getLabel("TOTAL"));
        _addCell(JANumberFormatter.format(sumMontantTotal.toString(), 0, 2, JANumberFormatter.NEAR));
    }

}
