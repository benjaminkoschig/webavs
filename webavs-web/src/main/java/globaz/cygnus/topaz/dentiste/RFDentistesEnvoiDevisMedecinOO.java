/*
 * Créé le 8 novembre 2010
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
public class RFDentistesEnvoiDevisMedecinOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_LETTRE_TYPE_DENTISTE_ENVOI_DEVIS_MEDECIN";

    protected final String DATE = "{date}";

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DEVIS_MEDECIN);
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
            caisseHelper.setTemplateName(RFDentistesEnvoiDevisMedecinOO.FICHIER_MODELE_DOCUMENTS_RFM);

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
        data.addData("idProcess", "RFDentistesEnvoiDevisMedecinOO");

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
            throw new Exception("Erreur : Pas d'adresse tiers (RFDentistesEnvoiDevisMedecinOO.generationLettre())");
        }

    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean isSendToGed) throws Exception {

        setSession(process.getSession());
        setDateSurDocument(process.getDateDocument());
        setIdTiers(process.getIdTiers());
        setIsCopie(isCopie);

        return super.remplir(documentContainer, process, getIdTiers(),
                IPRConstantesExternes.RFM_LETTRE_TYPE_DENTISTE_ENVOIS_DEVIS_AU_MEDECIN_CONSEIL, isSendToGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // %=DOSSIER=%
            data.addData("DOSSIER", documentPrincipale.getTextes(2).getTexte(1).getDescription());
            // %=NSS=%
            data.addData("NSS", documentPrincipale.getTextes(2).getTexte(2).getDescription());
            // %=NOTE=%
            data.addData("NOTE", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=DENTISTE=%
            data.addData("DENTISTE", documentPrincipale.getTextes(3).getTexte(2).getDescription());
            // %=DATE_HONORAIRE=%
            data.addData("DATE_HONORAIRE", documentPrincipale.getTextes(3).getTexte(3).getDescription());
            // %=MONTANT=%
            data.addData("MONTANT", documentPrincipale.getTextes(3).getTexte(4).getDescription());
            // %=TITRE=%
            data.addData("TITRE", PRStringUtils.replaceString(documentPrincipale.getTextes(4).getTexte(1)
                    .getDescription(), TITRE, sexe.equals("516001") ? "Monsieur" : "Madame"));
            // %=NOTE_HONORAIRE=%
            data.addData("NOTE_HONORAIRE", documentPrincipale.getTextes(4).getTexte(2).getDescription());
            // %=QUESTIONNAIRE=%
            data.addData("QUESTIONNAIRE", documentPrincipale.getTextes(4).getTexte(3).getDescription());
            // %=ENVOI=%
            data.addData("ENVOI", documentPrincipale.getTextes(4).getTexte(4).getDescription());
            // %=SALUTATIONS=%
            data.addData("SALUTATIONS", PRStringUtils.replaceString(documentPrincipale.getTextes(4).getTexte(5)
                    .getDescription(), TITRE, sexe.equals("516001") ? "Monsieur" : "Madame"));
            // %=REPONSE=%
            data.addData("REPONSE", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=EXAMEN_QUESTIONNAIRE=%
            data.addData("EXAMEN_QUESTIONNAIRE", documentPrincipale.getTextes(5).getTexte(2).getDescription());
            // %=INFORME=%
            data.addData("INFORME", documentPrincipale.getTextes(5).getTexte(3).getDescription());
            // %=PAS_CONTACTE=%
            data.addData("PAS_CONTACTE", documentPrincipale.getTextes(6).getTexte(1).getDescription());
            // %=CONTACTE_DATE=%
            data.addData("CONTACTE_DATE", documentPrincipale.getTextes(6).getTexte(2).getDescription());
            // %=CONTACTE_SOLUTION=%
            data.addData("CONTACTE_SOLUTION", documentPrincipale.getTextes(6).getTexte(3).getDescription());
            // %=NOTE_CORRECTE=%
            data.addData("NOTE_CORRECTE", documentPrincipale.getTextes(7).getTexte(1).getDescription());
            // %=RAMENE=%
            data.addData("RAMENE", documentPrincipale.getTextes(7).getTexte(2).getDescription());
            // %=FR=%
            data.addData("FR", documentPrincipale.getTextes(7).getTexte(3).getDescription());
            // %=TRAITEMENT=%
            data.addData("TRAITEMENT", documentPrincipale.getTextes(7).getTexte(4).getDescription());
            // %=SEA=%
            data.addData("SEA", documentPrincipale.getTextes(7).getTexte(5).getDescription());
            // %=LOI=%
            data.addData("LOI", documentPrincipale.getTextes(7).getTexte(6).getDescription());
            // %=SA=%
            data.addData("SA", documentPrincipale.getTextes(7).getTexte(7).getDescription());
            // %=RESTE_MONTANT=%
            data.addData("RESTE_MONTANT", documentPrincipale.getTextes(7).getTexte(8).getDescription());
            // %=DETAILS=%
            data.addData("DETAILS", documentPrincipale.getTextes(7).getTexte(9).getDescription());
            // %=ANNEXE=%
            data.addData("ANNEXE", documentPrincipale.getTextes(8).getTexte(1).getDescription());
            // %=MENTIONNE=%
            data.addData("MENTIONNE", documentPrincipale.getTextes(8).getTexte(2).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

}
