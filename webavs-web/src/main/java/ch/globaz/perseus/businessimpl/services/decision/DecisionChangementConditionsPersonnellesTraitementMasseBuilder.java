package ch.globaz.perseus.businessimpl.services.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.process.decision.PFImpressionDecisionTraitementMasseProcess;
import globaz.prestation.tools.PRStringUtils;
import java.util.HashMap;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionChangementConditionsPersonnellesTraitementMasseBuilder extends AbstractDecisionBuilder {

    private static final String CDT_MONTANT_IMPOT_SOURCE = "{montantImpotSource}";
    private static final String CDT_MONTANT_PRESTATION_MENSUELLE = "{montantPrestationMensuelle}";
    private static final String CDT_NUM_COMPTE = "{numCompte}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    private boolean copieOVAM = false;

    private HashMap<String, JadePrintDocumentContainer> hashMapAllDoc = null;

    public String idProcess = "";

    public DecisionChangementConditionsPersonnellesTraitementMasseBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    public HashMap<String, JadePrintDocumentContainer> build(String decisionId, String mailCaisse, String dateDoc,
            boolean isSendToGed, HashMap<String, JadePrintDocumentContainer> hashMap) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        hashMapAllDoc = hashMap;

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, decisionId);

        createJadePublishDocInfo(dateDoc, mailCaisse, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES_MAIL") + " - " + " "
                        + getSujetMail());

        Demande demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(
                getDecisionOO().getDemande());
        if (null != demandePrecedante) {
            DecisionSearchModel dsm = new DecisionSearchModel();
            dsm.setForIdDemande(demandePrecedante.getSimpleDemande().getIdDemande());
            dsm = PerseusServiceLocator.getDecisionService().search(dsm);
            for (JadeAbstractModel model : dsm.getSearchResults()) {
                Decision ad = (Decision) model;
                if (CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(ad.getSimpleDecision().getCsTypeDecision())
                        && CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(
                                getDecisionOO().getSimpleDecision().getCsTypeDecision())) {
                    copieOVAM = true;
                    break;
                }
            }
        }

        // ***************************************************

        // Création de la décision originale
        if (CSCaisse.CCVD.getCodeSystem().equals(getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
            createDecision(dateDoc, false, isSendToGed, PFImpressionDecisionTraitementMasseProcess.CCVD_Benef);
        } else {
            createDecision(dateDoc, false, isSendToGed, PFImpressionDecisionTraitementMasseProcess.AGLAU_Benef);
        }

        // Création de la lettre d'entete, de la copie de décision et du plan de calcul
        if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {

            // Création de la copie de la décision et du plan de calcul pour l'OCC
            if (copieOVAM) {
                if (CSCaisse.CCVD.getCodeSystem().equals(getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                    createDecision(dateDoc, true, false, PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_OVAM);
                } else {
                    createDecision(dateDoc, true, false, PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_OVAM);
                }

            }

            // Création des lettres d'entete et des copies pour toutes les copies ajoutées dans la JSP
            for (CopieDecision copieDecision : getDecisionOO().getListeCopie()) {
                if (CSCaisse.CCVD.getCodeSystem().equals(getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                    createLettreEntete(dateDoc, copieDecision.getSimpleCopieDecision().getIdTiers(),
                            PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_SUPP);
                    createDecision(dateDoc, true, false, PFImpressionDecisionTraitementMasseProcess.CCVD_COPIE_SUPP);
                } else {
                    createLettreEntete(dateDoc, copieDecision.getSimpleCopieDecision().getIdTiers(),
                            PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_SUPP);
                    createDecision(dateDoc, true, false, PFImpressionDecisionTraitementMasseProcess.AGLAU_COPIE_SUPP);
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
            String titreDocument = getBabelContainer().getTexte(
                    IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 1, 1);

            if (getDecisionOO().getDemande().getSimpleDemande().getCoaching()) {
                titreDocument = titreDocument + "\n"
                        + getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 8);
            }

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionChangementConditionsPersonnelles");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionChangementConditionsPersonnellesTraitementMasseBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du premiers paragraphe.
            data.addData(
                    "Paragraphe1",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 3,
                            1));

            // ######Insertion des paragraphes dans l'encadré ######
            // Insertion de la periode avec date de fin si la demande contient une date de fin
            if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getDateFin())) {
                // Texte periode
                data.addData(
                        "PeriodeCalcul",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES,
                                4, 1));
                // Date debut de periode
                data.addData("DatePeriode", getDecisionOO().getDemande().getSimpleDemande().getDateDebut());

                data.addData(
                        "RemarqueSuite",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES,
                                4, 3));

                // Date fin de periode
                data.addData("DateFinPeriode", getDecisionOO().getDemande().getSimpleDemande().getDateFin());
            } else {
                // Texte periode
                data.addData(
                        "PeriodeCalcul",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES,
                                4, 1));
                // Date debut de periode
                data.addData("DatePeriode", getDecisionOO().getDemande().getSimpleDemande().getDateDebut());

            }

            // Insertion du texte avec impot à la source si présent.
            String montantRetenue = null;
            montantRetenue = getRetenueImpotSource(getDecisionOO().getPcfAccordee());
            if (!JadeStringUtil.isEmpty(montantRetenue)) {
                // Texte montantPrestation
                String texteMontantPrestation = PRStringUtils
                        .replaceString(
                                getBabelContainer().getTexte(
                                        IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 4, 6),
                                DecisionChangementConditionsPersonnellesTraitementMasseBuilder.CDT_MONTANT_PRESTATION_MENSUELLE,
                                convertCentimes(getDecisionOO().getPcfAccordee().getMontant()));
                data.addData("TexteMontantPrestation", PRStringUtils.replaceString(texteMontantPrestation,
                        DecisionChangementConditionsPersonnellesTraitementMasseBuilder.CDT_MONTANT_IMPOT_SOURCE,
                        convertCentimes(montantRetenue)));

                // Montant de la prestation
                Float montantPrestation = Float.valueOf(getDecisionOO().getPcfAccordee().getMontant());
                Float montantImpotSource = Float.valueOf(montantRetenue);
                Float totalMontantPrestation = montantPrestation - montantImpotSource;
                data.addData("MontantPrestation", convertCentimes(totalMontantPrestation.toString()));
            } else {
                // Texte montantPrestation
                data.addData(
                        "TexteMontantPrestation",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES,
                                4, 4));

                // Montant de la prestation
                data.addData("MontantPrestation", convertCentimes(getDecisionOO().getPcfAccordee().getMontant()));
            }

            // Date versement et numéro de compte
            data.addData("CompteVersement", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 4,
                            5), DecisionChangementConditionsPersonnellesTraitementMasseBuilder.CDT_NUM_COMPTE,
                    getAdressePaiement()));

            // Insertion de la remarque selon condition
            if (!JadeStringUtil.isEmpty(getDecisionOO().getSimpleDecision().getRemarqueUtilisateur())) {
                data.addData(
                        "TitreRemarque",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES,
                                5, 1));
                data.addData("TexteRemarque", getDecisionOO().getSimpleDecision().getRemarqueUtilisateur());
            }

            // ############################## INSERTION DU TABLEAUX SELON CONDITION ####################################
            // #########################################################################################################

            // Insertion du tableau si retro-actif présent ou si prestation existante
            if (hasTableau()) {
                data.addData("isTableauInclude", "TRUE");
                data = buildTableau(data);

            } else {
                data.addData("isTableauInclude", "FALSE");
            }
            // #########################################################################################################

            // Insertion du paragraphe 2
            data.addData(
                    "Paragraphe2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 6,
                            1));

            if (getDecisionOO().getDemande().getSimpleDemande().getCoaching()) {
                data.addData(
                        "Paragraphe2Souligne",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES,
                                6, 5));
            } else {
                data.addData(
                        "Paragraphe2Souligne",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES,
                                6, 2));
            }

            data.addData(
                    "Paragraphe2Suite",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 6,
                            3));
            data.addData(
                    "Paragraphe2Gras",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 6,
                            4));

            // Insertion du titre et du texte de réclamation
            data.addData(
                    "Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 7,
                            1));
            data.addData(
                    "Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 7,
                            2));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_CHANGEMENT_CONDITIONS_PERSONNELLES, 8,
                            1), DecisionChangementConditionsPersonnellesTraitementMasseBuilder.CDT_TITRE_TIERS,
                    titreTiers));

            // !!! LA SIGNATURE EST CHARGEE DANS AbstractDocumentBuilder ....

            // Insertion du titre et du texte Copie en bas de page
            if (getDecisionOO().getDemande().getSimpleDemande().getCoaching()) {
                if (copieOVAM) {
                    data = buildBasDePage(data, false, false, true);
                } else {
                    data = buildBasDePage(data, false, false, false);
                }

            } else {
                if (copieOVAM) {
                    data = buildBasDePage(data, true, false, true);
                } else {
                    data = buildBasDePage(data, true, false, false);
                }

            }

            // Insertion du plan de calcul
            data.addData("isPlanDeCalculInclude", "TRUE");
            data = createPlanDeCalcul(dateDoc, isCopie, data);

            hashMapAllDoc.put(
                    cleHashMapAllDoc,
                    dataAndPubInfo(hashMapAllDoc.get(cleHashMapAllDoc), data, isCopie, ged,
                            IPRConstantesExternes.PCF_DECISION_OCTROI_ANNONCE_CHANGEMENT));
        } catch (Exception e) {
            throw new DecisionException("DecisionChangementConditionsPersonnellesBuilder -  NSS : "
                    + this.getNssNomTiers() + ", Détail de l'erreur : " + e.toString(), e);
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
