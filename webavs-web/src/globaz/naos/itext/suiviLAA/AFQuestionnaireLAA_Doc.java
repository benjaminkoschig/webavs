package globaz.naos.itext.suiviLAA;

import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;

public class AFQuestionnaireLAA_Doc extends AFAbstractTiersDocument {

    private static final long serialVersionUID = -5037820896878498897L;

    public AFQuestionnaireLAA_Doc() throws Exception {
        super();
    }

    public AFQuestionnaireLAA_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFQuestionnaireLAA_Param.L_NOMDOC));
    }

    @Override
    public void beforeBuildReport() {
        ICaisseReportHelper caisseReportHelper = null;
        try {
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
        getDocumentInfo().setDocumentTypeNumber("0104CAF");
        super.fillDocInfo();
    }

    @Override
    public String getCategorie() {
        return CodeSystem.TYPE_CAT_QUEST_LAA;
    }

    @Override
    public String getDomaine() {
        return CodeSystem.DOMAINE_CAT_LAA;
    }

    @Override
    public int getNbLevel() {
        return AFQuestionnaireLAA_Param.NB_LEVEL;
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFQuestionnaireLAA_Param.L_NOMDOC);
    }

    @Override
    protected String getTemplate() {
        return AFQuestionnaireLAA_Param.TEMPLATE_QUEST_LAA;
    }

    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        switch (i) {
            case 1:
                this.setParametres(AFQuestionnaireLAA_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFQuestionnaireLAA_Param.P_CORPS, format(value, getParams("", getSession())));
                break;
            case 3:
                this.setParametres(AFQuestionnaireLAA_Param.P_SIGN_LETTRE, value);
                break;
            case 4:
                this.setParametres(AFQuestionnaireLAA_Param.P_QUEST, value);
                break;
            case 5:
                this.setParametres(AFQuestionnaireLAA_Param.P_OUI, value);
                break;
            case 6:
                this.setParametres(AFQuestionnaireLAA_Param.P_NON, value);
                break;
            case 7:
                this.setParametres(AFQuestionnaireLAA_Param.P_NOMASS, value);
                break;
            case 8:
                this.setParametres(AFQuestionnaireLAA_Param.P_MOTIF, value);
                break;
            case 9:
                this.setParametres(AFQuestionnaireLAA_Param.P_NUMPOLICE, value);
                break;
            case 10:
                this.setParametres(AFQuestionnaireLAA_Param.P_LIEUDATELIBELLE, value);
                break;
            case 11:
                this.setParametres(AFQuestionnaireLAA_Param.P_SIGNATURE, value);
                break;
            case 12:
                this.setParametres(AFQuestionnaireLAA_Param.P_COMMENTSIGN, value);
                break;
        }
    }

}
