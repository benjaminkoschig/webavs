package globaz.naos.itext.suiviLAA;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;

public class AFRappelLAA_Doc extends AFAbstractTiersDocument {

    private static final long serialVersionUID = -922377260563165910L;

    public AFRappelLAA_Doc() throws Exception {
        super();
    }

    public AFRappelLAA_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFRappelLAA_Param.L_NOMDOCRAPPEL));
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
    }

    @Override
    protected void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber("0105CAF");
        super.fillDocInfo();
    }

    @Override
    public String getCategorie() {
        return CodeSystem.TYPE_CAT_RAPPEL_LAA;
    }

    @Override
    public String getDomaine() {
        return CodeSystem.DOMAINE_CAT_LAA;
    }

    @Override
    public int getNbLevel() {
        return AFRappelLAA_Param.NB_LEVEL;
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFRappelLAA_Param.L_NOMDOCRAPPEL);
    }

    @Override
    protected String getTemplate() {
        return AFRappelLAA_Param.TEMPLATE_RAPPEL_LAA;
    }

    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        String[] params = getParams(getIdEnvoiParent(), getSession());
        switch (i) {
            case 1:
                this.setParametres(AFRappelLAA_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFRappelLAA_Param.P_CORPS, format(value, params));
                break;
            case 3:
                this.setParametres(AFRappelLAA_Param.P_DOCUMENT, value);
                break;
            case 4:
                this.setParametres(AFRappelLAA_Param.P_CORPS2, format(value, params));
                break;
        }
    }
}
