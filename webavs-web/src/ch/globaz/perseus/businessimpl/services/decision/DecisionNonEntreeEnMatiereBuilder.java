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

public class DecisionNonEntreeEnMatiereBuilder extends AbstractDecisionBuilder {

    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    private static final String CDT_DATE_DEPOT = "{date_depot}";
    private static final String CDT_DATE_LISTE_LETTRE = "{date_liste_lettres}";

    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    private boolean isCopieRI = false;

    public DecisionNonEntreeEnMatiereBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_NON_ENTREE_EN_MATIERE, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_NON_ENTREE_MATIERE_TITRE_MAIL") + " - " + " " + getSujetMail());

        if (getDecisionOO().getDemande().getSimpleDemande().getFromRI()
                && !JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi())) {
            isCopieRI = true;
        }

        // Création de la décision originale
        createDecision(dateDoc, false, isSendToGed);

        // Création de la lettre d'entete, de la copie de décision et du plan de calcul
        if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {

            // Création des lettres d'en-tête et des copie pour le RI, si case à cocher vient du RI
            if (isCopieRI) {
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
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_NON_ENTREE_EN_MATIERE,
                    1, 1);

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionNonEntreeEnMatiere");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionNonEntreeEnMatiereBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du paragraphe 1.
            String dateDepot = getDecisionOO().getDemande().getSimpleDemande().getDateDepot();
            String DateListeLettre = PRStringUtils.replaceString(getDecisionOO().getDemande().getSimpleDemande()
                    .getDateListeNonEntreeEnMatiere(), ";", ", ");

            data.addData("Paragraphe1", PRStringUtils.replaceString(PRStringUtils.replaceString(getBabelContainer()
                    .getTexte(IPFCatalogueTextes.CS_DECISION_NON_ENTREE_EN_MATIERE, 2, 1),
                    DecisionNonEntreeEnMatiereBuilder.CDT_DATE_DEPOT, dateDepot),
                    DecisionNonEntreeEnMatiereBuilder.CDT_DATE_LISTE_LETTRE, DateListeLettre));

            // Insertion de paragraphe 2
            data.addData("Paragraphe2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_NON_ENTREE_EN_MATIERE, 2, 2));

            // Insertion de salutations et du titre du tiers
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_NON_ENTREE_EN_MATIERE, 3, 1),
                    DecisionNonEntreeEnMatiereBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre reclamation
            data.addData("Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_NON_ENTREE_EN_MATIERE, 4, 1));

            // Insertion du texte reclamation
            data.addData("Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_NON_ENTREE_EN_MATIERE, 4, 2));

            // Insertion du titre copie
            data = buildBasDePage(data, true, isCopieRI, false);
            // data.addData("Copie", this.getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 5, 1));

            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged,
                    IPRConstantesExternes.PCF_DECISION_NON_ENTREE_EN_MATIERE);
        } catch (Exception e) {
            throw new DecisionException("DecisionNonEntreeEnMatiereBuilder -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED_SANS_RECTOVERSO));
    }
}
