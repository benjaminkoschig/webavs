/*
 * Cr?? le 8 novembre 2010
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
public class RFDecisionSuppressionOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_LETTRE_TYPE_REGIME_DECISION_SUPPRESSION";

    protected final String DATE = "{date}";
    protected final String DATE17 = "{date17}";
    protected final String DATE18 = "{date18}";
    private Boolean isRegimeRecenteRevision = Boolean.FALSE;

    private FWMemoryLog memoryLog;
    protected final String MONTANT = "{montantRFM}";
    private String regimeDateAllocationMensuelleSuppression = "";
    private String regimeDateEnvoiLettre1_7 = "";
    private String regimeDateEnvoiLettre1_8 = "";

    private String regimeMontantAllocationMensuelleSuppression = "";

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

            // R?cup?ration de la variable du tableau
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
            addErreurMail(memoryLog, e.getMessage(), "RFDecisionSuppressionOO:ajouterSignature");
            throw new Exception(e.toString());
        }

    }

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_SUPPRESSION);
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
            caisseHelper.setTemplateName(RFDecisionSuppressionOO.FICHIER_MODELE_DOCUMENTS_RFM);

            // Insertion de l'ent?te
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
        data.addData("idProcess", "RFDecisionSuppressionOO");

        // Retrieve d'informations pour la cr?ation de la d?cision
        tiersWrapper = PRTiersHelper.getTiersParId(getSession(), idTiers);
        if (null == tiersWrapper) {
            tiersWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
        }

        if (null != tiersWrapper) {

            codeIsoLangue = getSession().getCode(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            nom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);
            prenom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            setTitreComplet(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_TITRE));

            // Chargement des catalogues de texte
            chargerCatalogueTexte();

            // Cr?ation des param?tres pour l'en-t?te
            chargerDonneesEnTete();

            // Remplissage des champs du document
            remplirDocument();

            // Ajout signature
            ajouterSignature();

            // Ajout des copies
            // ajoutCopiesAnnexes();
            setDocumentData(data);

        } else {
            throw new Exception("Erreur : Pas d'adresse tiers (RFDecisionSuppressionOO.generationLettre())");
        }

    }

    public Boolean getIsRegimeRecenteRevision() {
        return isRegimeRecenteRevision;
    }

    public String getRegimeDateAllocationMensuelleSuppression() {
        return regimeDateAllocationMensuelleSuppression;
    }

    public String getRegimeDateEnvoiLettre1_7() {
        return regimeDateEnvoiLettre1_7;
    }

    public String getRegimeDateEnvoiLettre1_8() {
        return regimeDateEnvoiLettre1_8;
    }

    public String getRegimeMontantAllocationMensuelleSuppression() {
        return regimeMontantAllocationMensuelleSuppression;
    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean miseEnGed) throws Exception {

        setRegimeMontantAllocationMensuelleSuppression(process.getRegimeMontantAllocationMensuelleSuppression());
        setRegimeDateAllocationMensuelleSuppression(process.getRegimeDateAllocationMensuelleSuppression());
        setRegimeDateEnvoiLettre1_7(process.getRegimeDateEnvoiLettre1_7());
        setRegimeDateEnvoiLettre1_8(process.getRegimeDateEnvoiLettre1_8());
        setIsRegimeRecenteRevision(process.getIsRegimeRecenteRevision());

        return super.remplir(documentContainer, process, process.getIdTiers(),
                RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_DECISION_SUPPRESSION.getNoInforom(), miseEnGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // %=DECISION_SUPPRESSION=%
            data.addData("DECISION_SUPPRESSION", documentPrincipale.getTextes(1).getTexte(1).getDescription());
            // %=OBJET=%
            data.addData("OBJET", documentPrincipale.getTextes(1).getTexte(2).getDescription());
            // %=TITRE=%
            data.addData("TITRE", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(1)
                    .getDescription(), TITRE, titreComplet));
            // %=PRESTATION=%
            data.addData("PRESTATION", documentPrincipale.getTextes(2).getTexte(2).getDescription());
            // %=MONTANT=%
            data.addData("MONTANT", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(3)
                    .getDescription(), MONTANT, regimeMontantAllocationMensuelleSuppression));
            // %=SUPPRIME=%
            data.addData("SUPPRIME", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=DATE_SUPPRESSION=%
            data.addData("DATE_SUPPRESSION", PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(2)
                    .getDescription(), DATE, regimeDateAllocationMensuelleSuppression));
            // %=MOTIF=%
            data.addData("MOTIF", documentPrincipale.getTextes(3).getTexte(3).getDescription());
            // %=LOI=%
            data.addData("LOI", documentPrincipale.getTextes(4).getTexte(1).getDescription());
            // %=CHOIX=%
            if (isRegimeRecenteRevision) {
                data.addData("CHOIX", documentPrincipale.getTextes(4).getTexte(2).getDescription());
            } else {
                data.addData("CHOIX", documentPrincipale.getTextes(4).getTexte(3).getDescription());
                data.addData("CHOIX", PRStringUtils.replaceString(PRStringUtils.replaceString(documentPrincipale
                        .getTextes(4).getTexte(3).getDescription(), DATE17, regimeDateEnvoiLettre1_7), DATE18,
                        regimeDateEnvoiLettre1_8));
            }
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(4).getTexte(4)
                    .getDescription(), TITRE, titreComplet));
            // %=OPPOSITION=%
            data.addData("OPPOSITION", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=DETAILS=%
            data.addData("DETAILS", documentPrincipale.getTextes(5).getTexte(2).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public void setIsRegimeRecenteRevision(Boolean isRegimeRecenteRevision) {
        this.isRegimeRecenteRevision = isRegimeRecenteRevision;
    }

    public void setRegimeDateAllocationMensuelleSuppression(String regimeDateAllocationMensuelleSuppression) {
        this.regimeDateAllocationMensuelleSuppression = regimeDateAllocationMensuelleSuppression;
    }

    public void setRegimeDateEnvoiLettre1_7(String regimeDateEnvoiLettre1_7) {
        this.regimeDateEnvoiLettre1_7 = regimeDateEnvoiLettre1_7;
    }

    public void setRegimeDateEnvoiLettre1_8(String regimeDateEnvoiLettre1_8) {
        this.regimeDateEnvoiLettre1_8 = regimeDateEnvoiLettre1_8;
    }

    public void setRegimeMontantAllocationMensuelleSuppression(String regimeMontantAllocationMensuelleSuppression) {
        this.regimeMontantAllocationMensuelleSuppression = regimeMontantAllocationMensuelleSuppression;
    }

}
