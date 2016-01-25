/*
 * Créé le 8 novembre 2010
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
public class RFQuestionnaireRevisionsOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_LETTRE_TYPE_REGIME_QUESTIONNAIRE_REVISION";

    protected final String DATE = "{dateRegime}";
    private FWMemoryLog memoryLog;
    protected final String MONTANT_RFM = "{montantRFM}";
    protected String regimeMontantAllocationMensuelle = "";

    protected final String VERSO_OU_CERTIFICAT_MEDICAL = "{versoOuCertificatMedical}";

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
            String adresse = PRTiersHelper.getAdresseDomicileFormatee(getSession(), idTiers).toString();

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
            addErreurMail(memoryLog, e.getMessage(), "RFQuestionnaireRevisionsOO:ajouterSignature");
            throw new Exception(e.toString());
        }

    }

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_QUESTIONNAIRE_REVISION);
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

    // peut sans doute mettre dans la classe mere
    @Override
    public void chargerDonneesEnTete() throws Exception {

        try {

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            // Recherche de l'adresse du tiers
            String adresse = PRTiersHelper.getAdresseDomicileFormatee(session, idTiers).toString();

            crBean.setAdresse(adresse);

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(RFQuestionnaireRevisionsOO.FICHIER_MODELE_DOCUMENTS_RFM);

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

        // Chargement informations principales
        data = new DocumentData();

        // Chargement du template
        data.addData("idProcess", "RFQuestionnaireRevisionsOO");

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
            throw new Exception("Erreur : Pas d'adresse tiers (RFDecisionRefusOO.generationLettre())");
        }

    }

    public String getRegimeMontantAllocationMensuelle() {
        return regimeMontantAllocationMensuelle;
    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean miseEnGed) throws Exception {

        setRegimeMontantAllocationMensuelle(process.getRegimeMontantAllocationMensuelle());

        return super.remplir(documentContainer, process, process.getIdTiers(),
                RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_QUESTIONNAIRE_REVISION.getNoInforom(), miseEnGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {

            /* montant et date de décision? */

            // %=OBJET=%
            data.addData("OBJET", documentPrincipale.getTextes(1).getTexte(1).getDescription());
            // %=TITRE=%
            data.addData("TITRE", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(1)
                    .getDescription(), TITRE, titreComplet));
            // %=MONTANT=%
            data.addData("MONTANT", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(2)
                    .getDescription(), MONTANT_RFM, regimeMontantAllocationMensuelle));
            // %=LOI=%
            data.addData("LOI", documentPrincipale.getTextes(2).getTexte(3).getDescription());
            // %=MEDECIN=%
            data.addData("MEDECIN", documentPrincipale.getTextes(2).getTexte(4).getDescription());
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(5)
                    .getDescription(), TITRE, titreComplet));
            // %=VERSO=%
            data.addData("VERSO", documentPrincipale.getTextes(2).getTexte(6).getDescription());
            // %=POUR_MEDECIN=%
            data.addData("POUR_MEDECIN", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=TITRE_DOCTEUR=%
            data.addData("TITRE_DOCTEUR", documentPrincipale.getTextes(3).getTexte(2).getDescription());
            // %=PATIENT=%
            data.addData("PATIENT", PRStringUtils.replaceString(
                    PRStringUtils.replaceString(PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(3)
                            .getDescription(), NOM_ASSURE, nom), PRENOM_ASSURE, prenom), TITRE, titreComplet));
            // %=ARTICLE_LOI=%
            data.addData("ARTICLE_LOI", documentPrincipale.getTextes(3).getTexte(4).getDescription());
            // %=CITATION_LOI=%
            data.addData("CITATION_LOI", documentPrincipale.getTextes(3).getTexte(5).getDescription());
            // %=RENSEIGNEMENT=%
            data.addData("RENSEIGNEMENT", documentPrincipale.getTextes(3).getTexte(6).getDescription());
            // %=REPONSE_MEDECIN=%
            data.addData("REPONSE_MEDECIN", documentPrincipale.getTextes(4).getTexte(1).getDescription());
            // %=PATIENT_REGIME=%
            data.addData("PATIENT_REGIME", PRStringUtils.replaceString(
                    PRStringUtils.replaceString(
                            PRStringUtils.replaceString(
                                    PRStringUtils.replaceString(documentPrincipale.getTextes(4).getTexte(2)
                                            .getDescription(), NOM_ASSURE, nom), PRENOM_ASSURE, prenom), TITRE, titre),
                    DATE, ""));
            // %=REGIME_DIABETIQUE=%
            data.addData("REGIME_DIABETIQUE", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=REGIME_ALIMENTAIRE=%
            data.addData("REGIME_ALIMENTAIRE", documentPrincipale.getTextes(5).getTexte(2).getDescription());
            // %=DETAILS_REGIME=%
            data.addData("DETAILS_REGIME", documentPrincipale.getTextes(5).getTexte(3).getDescription());
            // %=NECESSITE_REGIME=%
            data.addData("NECESSITE_REGIME", documentPrincipale.getTextes(6).getTexte(1).getDescription());
            // %=OUI=%
            data.addData("OUI", documentPrincipale.getTextes(6).getTexte(2).getDescription());
            // %=NON=%
            data.addData("NON", documentPrincipale.getTextes(6).getTexte(3).getDescription());
            // %=DUREE=%
            data.addData("DUREE", documentPrincipale.getTextes(7).getTexte(1).getDescription());
            // %=DEPENSES1=%
            data.addData("DEPENSES1", documentPrincipale.getTextes(8).getTexte(1).getDescription());
            // %=IMPORTANTES=%
            data.addData("IMPORTANTES", documentPrincipale.getTextes(8).getTexte(2).getDescription());
            // %=DEPENSES2=%
            data.addData("DEPENSES2", documentPrincipale.getTextes(8).getTexte(3).getDescription());
            // %=DATE=%
            data.addData("DATE", documentPrincipale.getTextes(9).getTexte(1).getDescription());
            // %=FRAIS_SUP=%
            data.addData("FRAIS_SUP", documentPrincipale.getTextes(10).getTexte(1).getDescription());
            // %=SALUTATIONS_DOCTEUR=%
            data.addData("SALUTATIONS_DOCTEUR", documentPrincipale.getTextes(10).getTexte(2).getDescription());
            // %=SERVICE=%
            data.addData("SERVICE", documentPrincipale.getTextes(10).getTexte(3).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public void setRegimeMontantAllocationMensuelle(String regimeMontantAllocationMensuelle) {
        this.regimeMontantAllocationMensuelle = regimeMontantAllocationMensuelle;
    }
}
