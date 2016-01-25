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
public class RFDecisionRefus3OO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_LETTRE_TYPE_REGIME_DECISION_REFUS_3";

    protected final String DATE_RAPPEL1 = "{dateRappel1}";
    protected final String DATE_RAPPEL2 = "{dateRappel2}";
    protected final String DATE1 = "{date1}";
    private FWMemoryLog memoryLog;
    private String regimeDateDemandeIndemnisation = "";
    private String regimeDateLettre1_11 = "";

    private String regimeDateLettre1_3 = "";

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
            addErreurMail(memoryLog, e.getMessage(), "RFDecisionRefus3OO:ajouterSignature");
            throw new Exception(e.toString());
        }

    }

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_REFUS3);
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
            caisseHelper.setTemplateName(RFDecisionRefus3OO.FICHIER_MODELE_DOCUMENTS_RFM);

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
        data.addData("idProcess", "RFDecisionRefus3OO");

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
            throw new Exception("Erreur : Pas d'adresse tiers (RFDecisionRefus3OO.generationLettre())");
        }

    }

    public String getRegimeDateDemandeIndemnisation() {
        return regimeDateDemandeIndemnisation;
    }

    public String getRegimeDateLettre1_11() {
        return regimeDateLettre1_11;
    }

    public String getRegimeDateLettre1_3() {
        return regimeDateLettre1_3;
    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean miseEnGed) throws Exception {

        setRegimeDateDemandeIndemnisation(process.getRegimeDateDemandeIndemnisation());
        setRegimeDateLettre1_11(process.getRegimeDateLettre1_11());
        setRegimeDateLettre1_3(process.getRegimeDateLettre1_3());

        return super.remplir(documentContainer, process, process.getIdTiers(),
                RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_DECISION_REFUS_3.getNoInforom(), miseEnGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // %=DECISION_REFUS3=%
            data.addData("DECISION_REFUS3", documentPrincipale.getTextes(1).getTexte(1).getDescription());
            // %=OBJET=%
            data.addData("OBJET", documentPrincipale.getTextes(1).getTexte(2).getDescription());
            // %=TITRE=%
            data.addData("TITRE", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(1)
                    .getDescription(), TITRE, titreComplet));
            // %=DATE_DEMANDE=%
            data.addData("DATE_DEMANDE", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(2)
                    .getDescription(), DATE1, regimeDateDemandeIndemnisation));
            // %=DATE_RAPPELS=%
            data.addData(
                    "DATE_RAPPELS",
                    PRStringUtils.replaceString(PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(3)
                            .getDescription(), DATE_RAPPEL1, regimeDateLettre1_3), DATE_RAPPEL2, regimeDateLettre1_11));
            // %=RESULTAT=%
            data.addData("RESULTAT", documentPrincipale.getTextes(2).getTexte(4).getDescription());
            // %=SALUTATIONS=% -> ajout TITRE !!
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(5)
                    .getDescription(), TITRE, titreComplet));
            // %=OPPOSITION=%
            data.addData("OPPOSITION", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=DETAILS=%
            data.addData("DETAILS", documentPrincipale.getTextes(3).getTexte(2).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public void setRegimeDateDemandeIndemnisation(String regimeDateDemandeIndemnisation) {
        this.regimeDateDemandeIndemnisation = regimeDateDemandeIndemnisation;
    }

    public void setRegimeDateLettre1_11(String regimeDateLettre1_11) {
        this.regimeDateLettre1_11 = regimeDateLettre1_11;
    }

    public void setRegimeDateLettre1_3(String regimeDateLettre1_3) {
        this.regimeDateLettre1_3 = regimeDateLettre1_3;
    }

}
