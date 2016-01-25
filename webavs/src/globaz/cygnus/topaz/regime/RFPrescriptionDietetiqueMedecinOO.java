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
public class RFPrescriptionDietetiqueMedecinOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_DOCUMENTS";
    private FWMemoryLog memoryLog;

    private String regimeDateCourrierPrecedent13 = "";

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
            addErreurMail(memoryLog, e.getMessage(), "RFPrescriptionDietetiqueMedecinOO:ajouterSignature");
            throw new Exception(e.toString());
        }

    }

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_PRESCRIPTION_DIETETIQUE_MEDECIN);
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
            caisseHelper.setTemplateName(RFPrescriptionDietetiqueMedecinOO.FICHIER_MODELE_DOCUMENTS_RFM);

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
        data.addData("idProcess", "RFPrescriptionDietetiqueMedecinOO");

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

            // Ajout de la signature en bas de page
            ajouterSignature();

            // Ajout des copies
            ajoutCopiesAnnexes();

            setDocumentData(data);

        } else {
            throw new Exception("Erreur : Pas d'adresse tiers (RFDecisionRefusOO.generationLettre())");
        }

    }

    public String getRegimeDateCourrierPrecedent13() {
        return regimeDateCourrierPrecedent13;
    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean miseEnGed) throws Exception {

        setRegimeDateCourrierPrecedent13(process.getRegimeDateCourrierPrecedent13());

        return super.remplir(documentContainer, process, process.getIdTiers(),
                RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_PRESCRIPTION_DIETETIQUE_MEDECIN.getNoInforom(), miseEnGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // Objet
            data.addData(
                    "OBJET",
                    PRStringUtils.replaceString(
                            PRStringUtils.replaceString(
                                    PRStringUtils.replaceString(documentPrincipale.getTextes(1).getTexte(1)
                                            .getDescription(), NOM_ASSURE, nom), PRENOM_ASSURE, prenom), SEXE, titre));
            // %=DESTINATAIRE=%
            data.addData(
                    "DESTINATAIRE",
                    PRStringUtils.replaceString(
                            PRStringUtils.replaceString(
                                    PRStringUtils.replaceString(documentPrincipale.getTextes(1).getTexte(2)
                                            .getDescription(), NOM_ASSURE, ""), PRENOM_ASSURE, ""), SEXE, titreComplet));
            // %=DATE_DEMANDE=%
            data.addData("DATE_DEMANDE", PRStringUtils.replaceString(documentPrincipale.getTextes(1).getTexte(3)
                    .getDescription(), DATE_DEMANDE, getRegimeDateCourrierPrecedent13()));
            // %=MANDATER_DIETETICIENNE_DEBUT=%
            data.addData("MANDATER_DIETETICIENNE_DEBUT", documentPrincipale.getTextes(1).getTexte(4).getDescription());
            // %=OMSV=%
            data.addData("OMSV", documentPrincipale.getTextes(1).getTexte(5).getDescription());
            // %=MANDATER_DIETETICIENNE_FIN=%
            data.addData("MANDATER_DIETETICIENNE_FIN", documentPrincipale.getTextes(1).getTexte(6).getDescription());
            // %=REMPLIR_FORMULAIRE=%
            data.addData("REMPLIR_FORMULAIRE", documentPrincipale.getTextes(1).getTexte(7).getDescription());
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(1).getTexte(8)
                    .getDescription(), TITRE, titreComplet));
            // %=ANNEXE=%
            data.addData("ANNEXE", documentPrincipale.getTextes(2).getTexte(1).getDescription());
            // %=FORM=%
            data.addData("FORM", documentPrincipale.getTextes(2).getTexte(2).getDescription());
            // %=COPIE=%
            data.addData("COPIE", documentPrincipale.getTextes(2).getTexte(3).getDescription());
            // %=BENEF_PC=%
            data.addData("BENEF_PC", documentPrincipale.getTextes(2).getTexte(4).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public void setRegimeDateCourrierPrecedent13(String regimeDateCourrierPrecedent13) {
        this.regimeDateCourrierPrecedent13 = regimeDateCourrierPrecedent13;
    }

}
