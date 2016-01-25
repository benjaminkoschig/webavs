package globaz.pegasus.utils.calculmoissuivant;

import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCActions;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAllocationsFamiliales;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAssuranceRenteViagere;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAssuranceVie;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutreFortuneMobiliere;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutresAPI;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutresDettesProuvees;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutresRentes;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutresRevenus;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBetail;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoAnnexe;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoNonHabitable;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoPrincipal;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCCapitalLPP;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCCompteBancaireCPP;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCContratEntretienViager;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCCotisationPSAL;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDessaisissementFortune;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDessaisissementRevenu;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCIJAI;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCIJAPG;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCLoyer;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCMarchandiseStock;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCNumeraire;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCPensionAlimentaire;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCPretEnversTiers;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRevenuActiviteDependante;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRevenuActiviteIndependante;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRevenuHypothetique;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTitre;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCVehicule;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * Classe chargée de fournir la collection <code>java.util.Map</code> contenant le mapping des propriétés des données
 * financières à afficher pour la calcul mois suivants Projet: Pegasus
 * 
 * Projet Pegasus (v1.11)
 * 
 * @author sce
 * @date 26.10.2012
 */
public class DonneesFinancieresDescriptorProvider {

    /** Collections MAP des descripteurs DF */
    private static Map<String, DonneeFinanciereDescriptor> donneFinanciereDroitDescriptor = new HashMap<String, DonneeFinanciereDescriptor>();

