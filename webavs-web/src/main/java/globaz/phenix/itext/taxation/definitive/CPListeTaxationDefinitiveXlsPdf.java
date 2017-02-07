package globaz.phenix.itext.taxation.definitive;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.util.CPProperties;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.mail.UsersMail;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import com.google.common.base.Joiner;

public class CPListeTaxationDefinitiveXlsPdf extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String NUM_REF_INFOROM_LISTE_TAXA_DEF = "0157CFA";

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

    private JadePublishDocumentInfo createDocInfo() {
        JadePublishDocumentInfo documentInfo = createDocumentInfo();
        documentInfo.setPublishDocument(true);
        documentInfo.setDocumentTypeNumber(NUM_REF_INFOROM_LISTE_TAXA_DEF);
        return documentInfo;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        LoadTaxationsDefinitives loader = new LoadTaxationsDefinitives(getSession());
        List<TaxationDefinitiveForList> listOutput = loader.load(criteria);

        JadePublishDocumentInfo documentInfoPdf = createDocInfo();
        JadePublishDocumentInfo documentInfoXls = createDocInfo();

        String nomCaise = FWIImportProperties.getInstance().getProperty(documentInfoPdf,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase());

        Details details = new Details();
        details.add(nomCaise, "");
        details.add("N°", NUM_REF_INFOROM_LISTE_TAXA_DEF);
        details.newLigne();

        SimpleOutputListBuilderJade builder = SimpleOutputListBuilderJade.newInstance();
        builder.session(getSession()).globazTheme().addTranslater(getSession())
                .outputNameAndAddPath(NUM_REF_INFOROM_LISTE_TAXA_DEF + "_PRESTATIONS").addList(listOutput)
                .classElementList(TaxationDefinitiveForList.class).addHeaderDetails(details)
                .addTitle("Liste des taxations définitives APG/Maternité", Align.CENTER);
        File filePdf = builder.asPdf().build();
        File fileXls = builder.asXls().build();
        builder.close();

        String groupMail = CPProperties.LISTE_TAXATION_DEFINITIVE_GROUP_MAIL.getValue();
        if (groupMail != null) {
            groupMail = groupMail.trim();
            if (!groupMail.isEmpty()) {
                List<String> mailList = UsersMail
                        .resolveMailsByGroupId(CPProperties.LISTE_TAXATION_DEFINITIVE_GROUP_MAIL.getValue());
                Set<String> mails = new HashSet<String>();
                mails.addAll(mailList);
                mails.add(getEMailAddress());
                documentInfoPdf.setPublishProperty(JadePublishDocumentInfo.MAILS_TO, Joiner.on(";").join(mails));
                documentInfoXls.setPublishProperty(JadePublishDocumentInfo.MAILS_TO, Joiner.on(";").join(mails));
            }
        }

        this.registerAttachedDocument(documentInfoPdf, filePdf.getAbsolutePath());
        this.registerAttachedDocument(documentInfoXls, fileXls.getAbsolutePath());
        return true;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_TAXATION_DEFINITIVE");
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }
}
