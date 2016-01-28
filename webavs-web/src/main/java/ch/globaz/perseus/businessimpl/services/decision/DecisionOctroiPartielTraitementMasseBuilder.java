package ch.globaz.perseus.businessimpl.services.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.process.decision.PFImpressionDecisionTraitementMasseProcess;
import globaz.prestation.tools.PRStringUtils;
import java.util.HashMap;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionOctroiPartielTraitementMasseBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATEDEMANDE = "{dateDemande}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    private HashMap<String, JadePrintDocumentContainer> hashMapAllDoc = null;

    public String idProcess = "";

    public DecisionOctroiPartielTraitementMasseBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    public HashMap<String, JadePrintDocumentContainer> build(String decisionId, String mailGest, String dateDoc,
            boolean isSendToGed, HashMap<String, JadePrintDocumentContainer> hashMap) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        hashMapAllDoc = hashMap;

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_OCTROI_PARTIEL_TITRE_MAIL") + " - " + " " + getSujetMail());
        // ***************************************************

        // Création de la décision originale
        if (CSCaisse.CCVD.getCodeSystem().equals(getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
            createDecision(dateDoc, false, isSendToGed, PFImpressionDecisionTraitementMasseProcess.CCVD_Benef);
        } else {
            createDecision(dateDoc, false, isSendToGed, PFImpressionDecisionTraitementMasseProcess.AGLAU_Benef);
        }

        // Création des copies uniquement lorsque la décision est validée
        if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {

            // Création des lettres d'en-tête et des copie pour le RI, si le requérant vient du RI
            if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi())) {
                if (getDecisionOO().getDemande().getSimpleDemande().getFromRI()) {
                    if (CSCaisse.CCVD.getCodeSystem().equals(
                            getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                        // Création de la lettre d'entete pour le RI
                        createLettreEntete(dateDoc, getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi(),
                                PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_RI);
                        // Création de la copie de la décision pour le RI
                        createDecision(dateDoc, true, false, PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_RI);
                    } else {
                        // Création de la lettre d'entete pour le RI
                        createLettreEntete(dateDoc, getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi(),
                                PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_RI);
                        // Création de la copie de la décision pour le RI
                        createDecision(dateDoc, true, false, PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_RI);
                    }

                }
            }

            // Création des lettres d'entete et des copies pour toutes les copies ajoutées dans la JSP
            if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {
                for (CopieDecision copieDecision : getDecisionOO().getListeCopie()) {
                    if (CSCaisse.CCVD.getCodeSystem().equals(
                            getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                        createLettreEntete(dateDoc, copieDecision.getSimpleCopieDecision().getIdTiers(),
                                PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_SUPP);
                        createDecision(dateDoc, true, false, PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_SUPP);
                    } else {
                        createLettreEntete(dateDoc, copieDecision.getSimpleCopieDecision().getIdTiers(),
                                PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_SUPP);
                        createDecision(dateDoc, true, false,
                                PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_SUPP);
                    }
                }
            }
        }

        return hashMapAllDoc;

    }

    private void createDecision(String dateDoc, boolean isCopie, boolean ged, String cleHashMapAllDoc) throws Exception {
        try {
            /**
             * ********** GENERATION DOCUMENT**************
             */
            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Chargement du titre du document
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 1, 1);

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionOctroiPartiel");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionOctroiPartielTraitementMasseBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du paragraphe et de la date.
            data.addData("Paragraphe1", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 3, 1),
                    DecisionOctroiPartielTraitementMasseBuilder.CDT_DATEDEMANDE, getDecisionOO().getDemande()
                            .getSimpleDemande().getDateDepot()));

            // Insertion de paragraphes
            data.addData("Paragraphe2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 3, 2));
            data.addData("Paragraphe3",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 3, 3));
            data.addData("Paragraphe4",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 3, 4));

            // Insertion du parapgraphe 4_5
            data.addData("Paragraphe4_5",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 3, 7));

            // ############################## INSERTION DU TABLEAUX SELON CONDITION ####################################
            // #########################################################################################################

            // Insertion du tableau si retro-actif présent ou si prestation existante
            if (hasTableau()) {
                data.addData("isTableauInclude", "TRUE");
                // data.add(this.buildTableau());
                data = buildTableau(data);
            } else {
                data.addData("isTableauInclude", "FALSE");
            }
            // #########################################################################################################

            data.addData("Paragraphe5",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 3, 5));

            // Insertion de salutations et du titre du tiers
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 3, 6),
                    DecisionOctroiPartielTraitementMasseBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre reclamation
            data.addData("Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 4, 1));

            // Insertion du texte reclamation
            data.addData("Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 4, 2));

            // Insertion du titre copie
            data = buildBasDePage(data, true, false, false);

            // Insertion du plan de calcul
            data.addData("isPlanDeCalculInclude", "TRUE");
            data = createPlanDeCalcul(dateDoc, isCopie, data);

            /**
             * ******* AJOUT DU DOCUMENT DANS LE CONTAINER******
             */

            hashMapAllDoc.put(
                    cleHashMapAllDoc,
                    dataAndPubInfo(hashMapAllDoc.get(cleHashMapAllDoc), data, isCopie, ged,
                            IPRConstantesExternes.PCF_DECISION_OCTROI_PARTIEL));

        } catch (Exception e) {
            throw new DecisionException("DecisionOctroiPartielBuilder -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers, String cleHasHMapAllDoc) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        JadePrintDocumentContainer allDoc = hashMapAllDoc.get(cleHasHMapAllDoc);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED));
        hashMapAllDoc.put(cleHasHMapAllDoc, allDoc);
    }
}
