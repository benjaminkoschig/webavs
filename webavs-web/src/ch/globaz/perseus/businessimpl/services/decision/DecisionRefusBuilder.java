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

public class DecisionRefusBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATEDEMANDE = "{dateDemande}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionRefusBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_REFUS, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_REFUS_TITRE_MAIL") + " - " + " " + getSujetMail());

        // Création de la décision originale
        createDecision(dateDoc, false, isSendToGed);

        // Création de la lettre d'entete, de la copie de décision et du plan de calcul
        if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {

            // Création des lettres d'en-tête et des copie pour le RI, si case à cocher vient du RI
            if (getDecisionOO().getDemande().getSimpleDemande().getFromRI()) {
                if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi())) {
                    // Création de la lettre d'entete pour le RI
                    createLettreEntete(dateDoc, getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi());
                    // Création de la copie de la décision pour le RI
                    createDecision(dateDoc, true, false);
                }
            }

            // Création des lettres d'entete et des copies pour toutes les copies ajoutées dans la JSP

            for (CopieDecision copieDecision : getDecisionOO().getListeCopie()) {
                createLettreEntete(dateDoc, copieDecision.getSimpleCopieDecision().getIdTiers());
                createDecision(dateDoc, true, false);
            }
        }

        allDoc.setMergedDocDestination(getConteneurPubInfos().get(
                AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION_RECTOVERSO));
        return allDoc;

    }

    private void createDecision(String dateDoc, boolean isCopie, boolean ged) throws Exception {

        try {
            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Chargement du titre du document
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 1, 1);

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionRefus");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionRefusBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du paragraphe 1.
            data.addData("Paragraphe1", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 3, 1),
                    DecisionRefusBuilder.CDT_DATEDEMANDE, getDecisionOO().getDemande().getSimpleDemande()
                            .getDateDepot()));

            // Insertion de paragraphe 2
            data.addData("Paragraphe2", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 3, 2));

            // Insertion du paragraphe 3 selon condition
            // Si case à cocher activé le texte doit provenir d'une remarque personnelle de la caisse, sinon le texte
            // provient du catalogue en fonction de la situation du requérant

            if (false == getDecisionOO().getDemande().getSimpleDemande().getRefusForce()) {
                // Si il y a des enfants de moins de 16 et que le requerant est établi dans le canton depuis plus de
                // trois,
                // les 2 phrases s'affiches
                if ((isEtabli() == false) && (hasEnfant() == false)) {// this.isCalculable() == true) {
                    data.addData("Paragraphe3", getBabelContainer()
                            .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 4, 1));

                    data.addData("Paragraphe4", getBabelContainer()
                            .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 4, 2));
                }

                // Affiche le texte relatif au 3 ans d'habitation dans le canton
                if ((isEtabli() == false) && (hasEnfant() == true)) {
                    data.addData("Paragraphe3", getBabelContainer()
                            .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 4, 1));
                }
                // Affiche le texte relatif aux enfants de moins de 16ans
                if ((hasEnfant() == false) && (isEtabli() == true)) {
                    data.addData("Paragraphe3", getBabelContainer()
                            .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 4, 2));
                }
            } else {
                // Si la case de refus forcé est activé, une phrase personnalisée par la caisse s'affiche
                data.addData("Paragraphe3", getDecisionOO().getSimpleDecision().getRemarqueUtilisateur());
            }

            // Insertion du parapgraphe 5
            data.addData("Paragraphe5", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 4, 3));

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

            // Insertion du parapgraphe 6
            data.addData("Paragraphe6", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 4, 4));

            // Insertion de salutations et du titre du tiers
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 5, 1),
                    DecisionRefusBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre reclamation
            data.addData("Reclamation", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 6, 1));

            // Insertion du texte reclamation
            data.addData("Texte_Reclamation", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 6, 2));

            // Insertion du titre copie
            data = buildBasDePage(data, true, false, false);
            // data.addData("Copie", this.getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 5, 1));

            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged, IPRConstantesExternes.PCF_DECISION_REFUS);
        } catch (Exception e) {
            throw new DecisionException("DecisionRefusBuilder -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED_SANS_RECTOVERSO));
    }
}
