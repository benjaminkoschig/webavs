/*
 * Cr?? le 8 novembre 2010
 */
package globaz.cygnus.topaz.dentiste;

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
public class RFDentistesDemandeDocumentsManquantsQMDOO extends RFAbstractDocumentOO {

    protected final String DATE = "{date}";

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_PRECISIONS_DENTISTE);
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

            // Insertion de l'ent?te
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);

            // Chargement de l'en-t?te de la caisse
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
        data.addData("idProcess", "RFDentistesDemandeDocumentsManquantsQMDOO");

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

            // Chargement des catalogues de texte
            chargerCatalogueTexte();

            // Cr?ation des param?tres pour l'en-t?te
            chargerDonneesEnTete();

            // Remplissage des champs du document
            remplirDocument();

            // Ajout des copies
            // ajoutCopiesAnnexes();
            setDocumentData(data);

        } else {
            throw new Exception(
                    "Erreur : Pas d'adresse tiers (RFDentistesDemandeDocumentsManquantsQMDOO.generationLettre())");
        }

    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean isSendToGed) throws Exception {

        setSession(process.getSession());
        setDateSurDocument(process.getDateDocument());
        setIdTiers(process.getIdTiers());
        setIsCopie(isCopie);

        return super.remplir(documentContainer, process, getIdTiers(),
                IPRConstantesExternes.RFM_LETTRE_TYPE_DENTISTE_DEMANDE_DOCUMENTS_MANQUANTS_ET_QMD_AU_DENTISTE,
                isSendToGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // %=OBJET=%
            data.addData("OBJET", documentPrincipale.getTextes(1).getTexte(1).getDescription());
            // %=DOSSIER=%
            data.addData("DOSSIER", documentPrincipale.getTextes(1).getTexte(2).getDescription());
            // %=TITRE=%
            data.addData("TITRE", PRStringUtils.replaceString(documentPrincipale.getTextes(2).getTexte(1)
                    .getDescription(), TITRE, sexe.equals("516001") ? "Monsieur" : "Madame"));
            // %=RECU=%
            data.addData("RECU", documentPrincipale.getTextes(2).getTexte(2).getDescription());
            // %=MANQUANT=%
            data.addData("MANQUANT", documentPrincipale.getTextes(2).getTexte(3).getDescription());
            // %=QUESTIONNAIRE=%
            data.addData("QUESTIONNAIRE", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=RADIO=%
            data.addData("RADIO", documentPrincipale.getTextes(3).getTexte(2).getDescription());
            // %=LABO=%
            data.addData("LABO", documentPrincipale.getTextes(3).getTexte(3).getDescription());
            // %=MOTIFS=%
            data.addData("MOTIFS", documentPrincipale.getTextes(3).getTexte(4).getDescription());
            // %=DIAGNOSTIQUE=%
            data.addData("DIAGNOSTIQUE", documentPrincipale.getTextes(3).getTexte(5).getDescription());
            // %=MODELE=%
            data.addData("MODELE", documentPrincipale.getTextes(3).getTexte(6).getDescription());
            // %=GRAVITE=%
            data.addData("GRAVITE", documentPrincipale.getTextes(3).getTexte(7).getDescription());
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(4).getTexte(1)
                    .getDescription(), TITRE, sexe.equals("516001") ? "Monsieur" : "Madame"));
            // %=ANNEXE=%
            data.addData("ANNEXE", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=QUESTIONS=%
            data.addData("FORM", documentPrincipale.getTextes(5).getTexte(2).getDescription());
            // %=LISTE=%
            data.addData("FORM", documentPrincipale.getTextes(5).getTexte(3).getDescription());
            // %=COPIE=%
            data.addData("COPIE", documentPrincipale.getTextes(5).getTexte(4).getDescription());
            // %=BENEF_PC=%
            data.addData("BENEF_PC", documentPrincipale.getTextes(5).getTexte(5).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

}
