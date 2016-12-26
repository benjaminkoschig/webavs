/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.topaz.litElectrique;

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
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * author fha
 */
public class RFBonLivraisonOO extends RFAbstractDocumentOO {

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_BON_LIVRAISON);
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
            caisseHelper.setTemplateName("");

            // Insertion de l'entête
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);

            // TODO: A voir, utiliser le fichier de propriété
            data.addData("idEntete", "RFM");

        } catch (Exception e) {
            throw new Exception(e.toString());
        }

    }

    @Override
    public void generationLettre(RFCopieDecisionsValidationData... copie) throws Exception {

        // Chargement informations principales
        data = new DocumentData();
        data.addData("idProcess", "RFBonLivraisonOO");

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
            throw new Exception("Erreur : Pas d'adresse tiers (RFBonLivraisonOO.generationLettre())");
        }

    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean isSendToGed) throws Exception {

        setSession(process.getSession());
        setDateSurDocument(process.getDateDocument());
        setIdTiers(process.getIdTiers());
        setIsCopie(isCopie);

        return super.remplir(documentContainer, process, getIdTiers(),
                IPRConstantesExternes.RFM_LETTRE_TYPE_LIT_ELECTRIQUE_BON_DE_LIVRAISON, isSendToGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // %=PRESTATION=%
            data.addData("PRESTATION", documentPrincipale.getTextes(1).getTexte(1).getDescription());
            // %=NUMERO_ASSURE=%
            data.addData("NUMERO_ASSURE", documentPrincipale.getTextes(1).getTexte(2).getDescription());
            // %=BON=%
            data.addData("BON", documentPrincipale.getTextes(2).getTexte(1).getDescription());
            // %=ASSURE=%
            data.addData("ASSURE", documentPrincipale.getTextes(2).getTexte(2).getDescription());
            // %=INFO_ASSURE=%
            data.addData("INFO_ASSURE", documentPrincipale.getTextes(2).getTexte(3).getDescription());
            // %=LOCATION_LIT=%
            data.addData("LOCATION_LIT", documentPrincipale.getTextes(2).getTexte(4).getDescription());
            // %=DATE_DEMANDE=%
            data.addData("DATE_DEMANDE", documentPrincipale.getTextes(2).getTexte(5).getDescription());
            // %=PRISE_EN_CHARGE=%
            data.addData("PRISE_EN_CHARGE", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=CENTRE=%
            data.addData("CENTRE", documentPrincipale.getTextes(4).getTexte(1).getDescription());
            // %=DATE=%
            data.addData("DATE", documentPrincipale.getTextes(4).getTexte(2).getDescription());
            // %=OFFICE=%
            data.addData("OFFICE", documentPrincipale.getTextes(4).getTexte(3).getDescription());
            // %=REMARQUE=%
            data.addData("REMARQUE", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=CONTACT=%
            data.addData("CONTACT", documentPrincipale.getTextes(5).getTexte(2).getDescription());
            // %=ATTESTATION=%
            data.addData("ATTESTATION", documentPrincipale.getTextes(6).getTexte(1).getDescription());
            // %=ASSURE_ATTESTE=%
            data.addData("ASSURE_ATTESTE", documentPrincipale.getTextes(6).getTexte(2).getDescription());
            // %=SIGNATURE=%
            data.addData("SIGNATURE", documentPrincipale.getTextes(7).getTexte(1).getDescription());
            // %=ADRESSE_REPRESENTANT=%
            data.addData("ADRESSE_REPRESENTANT", documentPrincipale.getTextes(7).getTexte(2).getDescription());
            // %=CONDITIONS_REMISES=%
            data.addData("CONDITIONS_REMISES", documentPrincipale.getTextes(8).getTexte(1).getDescription());
            // 1.%=CONDITION1=%
            data.addData("CONDITION1", documentPrincipale.getTextes(8).getTexte(2).getDescription());
            // 2.%=CONDITION2=%
            data.addData("CONDITION2", documentPrincipale.getTextes(8).getTexte(3).getDescription());
            // 3.%=CONDITION3=%
            data.addData("CONDITION3", documentPrincipale.getTextes(8).getTexte(4).getDescription());
            // 4.%=CONDITION4=%
            data.addData("CONDITION4", documentPrincipale.getTextes(8).getTexte(5).getDescription());
            // 5.%=CONDITION5=%
            data.addData("CONDITION5", documentPrincipale.getTextes(8).getTexte(6).getDescription());
            // 6.%=CONDITION6=%
            data.addData("CONDITION6", documentPrincipale.getTextes(8).getTexte(7).getDescription());
            // 7.%=CONDITION7=%
            data.addData("CONDITION7", documentPrincipale.getTextes(8).getTexte(8).getDescription());
            // 8.%=CONDITION8=%
            data.addData("CONDITION8", documentPrincipale.getTextes(8).getTexte(9).getDescription());
            // 7.%=COPIE_CENTRE=%
            data.addData("COPIE_CENTRE", documentPrincipale.getTextes(9).getTexte(1).getDescription());
            // 8.%=COPIE_PC=%
            data.addData("COPIE_PC", documentPrincipale.getTextes(9).getTexte(2).getDescription());
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

}
