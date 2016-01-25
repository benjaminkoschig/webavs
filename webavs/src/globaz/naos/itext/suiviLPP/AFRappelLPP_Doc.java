package globaz.naos.itext.suiviLPP;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AFRappelLPP_Doc extends AFAbstractTiersDocument {

    private static final long serialVersionUID = 3991411796114140045L;

    public AFRappelLPP_Doc() throws Exception {
        super();
    }

    public AFRappelLPP_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFRappelLPP_Param.L_NOMDOCRAPPEL));
    }

    @Override
    public void beforeBuildReport() {
        ICaisseReportHelper caisseReportHelper;
        try {
            caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), super.getIsoLangueDestinataire());
            CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
            signBean.setService2("");
            signBean.setSignataire2("");
            signBean.setService(getSession().getLabel("MSG_SERVICE_NOM"));
            signBean.setSignataire(getSession().getUserFullName());
            caisseReportHelper.addSignatureParameters(this, signBean);
            super.beforeBuildReport(caisseReportHelper);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    public void createDataSource() throws Exception {
        super.createDataSource();
        fillDocInfo();
        List documents = new ArrayList();
        Map nomDoc = new HashMap();
        nomDoc.put(AFRappelLPP_Param.F_NOMDOCATTACHE,
                getSession().getApplication().getLabel("NAOS_RAPPEL_LPP_DOCNAME1", super.getIsoLangueDestinataire()));
        documents.add(nomDoc);
        nomDoc = new HashMap();
        nomDoc.put(AFRappelLPP_Param.F_NOMDOCATTACHE,
                getSession().getApplication().getLabel("NAOS_RAPPEL_LPP_DOCNAME2", super.getIsoLangueDestinataire()));
        documents.add(nomDoc);
        this.setDataSource(documents);
    }

    @Override
    protected void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber("0109CAF");
        super.fillDocInfo();
    }

    @Override
    public String getCategorie() {
        return CodeSystem.TYPE_CAT_RAPPEL_LPP;
    }

    @Override
    public String getDomaine() {
        return CodeSystem.DOMAINE_CAT_LPP;
    }

    @Override
    public int getNbLevel() {
        return AFRappelLPP_Param.NB_LEVEL;
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFRappelLPP_Param.L_NOMDOCRAPPEL);
    }

    @Override
    protected String getTemplate() {
        return AFRappelLPP_Param.TEMPLATE_RAPPEL_LPP;
    }

    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        switch (i) {
            case 1:
                this.setParametres(AFRappelLPP_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFRappelLPP_Param.P_CORPS,
                        format(value, getParams(getIdEnvoiParent(), getSession())));
                break;
            case 3:
                this.setParametres(AFRappelLPP_Param.P_DOCUMENT, value);
                break;
            case 4:
                this.setParametres(AFRappelLPP_Param.P_CORPS2,
                        format(value, getParams(getIdEnvoiParent(), getSession())));
                break;
        }
    }
}
