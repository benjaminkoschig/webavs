/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.topaz.moyensAuxiliaires;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.process.RFDocumentsProcess;
import globaz.cygnus.topaz.RFAbstractDocumentOO;
import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.util.JACalendar;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * author fha
 */
public class RFBonAchatMoyensAuxiliairesOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_LETTRE_TYPE_MOYENS_AUX_BON_ACHAT_REMISE_PRET";

    protected final String DATE = "{date}";

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_BON_ACHAT_REMISE_PRET);
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

            // Ajout du NSS
            crBean.setNoAvs(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(RFBonAchatMoyensAuxiliairesOO.FICHIER_MODELE_DOCUMENTS_RFM);

            // Insertion de l'entête
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);

            // Chargement de l'en-tête de la caisse
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
        data.addData("idProcess", "RFBonAchatMoyensAuxiliairesOO");

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

            // Chargement des catalogues de texte
            chargerCatalogueTexte();

            // Création des paramètres pour l'en-tête
            chargerDonneesEnTete();

            // Remplissage des champs du document
            remplirDocument();

            // Ajout des copies
            // ajoutCopiesAnnexes();
            setDocumentData(data);

        } else {
            throw new Exception("Erreur : Pas d'adresse tiers (RFBonAchatMoyensAuxiliairesOO.generationLettre())");
        }

    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean isSendToGed) throws Exception {

        setSession(process.getSession());
        setDateSurDocument(process.getDateDocument());
        setIdTiers(process.getIdTiers());
        setIsCopie(isCopie);

        return super.remplir(documentContainer, process, getIdTiers(),
                IPRConstantesExternes.RFM_LETTRE_TYPE_MOYENS_AUXILIAIRES_BON_ACHAT_POUR_REMISE_EN_PRET, isSendToGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // %=BON_ACHAT=%
            data.addData("BON_ACHAT", documentPrincipale.getTextes(2).getTexte(1).getDescription());
            // %=OBJET=%
            data.addData("OBJET", documentPrincipale.getTextes(2).getTexte(2).getDescription());
            // %=TITRE=%
            data.addData("TITRE", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=BENEFICIAIRE=%
            data.addData("BENEFICIAIRE", documentPrincipale.getTextes(3).getTexte(2).getDescription());
            // %=INDISPONIBLE=%
            data.addData("INDISPONIBLE", documentPrincipale.getTextes(4).getTexte(1).getDescription());
            // %=ENTRETIEN=%
            data.addData("ENTRETIEN",
                    PRStringUtils.replaceString(documentPrincipale.getTextes(4).getTexte(2).getDescription(), DATE, ""));
            // %=OBTENIR=%
            data.addData("OBTENIR", documentPrincipale.getTextes(4).getTexte(3).getDescription());
            // %=REPRESENTANT=%
            data.addData("REPRESENTANT", documentPrincipale.getTextes(4).getTexte(4).getDescription());
            // %=COPIE=%
            data.addData("COPIE", documentPrincipale.getTextes(4).getTexte(5).getDescription());
            // %=ACCUSE=%
            data.addData("ACCUSE", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=REPRES=%
            data.addData("REPRES", documentPrincipale.getTextes(5).getTexte(2).getDescription());
            // %=CONFIRMATION=%
            data.addData("CONFIRMATION", documentPrincipale.getTextes(5).getTexte(3).getDescription());
            // %=FACTURE=%
            data.addData("FACTURE", documentPrincipale.getTextes(6).getTexte(1).getDescription());
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(6).getTexte(2)
                    .getDescription(), TITRE, sexe.equals("516001") ? "Monsieur" : "Madame"));
            // %=ANNEXE=%
            data.addData("ANNEXE", documentPrincipale.getTextes(7).getTexte(1).getDescription());
            // %=MENTIONNE=%
            data.addData("MENTIONNE", documentPrincipale.getTextes(7).getTexte(2).getDescription());
            // %=COPIE=%
            data.addData("COPIE", documentPrincipale.getTextes(7).getTexte(3).getDescription());
            // %=BENEF_PC=%
            data.addData("BENEF_PC", documentPrincipale.getTextes(7).getTexte(4).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

}
