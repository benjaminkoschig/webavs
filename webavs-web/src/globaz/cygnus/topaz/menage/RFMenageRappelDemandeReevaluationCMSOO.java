/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.topaz.menage;

/*
 * author fha
 */
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

public class RFMenageRappelDemandeReevaluationCMSOO extends RFAbstractDocumentOO {

    protected final String ASSURE = "{assure}";
    protected final String DATE = "{date}";

    private void ajoutCopiesAnnexes() throws Exception {

    }

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_RAPPEL_DEMANDE_EVALUATION_BESOINS_CMS);
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

            // Ajout du NSS
            crBean.setNoAvs(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName("");

            // Insertion de l'entête
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);

            data.addData("idEntete", "HEADER_CAISSE");

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    @Override
    public void generationLettre(RFCopieDecisionsValidationData... copie) throws Exception {

        // Chargement informations principales
        data = new DocumentData();

        // Chargement template
        data.addData("idProcess", "RFMenageRappelDemandeReevaluationCMSOO");

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
            adresse = PRTiersHelper.getAdresseDomicileFormatee(session, idTiers).toString();

            // Chargement des catalogues de texte
            chargerCatalogueTexte();

            // Création des paramètres pour l'en-tête
            chargerDonneesEnTete();

            // Remplissage des champs du document
            remplirDocument();

            // Ajout des copies
            ajoutCopiesAnnexes();

            setDocumentData(data);

        } else {
            throw new Exception(
                    "Erreur : Pas d'adresse tiers (RFMenageRappelDemandeReevaluationCMSOO.generationLettre())");
        }

    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean isSendToGed) throws Exception {

        setSession(process.getSession());
        setDateSurDocument(process.getDateDocument());
        setIdTiers(process.getIdTiers());
        setIsCopie(isCopie);

        return super
                .remplir(
                        documentContainer,
                        process,
                        getIdTiers(),
                        IPRConstantesExternes.RFM_LETTRE_TYPE_AIDE_AU_MENAGE_PRIVE_RAPPEL_DEMANDE_EVALUATION_DES_BESOINS_AU_CMS,
                        isSendToGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // %=RAPPEL=%
            data.addData("RAPPEL", documentPrincipale.getTextes(2).getTexte(1).getDescription());
            // %=OBJET=%
            data.addData("OBJET", PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(1)
                    .getDescription(), ASSURE, ""));
            // %=TITRE=%
            data.addData("TITRE", PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(1)
                    .getDescription(), TITRE, sexe.equals("516001") ? "Monsieur" : "Madame"));
            // %=COURRIER=%
            data.addData("COURRIER",
                    PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(2).getDescription(), DATE, ""));
            // %=DETERMINATION=%
            data.addData("DETERMINATION", documentPrincipale.getTextes(3).getTexte(3).getDescription());
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(4)
                    .getDescription(), TITRE, sexe.equals("516001") ? "Monsieur" : "Madame"));
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
