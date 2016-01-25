package ch.globaz.perseus.businessimpl.services.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
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
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionSuiteAuProjetBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATE_DEBUT_PROJET = "{dateDebutProjet}";
    private static final String CDT_DATE_FIN_PROJET = "{dateFinProjet}";
    private static final String CDT_MONTANT_IMPOT_SOURCE = "{montantImpotSource}";
    private static final String CDT_MONTANT_PRESTATION_MENSUELLE = "{montantPrestationMensuelle}";
    private static final String CDT_NUM_COMPTE = "{numCompte}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionSuiteAuProjetBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_SUITE_AU_PROJET_TITRE_MAIL") + " - " + " " + getSujetMail());

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
        allDoc.setMergedDocDestination(getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION));
        return allDoc;
    }

    private void createDecision(String dateDoc, boolean isCopie, boolean ged) throws Exception {
        try {
            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Chargement du titre du document
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 1, 1);
            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionSuiteAuProjet");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", titreTiers);

            // Insertion du premier paragraphe
            DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
            decisionSearchModel.setForIdDemande(getDecisionOO().getDemande().getSimpleDemande().getIdDemande());
            decisionSearchModel.setForNumeroDecision(getDecisionOO().getSimpleDecision().getNumeroDecision());
            decisionSearchModel.setForCsTypeDecision(CSTypeDecision.PROJET.getCodeSystem());
            decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
            decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
            Decision projetDecision = (Decision) decisionSearchModel.getSearchResults()[0];

            String dateDebutProjet = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 3, 1),
                    DecisionSuiteAuProjetBuilder.CDT_DATE_DEBUT_PROJET, projetDecision.getSimpleDecision()
                            .getDateDocument());
            data.addData("Paragraphe1", PRStringUtils
                    .replaceString(dateDebutProjet, DecisionSuiteAuProjetBuilder.CDT_DATE_FIN_PROJET, projetDecision
                            .getSimpleDecision().getDateChoix()));

            // Insertion du deuxieme paragraphe
            data.addData("Paragraphe2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 3, 2));

            // ######Insertion des paragraphes dans l'encadré ######
            // Insertion de la periode avec date de fin si la demande contient une date de fin
            if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getDateFin())) {
                // Texte periode
                data.addData("PeriodeCalcul",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 4, 1));
                // Date debut de periode
                data.addData("DatePeriode", getDecisionOO().getDemande().getSimpleDemande().getDateDebut());
                // Ajout de la suite de la remarque si date de fin
                data.addData("RemarqueSuite",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 4, 3));
                // Date fin de periode
                data.addData("DateFinPeriode", getDecisionOO().getDemande().getSimpleDemande().getDateFin());
            } else {
                // Texte periode
                data.addData("PeriodeCalcul",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 4, 1));
                // Date debut de periode
                data.addData("DatePeriode", getDecisionOO().getDemande().getSimpleDemande().getDateDebut());
            }

            // Insertion du texte avec impot à la source si présent.
            String montantRetenue = null;
            montantRetenue = getRetenueImpotSource(getDecisionOO().getPcfAccordee());
            if (!JadeStringUtil.isEmpty(montantRetenue)) {
                // Texte montantPrestation
                String texteMontantPrestation = PRStringUtils.replaceString(
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 4, 6),
                        DecisionSuiteAuProjetBuilder.CDT_MONTANT_PRESTATION_MENSUELLE, convertCentimes(getDecisionOO()
                                .getPcfAccordee().getMontant()));
                data.addData("TexteMontantPrestation", PRStringUtils.replaceString(texteMontantPrestation,
                        DecisionSuiteAuProjetBuilder.CDT_MONTANT_IMPOT_SOURCE, convertCentimes(montantRetenue)));

                // Montant de la prestation
                Float montantPrestation = Float.valueOf(getDecisionOO().getPcfAccordee().getMontant());
                Float montantImpotSource = Float.valueOf(montantRetenue);
                Float totalMontantPrestation = montantPrestation - montantImpotSource;
                data.addData("MontantPrestation", convertCentimes(totalMontantPrestation.toString()));
            } else {
                // Texte montantPrestation
                data.addData("TexteMontantPrestation",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 4, 4));

                // Montant de la prestation
                data.addData("MontantPrestation", convertCentimes(getDecisionOO().getPcfAccordee().getMontant()));
            }

            // Date versement et numéro de compte
            data.addData("CompteVersement", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 4, 5),
                    DecisionSuiteAuProjetBuilder.CDT_NUM_COMPTE, getAdressePaiement()));

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

            // Insertion du titre du nombre de personnes comprises dans le calcul
            data.addData("PersonneComprises",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 6, 1));

            // Insertion des personnes comprises dans le calcul
            data.addData("NomPrenom", getMembreFamille(data));

            // Insertion du paragraphe 4
            data.addData("Paragraphe4Debut",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 7, 1));
            data.addData("Paragraphe4Souligne",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 7, 2));
            data.addData("Paragraphe4Suite",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 7, 3));
            data.addData("Paragraphe4Gras",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 7, 4));

            // Insertion du titre et du texte de réclamation
            data.addData("Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 8, 1));
            data.addData("Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 8, 2));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUITE_AU_PROJET, 9, 1),
                    DecisionSuiteAuProjetBuilder.CDT_TITRE_TIERS, titreTiers));

            // !!! LA SIGNATURE EST CHARGEE DANS AbstractDocumentBuilder ....

            // Insertion du titre et du texte Copie en bas de page
            data = buildBasDePage(data, true, true, true);

            // Insertion du plan de calcul
            data.addData("isPlanDeCalculInclude", "TRUE");
            data = createPlanDeCalcul(dateDoc, isCopie, data);

            /**
             * ******* AJOUT DU DOCUMENT DANS LE CONTAINER******
             */
            // ajout du document dans le container

            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged,
                    IPRConstantesExternes.PCF_DECISION_OCTROI_RI_AVEC_PROJET);
        } catch (Exception e) {
            throw new DecisionException("DecisionSuiteAuProjetBuilder -  Requérant NSS n° : " + this.getNssNomTiers()
                    + " " + e.toString());
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED));
    }
}
