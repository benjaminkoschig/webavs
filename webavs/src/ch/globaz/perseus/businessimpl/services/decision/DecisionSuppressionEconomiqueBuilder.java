package ch.globaz.perseus.businessimpl.services.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionSuppressionEconomiqueBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATE_DECISION = "{dateDecision}";
    private static final String CDT_MONTANT_MENSUEL = "{montantMensuel}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionSuppressionEconomiqueBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_SUPPRESSION_ECONOMIQUE_TITRE_MAIL") + " - " + " "
                        + getSujetMail());

        // Création de la décision originale
        createDecision(dateDoc, false, isSendToGed);

        // Création des copies uniquement lorsque la décision est validée
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

            // Création de la copie de la décision et du plan de calcul pour l'OCC
            createDecision(dateDoc, true, false);

            // Création des lettres d'entete et des copies pour toutes les copies ajoutées dans la JSP
            for (CopieDecision copieDecision : getDecisionOO().getListeCopie()) {
                createLettreEntete(dateDoc, copieDecision.getSimpleCopieDecision().getIdTiers());
                createDecision(dateDoc, true, false);
            }
        }
        allDoc.setMergedDocDestination(getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION));
        return allDoc;

    }

    private void createDecision(String dateDoc, boolean isCopie, boolean ged) throws Exception {
        try {
            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Chargement du titre du document
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE,
                    1, 1);
            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionSuppressionEconomique");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionSuppressionEconomiqueBuilder.CDT_TITRE_TIERS, titreTiers));

            // Récupération de la date et du montant de l'ancienne décision
            String idDemande = getDecisionOO().getDemande().getSimpleDemande().getIdDemande();
            Demande demande = PerseusServiceLocator.getDemandeService().read(idDemande);
            Demande demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(demande);
            PCFAccordee pcfAncienne = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                    demandePrecedante.getId());
            String montantAncienneDecision = pcfAncienne.getSimplePCFAccordee().getMontant();
            // Récupération de l'ancienne décision
            DecisionSearchModel dsm = new DecisionSearchModel();
            dsm.setForIdDemande(demandePrecedante.getSimpleDemande().getIdDemande());
            dsm = PerseusServiceLocator.getDecisionService().search(dsm);
            Decision ancienneDecision = null;
            for (JadeAbstractModel model : dsm.getSearchResults()) {
                Decision ad = (Decision) model;
                if (!CSTypeDecision.PROJET.getCodeSystem().equals(ad.getSimpleDecision().getCsTypeDecision())
                        || !CSTypeDecision.SUPPRESSION.getCodeSystem().equals(
                                ad.getSimpleDecision().getCsTypeDecision())) {
                    ancienneDecision = ad;
                    break;
                }
            }

            // Insertion du paragraphe 1
            String para1 = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 3, 1),
                    DecisionSuppressionEconomiqueBuilder.CDT_DATE_DECISION, ancienneDecision.getSimpleDecision()
                            .getDateDocument());
            data.addData("Paragraphe1", PRStringUtils.replaceString(para1,
                    DecisionSuppressionEconomiqueBuilder.CDT_MONTANT_MENSUEL, convertCentimes(montantAncienneDecision)));

            // Récupération de la date de fin des prestations
            // Si l'ancienne demande à été validé, récupération de la date de fin
            String dateFinPrestation = "";
            if (CSEtatDemande.VALIDE.getCodeSystem().equals(
                    pcfAncienne.getDemande().getSimpleDemande().getCsEtatDemande())) {
                dateFinPrestation = pcfAncienne.getDemande().getSimpleDemande().getDateFin();
            }// Sinon, récupération du dernier jour du mois de la nouvelle demande
            else {
                dateFinPrestation = JadeDateUtil.addDays(
                        JadeDateUtil.addMonths("01."
                                + getDecisionOO().getDemande().getSimpleDemande().getDateDebut().substring(3), 1), -1);
            }

            // Insertion du texte libre, si il n'est pas vide
            if (!JadeStringUtil.isEmpty(getDecisionOO().getSimpleDecision().getRemarqueUtilisateur())) {
                data.addData("TexteLibre", getDecisionOO().getSimpleDecision().getRemarqueUtilisateur());
            }

            // Insertion de paragraphe 2
            data.addData("Paragraphe2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 3, 2));

            // Insertion de paragraphe 3 selon conditions

            data.addData("Paragraphe3",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 3, 3));

            // Insertion de paragraphe 4
            data.addData("Paragraphe4",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 3, 4));

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

            // Insertion de paragraphe 4_5
            data.addData("Paragraphe4_5",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 3, 7));

            // Insertion du paragraphe 5
            data.addData("Paragraphe5",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 3, 5));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 3, 6),
                    DecisionSuppressionEconomiqueBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre reclamation
            data.addData("Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 4, 1));

            // Insertion du texte reclamation
            data.addData("Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 4, 2));

            // Insertion du titre copie
            data = buildBasDePage(data, true, false, true);

            // Insertion du plan de calcul
            data.addData("isPlanDeCalculInclude", "TRUE");
            data = createPlanDeCalcul(dateDoc, isCopie, data);

            /**
             * ******* AJOUT DU DOCUMENT DANS LE CONTAINER******
             */
            // ajout du document dans le container

            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged,
                    IPRConstantesExternes.PCF_DECISION_OCTROI_PARTIEL_MODIF_ECONOMIQUE);
        } catch (Exception e) {
            throw new DecisionException("DecisionSuppressionEconomiqueBuilder -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED));
    }
}
