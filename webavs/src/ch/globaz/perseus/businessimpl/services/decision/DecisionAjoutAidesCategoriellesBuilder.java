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

public class DecisionAjoutAidesCategoriellesBuilder extends AbstractDecisionBuilder {

    private static final String CDT_MONTANT_IMPOT_SOURCE = "{montantImpotSource}";
    private static final String CDT_MONTANT_PRESTATION_MENSUELLE = "{montantPrestationMensuelle}";
    private static final String CDT_NUM_COMPTE = "{numCompte}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionAjoutAidesCategoriellesBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, decisionId);
        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_AJOUT_AIDES_CATEGORIELLES_MAIL") + " - " + " " + getSujetMail());

        // ***************************************************

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

            // Création des lettres d'entete et des copies pour toutes les copies ajoutées dans la JSP
            for (CopieDecision copieDecision : getDecisionOO().getListeCopie()) {
                createLettreEntete(dateDoc, copieDecision.getSimpleCopieDecision().getIdTiers());
                createDecision(dateDoc, true, false);
            }
        }

        // ***************************************************

        // on retourne le container

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
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 1, 1);

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionAjoutAideCat");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionAjoutAidesCategoriellesBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du premier paragraphes
            data.addData("Paragraphe1",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 3, 1));

            // Insertion du/des choix du paragraphe 1
            String choixParagraphe = "";
            int nbrChoix = 0;
            if (getDecisionOO().getSimpleDecision().getAideAuLogement()) {
                nbrChoix++;
                choixParagraphe = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 3, 2);

            }
            if (getDecisionOO().getSimpleDecision().getPensionAlimentaire()) {
                nbrChoix++;
                if (JadeStringUtil.isEmpty(choixParagraphe)) {
                    choixParagraphe = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 3, 3);
                } else {
                    choixParagraphe = choixParagraphe + ", "
                            + getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 3, 3);
                }
            }
            if (getDecisionOO().getSimpleDecision().getAideAuxEtudes()) {
                nbrChoix++;
                if (JadeStringUtil.isEmpty(choixParagraphe)) {
                    choixParagraphe = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 3, 4);
                } else {
                    choixParagraphe = choixParagraphe + ", "
                            + getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 3, 4);
                }
            }
            data.addData("ChoixParagraphe1", choixParagraphe);
            if (nbrChoix > 1) {
                data.addData("SuiteParagraphe1",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 3, 6));
            } else {
                data.addData("SuiteParagraphe1",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 3, 5));
            }
            // ######Insertion des paragraphes dans l'encadré ######
            // Insertion de la periode avec date de fin si la demande contient une date de fin
            if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getDateFin())) {
                // Texte periode
                data.addData("PeriodeCalcul",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 4, 1));
                // Date debut de periode
                data.addData("DatePeriode", getDecisionOO().getDemande().getSimpleDemande().getDateDebut());
                // Ajout de la suite de la remarque si date de fin
                data.addData("RemarqueSuite",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 4, 3));
                // Date fin de periode
                data.addData("DateFinPeriode", getDecisionOO().getDemande().getSimpleDemande().getDateFin());
            } else {
                // Texte periode
                data.addData("PeriodeCalcul",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 4, 1));
                // Date debut de periode
                data.addData("DatePeriode", getDecisionOO().getDemande().getSimpleDemande().getDateDebut());
            }

            // Insertion du texte avec impot à la source si présent.
            String montantRetenue = null;
            montantRetenue = getRetenueImpotSource(getDecisionOO().getPcfAccordee());
            if (!JadeStringUtil.isEmpty(montantRetenue)) {

                // Texte montantPrestation
                String texteMontantPrestation = PRStringUtils.replaceString(
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 4, 6),
                        DecisionAjoutAidesCategoriellesBuilder.CDT_MONTANT_PRESTATION_MENSUELLE,
                        convertCentimes(getDecisionOO().getPcfAccordee().getMontant()));
                data.addData("TexteMontantPrestation", PRStringUtils.replaceString(texteMontantPrestation,
                        DecisionAjoutAidesCategoriellesBuilder.CDT_MONTANT_IMPOT_SOURCE,
                        convertCentimes(montantRetenue)));

                // Montant de la prestation
                Float montantPrestation = Float.valueOf(getDecisionOO().getPcfAccordee().getMontant());
                Float montantImpotSource = Float.valueOf(montantRetenue);
                Float totalMontantPrestation = montantPrestation - montantImpotSource;
                data.addData("MontantPrestation", convertCentimes(totalMontantPrestation.toString()));

            } else {
                // Texte montantPrestation
                data.addData("TexteMontantPrestation",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 4, 4));

                // Montant de la prestation
                data.addData("MontantPrestation", convertCentimes(getDecisionOO().getPcfAccordee().getMontant()));
            }

            // Date versement et numéro de compte
            data.addData("CompteVersement", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 4, 5),
                    DecisionAjoutAidesCategoriellesBuilder.CDT_NUM_COMPTE, getAdressePaiement()));

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
            // #########################################################################################################

            // Insertion du paragraphe 4
            data.addData("Paragraphe4",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 7, 1));
            data.addData("Paragraphe4Souligne",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 7, 2));
            data.addData("Paragraphe4Suite",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 7, 3));
            data.addData("Paragraphe4Gras",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 7, 4));

            // Insertion du titre et du texte de réclamation
            data.addData("Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 8, 1));
            data.addData("Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 8, 2));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_AJOUT_AIDE_CAT, 9, 1),
                    DecisionAjoutAidesCategoriellesBuilder.CDT_TITRE_TIERS, titreTiers));

            // !!! LA SIGNATURE EST CHARGEE DANS AbstractDocumentBuilder ....

            // Insertion du titre et du texte Copie en bas de page
            data = buildBasDePage(data, true, false, false);

            // Insertion du plan de calcul
            data.addData("isPlanDeCalculInclude", "TRUE");
            data = createPlanDeCalcul(dateDoc, isCopie, data);

            /**
             * ******* AJOUT DU DOCUMENT DANS LE CONTAINER******
             */
            // ajout du document dans le container

            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged,
                    IPRConstantesExternes.PCF_DECISION_OCTROI_AJOUT_AIDES_CATEGORIELLES);
        } catch (Exception e) {
            throw new DecisionException("DecisionAjoutAideCategorielle -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED));
    }
}
