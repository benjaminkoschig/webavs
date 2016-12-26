/*
 * Cr�� le 8 novembre 2010
 */
package globaz.cygnus.topaz.regime;

/*
 * author fha
 */
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

public class RFDemandeEvaluationCMSOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_LETTRE_TYPE_REGIME_DEMANDE_EVALUATION_CMS";

    private FWMemoryLog memoryLog;

    private void addErreurMail(FWMemoryLog memoryLog, String message, String source) {
        memoryLog.logMessage(message, new Integer(JadeBusinessMessageLevels.ERROR).toString(), source);
    }

    private void ajoutCopiesAnnexes() throws Exception {

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

            // R�cup�ration de la variable du tableau
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
            addErreurMail(memoryLog, e.getMessage(), "RFDemandeEvaluationCMSOO:ajouterSignature");
            throw new Exception(e.toString());
        }

    }

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DEMANDE_EVALUTION_CMS);
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
            String adresse = PRTiersHelper.getAdresseCourrierFormatee(session, idTiers, "",
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).toString();

            crBean.setAdresse(adresse);

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(RFDemandeEvaluationCMSOO.FICHIER_MODELE_DOCUMENTS_RFM);

            // Insertion de l'ent�te
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

        // Chargement du document
        data.addData("idProcess", "RFDemandeEvaluationCMSOO");

        // Retrieve d'informations pour la cr�ation de la d�cision
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
            adresse = PRTiersHelper.getAdresseCourrierFormatee(session, idTiers, "",
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).toString();
            setTitreComplet(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_TITRE));

            // Chargement des catalogues de texte
            chargerCatalogueTexte();

            // Cr�ation des param�tres pour l'en-t�te
            chargerDonneesEnTete();

            // Remplissage des champs du document
            remplirDocument();

            // Ajout signature
            ajouterSignature();

            // Ajout des copies
            ajoutCopiesAnnexes();

            setDocumentData(data);

        } else {
            throw new Exception("Erreur : Pas d'adresse tiers (RFDecisionRefusOO.generationLettre())");
        }

    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean miseEnGed) throws Exception {

        setSession(process.getSession());
        setDateSurDocument(process.getDateDocument());
        setIdTiers(process.getIdTiers());
        setIsCopie(isCopie);

        return super.remplir(documentContainer, process, getIdTiers(),
                RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_DEMANDE_EVALUATION_AU_CMS.getNoInforom(), miseEnGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            Collection newTable = new Collection("tabDest");
            // %=DESTINATAIRE=%
            data.addData("DESTINATAIRE", documentPrincipale.getTextes(1).getTexte(1).getDescription());

            DataList line1 = new DataList("ligne");
            newTable.add(line1);
            // %=ADRESSE_DESTINATAIRE=%
            line1.addData("ADRESSE_DESTINATAIRE", PRStringUtils.replaceString(
                    documentPrincipale.getTextes(1).getTexte(2).getDescription(), ADRESSE_DESTINATAIRE, adresse));
            data.add(newTable);

            // %=CENTREMEDICOSOCIAL=%
            data.addData("CENTREMEDICOSOCIAL",
                    PRStringUtils.replaceString(documentPrincipale.getTextes(1).getTexte(3).getDescription(), "", ""));
            // %=OBJET=%
            data.addData("OBJET", PRStringUtils.replaceString(
                    PRStringUtils.replaceString(PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(1)
                            .getDescription(), NOM_ASSURE, nom), PRENOM_ASSURE, prenom), TITRE, titreComplet));
            // %=TITRE=%
            data.addData("TITRE", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(2)
                    .getDescription(), TITRE, titreComplet));
            // %=PARAGRAPHE1=%
            data.addData("PARAGRAPHE1", documentPrincipale.getTextes(2).getTexte(3).getDescription());
            // %=PARAGRAPHE2=%
            data.addData("PARAGRAPHE2", documentPrincipale.getTextes(2).getTexte(4).getDescription());
            // %=PARAGRAPHE31=%
            data.addData("PARAGRAPHE31", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=PARAGRAPHE32=%
            data.addData("PARAGRAPHE32", documentPrincipale.getTextes(3).getTexte(2).getDescription());
            // %=PARAGRAPHE33=%
            data.addData("PARAGRAPHE33", documentPrincipale.getTextes(3).getTexte(3).getDescription());
            // %=PARAGRAPHE34=%
            data.addData("PARAGRAPHE34", documentPrincipale.getTextes(3).getTexte(4).getDescription());
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(5)
                    .getDescription(), TITRE, titreComplet));
            // %=ANNEXE=%
            data.addData("ANNEXE", documentPrincipale.getTextes(4).getTexte(1).getDescription());
            // %=MENTIONNE=%
            data.addData("MENTIONNE", documentPrincipale.getTextes(4).getTexte(2).getDescription());
            // %=COPIE=%
            data.addData("COPIE", documentPrincipale.getTextes(4).getTexte(3).getDescription());
            // %=BENEF_PC=%
            data.addData("BENEF_PC", documentPrincipale.getTextes(4).getTexte(4).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

}
