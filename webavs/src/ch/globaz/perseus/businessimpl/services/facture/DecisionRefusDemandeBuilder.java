package ch.globaz.perseus.businessimpl.services.facture;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionRefusDemandeBuilder extends AbstractFactureBuilder {

    private static final String CDT_DATE_DECISION = "{dateDecision}";
    private static final String CDT_NUM_DECISION = "{numDecision}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    public String agenceAssurance = "";
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
    public String caisse = "";
    public String dateDoc = "";
    public String detailAssure = "";
    private Map<String, String> donneesProcess = null;

    public String gestionnaire = "";
    public String idDomaineApplicatifAdresseCourrier = "";
    public String idDossier = "";
    public String idProcess = "";
    public String idTiersAdresseCourrier = "";

    public String mailGest = "";
    public String membreFamille = "";
    public String numDecision = "";
    private String numeroDecision = "";
    public String texteLibre = "";

    public String typeFacture = "";

    public DecisionRefusDemandeBuilder() {
        donneesProcess = new HashMap<String, String>();
    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */

    public JadePrintDocumentContainer build() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {

        loadEntityFacture();
        createJadePublishDocInfo(dateDoc, mailGest, isSendToGed, dossier.getDemandePrestation().getDemandePrestation()
                .getIdTiers(), getSession().getLabel("PDF_PF_DECISION_REFUS_DEMANDE_TITRE_MAIL") + " - " + " "
                + getSujetMailFacture(idDossier));

        try {

            // demande du numéro de décision pour l'insérer dans le document principale et la copie;
            numeroDecision = PerseusServiceLocator.getDecisionService().getNumeroDemandeCalculee(dateDoc.substring(6));

            // Création de la décision originale
            createDecision(dateDoc, false, isSendToGed);

            allDoc.setMergedDocDestination(getConteneurPubInfos().get(
                    AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION_RECTOVERSO));
            // on retourne le container
            return allDoc;
        } catch (Exception e) {
            throw new DecisionException("DecisionRefusDemandeBuilder -  NSS : " + getNssNomTiers(dossier)
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createDecision(String dateDoc, boolean isCopie, boolean ged) throws Exception {
        try {

            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Chargement du titre du document
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 1, 1);

            data = buildHeaderFacture(data, isCopie, donneesProcess.get("idTiersAdresseCourrier"), gestionnaire,
                    caisse, this.dateDoc, false, titreDocument, false);

            data.addData("idProcess", "PCFDecisionRefusDemande");

            // Insertion dans document du titre de document
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(caisse)) {
                data.addData("TitreDocument",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 1, 1));
            }

            // Chargement du requérant
            String requerant = dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel()
                    + " - "
                    + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                    + " "
                    + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2();
            // Renseignement du requerant
            data.addData("Requerant", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 2, 1)
                    + " " + requerant);
            // Chargement du NIP
            String nip = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();
            // Renseignement du NIP
            data.addData("Nip", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 2, 2) + " "
                    + nip);

            // Renseignement de la date du document
            data.addData("DateDecision", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 2, 3),
                    DecisionRefusDemandeBuilder.CDT_DATE_DECISION, this.dateDoc));

            // Renseignement du numero de décision
            data.addData("NumDecision", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 2, 4),
                    DecisionRefusDemandeBuilder.CDT_NUM_DECISION, numeroDecision));

            // Insertion dans document du titre du tiers
            data.addData("TitreTiers", titreTiers + ",");

            // Insertion du paragraphe 1
            data.addData("Paragraphe1", getBabelContainer()
                    .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 3, 1));

            // Insertion du paragraphe 2
            data.addData("Paragraphe2", getBabelContainer()
                    .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 3, 2));

            // Insertion du paragraphe 3
            data.addData("Paragraphe3", getBabelContainer()
                    .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 3, 3));

            // Insertion du paragraphe 4
            data.addData("Paragraphe4", getBabelContainer()
                    .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 3, 4));

            // Insertion du paragraphe 5
            data.addData("Paragraphe5", getBabelContainer()
                    .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 3, 5));

            // Insertion du paragraphe 6
            data.addData("Paragraphe6", getBabelContainer()
                    .getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 3, 6));

            // Insertion des salutations
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 3, 7),
                    DecisionRefusDemandeBuilder.CDT_TITRE_TIERS, titreTiers));

            // Insertion du titre réclamation
            data.addData("TitreReclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 4, 1));

            // Insertion du texte de réclamation
            data.addData("TexteReclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE, 4, 2));

            // Insertion du titre et du texte Copie en bas de page
            data = buildBasDePageFacture(data, agenceAssurance, true);

            // !!! LA SIGNATURE EST CHARGEE DANS AbstractDocumentBuilder ....

            // ajout du document dans le container
            allDoc = dataAndPubInfo(allDoc, data, isCopie, ged, IPRConstantesExternes.PCF_FACTURE_REFUS_DEMANDE);
        } catch (Exception e) {
            throw new DecisionException("DecisionRefusFactureBuilder -  NSS : " + getNssNomTiers(dossier)
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    public Map<String, String> getDonneesProcess() {
        return donneesProcess;
    }

    private void loadEntityFacture() throws CatalogueTexteException, Exception {
        // Initialisation des variables avec les données reçues dans le helper
        try {

            dateDoc = donneesProcess.get("dateDocument").toString();
            mailGest = donneesProcess.get("eMailAdresse").toString();
            idDossier = donneesProcess.get("idDossier").toString();
            gestionnaire = donneesProcess.get("gestionnaire").toString();
            idTiersAdresseCourrier = donneesProcess.get("idTiersAdresseCourrier").toString();
            idDomaineApplicatifAdresseCourrier = donneesProcess.get("idDomaineApplicatifAdresseCourrier").toString();
            agenceAssurance = donneesProcess.get("agenceAssurance").toString();
            caisse = donneesProcess.get("caisse").toString();

            loadEntityAbstractFacture(idDossier, IPFCatalogueTextes.CS_DECISION_REFUS_DEMANDE);

        } catch (Exception e) {
            throw new DecisionException("DecisionRefusDemandeBuilder -  NSS : " + getNssNomTiers(dossier)
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    public void setDonneesProcess(Map<String, String> donneesProcess) {
        this.donneesProcess = donneesProcess;
    }

}
