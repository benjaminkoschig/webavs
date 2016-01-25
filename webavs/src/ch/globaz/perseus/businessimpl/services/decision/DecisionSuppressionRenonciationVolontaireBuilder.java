package ch.globaz.perseus.businessimpl.services.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionSuppressionRenonciationVolontaireBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATE_FIN = "{dateFin}";
    private static final String CDT_DATE_LETTRE = "{dateLettre}";
    private static final String CDT_MONTANT_PRESTATION = "{montantPrestation}";

    private static final String CDT_TITRE_TIERS = "{titreTiers}";
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionSuppressionRenonciationVolontaireBuilder() {

    }

    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        // this.loadEntity(decisionId);
        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE_MAIL") + " - " + " "
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
        allDoc.setMergedDocDestination(getConteneurPubInfos().get(
                AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION_RECTOVERSO));
        return allDoc;

    }

    private void createDecision(String dateDoc, boolean isCopie, boolean ged) throws Exception {
        try {
            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Chargement du titre du document
            String titreDocument = getBabelContainer().getTexte(
                    IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE, 1, 1);

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionSuppressionRenonciationVolontaire");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionSuppressionRenonciationVolontaireBuilder.CDT_TITRE_TIERS, titreTiers));

            // Récupération des dates et du montant
            // Date de la lettre
            String dateLettre = getDecisionOO().getSimpleDecision().getDateSuppression();
            String dateFin = "";
            if (JadeStringUtil.isEmpty(getDecisionOO().getDemande().getSimpleDemande().getDateFin())) {
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            } else {
                dateFin = getDecisionOO().getDemande().getSimpleDemande().getDateFin();
            }

            // Insertion du paragraphe 1
            String para1 = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE, 3,
                            1), DecisionSuppressionRenonciationVolontaireBuilder.CDT_DATE_LETTRE, dateLettre);
            data.addData("Paragraphe1", PRStringUtils.replaceString(para1,
                    DecisionSuppressionRenonciationVolontaireBuilder.CDT_MONTANT_PRESTATION,
                    convertCentimes(getDecisionOO().getPcfAccordee().getMontant())));

            // Insertion de paragraphe 2
            data.addData("Paragraphe2", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE, 3,
                            2),
                    DecisionSuppressionRenonciationVolontaireBuilder.CDT_DATE_FIN,
                    PRDateFormater.format_MMMYYYY(
                            new JADate(dateFin),
                            getBabelContainer().getCodeIsoLangue(
                                    IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE))));

            // Insertion du paragraphe 3 selon conditions
            data.addData(
                    "Paragraphe3",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE, 3,
                            3));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE, 3,
                            4), DecisionSuppressionRenonciationVolontaireBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre reclamation
            data.addData(
                    "Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE, 4,
                            1));

            // Insertion du texte reclamation
            data.addData(
                    "Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_RENONCIATION_VOLONTAIRE, 4,
                            2));

            // Insertion du titre copie
            data = buildBasDePage(data, true, false, true);

            /**
             * ******* AJOUT DU DOCUMENT DANS LE CONTAINER******
             */
            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged,
                    IPRConstantesExternes.PCF_DECISION_SUPPRESSION_VOLONTAIRE);
        } catch (Exception e) {
            throw new DecisionException("DecisionSuppressionRenonciationVolontaireBuilder -  NSS : "
                    + this.getNssNomTiers() + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED_SANS_RECTOVERSO));
    }
}
