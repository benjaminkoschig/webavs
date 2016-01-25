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

public class DecisionSuppressionPersonnelleBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATE_DECISION = "{dateDecision}";
    private static final String CDT_DATE_RUPTURE = "{dateRupture}";
    private static final String CDT_MONTANT_MENSUEL = "{montantMensuel}";

    private static final String CDT_TITRE_TIERS = "{titreTiers}";
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionSuppressionPersonnelleBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_SUPPRESSION_PERSONNELLE_TITRE_MAIL") + " - " + " "
                        + getSujetMail());
        // ***************************************************

        // Création de la décision originale
        createDecision(dateDoc, false, isSendToGed);

        // Création des copies uniquement lorsque la décision est validée
        if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {

            // Création des lettres d'en-tête et des copie pour le RI
            if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi())) {
                // Création de la lettre d'entete pour le RI
                createLettreEntete(dateDoc, getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi());
                // Création de la copie de la décision pour le RI
                createDecision(dateDoc, true, false);
            }

            // Création de la copie de la décision et du plan de calcul pour l'OCC
            createDecision(dateDoc, true, false);

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
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE,
                    1, 1);

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionSuppressionPersonnelle");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionSuppressionPersonnelleBuilder.CDT_TITRE_TIERS, titreTiers));

            // Récupération de la date de décision
            String idDemande = getDecisionOO().getDemande().getSimpleDemande().getIdDemande();
            Demande demande = PerseusServiceLocator.getDemandeService().read(idDemande);
            Demande demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(demande);
            PCFAccordee pcfAncienne = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                    demandePrecedante.getId());

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
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 3, 1),
                    DecisionSuppressionPersonnelleBuilder.CDT_DATE_DECISION, ancienneDecision.getSimpleDecision()
                            .getDateDocument());
            data.addData("Paragraphe1", PRStringUtils.replaceString(para1,
                    DecisionSuppressionPersonnelleBuilder.CDT_MONTANT_MENSUEL, convertCentimes(pcfAncienne
                            .getSimplePCFAccordee().getMontant())));

            // Insertion de paragraphe 2
            // Récupération de la date de fin de prestation
            String dateFin = JadeDateUtil.addDays(getDecisionOO().getDemande().getSimpleDemande().getDateDebut(), -1);

            data.addData("Paragraphe2", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 3, 2),
                    DecisionSuppressionPersonnelleBuilder.CDT_DATE_RUPTURE, dateFin));

            // Insertion du paragraphe 3 selon conditions
            if (false == getDecisionOO().getDemande().getSimpleDemande().getRefusForce()) {
                // Insertion des deux paragraphe si aucune condition n'est respecté
                if (isCalculable() == true) {
                    data.addData("Paragraphe3",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 4, 1));

                    data.addData("Paragraphe4",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 4, 2));
                }
                // Insertion du premier paragraphe si la durée de domicile dans le canton n'est pas suffisante
                if (isEtabli() == false) {
                    data.addData("Paragraphe3",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 4, 1));
                }
                // Insertion du second paragraphe si il n'y a pas d'enfants de moins de 16ans dans le ménage
                if (hasEnfant() == false) {
                    data.addData("Paragraphe3",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 4, 2));
                }
            } else {
                // Si la case de refus forcé est activé, une phrase personnalisée par la caisse s'affiche
                data.addData("Paragraphe3", getDecisionOO().getSimpleDecision().getRemarqueUtilisateur());
            }

            // Insertion de paragraphe 5
            data.addData("Paragraphe5",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 4, 3));

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

            // Insertion du paragraphe 6
            data.addData("Paragraphe6",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 4, 4));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 5, 1),
                    DecisionSuppressionPersonnelleBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre reclamation
            data.addData("Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 6, 1));

            // Insertion du texte reclamation
            data.addData("Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 6, 2));

            // Insertion du titre copie
            data = buildBasDePage(data, true, true, true);

            // ajout du document dans le container
            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged, IPRConstantesExternes.PCF_DECISION_SUPPRESSION);
        } catch (Exception e) {
            throw new DecisionException("DecisionSuppressionPersonnelleBuilder -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED_SANS_RECTOVERSO));
    }
}
