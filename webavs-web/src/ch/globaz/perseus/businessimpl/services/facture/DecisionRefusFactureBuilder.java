package ch.globaz.perseus.businessimpl.services.facture;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSTypeFacture;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionRefusFactureBuilder extends AbstractFactureBuilder {

    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    public String agenceAssurance = "";
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
    public String caisse = "";
    public String dateDoc = "";
    public String detailAssure = "";
    private Map<String, String> donneesProcess = null;
    public String gestionnaire = "";
    public String idDossier = "";
    public String idProcess = "";

    public String mailGest = "";
    public String membreFamille = "";
    public String texteLibre = "";
    public String typeFacture = "";

    public DecisionRefusFactureBuilder() {
        donneesProcess = new HashMap<String, String>();
    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */

    public JadePrintDocumentContainer build() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {
        this.loadEntity();

        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, dossier.getDemandePrestation().getDemandePrestation()
                .getIdTiers(), getSession().getLabel("PDF_PF_DECISION_REFUS_FACTURE_TITRE_MAIL") + " - " + " "
                + getSujetMailFacture(idDossier));

        // *****************
        try {
            // Création de la décision originale
            createDecision(false, isSendToGed);

            // on retourne le container
            allDoc.setMergedDocDestination(getConteneurPubInfos().get(
                    AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION_RECTOVERSO));
            return allDoc;
        } catch (Exception e) {
            throw new DecisionException("DecisionRefusFactureBuilder -  NSS : " + getNssNomTiers(dossier)
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createDecision(boolean isCopie, boolean ged) throws Exception {
        try {
            /**
             * ********** GENERATION DOCUMENT**************PDF_PF_DECISION_OCTROI_TITRE_MAIL
             */

            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Chargement du titre du document selon le type de facture générée
            String titreDocument = null;
            // Si frais de garde
            if (CSTypeFacture.FRAIS_DE_GARDE.getCodeSystem().equals(typeFacture)) {
                titreDocument = getBabelContainer().getTexte(
                        IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_GARDE, 1, 1);
                // Si frais de maladie
            } else if (CSTypeFacture.FRAIS_DE_MALADIE.getCodeSystem().equals(typeFacture)) {
                titreDocument = getBabelContainer().getTexte(
                        IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_MALADIE, 1, 1);
                // Si autres frais
            } else {
                titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_AUTRES_FRAIS,
                        1, 1);
            }

            data = buildHeaderFacture(data, isCopie, donneesProcess.get("idTiersAdresseCourrier"), gestionnaire,
                    caisse, dateDoc, false, titreDocument, false);

            data.addData("idProcess", "PCFDecisionRefusFacture");

            // Chargement du requérant
            String requerant = dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel()
                    + " - "
                    + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                    + " "
                    + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2();
            // Renseignement du requerant
            data.addData("Requerant", getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_COMMUNES, 2, 1) + " "
                    + requerant);

            // Chargement du NIP
            String nip = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();

            // Renseignement du NIP
            data.addData("Nip", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 2, 3) + " " + nip);

            // Chargement de l'ayant droit
            String ayantDroit = "";
            if (!JadeStringUtil.isEmpty(membreFamille)) {
                SimpleMembreFamille mf = PerseusImplServiceLocator.getSimpleMembreFamilleService().read(membreFamille);
                PersonneEtendueComplexModel personne = TIBusinessServiceLocator.getPersonneEtendueService().read(
                        mf.getIdTiers());

                ayantDroit = personne.getPersonneEtendue().getNumAvsActuel() + " - "
                        + personne.getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2();

            }

            // Renseignement de l'ayant droit si il y en a un de renseigné
            if (!JadeStringUtil.isEmpty(ayantDroit)) {
                data.addData("AyantDroit", getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_COMMUNES, 2, 2)
                        + " " + ayantDroit);
            }

            // Si frais de garde
            if (CSTypeFacture.FRAIS_DE_GARDE.getCodeSystem().equals(typeFacture)) {

                // Insertion dans document du titre de document
                if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(caisse)) {
                    data.addData(
                            "TitreDocument",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_GARDE,
                                    1, 1));
                }

                // Insertion des 2 premiers paragraphe et de la date.
                data.addData("Paragraphe1",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_GARDE, 3, 1));
                data.addData("Paragraphe2",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_GARDE, 3, 2));

                // Si frais de maladie
            } else if (CSTypeFacture.FRAIS_DE_MALADIE.getCodeSystem().equals(typeFacture)) {

                // Insertion dans document du titre de document
                if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(caisse)) {
                    data.addData(
                            "TitreDocument",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_MALADIE,
                                    1, 1));
                }

                // Insertion des 2 premiers paragraphe et de la date.
                data.addData(
                        "Paragraphe1",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_MALADIE, 3,
                                1));
                data.addData(
                        "Paragraphe2",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_MALADIE, 3,
                                2));
                // Si autres frais
            } else {
                // Insertion dans document du titre de document
                if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(caisse)) {
                    data.addData(
                            "TitreDocument",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_AUTRES_FRAIS, 1,
                                    1));
                }

                // Insertion des 2 premiers paragraphe et de la date.
                data.addData("Paragraphe1",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_AUTRES_FRAIS, 3, 1));
                data.addData("Paragraphe2",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_AUTRES_FRAIS, 3, 2));
            }

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_COMMUNES, 3, 1),
                    DecisionRefusFactureBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du texte libre
            data.addData("Paragraphe3", texteLibre);

            // Insertion du paragraphe 4
            data.addData("Parapgraphe4", getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_COMMUNES, 3, 4));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_COMMUNES, 3, 5),
                    DecisionRefusFactureBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre réclamation
            data.addData("Reclamation", getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_COMMUNES, 4, 1));

            // Insertion du texte de réclamation
            data.addData("Texte_Reclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_COMMUNES, 4, 2));

            // Insertion du titre et du texte Copie en bas de page
            data = buildBasDePageFacture(data, agenceAssurance, true);

            // ajout du document dans le container
            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged,
                    IPRConstantesExternes.PCF_FACTURE_IMPRIMER_REFUS_FACTURE);
        } catch (Exception e) {
            throw new DecisionException("DecisionRefusFactureBuilder -  NSS : " + getNssNomTiers(dossier)
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    public Map<String, String> getDonneesProcess() {
        return donneesProcess;
    }

    private void loadEntity() throws CatalogueTexteException, Exception {
        // Chargement du catalogue de texte de frais de garde
        // Récupération du codeIsoLangue
        // Initialisation des variables avec les données reçues dans le helper
        try {
            dateDoc = donneesProcess.get("dateDocument").toString();
            mailGest = donneesProcess.get("eMailAdresse").toString();
            detailAssure = donneesProcess.get("detailAssure").toString();
            gestionnaire = donneesProcess.get("gestionnaire").toString();
            idDossier = donneesProcess.get("idDossier").toString();
            agenceAssurance = donneesProcess.get("agenceAssurance").toString();
            caisse = donneesProcess.get("caisse").toString();
            membreFamille = donneesProcess.get("membreFamille").toString();
            texteLibre = donneesProcess.get("texteLibre").toString();
            typeFacture = donneesProcess.get("typeFacture").toString();

            // Chargement du catalogue Frais de garde, si frais de garde sélectionné
            if (CSTypeFacture.FRAIS_DE_GARDE.getCodeSystem().equals(typeFacture)) {
                loadEntityAbstractFacture(idDossier, IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_GARDE);
                // Chargement du catalogue Frais de maladie, si frais de maladie sélectionné
            } else if (CSTypeFacture.FRAIS_DE_MALADIE.getCodeSystem().equals(typeFacture)) {
                loadEntityAbstractFacture(idDossier, IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_FRAIS_DE_MALADIE);
                // Chargement du catalogue Autres Frais, si autres frais sélectionné
            } else {
                loadEntityAbstractFacture(idDossier, IPFCatalogueTextes.CS_DECISION_REFUS_FACTURE_AUTRES_FRAIS);
            }

        } catch (Exception e) {
            throw new DecisionException("DecisionRefusFactureBuilder -  NSS : " + getNssNomTiers(dossier)
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    public void setDonneesProcess(Map<String, String> donneesProcess) {
        this.donneesProcess = donneesProcess;
    }

}
