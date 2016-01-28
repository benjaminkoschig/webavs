package globaz.naos.itext.suiviLPP;

import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;

public class AFQuestionnaireLPP_Doc extends AFAbstractTiersDocument {

    private static final long serialVersionUID = 4255258269940535901L;
    private String modelDe = "NAOS_QUESTIONNAIRE_LPP_VERSO_DE";
    private String modelFr = "NAOS_QUESTIONNAIRE_LPP_VERSO_FR";

    private String modelPath = "model/static/";

    public AFQuestionnaireLPP_Doc() throws Exception {
        super();
    }

    public AFQuestionnaireLPP_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFQuestionnaireLPP_Param.L_NOMDOC));
    }

    @Override
    public void afterBuildReport() {
        FWIImportManager m = new FWIImportManager();
        try {
            if ("DE".equals(getIsoLangueDestinataire())) {
                addDocument(m.importReport(modelDe, Jade.getInstance().getExternalModelDir()
                        + AFApplication.DEFAULT_APPLICATION_NAOS_REP + "//" + modelPath));
            } else {
                addDocument(m.importReport(modelFr, Jade.getInstance().getExternalModelDir()
                        + AFApplication.DEFAULT_APPLICATION_NAOS_REP + "//" + modelPath));
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
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
        getDocumentInfo().setDocumentTypeNumber("0108CAF");
        super.fillDocInfo();
    }

    @Override
    public String getCategorie() {
        return CodeSystem.TYPE_CAT_QUEST_LPP;
    }

    @Override
    public String getDomaine() {
        return CodeSystem.DOMAINE_CAT_LPP;
    }

    @Override
    public int getNbLevel() {
        return AFQuestionnaireLPP_Param.NB_LEVEL;
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFQuestionnaireLPP_Param.L_NOMDOC);
    }

    @Override
    protected String getTemplate() {
        return AFQuestionnaireLPP_Param.TEMPLATE_QUEST_LPP;
    }

    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        switch (i) {
            case 1:
                this.setParametres(AFQuestionnaireLPP_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFQuestionnaireLPP_Param.P_CORPS, format(value, getParams("", getSession())));
                break;
            case 3:
                this.setParametres(AFQuestionnaireLPP_Param.P_SIGN_LETTRE, value);
                break;
            case 4:
                this.setParametres(AFQuestionnaireLPP_Param.P_QUESTGAUCHE, value);
                break;
            case 5:
                this.setParametres(AFQuestionnaireLPP_Param.P_TITLEQUEST, value);
                break;
            case 6:
                this.setParametres(AFQuestionnaireLPP_Param.P_QUEST, value);
                break;
            case 7:
                this.setParametres(AFQuestionnaireLPP_Param.P_OUI, value);
                break;
            case 8:
                this.setParametres(AFQuestionnaireLPP_Param.P_NON, value);
                break;
            case 9:
                this.setParametres(AFQuestionnaireLPP_Param.P_QUESTDROITE, value);
                break;
            case 10:
                this.setParametres(AFQuestionnaireLPP_Param.P_QUESTDROITE2, value);
                break;
            case 11:
                this.setParametres(AFQuestionnaireLPP_Param.P_QUESTDROITE3, value);
                break;
            case 12:
                this.setParametres(AFQuestionnaireLPP_Param.P_MOTIF, value);
                break;
            case 13:
                this.setParametres(AFQuestionnaireLPP_Param.P_QUESTGAUCHE2, value);
                break;
            case 14:
                this.setParametres(AFQuestionnaireLPP_Param.P_QUESTGAUCHEJOIN, value);
                break;
            case 15:
                this.setParametres(AFQuestionnaireLPP_Param.P_QUESTGAUCHE3, value);
                break;
            case 16:
                this.setParametres(AFQuestionnaireLPP_Param.P_LIEUDATELIBELLE, value);
                break;
            case 17:
                this.setParametres(AFQuestionnaireLPP_Param.P_SIGNATURE, value);
                break;
            case 18:
                this.setParametres(AFQuestionnaireLPP_Param.P_COMMENTSIGN, value);
                break;
            default:
                throw new Exception("Ce paramètre ne peut pas être pris en compte. "
                        + "Veuillez sélectionner des niveaux compris entre 1 et 18");

        }

    }

}
