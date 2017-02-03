package globaz.phenix.itext.taxation.definitive;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;

public class CPListeTaxationDefinitiveXlsPdf extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String NUM_REF_INFOROM_LISTE_TAXA_DEF = "0157CFA";

    private final transient SimpleOutputListBuilderJade builder = SimpleOutputListBuilderJade.newInstance();
    private ListTaxationsDefinitivesCriteria criteria = new ListTaxationsDefinitivesCriteria();

    public void setCriteria(ListTaxationsDefinitivesCriteria criteria) {
        this.criteria = criteria;
    }

    public void setNoPassage(String noPassage) {
        criteria.setNoPassage(noPassage);
    }

    public String getNoPassage() {
        return criteria.getNoPassage();
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }

        if (JadeStringUtil.isEmpty(criteria.getNoPassage())) {
            this._addError(getSession().getLabel("LISTE_TAX_DEF_NO_PASSAGE_EMPTY"));
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // setEMailAddress("integration@globaz.ch");
        LoadTaxationsDefinitives loader = new LoadTaxationsDefinitives(getSession());
        List<TaxationDefinitiveForList> listOutput = loader.load(criteria);

        JadePublishDocumentInfo documentInfo = createDocumentInfo();
        documentInfo.setPublishDocument(true);
        documentInfo.setDocumentTypeNumber(NUM_REF_INFOROM_LISTE_TAXA_DEF);

        String nomCaise = FWIImportProperties.getInstance().getProperty(documentInfo,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase());

        Details details = new Details();
        details.add(nomCaise, "");
        details.newLigne();

        builder.session(getSession()).globazTheme().addTranslater(getSession())
                .outputNameAndAddPath(NUM_REF_INFOROM_LISTE_TAXA_DEF + "_PRESTATIONS").addList(listOutput)
                .classElementList(TaxationDefinitiveForList.class).addHeaderDetails(details)
                .addTitle("Liste des taxations définitives APG/Maternité", Align.CENTER);

        File filePdf = builder.asPdf().build();
        File fileXls = builder.asXls().build();

        documentInfo.setOwnerCompany("integration@globaz.ch");
        // documentInfo.setPublishDocument(true);
        this.registerAttachedDocument(documentInfo, filePdf.getAbsolutePath());
        this.registerAttachedDocument(documentInfo, fileXls.getAbsolutePath());

        List<String> list = new ArrayList<String>();

        // setEMailAddress("dma@globaz.ch");
        // list.add(fileXls.getAbsolutePath());
        // list.add(fileXls.getAbsolutePath());

        // Joiner.on(",").join(parts)

        // /List<String> listAttachedDocumentLocation

        // List<String> mails = AFIDEUtil
        // .giveMeUserGroupMail(CPProperties.LISTE_TAXATION_DEFINITIVE_GROUP_MAIL.getValue());

        // JadeSmtpClient.getInstance().sendMail(mails.toArray(), getEMailObject(), getSubjectDetail(),
        // listAttachedDocumentLocation.toArray());

        builder.close();
        return true;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_TAXATION_DEFINITIVE");
    }

    @Override
    protected void _executeCleanUp() {
        builder.close();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }
}
