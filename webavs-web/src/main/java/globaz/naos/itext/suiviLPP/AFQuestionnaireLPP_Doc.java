package globaz.naos.itext.suiviLPP;

import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.hercule.service.CETiersService;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.controleLpp.AFExtraitDS;
import globaz.naos.db.controleLpp.AFExtraitDSManager;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.listes.pdf.extraitDS.AFListeExtraitDS;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.List;

public class AFQuestionnaireLPP_Doc extends AFAbstractTiersDocument {

    private static final long serialVersionUID = 4255258269940535901L;
    private String modelDe = "NAOS_QUESTIONNAIRE_LPP_VERSO_DE";
    private String modelFr = "NAOS_QUESTIONNAIRE_LPP_VERSO_FR";

    private String modelPath = "model/static/";

    private String pathListeExtraitDS;
    private JadePublishDocumentInfo documentInfoListeExtraitDS;

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

            // Ajout du fichier d'extrait de salaire.
            registerAttachedDocument(documentInfoListeExtraitDS, pathListeExtraitDS);

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

        // Lancement du process pour g�n�rer la liste extrait de salaires
        initAndLaunchProcessListExtraitDS("15", 2009);

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
        //
    }

    private void initAndLaunchProcessListExtraitDS(String numAffilie, int annee) throws Exception {
        AFExtraitDSManager mgr = new AFExtraitDSManager();
        mgr.setSession(getSession());
        mgr.setForAnnee(annee);
        mgr.setForIdAffilie(numAffilie);
        mgr.find(BManager.SIZE_NOLIMIT);

        List<AFExtraitDS> listeDS = new ArrayList<AFExtraitDS>();

        if (mgr.getSize() > 0) {
            for (int i = 0; i < mgr.getSize(); i++) {
                AFExtraitDS extraitDS = (AFExtraitDS) mgr.getEntity(i);
                extraitDS.calculSeuilLPP();
                listeDS.add(extraitDS);
            }
        }

        AFAffiliation employeur;

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(getSession());
        affiliationManager.setForAffiliationId(numAffilie);
        affiliationManager.find(BManager.SIZE_NOLIMIT);

        if (affiliationManager.size() == 1) {
            employeur = (AFAffiliation) affiliationManager.get(0);
        } else {
            throw new Exception("L'affiliation correspondant au num�ro " + numAffilie + " n'a pas �t� trouv�e");
        }

        TIAdresseDataSource adresseEmployeur = getAdresseFromItTiersEmployeur(getSession(), "20",
                employeur.getIdTiers(), numAffilie);

        AFListeExtraitDS processListeExtraitDS = new AFListeExtraitDS();
        processListeExtraitDS.setSession(getSession());
        processListeExtraitDS.setListeDS(listeDS);
        processListeExtraitDS.setEmployeur(employeur);
        processListeExtraitDS.setAdresseEmployeur(adresseEmployeur);
        processListeExtraitDS.setAnnee(String.valueOf(annee));
        processListeExtraitDS.executeProcess();

        pathListeExtraitDS = processListeExtraitDS.getPath();
        documentInfoListeExtraitDS = processListeExtraitDS.getDocumentInfoPdf();
    }

    private TIAdresseDataSource getAdresseFromItTiersEmployeur(BSession session, String typeAdresse, String idTiers,
            String numAffilie) throws Exception {

        TITiers tiers = CETiersService.retrieveTiers(session, idTiers);

        TIAdresseDataSource d;

        try {
            d = CETiersService.retrieveAdresseDataSource(typeAdresse, tiers, numAffilie);
        } catch (Exception e) {
            throw new Exception("Technical Exception, Unabled to retrieve the adresse ( idTiers = " + idTiers + ")", e);
        }

        return d;
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
                throw new Exception("Ce param�tre ne peut pas �tre pris en compte. "
                        + "Veuillez s�lectionner des niveaux compris entre 1 et 18");

        }

    }

}
