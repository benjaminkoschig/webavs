package ch.globaz.perseus.businessimpl.services.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSChoixDecision;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionRenonciationBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATE_PROJET = "{dateProjet}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionRenonciationBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_RENONCIATION, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_RENONCIATION_TITRE_MAIL") + " - " + " " + getSujetMail());
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
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_RENONCIATION, 1, 1);

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionRenonciation");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionRenonciationBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du paragraphe 1 selon condition
            // Récupération du projet lié à la décision (Document: 5)
            DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
            decisionSearchModel.setForIdDemande(getDecisionOO().getDemande().getSimpleDemande().getId());
            decisionSearchModel.setForNumeroDecision(getDecisionOO().getSimpleDecision().getNumeroDecision());
            decisionSearchModel.setForCsTypeDecision(CSTypeDecision.PROJET.getCodeSystem());
            decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());

            decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
            Decision projet = (Decision) decisionSearchModel.getSearchResults()[0];
            if (null != projet) {

                if (CSChoixDecision.NEGATIVE_REPONSE.getCodeSystem().equals(projet.getSimpleDecision().getCsChoix())) {
                    data.addData("Paragraphe1",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_RENONCIATION, 3, 1));
                } else if (CSChoixDecision.SANS_REPONSE.getCodeSystem().equals(projet.getSimpleDecision().getCsChoix())) {
                    data.addData("Paragraphe1", PRStringUtils.replaceString(
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_RENONCIATION, 3, 2),
                            DecisionRenonciationBuilder.CDT_DATE_PROJET, projet.getSimpleDecision().getDateDocument()));
                }
            }

            // Insertion de paragraphe 2
            data.addData("Paragraphe2", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_RENONCIATION, 3, 3));
            // Insertion de paragraphe 3
            data.addData("Paragraphe3", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_RENONCIATION, 3, 4));
            // Insertion de paragraphe 4
            data.addData("Paragraphe4", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_RENONCIATION, 3, 5),
                    DecisionRenonciationBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre reclamation
            data.addData("Reclamation", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_RENONCIATION, 4, 1));

            // Insertion du texte reclamation
            data.addData("Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_RENONCIATION, 4, 2));

            // Insertion du titre copie
            data = buildBasDePage(data, false, true, false);

            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged, IPRConstantesExternes.PCF_DECISION_RENONCIATION);
        } catch (Exception e) {
            throw new DecisionException("DecisionRenonciationBuilder -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED_SANS_RECTOVERSO));
    }
}