    /**
     * Constructeur statique Instanciation de la map de configuration
     */
    static {

        /************************************************* ### RENTE IJ API ### ********************************************************/
        /************************************************* rente avs-ai ********************************************************/
        ArrayList<DonneeFinancierePropertiesDescriptor> dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("renteAVSAIMontant", "JSP_CMS_64007001_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("renteAVSAICsTypeRente",
                "JSP_CMS_64007001_TYPERENTE", 2, true));
        DonneeFinanciereDescriptor descripteur = null;
        try {
            descripteur = new DonneeFinanciereDescriptor(1, dfProperties, "MENU_OPTION_DROITS_RENTES_AJ_API",
                    "MENU_ONGLET_DROITS_RENTE_AVS_API", IPCActions.ACTION_DROIT_AVS_AI,
                    PegasusImplServiceLocator.getRenteAvsAiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCRenteAvsAi.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************************* rente IJAI ********************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("IJAIMontant", "JSP_CMS_64007002_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("IJAIJours", "JSP_CMS_64007002_NBREJOURS", 2));
        try {
            descripteur = new DonneeFinanciereDescriptor(2, dfProperties, "MENU_OPTION_DROITS_RENTES_AJ_API",
                    "MENU_ONGLET_DROITS_IJAI", IPCActions.ACTION_DROIT_IJAI,
                    PegasusImplServiceLocator.getIndemniteJournaliereAiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCIJAI.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);
        /************************************************* API avs ai ********************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("APIAVSAIMontant", "JSP_CMS_64007003_MONTANT", 1));
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("APIAVSCsType", "JSP_CMS_64007003_TYPERENTE", 2, true));
        try {
            descripteur = new DonneeFinanciereDescriptor(3, dfProperties, "MENU_OPTION_DROITS_RENTES_AJ_API",
                    "MENU_ONGLET_DROITS_ALLOCATION_IMPOTENT_AVS_AI",
                    IPCActions.ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI,
                    PegasusImplServiceLocator.getAllocationImpotentService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);
        /************************************************* Autre rente ********************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("autresRentesMontant", "JSP_CMS_64007004_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autresRentesCsType", "JSP_CMS_64007004_TYPERENTE",
                2, true));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autresRentesCsGenre", "JSP_CMS_64007004_GENRERENTE",
                3, true));
        try {
            descripteur = new DonneeFinanciereDescriptor(4, dfProperties, "MENU_OPTION_DROITS_RENTES_AJ_API",
                    "MENU_ONGLET_DROITS_AUTRES_RENTES", IPCActions.ACTION_DROIT_AUTRES_RENTES,
                    PegasusImplServiceLocator.getAutreRenteService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCAutresRentes.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************************* IJ APG ********************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("IJAPGMontant", "JSP_CMS_64007005_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("IJAPGnbJours", "JSP_CMS_64007005_NBREJOURS", 2));
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("IJAPGGenre", "JSP_CMS_64007005_GENRERENTE", 3, true));
        try {
            descripteur = new DonneeFinanciereDescriptor(5, dfProperties, "MENU_OPTION_DROITS_RENTES_AJ_API",
                    "MENU_ONGLET_DROITS_INDEMNITES_JOURNALIERES_APG",
                    IPCActions.ACTION_DROIT_INDEMNITES_JOURNALIERES_APG, PegasusImplServiceLocator.getIjApgService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCIJAPG.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);
        /************************************************* AUTRE API ********************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autresAPIMontant", "JSP_CMS_64007006_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autresAPICsType", "JSP_CMS_64007006_TYPEAPI", 2,
                true));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autresAPICsGenre", "JSP_CMS_64007006_GENRERENTE", 3,
                true));
        try {
            descripteur = new DonneeFinanciereDescriptor(6, dfProperties, "MENU_OPTION_DROITS_RENTES_AJ_API",
                    "MENU_ONGLET_DROITS_AUTRES_API", IPCActions.ACTION_DROIT_AUTRES_API,
                    PegasusImplServiceLocator.getAutreApiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCAutresAPI.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);

        /************************************************* ### HABITAT ### ********************************************************/
        /************************************************* loyer ********************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("loyerMontant", "JSP_CMS_64007007_LOYERNET", 1));
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("loyerMontantCharges", "JSP_CMS_64007007_CHARGES", 2));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("loyerCsTypeLoyer", "JSP_CMS_64007007_TYPELOYER", 3,
                true));
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("loyerNbPersonnes", "JSP_CMS_64007007_NBPERSONNES", 4));
        try {
            descripteur = new DonneeFinanciereDescriptor(7, dfProperties, "MENU_OPTION_DROITS_HABITAT",
                    "MENU_ONGLET_DROITS_LOYER", IPCActions.ACTION_DROIT_LOYER,
                    PegasusImplServiceLocator.getLoyerService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCLoyer.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);
        /******************************************** Taxes journalieres homes ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("taxeJournaliereMontantJournalierLCA",
                "JSP_CMS_64007008_MONTANTJOURNALIER", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("taxeJournalierePrimeAPayer",
                "JSP_CMS_64007007_PRIMESAPAYER", 2));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("taxeJournaliereDateEntreeHome",
                "JSP_CMS_64007007_DATEENTREEHOME", 3));
        try {
            descripteur = new DonneeFinanciereDescriptor(8, dfProperties, "MENU_OPTION_DROITS_HABITAT",
                    "MENU_ONGLET_DROITS_TAXE_JOURNALIERE", IPCActions.ACTION_DROIT_TAXE_JOURNALIERE_HOME,
                    PegasusImplServiceLocator.getTaxeJournaliereHomeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE, descripteur);

        /******************************************** ### FORTUNE USUELLE ### ****************************************************/
        /******************************************** CPP CB ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("compteBancaireCPPMontant",
                "JSP_CMS_64007009_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("compteBancaireCPPMontantInterets",
                "JSP_CMS_64007009_INTERETS", 2));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("compteBancaireCPPMontantFrais",
                "JSP_CMS_64007009_FRAIS", 3));
        try {
            descripteur = new DonneeFinanciereDescriptor(9, dfProperties, "MENU_OPTION_DROITS_FORTUNE_USUELLE",
                    "MENU_ONGLET_DROITS_COMPTE_BANCAIRE_CCP", IPCActions.ACTION_DROIT_COMPTE_BANCAIRE_CCP,
                    PegasusImplServiceLocator.getCompteBancaireCCPService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCCompteBancaireCPP.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /********************************************** Titres ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("titreMontant", "JSP_CMS_64007011_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("titreRendement", "JSP_CMS_64007011_RENDEMENT", 2));
        try {
            descripteur = new DonneeFinanciereDescriptor(10, dfProperties, "MENU_OPTION_DROITS_FORTUNE_USUELLE",
                    "MENU_ONGLET_DROITS_TITRES", IPCActions.ACTION_DROIT_TITRE,
                    PegasusImplServiceLocator.getTitreService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCTitre.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);
        /********************************************** Assurance vie ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("assuranceVieMontantValeurRachat",
                "JSP_CMS_64007012_MONTANT", 1));
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("assuranceVieNoPolice", "JSP_CMS_64007012_POLICE", 2));
        try {
            descripteur = new DonneeFinanciereDescriptor(11, dfProperties, "MENU_OPTION_DROITS_FORTUNE_USUELLE",
                    "MENU_ONGLET_DROITS_ASSURANCE_VIE", IPCActions.ACTION_DROIT_ASSURANCE_VIE,
                    PegasusImplServiceLocator.getAssuranceVieService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCAssuranceVie.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /********************************************** LPP ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("capitalLPPMontant", "JSP_CMS_64007013_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("capitalLPPMontantInterets",
                "JSP_CMS_64007013_INTERETS", 2));
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("capitalLPPMontantFrais", "JSP_CMS_64007013_FRAIS", 3));
        try {
            descripteur = new DonneeFinanciereDescriptor(12, dfProperties, "MENU_OPTION_DROITS_FORTUNE_USUELLE",
                    "MENU_ONGLET_DROITS_CAPITAL_LPP", IPCActions.ACTION_DROIT_CAPITAL_LPP,
                    PegasusImplServiceLocator.getCapitalLPPService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCCapitalLPP.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /********************************************** Autres dette prouvées ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autresDettesProuveesMontant",
                "JSP_CMS_64007014_MONTANT", 1));
        try {
            descripteur = new DonneeFinanciereDescriptor(13, dfProperties, "MENU_OPTION_DROITS_FORTUNE_USUELLE",
                    "MENU_ONGLET_DROITS_AUTRES_DETTES_PROUVEES", IPCActions.ACTION_DROIT_AUTRES_DETTES_PROUVEES,
                    PegasusImplServiceLocator.getAutresDettesProuveesService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCAutresDettesProuvees.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /***************************************** Biens immo principal ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoPrincipalMontantValeurLocative",
                "JSP_CMS_64007032_MONTANTVALLOC", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoPrincipalMontantValeurFiscale",
                "JSP_CMS_64007032_MONTANTVALFISC", 2));

        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoPrincipalMontantInteretHypothecaire",
                "JSP_CMS_64007032_INTHYPO", 4));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoPrincipalMontantDetteHypothecaire",
                "JSP_CMS_64007032_DETTEYPO", 3));

        try {
            descripteur = new DonneeFinanciereDescriptor(14, dfProperties, "MENU_OPTION_DROITS_FORTUNE_USUELLE",
                    "MENU_ONGLET_DROITS_BIEN_IMMOBILIER_SHP", IPCActions.ACTION_DROIT_BIEN_IMMOBILIER_SHP,
                    PegasusImplServiceLocator.getBienImmobilierServantHabitationPrincipaleService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCBienImmoPrincipal.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /***************************************** Biens immo annexe ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoAnnexeMontantValeurLocative",
                "JSP_CMS_64007034_MONTANTVALLOC", 1));

        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoAnnexeCsTypePropriete",
                "JSP_CMS_64007034_TYPEPROP", 2, true));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoAnnexeMontantInteretHypothecaire",
                "JSP_CMS_64007034_INTHYPO", 4));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoAnnexeMontantDetteHypothecaire",
                "JSP_CMS_64007034_DETTEYPO", 3));
        try {
            descripteur = new DonneeFinanciereDescriptor(15, dfProperties, "MENU_OPTION_DROITS_FORTUNE_USUELLE",
                    "MENU_ONGLET_DROITS_BIEN_IMMOBILIER_NSPHP", IPCActions.ACTION_DROIT_BIEN_IMMOBILIER_NSPHP,
                    PegasusImplServiceLocator.getBienImmobilierHabitationNonPrincipaleService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCBienImmoAnnexe.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /***************************************** Biens immo non habitables ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoNonHabitableMontantRendement",
                "JSP_CMS_64007033_MONTANTRENDEMENT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoNonHabitableCsTypePropriete",
                "JSP_CMS_64007033_TYPEPROP", 2, true));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoNonHabitableMontantInteretHypothecaire",
                "JSP_CMS_64007033_INTHYPO", 4));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("bienImmoNonHabitableMontantDetteHypothecaire",
                "JSP_CMS_64007033_DETTEYPO", 3));
        try {
            descripteur = new DonneeFinanciereDescriptor(16, dfProperties, "MENU_OPTION_DROITS_FORTUNE_USUELLE",
                    "MENU_ONGLET_DROITS_BIEN_IMMOBILIER_NH", IPCActions.ACTION_DROIT_BIEN_IMMOBILIER_NH,
                    PegasusImplServiceLocator.getBienImmobilierNonHabitableService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCBienImmoNonHabitable.CS_TYPE_DONNEE_FINANCIERE, descripteur);

        /******************************************** ### FORTUNE PARTICULIERES ### ****************************************************/
        /***************************************** Prest envers tiers ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("pretEnversTiersMontant", "JSP_CMS_64007015_MONTANT",
                1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("pretEnversTiersMontantInterets",
                "JSP_CMS_64007015_INTERETS", 2));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("pretEnversTiersCsTypePropriete",
                "JSP_CMS_64007015_TYPEPROP", 3, true));

        try {
            descripteur = new DonneeFinanciereDescriptor(17, dfProperties, "MENU_OPTION_DROITS_FORTUNE_PARTICULIERE",
                    "MENU_ONGLET_DROITS_PRET_ENVERS_TIERS", IPCActions.ACTION_DROIT_PRET_ENVERS_TIERS,
                    PegasusImplServiceLocator.getPretEnversTiersService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCPretEnversTiers.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /***************************************** Assurance rente viagère ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("assuranceRenteViagereMontant",
                "JSP_CMS_64007016_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("assuranceRenteViagereMontantValeurRachat",
                "JSP_CMS_64007016_RACHAT", 2));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("assuranceRenteViagereExcedant",
                "JSP_CMS_64007016_EXCEDANT", 3));

        try {
            descripteur = new DonneeFinanciereDescriptor(18, dfProperties, "MENU_OPTION_DROITS_FORTUNE_PARTICULIERE",
                    "MENU_ONGLET_DROITS_ASSURANCE_RENTE_VIAGERE", IPCActions.ACTION_DROIT_ASSURANCE_RENTE_VIAGERE,
                    PegasusImplServiceLocator.getAssuranceRenteViagereService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCAssuranceRenteViagere.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /***************************************** Numéraires ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("numeraireMontant", "JSP_CMS_64007017_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("numeraireMontantInterets",
                "JSP_CMS_64007017_INTERETS", 2));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("numeraireCsTypePropriete",
                "JSP_CMS_64007017_PROPRIETE", 3, true));

        try {
            descripteur = new DonneeFinanciereDescriptor(19, dfProperties, "MENU_OPTION_DROITS_FORTUNE_PARTICULIERE",
                    "MENU_ONGLET_DROITS_NUMERAIRES", IPCActions.ACTION_DROIT_NUMERAIRE,
                    PegasusImplServiceLocator.getNumeraireService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCNumeraire.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);
        /***************************************** Marchandise stocks ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("marchandiseStockMontant",
                "JSP_CMS_64007018_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("marchandiseStockCsTypePropriete",
                "JSP_CMS_64007018_PROPRIETE", 2, true));

        try {
            descripteur = new DonneeFinanciereDescriptor(20, dfProperties, "MENU_OPTION_DROITS_FORTUNE_PARTICULIERE",
                    "MENU_ONGLET_DROITS_MARCHANDISES_STOCK", IPCActions.ACTION_DROIT_MARCHANDISES_STOCK,
                    PegasusImplServiceLocator.getMarchandisesStockService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCMarchandiseStock.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /***************************************** Vehicules ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("vehiculeMontant", "JSP_CMS_64007019_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("vehiculeCsTypePropriete",
                "JSP_CMS_64007019_PROPRIETE", 2, true));

        try {
            descripteur = new DonneeFinanciereDescriptor(21, dfProperties, "MENU_OPTION_DROITS_FORTUNE_PARTICULIERE",
                    "MENU_ONGLET_DROITS_VEHICULE", IPCActions.ACTION_DROIT_VEHICULE,
                    PegasusImplServiceLocator.getVehiculeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCVehicule.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);
        /***************************************** betails ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("betailMontant", "JSP_CMS_64007020_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("betailCsTypePropriete",
                "JSP_CMS_64007020_PROPRIETE", 2, true));

        try {
            descripteur = new DonneeFinanciereDescriptor(22, dfProperties, "MENU_OPTION_DROITS_FORTUNE_PARTICULIERE",
                    "MENU_ONGLET_DROITS_BETAIL", IPCActions.ACTION_DROIT_BETAIL,
                    PegasusImplServiceLocator.getBetailService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(IPCBetail.CS_TYPE_DONNEE_FINANCIERE,
                descripteur);
        /***************************************** Autres fortunes mobi ****************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autreFortuneMobiliereMontant",
                "JSP_CMS_64007021_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autreFortuneMobiliereCsTypePropriete",
                "JSP_CMS_64007021_PROPRIETE", 2, true));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("autreFortuneMobiliereCsTypeFortune",
                "JSP_CMS_64007021_PROPRIETE", 3, true));

        try {
            descripteur = new DonneeFinanciereDescriptor(23, dfProperties, "MENU_OPTION_DROITS_FORTUNE_PARTICULIERE",
                    "MENU_ONGLET_DROITS_AUTRES_FORTUNES", IPCActions.ACTION_DROIT_AUTRE_FORTUNE_MOBILIERE,
                    PegasusImplServiceLocator.getAutreFortuneMobiliereService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCAutreFortuneMobiliere.CS_TYPE_DONNEE_FINANCIERE, descripteur);

        /******************************************** ### REVENU DEPENSES ### ****************************************************/
        /************************************** Activite lucrative dependante ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("revenuActiviteLucrativeDependanteMontant",
                "JSP_CMS_64007031_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor(
                "revenuActiviteLucrativeDependanteDeductionsSociales", "JSP_CMS_64007031_DEDSOCIALES", 2));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("revenuActiviteLucrativeDependanteDeductionsLPP",
                "JSP_CMS_64007031_DEDLPP", 3));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor(
                "revenuActiviteLucrativeDependanteMontantFraisEffectifs", "JSP_CMS_64007031_FRAISEFFECTIFS", 4));
        try {
            descripteur = new DonneeFinanciereDescriptor(24, dfProperties, "MENU_OPTION_DROITS_REVENUS_DEPENSES",
                    "MENU_ONGLET_DROITS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE",
                    IPCActions.ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE,
                    PegasusImplServiceLocator.getRevenuActiviteLucrativeDependanteService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCRevenuActiviteDependante.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************** Activite lucrative independante ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("revenuActiviteLucrativeIndependanteMontant",
                "JSP_CMS_64007022_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("revenuActiviteLucrativeIndependanteCSGenreRevenu",
                "JSP_CMS_64007022_GENREREVENU", 2, true));

        try {
            descripteur = new DonneeFinanciereDescriptor(25, dfProperties, "MENU_OPTION_DROITS_REVENUS_DEPENSES",
                    "MENU_ONGLET_DROITS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE",
                    IPCActions.ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE,
                    PegasusImplServiceLocator.getRevenuActiviteLucrativeIndependanteService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCRevenuActiviteIndependante.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************** revenu hypo ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("revenuHypothetiqueMontantRevenuNet",
                "JSP_CMS_64007023_MONTANTREVNET", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("revenuHypothetiqueMontantRevenuBrut",
                "JSP_CMS_64007023_MONTANTREVBRUT", 2));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("revenuHypothetiqueMontantDeductionsSociales",
                "JSP_CMS_64007023_DEDSOC", 3));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("revenuHypothetiqueMontantFraisGarde",
                "JSP_CMS_64007023_FRAISGARDE", 4));
        try {
            descripteur = new DonneeFinanciereDescriptor(26, dfProperties, "MENU_OPTION_DROITS_REVENUS_DEPENSES",
                    "MENU_ONGLET_DROITS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE",
                    IPCActions.ACTION_DROIT_REVENU_HYPOTHETIQUE,
                    PegasusImplServiceLocator.getRevenuHypothetiqueService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCRevenuHypothetique.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************** allocations familiales ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("allocationFamilialeMontantMensuel",
                "JSP_CMS_64007024_MONTANT", 1));
        try {
            descripteur = new DonneeFinanciereDescriptor(27, dfProperties, "MENU_OPTION_DROITS_REVENUS_DEPENSES",
                    "MENU_ONGLET_DROITS_ALLOCATIONS_FAMILIALES", IPCActions.ACTION_DROIT_ALLOCATIONS_FAMILIALES,
                    PegasusImplServiceLocator.getAllocationsFamilialesService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCAllocationsFamiliales.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************** contrat entretien viager ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("contratEntretienViagerMontant",
                "JSP_CMS_64007025_MONTANTMENS", 1));
        try {
            descripteur = new DonneeFinanciereDescriptor(28, dfProperties, "MENU_OPTION_DROITS_REVENUS_DEPENSES",
                    "MENU_ONGLET_DROITS_CONTRAT_ENTRETIEN_VIAGER", IPCActions.ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER,
                    PegasusImplServiceLocator.getContratEntretienViagerService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCContratEntretienViager.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************** autres revenus ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("autresRevenusMontant", "JSP_CMS_64007026_MONTANT", 1));
        dfProperties
                .add(new DonneeFinancierePropertiesDescriptor("AutresRevenusLibelle", "JSP_CMS_64007026_LIBELLE", 2));
        try {
            descripteur = new DonneeFinanciereDescriptor(29, dfProperties, "MENU_OPTION_DROITS_REVENUS_DEPENSES",
                    "MENU_ONGLET_DROITS_AUTRES_REVENUS", IPCActions.ACTION_DROIT_AUTRES_REVENUS,
                    PegasusImplServiceLocator.getAutresRevenusService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCAutresRevenus.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************** PSAL ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("cotisationPSALMontantAnnuel",
                "JSP_CMS_64007027_MONTANTAN", 1));
        try {
            descripteur = new DonneeFinanciereDescriptor(30, dfProperties, "MENU_OPTION_DROITS_REVENUS_DEPENSES",
                    "MENU_ONGLET_DROITS_COTISATIONS_PSAL", IPCActions.ACTION_DROIT_COTISATIONS_PSAL,
                    PegasusImplServiceLocator.getCotisationsPsalService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCCotisationPSAL.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************** pension alimentaire ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("pensionAlimentaireMontant",
                "JSP_CMS_64007026_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("pensionAlimentaireCsTypePension",
                "JSP_CMS_64007026_TYPEPENSION", 2, true));

        dfProperties.add(new DonneeFinancierePropertiesDescriptor("pensionAlimentaireMontantRenteEnfant",
                "JSP_CMS_64007026_MONTANTRENTEENFANT", 4));
        try {
            descripteur = new DonneeFinanciereDescriptor(31, dfProperties, "MENU_OPTION_DROITS_REVENUS_DEPENSES",
                    "MENU_ONGLET_DROITS_PENSION_ALIMENTAIRE", IPCActions.ACTION_DROIT_PENSION_ALIMENTAIRE,
                    PegasusImplServiceLocator.getPensionAlimentaireService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCPensionAlimentaire.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /******************************************** ### DESSAISISSEMENTS ### ****************************************************/
        /************************************** dess revenus ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("dessaisissementRevenuMontant",
                "JSP_CMS_64007030_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("dessaisissementRevenuDeductions",
                "JSP_CMS_64007030_DEDUCTIONS", 2));
        try {
            descripteur = new DonneeFinanciereDescriptor(32, dfProperties, "MENU_OPTION_DROITS_DESSAISISSEMENTS",
                    "MENU_ONGLET_DROITS_DESSAISISSEMENT_REVENU", IPCActions.ACTION_DROIT_DESSAISISSEMENT_REVENU,
                    PegasusImplServiceLocator.getDessaisissementRevenuService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCDessaisissementRevenu.CS_TYPE_DONNEE_FINANCIERE, descripteur);
        /************************************** dess fortune ***************************************************/
        dfProperties = new ArrayList<DonneeFinancierePropertiesDescriptor>();
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("dessaisissementFortuneMontant",
                "JSP_CMS_64007029_MONTANT", 1));
        dfProperties.add(new DonneeFinancierePropertiesDescriptor("dessaisissementFortuneDeductions",
                "JSP_CMS_64007029_DEDUCTIONS", 2));
        try {
            descripteur = new DonneeFinanciereDescriptor(33, dfProperties, "MENU_OPTION_DROITS_DESSAISISSEMENTS",
                    "MENU_ONGLET_DROITS_DESSAISISSEMENT_REVENU", IPCActions.ACTION_DROIT_DESSAISISSEMENT_FORTUNE,
                    PegasusImplServiceLocator.getDessaisissementFortuneService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(null, "Service not vailable " + e.getMessage());
        }
        DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor.put(
                IPCDessaisissementFortune.CS_TYPE_DONNEE_FINANCIERE, descripteur);

    }

    /**
     * Retourne la collection sous forme de <code>java.util.Map</code> contenant le mapping de configuration des
     * donnéees financières à afficher
     * 
     * @return la collection de mapping des propriétés des donnés fianacières
     */
    public static Map<String, DonneeFinanciereDescriptor> getConfigurationMap() {

        return DonneesFinancieresDescriptorProvider.donneFinanciereDroitDescriptor;
    }
}
