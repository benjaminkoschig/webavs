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

public class DecisionProjetBuilder extends AbstractDecisionBuilder {

    private static final String CDT_DATEDEMANDE = "{dateDemande}";
    private static final String CDT_MONTANT_IMPOT_SOURCE = "{montantImpotSource}";
    private static final String CDT_MONTANT_PRESTATION_MENSUELLE = "{montantPrestationMensuelle}";
    private static final String CDT_NUM_COMPTE = "{numCompte}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    // ===============================================================

    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

    public String idProcess = "";

    public DecisionProjetBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */
    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        loadEntityDecision(IPFCatalogueTextes.CS_DECISION_PROJET, decisionId);

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, getDecisionOO().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_DECISION_PROJET_TITRE_MAIL") + " - " + " " + getSujetMail());
        // ***************************************************

        // Création de la décision originale
        createDecision(dateDoc, false, isSendToGed);

        // Création des copies uniquement lorsque les décisions sont en état validées
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
        allDoc.setMergedDocDestination(getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION));
        return allDoc;
    }

    private void createDecision(String dateDoc, boolean isCopie, boolean ged) throws Exception {
        try {

            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Chargement du titre du document
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 1, 1);

            data = buildHeaderDecision(data, false, isCopie, false, dateDoc, titreDocument);
            data.addData("idProcess", "PCFDecisionProjet");

            // Insertion dans document du titre de document si c'est l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(
                    getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                data.addData("TitreDocument", titreDocument);
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    DecisionProjetBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion des 2 premiers paragraphe et de la date.
            data.addData("Paragraphe1", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 3, 1),
                    DecisionProjetBuilder.CDT_DATEDEMANDE, getDecisionOO().getDemande().getSimpleDemande()
                            .getDateDepot()));
            data.addData("Paragraphe2", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 3, 2));

            // ######Insertion des paragraphes dans l'encadré 1 ######

            // Insertion de la periode avec date de fin si la demande contient une date de fin
            if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getDateFin())) {
                // Texte
                data.addData("PeriodeCalcul", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 4, 1));
                // DateDebutPeriode
                data.addData("DatePeriode", getDecisionOO().getDemande().getSimpleDemande().getDateDebut());
                // Remarque si date de fin
                data.addData("RemarqueSuite", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 4, 2));
                // DateFinPeriode
                data.addData("DateFinPeriode", getDecisionOO().getDemande().getSimpleDemande().getDateFin());
            } else {
                // Texte
                data.addData("PeriodeCalcul", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 4, 1));
                // DateDebutPeriode
                data.addData("DatePeriode", getDecisionOO().getDemande().getSimpleDemande().getDateDebut());
            }

            // Insertion du texte avec impot à la source si présent.
            String montantRetenue = null;
            montantRetenue = getRetenueImpotSource(getDecisionOO().getPcfAccordee());
            if (!JadeStringUtil.isEmpty(montantRetenue)) {
                // Texte montantPrestation
                String texteMontantPrestation = PRStringUtils.replaceString(
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 4, 5),
                        DecisionProjetBuilder.CDT_MONTANT_PRESTATION_MENSUELLE, convertCentimes(getDecisionOO()
                                .getPcfAccordee().getMontant()));
                data.addData("TexteMontantPrestation", PRStringUtils.replaceString(texteMontantPrestation,
                        DecisionProjetBuilder.CDT_MONTANT_IMPOT_SOURCE, convertCentimes(montantRetenue)));

                // Montant de la prestation
                Float montantPrestation = Float.valueOf(getDecisionOO().getPcfAccordee().getMontant());
                Float montantImpotSource = Float.valueOf(montantRetenue);
                Float totalMontantPrestation = montantPrestation - montantImpotSource;
                data.addData("MontantPrestation", convertCentimes(totalMontantPrestation.toString()));
            } else {
                // Texte montantPrestation
                data.addData("TexteMontantPrestation",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 4, 4));

                // Montant de la prestation
                data.addData("MontantPrestation", convertCentimes(getDecisionOO().getPcfAccordee().getMontant()));
            }

            // Date versement et numéro de compte
            data.addData("CompteVersement", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 4, 6),
                    DecisionProjetBuilder.CDT_NUM_COMPTE, getAdressePaiement()));

            // Insertion du titre du nombre de personnes comprises dans le calcul
            data.addData("PersonnesComprises", getBabelContainer()
                    .getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 5, 1));

            // Insertion de la liste des personnes
            data.addData("NomPrenom", getMembreFamille(data));

            // Insertion du paragraphe 3 en gras
            data.addData("Paragraphe3gras", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 6, 1));
            // Insertion du paragraphe 3 suite
            data.addData("Paragraphe3suite", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 6, 4));

            // Insertion du point 1
            data.addData("Point1", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 6, 2));
            // Insertion du point 2
            data.addData("Point2", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 6, 5));
            // Insertion du point 3
            data.addData("Point3", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 6, 3));

            // Insertion de l'encadré 2
            data.addData("Delai", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 7, 1));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_PROJET, 8, 1),
                    DecisionProjetBuilder.CDT_TITRE_TIERS, titreTiers));

            // !!! LA SIGNATURE EST CHARGEE DANS AbstractDocumentBuilder ....

            // Insertion du titre et du texte Copie en bas de page
            data = buildBasDePage(data, false, true, false);

            // Insertion du plan de calcul
            data.addData("isPlanDeCalculInclude", "TRUE");
            data = createPlanDeCalcul(dateDoc, isCopie, data);

            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged, IPRConstantesExternes.PCF_DECISION_PROJET);
        } catch (Exception e) {
            throw new DecisionException("DecisionProjetBuilder -  NSS : " + this.getNssNomTiers()
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreEntete(String dateDoc, String idTiers) throws Exception {

        DocumentData dataLettreEnTete = createLettreEnTete(dateDoc, idTiers);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED));
    }
}
