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

    private static final long serialVersionUID = 1L;

    public static final String NUM_REF_INFOROM_LISTE_TAXA_DEF = "0157CFA";

    private ListTaxationsDefinitivesCriteria criteria = new ListTaxationsDefinitivesCriteria();

    private String noPassage;
    private boolean fromFacturation = false;

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }

        boolean isAllEmpty = JadeStringUtil.isEmpty(criteria.getAnneeTaxationCP());
        isAllEmpty &= JadeStringUtil.isEmpty(criteria.getDateDebutDecisionsCP());
        isAllEmpty &= JadeStringUtil.isEmpty(criteria.getDateFinDecisionsCP());
        isAllEmpty &= JadeStringUtil.isEmpty(criteria.getAnneeDroit());
        isAllEmpty &= JadeStringUtil.isEmpty(criteria.getDateDebutDecompte());
        isAllEmpty &= JadeStringUtil.isEmpty(criteria.getDateFinDecompte());

        if (isAllEmpty) {
            this._addError(getSession().getLabel("JSP_LISTE_TAXATIONS_DEFINITIVES_ERREUR_CHAMPS_VIDE"));
        }

        if (JadeStringUtil.isEmpty(criteria.getNoPassage()) && fromFacturation) {
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

        if (criteria == null) {
            return false;
        }

        LoadTaxationsDefinitives loader = new LoadTaxationsDefinitives(getSession());
        List<TaxationDefinitiveForList> listOutput = loader.load(criteria);

        JadePublishDocumentInfo documentInfoPdf = createDocInfo();
        JadePublishDocumentInfo documentInfoXls = createDocInfo();

        String nomCaise = FWIImportProperties.getInstance().getProperty(documentInfoPdf,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase());

        Details details = new Details();
        details.add(nomCaise, "");
        details.add("N�", NUM_REF_INFOROM_LISTE_TAXA_DEF);
        details.newLigne();

        SimpleOutputListBuilderJade builder = SimpleOutputListBuilderJade.newInstance();
        builder.session(getSession()).globazTheme().addTranslater(getSession())
                .outputNameAndAddPath(NUM_REF_INFOROM_LISTE_TAXA_DEF + "_PRESTATIONS").addList(listOutput)
                .classElementList(TaxationDefinitiveForList.class).addHeaderDetails(details)
                .addTitle("Liste des taxations d�finitives APG/Maternit�", Align.CENTER);
        File filePdf = builder.asPdf().build();
        File fileXls = builder.asXls().build();
        builder.close();

        String groupMail = CPProperties.LISTE_TAXATION_DEFINITIVE_GROUP_MAIL.getValue();
        if (groupMail != null) {
            groupMail = groupMail.trim();
            Set<String> mails = new HashSet<String>();

            if (!groupMail.isEmpty() && fromFacturation) {
                List<String> mailList = UsersMail
                        .resolveMailsByGroupId(CPProperties.LISTE_TAXATION_DEFINITIVE_GROUP_MAIL.getValue());
                mails.addAll(mailList);
            }

            mails.add(getEMailAddress());

            documentInfoPdf.setPublishProperty(JadePublishDocumentInfo.MAILS_TO, Joiner.on(";").join(mails));
            documentInfoXls.setPublishProperty(JadePublishDocumentInfo.MAILS_TO, Joiner.on(";").join(mails));
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

    public void setCriteria(ListTaxationsDefinitivesCriteria criteria) {
        this.criteria = criteria;
    }

    public ListTaxationsDefinitivesCriteria getCriteria() {
        return criteria;
    }

    public void setNoPassage(String noPassage) {
        this.noPassage = noPassage;
        criteria.setNoPassage(noPassage);
    }

    public void setFromFacturation(boolean fromFacturation) {
        this.fromFacturation = fromFacturation;
    }

    public boolean isFromFacturation() {
        return fromFacturation;
    }

    public String getNoPassage() {
        return noPassage;
    }
}
