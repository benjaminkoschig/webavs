/*
 * public * Créé le 8 novembre 2010
 */
package globaz.cygnus.topaz.regime;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.api.decisions.IRFGenererDocumentDecision;
import globaz.cygnus.process.RFDocumentsProcess;
import globaz.cygnus.topaz.RFAbstractDocumentOO;
import globaz.cygnus.topaz.decision.RFGenererDecisionMainService;
import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.util.JACalendar;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.cygnus.process.document.RFDocumentEnum;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * author fha
 */
public class RFRegimeQuestionnaireOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_LETTRE_TYPE_REGIME_QUESTIONNAIRE";

    private FWMemoryLog memoryLog;

    private String regimeDateCourrierPrecedent11 = "";

    private void addErreurMail(FWMemoryLog memoryLog, String message, String source) {
        memoryLog.logMessage(message, new Integer(JadeBusinessMessageLevels.ERROR).toString(), source);
    }

    /**
     * Methode pour ajouter la signature en bas de document
     * 
     * @throws Exception
     */
    private void ajouterSignature() throws Exception {

        try {
            // Appel du tableau "tabSignature"
            Collection tabSignature = new Collection(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_SIGNATURE);

            // Récupération de la variable du tableau
            DataList lineSignature = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_SIGNATURE_DATA);

            // Insertion de la variable dans le tableau
            tabSignature.add(lineSignature);

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            // Recherche de l'adresse du tiers
            String adresse = PRTiersHelper.getAdresseCourrierFormatee(getSession(), idTiers, "",
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).toString();

            crBean.setAdresse(adresse);

            // Lieu et date
            // crBean.setDate(JACalendar.format(this.dateSurDocument, this.codeIsoLangue));

            // Ajout du NSS
            crBean.setNoAvs(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(RFGenererDecisionMainService.FICHIER_MODELE_DOCUMENT_DECISION_RFM);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);

            // Chargement de la signature
            data.addData("idSignature", "SIGNATURE_RFM_STANDART");

        } catch (Exception e) {
            addErreurMail(memoryLog, e.getMessage(), "RFRegimeQuestionnaireOO:ajouterSignature");
            throw new Exception(e.toString());
        }

    }

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_QUESTIONNAIRE);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCodeIsoLangue(codeIsoLangue);

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            documentPrincipale = documents[0];
        }

    }

    @Override
    public void chargerDonneesEnTete() throws Exception {

        try {

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            // Recherche de l'adresse du tiers
            String adresse = PRTiersHelper.getAdresseCourrierFormatee(session, idTiers, "",
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).toString();

            crBean.setAdresse(adresse);

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(RFRegimeQuestionnaireOO.FICHIER_MODELE_DOCUMENTS_RFM);

            // Insertion de l'entête
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);

            // Chargement du Header
            data.addData("idEntete", "HEADER_CAISSE");

        } catch (Exception e) {
            throw new Exception(e.toString());
        }

    }

    @Override
    public void generationLettre(RFCopieDecisionsValidationData... copie) throws Exception {

        // initialiser la caisse
        // this.caisse = this.initialiserCaisse();

        data = new DocumentData();

        // Chargement du document
        data.addData("idProcess", "RFRegimeQuestionnaireOO");

        // Retrieve d'informations pour la création de la décision
        tiersWrapper = PRTiersHelper.getTiersParId(getSession(), idTiers);
        if (null == tiersWrapper) {
            tiersWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
        }

        if (null != tiersWrapper) {

            codeIsoLangue = getSession().getCode(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            nom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);
            prenom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            sexe = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_SEXE);
            setTitre(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_TITRE));
            setTitreComplet(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_TITRE));

            // Chargement des catalogues de texte
            chargerCatalogueTexte();

            // Création des paramètres pour l'en-tête
            chargerDonneesEnTete();

            // Remplissage des champs du document
            remplirDocument();

            // Ajout signature
            ajouterSignature();

            // Ajout des copies
            // ajoutCopiesAnnexes();
            setDocumentData(data);

        } else {
            throw new Exception("Erreur : Pas d'adresse tiers (RFRegimeQuestionnaireOO.generationLettre())");
        }

    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRegimeDateCourrierPrecedent11() {
        return regimeDateCourrierPrecedent11;
    }

    // private Caisse initialiserCaisse() throws Exception {
    // if (RFApplication.PROPERTY_NUMERO_CAISSE_CCJU
    // .equals(this.getSession().getApplication().getProperty("noCaisse"))) {
    // return Caisse.CCJU;
    // } else {
    // if (RFApplication.PROPERTY_NUMERO_CAISSE_CCVD.equals(this.getSession().getApplication()
    // .getProperty("noCaisse"))) {
    // return Caisse.CCVD;
    // } else if (RFApplication.PROPERTY_NUMERO_CAISSE_LAUSANNE.equals(this.getSession().getApplication()
    // .getProperty("noCaisse"))) {
    // return Caisse.AGENCE_LAUSANNE;
    // } else {
    // throw new Exception("Erreur : Caisse inconnue(RFRegimeQuestionnaireOO.initialiserCaisse())");
    // }
    //
    // }
    // }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean miseEnGed) throws Exception {

        setRegimeDateCourrierPrecedent11(process.getRegimeDateCourrierPrecedent11());

        return super.remplir(documentContainer, process, process.getIdTiers(),
                RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_QUESTIONNAIRE.getNoInforom(), miseEnGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // Objet
            data.addData("OBJET", documentPrincipale.getTextes(1).getTexte(1).getDescription());
            // %=DESTINATAIRE=%
            data.addData(
                    "DESTINATAIRE",
                    PRStringUtils.replaceString(
                            PRStringUtils.replaceString(
                                    PRStringUtils.replaceString(documentPrincipale.getTextes(1).getTexte(2)
                                            .getDescription(), NOM_ASSURE, ""), PRENOM_ASSURE, ""), SEXE, titreComplet));
            // %=DATE_DEMANDE=%
            data.addData("DATE_DEMANDE", PRStringUtils.replaceString(documentPrincipale.getTextes(1).getTexte(3)
                    .getDescription(), DATE_DEMANDE, getRegimeDateCourrierPrecedent11()));
            // %=RENSEIGNEMENTS=%
            data.addData("RENSEIGNEMENTS", documentPrincipale.getTextes(1).getTexte(4).getDescription());
            // %=REPONSE=%
            data.addData("REPONSE", documentPrincipale.getTextes(1).getTexte(5).getDescription());
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(1).getTexte(6)
                    .getDescription(), TITRE, titreComplet));
            // %=VERSO=%
            data.addData("VERSO", documentPrincipale.getTextes(2).getTexte(1).getDescription());

            // %=MEDECIN=%
            data.addData("MEDECIN", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=DOCTEUR=%
            data.addData("DOCTEUR", documentPrincipale.getTextes(3).getTexte(2).getDescription());
            // %=DROIT_PC=%
            data.addData(
                    "DROIT_PC",
                    PRStringUtils.replaceString(
                            PRStringUtils.replaceString(
                                    PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(3)
                                            .getDescription(), NOM_ASSURE, nom), PRENOM_ASSURE, prenom), SEXE, titre));
            // %=INTRO_LOI=%
            data.addData("INTRO_LOI", documentPrincipale.getTextes(3).getTexte(4).getDescription());
            // %=QUOTE_LOI=%
            data.addData("QUOTE_LOI", documentPrincipale.getTextes(3).getTexte(5).getDescription());
            // %=COMMUNICATION=%
            data.addData("COMMUNICATION", documentPrincipale.getTextes(3).getTexte(6).getDescription());
            // %=REPONSE_MEDECIN=%
            data.addData("REPONSE_MEDECIN", documentPrincipale.getTextes(4).getTexte(1).getDescription());
            // %=PATIENT_REGIME=%
            data.addData(
                    "PATIENT_REGIME",
                    PRStringUtils.replaceString(
                            PRStringUtils.replaceString(
                                    PRStringUtils.replaceString(documentPrincipale.getTextes(4).getTexte(2)
                                            .getDescription(), NOM_ASSURE, nom), PRENOM_ASSURE, prenom), SEXE, titre));
            // %=REGIME_DIABETE=%
            data.addData("REGIME_DIABETE", documentPrincipale.getTextes(4).getTexte(3).getDescription());
            // %=REGIME_ALIMENTAIRE=%
            data.addData("REGIME_ALIMENTAIRE", documentPrincipale.getTextes(4).getTexte(4).getDescription());
            // %=DETAIL_REGIME=%
            data.addData("DETAIL_REGIME", documentPrincipale.getTextes(4).getTexte(5).getDescription());
            // %=SURVIE PATIENT=%
            data.addData("SURVIE_PATIENT", documentPrincipale.getTextes(4).getTexte(6).getDescription());
            // %=DUREE_REGIME=%
            data.addData("DUREE_REGIME", documentPrincipale.getTextes(4).getTexte(7).getDescription());
            // %=COUT_REGIME=%
            data.addData("COUT_REGIME", documentPrincipale.getTextes(4).getTexte(8).getDescription());
            // %=CONCLUSION=%
            data.addData("CONCLUSION", documentPrincipale.getTextes(4).getTexte(9).getDescription());
            // %=SALUTATION_MEDECIN=%
            data.addData("SALUTATION_MEDECIN", documentPrincipale.getTextes(4).getTexte(10).getDescription());
            // %=YES=%
            data.addData("YES", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=NO=%
            data.addData("NO", documentPrincipale.getTextes(5).getTexte(2).getDescription());
            // %=DATE=%
            data.addData("DATE", documentPrincipale.getTextes(5).getTexte(3).getDescription());
            // %=SERVICE=%
            data.addData("SERVICE", documentPrincipale.getTextes(6).getTexte(1).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    public void setRegimeDateCourrierPrecedent11(String regimeDateCourrierPrecedent11) {
        this.regimeDateCourrierPrecedent11 = regimeDateCourrierPrecedent11;
    }

}
