package globaz.naos.itext.suiviLPP;

import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.hercule.service.CETiersService;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.controleLpp.AFSuiviLppAnnuelSalarie;
import globaz.naos.db.controleLpp.AFSuiviLppAnnuelSalariesManager;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.listes.pdf.extraitDS.AFListeExtraitDS;
import globaz.naos.properties.AFProperties;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;

public class AFQuestionnaireLPPAnnuel_Doc extends AFAbstractTiersDocument {

    private static final String NUM_INFOROM_QUESTIONNAIRE = "0108CAF";
    private static final long serialVersionUID = 4255258269940535901L;

    private String pathListeExtraitDS;
    private JadePublishDocumentInfo documentInfoListeExtraitDS;

    public AFQuestionnaireLPPAnnuel_Doc() throws Exception {
        super();
    }

    public AFQuestionnaireLPPAnnuel_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFControleLPPAnnuel_Param.L_NOMDOC));
    }

    @Override
    public void afterBuildReport() {
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

        // Lancement du process pour générer la liste extrait de salaires
        if (AFProperties.CONTROLE_ANNUEL_LPP_GENERATION_EXTRAIT_DS.getBooleanValue()) {
            initAndLaunchProcessListExtraitDS(getIdAffiliation(), Integer.parseInt(getPeriode()), getDateImpression());
        }

        fillDocInfo();
    }

    @Override
    protected void fillDocInfo() {
        super.fillDocInfo();
        getDocumentInfo().setDocumentTypeNumber(NUM_INFOROM_QUESTIONNAIRE);
        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(true);
    }

    @Override
    public String getCategorie() {
        return CodeSystem.TYPE_CAT_CONTROLE_LPP;
    }

    @Override
    public String getDomaine() {
        return CodeSystem.DOMAINE_CAT_LPP;
    }

    @Override
    public int getNbLevel() {
        return AFControleLPPAnnuel_Param.NB_LEVEL;
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFControleLPPAnnuel_Param.L_NOMDOC);
    }

    @Override
    protected String getTemplate() {
        return AFControleLPPAnnuel_Param.TEMPLATE_CONTROLE_ANNUEL_LPP;
    }

    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
        //
    }

    private void initAndLaunchProcessListExtraitDS(String numAffilie, int annee, String dateImpression)
            throws Exception {

        AFSuiviLppAnnuelSalariesManager mgr = new AFSuiviLppAnnuelSalariesManager();
        mgr.setSession(getSession());
        mgr.setForIdAffiliation(Long.parseLong(numAffilie));
        mgr.setForAnnee(annee);
        mgr.find(BManager.SIZE_NOLIMIT);

        List<AFSuiviLppAnnuelSalarie> listeSalarie = new ArrayList<AFSuiviLppAnnuelSalarie>();

        if (mgr.getSize() > 0) {
            for (int i = 0; i < mgr.getSize(); i++) {
                AFSuiviLppAnnuelSalarie salarie = (AFSuiviLppAnnuelSalarie) mgr.getEntity(i);
                calculSeuilLPP(salarie);
                listeSalarie.add(salarie);
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
            throw new Exception("L'affiliation correspondant au numéro " + numAffilie + " n'a pas été trouvée");
        }

        TIAdresseDataSource adresseEmployeur = getAdresseFromItTiersEmployeur(getSession(), "20",
                employeur.getIdTiers(), numAffilie);

        AFListeExtraitDS processListeExtraitDS = new AFListeExtraitDS();
        processListeExtraitDS.setSession(getSession());
        processListeExtraitDS.setListeSalarie(listeSalarie);
        processListeExtraitDS.setEmployeur(employeur);
        processListeExtraitDS.setAdresseEmployeur(adresseEmployeur);
        processListeExtraitDS.setAnnee(String.valueOf(annee));
        processListeExtraitDS.setLangueIso(employeur.getTiers().getLangueIso());
        processListeExtraitDS.setDate(dateImpression);
        processListeExtraitDS.setSendCompletionMail(false);
        processListeExtraitDS.executeProcess();
        // date ?

        pathListeExtraitDS = processListeExtraitDS.getPath();
        documentInfoListeExtraitDS = processListeExtraitDS.getDocumentInfoPdf();

        // Ajout du fichier d'extrait de salaire.
        registerAttachedDocument(documentInfoListeExtraitDS, pathListeExtraitDS);
    }

    public void calculSeuilLPP(AFSuiviLppAnnuelSalarie salarie) {
        // - 1 pour tenir compte du mois en question
        int periode = salarie.getMoisFin() - (salarie.getMoisDebut() - 1);

        try {
            FWFindParameterManager manager = new FWFindParameterManager();
            manager.setSession(getSession());
            manager.setIdCodeSysteme(CodeSystem.CS_SEUIL_LPP);
            manager.setIdCleDiffere("SEUILLPP");
            manager.setDateDebutValidite("01.01." + salarie.getAnnee());
            manager.find(BManager.SIZE_NOLIMIT);

            Montant valeurSeuil = new Montant(((FWFindParameter) manager.getFirstEntity()).getValeurNumParametre());

            valeurSeuil = valeurSeuil.multiply(periode / 12.0);

            salarie.setSeuilEntree(valeurSeuil);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error when retrieving the 'plage de valeur' with key 'SEUILLPP' ("
                    + e.getMessage() + ")");
        }
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

        if (d == null) {
            throw new Exception("Unabled to retrieve the adresse ( Num Affilie = " + numAffilie + ")");
        }

        return d;
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        switch (i) {
            case 1:
                this.setParametres(AFControleLPPAnnuel_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFControleLPPAnnuel_Param.P_POLITESSE, format(value, getParams("", getSession())));
                break;
            case 3:
                this.setParametres(AFControleLPPAnnuel_Param.P_TEXTE, value);
                break;
            case 4:
                this.setParametres(AFControleLPPAnnuel_Param.P_TEXTE2, value);
                break;
            case 5:
                this.setParametres(AFControleLPPAnnuel_Param.P_TEXTE3, value);
                break;
            case 6:
                this.setParametres(AFControleLPPAnnuel_Param.P_TEXTE4, value);
                break;
            case 7:
                this.setParametres(AFControleLPPAnnuel_Param.P_TEXTE5, format(value, getParams("", getSession())));
                break;
            case 8:
                this.setParametres(AFControleLPPAnnuel_Param.P_SIGN_LETTRE, value);
                break;
            case 9:
                this.setParametres(AFControleLPPAnnuel_Param.P_ANNEXE, value);
                break;
            case 10:
                this.setParametres(AFControleLPPAnnuel_Param.P_ANNEXE2, value);
                break;
            case 11:
                this.setParametres(AFControleLPPAnnuel_Param.P_TEXTE6, value);
                break;
            case 12:
                this.setParametres(AFControleLPPAnnuel_Param.P_TEXTE7, value);
                break;
            case 13:
                this.setParametres(AFControleLPPAnnuel_Param.P_ASTERISQUE, value);
                break;
            case 14:
                this.setParametres(AFControleLPPAnnuel_Param.P_DESLE, value);
                break;
            case 15:
                this.setParametres(AFControleLPPAnnuel_Param.P_DATE, value);
                break;
            case 16:
                this.setParametres(AFControleLPPAnnuel_Param.P_SIGNATURE, value);
                break;
            case 17:
                this.setParametres(AFControleLPPAnnuel_Param.P_NIP, value);
                break;
            case 18:
                this.setParametres(AFControleLPPAnnuel_Param.P_WARNING, value);
                break;
            case 19:
                this.setParametres(AFControleLPPAnnuel_Param.P_WARNING2, value);
                break;
            default:
                throw new Exception("Ce paramètre ne peut pas être pris en compte. "
                        + "Veuillez sélectionner des niveaux compris entre 1 et 19");

        }

    }

    @Override
    public void afterExecuteReport() {

        if (getParent() == null) {
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentTypeNumber(NUM_INFOROM_QUESTIONNAIRE);
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);

            try {
                this.mergePDF(docInfo, false, 0, false, null);
            } catch (Exception e) {
                JadeLogger.error(e, "Unabled to merge PDF for Suivi LPP Annuel");
            }
        }
        super.afterExecuteReport();
    }

}
