package ch.globaz.perseus.businessimpl.services.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionOctroiPartielBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATEDEMANDE = "{dateDemande}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionOctroiPartielBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        // this.loadEntity(decisionId);
        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_OCTROI_PARTIEL_TITRE_MAIL") + " - " + " " + getSujetMail());
        // ***************************************************

        // Création de la décision originale
        createDecision(dateDoc, false, isSendToGed);

        // Création des copies uniquement lorsque la décision est validée
        if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {

            // Création des lettres d'en-tête et des copie pour le RI, si le requérant vient du RI
            if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi())) {
                if (getDecisionOO().getDemande().getSimpleDemande().getFromRI()) {
                    // Création de la lettre d'entete pour le RI
                    createLettreEntete(dateDoc, getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi());
                    // Création de la copie de la décision pour le RI
                    createDecision(dateDoc, true, false);
                }
            }

            // Création des lettres d'entete et des copies pour toutes les copies ajoutées dans la JSP
            if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {
                for (CopieDecision copieDecision : getDecisionOO().getListeCopie()) {
                    createLettreEntete(dateDoc, copieDecision.getSimpleCopieDecision().getIdTiers());
                    createDecision(dateDoc, true, false);
                }
            }
        }

        allDoc.setMergedDocDestination(getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION));
        return allDoc;

    }

    private void createDecision(String dateDoc, boolean isCopie, boolean ged) throws Exception {
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
                    DecisionOctroiPartielBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du paragraphe et de la date.
            data.addData("Paragraphe1", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_OCTROI_PARTIEL, 3, 1),
                    DecisionOctroiPartielBuilder.CDT_DATEDEMANDE, getDecisionOO().getDemande().getSimpleDemande()
                            .getDateDepot()));

            data.addData("TexteLibre", getDecisionOO().getSimpleDecision().getRemarqueUtilisateur());

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
                    DecisionOctroiPartielBuilder.CDT_TITRE_TIERS, titreTiers));

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
            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged, IPRConstantesExternes.PCF_DECISION_OCTROI_PARTIEL);
        } catch (Exception e) {
            throw new DecisionException("DecisionOctroiPartielBuilder -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED));
    }
}
