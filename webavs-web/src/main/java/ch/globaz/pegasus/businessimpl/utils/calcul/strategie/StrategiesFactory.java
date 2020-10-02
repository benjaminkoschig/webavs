package ch.globaz.pegasus.businessimpl.utils.calcul.strategie;

import java.util.HashMap;

import ch.globaz.pegasus.business.constantes.donneesfinancieres.*;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieFraisGarde;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategiePrimeAssuranceMaladie;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieSejourMoisPartiel;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieSubsideAssuranceMaladie;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieAssuranceRenteViagere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieAutreFortuneMobiliere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieAutresDettesProuvees;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieCapitalLPP;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieCompteBancaireCPP;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieDessaisissementFortune;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieMarchandiseStock;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategiePretEnversTiers;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieVehicule;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.*;

public class StrategiesFactory {
    private static StrategiesFactory depenseFactory;
    private static StrategiesFactory fortuneFactory = null;
    private static StrategiesFactory revenuFactory;
    private static StrategiesFactory revenuConjointFactory;
    private static StrategiesFactory revenuEnfantFactory;
    private static StrategiesFactory revenuMixteFactory = null;

    public static final StrategiesFactory getDepenseFactory() {
        if (StrategiesFactory.depenseFactory == null) {
            StrategiesFactory.depenseFactory = new StrategiesFactory();

            // ajout des stratégies

            StrategiesFactory.depenseFactory.container
                    .put(IPCBienImmoPrincipal.CS_TYPE_DONNEE_FINANCIERE,
                            new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.ProxyStrategieBienImmoPrincipal());

            StrategiesFactory.depenseFactory.container.put(IPCBienImmoAnnexe.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.ProxyStrategieBienImmoAnnexe());

            StrategiesFactory.depenseFactory.container
                    .put(IPCBienImmoNonHabitable.CS_TYPE_DONNEE_FINANCIERE,
                            new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.ProxyStrategieBienImmoNonHabitable());

            StrategiesFactory.depenseFactory.container.put(IPCCotisationPSAL.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieCotisationPSAL());

            StrategiesFactory.depenseFactory.container.put(IPCFraisGarde.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieFraisGarde());

            StrategiesFactory.depenseFactory.container.put(IPCPrimeAssuranceMaladie.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategiePrimeAssuranceMaladie());

            StrategiesFactory.depenseFactory.container.put(IPCPensionAlimentaire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategiePensionAlimentaire());

            StrategiesFactory.depenseFactory.container.put(IPCLoyer.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieLoyer());

            StrategiesFactory.depenseFactory.container.put(IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieTaxeJournaliere());

            StrategiesFactory.depenseFactory.container.put(IPCSejourMoisPartielHome.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieSejourMoisPartiel());
        }
        return StrategiesFactory.depenseFactory;
    }

    public static final StrategiesFactory getFortuneFactory() {
        if (StrategiesFactory.fortuneFactory == null) {
            StrategiesFactory.fortuneFactory = new StrategiesFactory();

            // ajout des strategies
            StrategiesFactory.fortuneFactory.container.put(IPCBetail.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieBetail());
            StrategiesFactory.fortuneFactory.container.put(IPCAutresDettesProuvees.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresDettesProuvees());
            StrategiesFactory.fortuneFactory.container.put(IPCNumeraire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieCalculNumeraire());
            StrategiesFactory.fortuneFactory.container.put(IPCPretEnversTiers.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategiePretEnversTiers());
            StrategiesFactory.fortuneFactory.container.put(IPCMarchandiseStock.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieMarchandiseStock());
            StrategiesFactory.fortuneFactory.container.put(IPCVehicule.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieVehicule());

            StrategiesFactory.fortuneFactory.container.put(IPCAssuranceRenteViagere.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAssuranceRenteViagere());

            StrategiesFactory.fortuneFactory.container.put(IPCCapitalLPP.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieCapitalLPP());

            StrategiesFactory.fortuneFactory.container.put(IPCCompteBancaireCPP.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieCompteBancaireCPP());

            StrategiesFactory.fortuneFactory.container.put(IPCAutreFortuneMobiliere.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutreFortuneMobiliere());

            StrategiesFactory.fortuneFactory.container.put(IPCTitre.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieTitre());

            StrategiesFactory.fortuneFactory.container.put(IPCBienImmoPrincipal.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieBienImmoPrincipal());

            StrategiesFactory.fortuneFactory.container.put(IPCBienImmoAnnexe.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieBienImmoAnnexe());

            StrategiesFactory.fortuneFactory.container.put(IPCBienImmoNonHabitable.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieBienImmoNonHabitable());

            StrategiesFactory.fortuneFactory.container.put(IPCAssuranceVie.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieAssuranceVie());

            StrategiesFactory.fortuneFactory.container
                    .put(IPCDessaisissementFortune.CS_TYPE_DONNEE_FINANCIERE,
                            new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieDessaisissementFortune());
        }
        return StrategiesFactory.fortuneFactory;
    }

    public static final StrategiesFactory getRevenuConjointFacotry() {
        if (StrategiesFactory.revenuConjointFactory == null) {
            StrategiesFactory.revenuConjointFactory = new StrategiesFactory();
            // ajout des strategies spécifique conjoint
            StrategiesFactory.revenuConjointFactory.container.put(IPCRevenuActiviteDependante.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuActiviteDependanteConjoint());

            StrategiesFactory.revenuConjointFactory.container.put(IPCRevenuActiviteIndependante.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuActiviteIndependanteConjoint());

            StrategiesFactory.revenuConjointFactory.container.put(IPCRevenuHypothetique.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuHypothetiqueConjoint());

            // ajout des strategies communes
            StrategiesFactory.revenuConjointFactory.container.put(IPCAutresAPI.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresAPI());

            StrategiesFactory.revenuConjointFactory.container.put(IPCAutresRentes.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresRentes());

            StrategiesFactory.revenuConjointFactory.container.put(IPCAutresRevenus.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresRevenus());

            StrategiesFactory.revenuConjointFactory.container.put(IPCAssuranceRenteViagere.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAssuranceRenteViagere());

            StrategiesFactory.revenuConjointFactory.container.put(IPCAllocationsFamiliales.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAllocationsFamiliales());

            StrategiesFactory.revenuConjointFactory.container.put(IPCRenteAvsAi.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRenteAVSAI());

            StrategiesFactory.revenuConjointFactory.container.put(IPCNumeraire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCalculNumeraire());

            StrategiesFactory.revenuConjointFactory.container.put(IPCBienImmoPrincipal.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoPrincipal());

            StrategiesFactory.revenuConjointFactory.container.put(IPCBienImmoAnnexe.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoAnnexe());

            StrategiesFactory.revenuConjointFactory.container.put(IPCBienImmoNonHabitable.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoNonHabitable());

            StrategiesFactory.revenuConjointFactory.container.put(IPCCapitalLPP.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCapitalLPP());

            StrategiesFactory.revenuConjointFactory.container.put(IPCCompteBancaireCPP.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCompteBancaireCPP());

            StrategiesFactory.revenuConjointFactory.container.put(IPCContratRenteViager.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieContratRenteViager());

            StrategiesFactory.revenuConjointFactory.container.put(IPCDessaisissementRevenu.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieDessaisissementRevenu());

            StrategiesFactory.revenuConjointFactory.container.put(IPCIJAI.CS_TYPE_DONNEE_FINANCIERE, new StrategieIJAI());

            StrategiesFactory.revenuConjointFactory.container.put(IPCIJAPG.CS_TYPE_DONNEE_FINANCIERE, new StrategieIJAPG());

            StrategiesFactory.revenuConjointFactory.container.put(IPCPensionAlimentaire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategiePensionAlimentaire());

            StrategiesFactory.revenuConjointFactory.container.put(IPCTitre.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieTitre());

            StrategiesFactory.revenuConjointFactory.container.put(IPCPretEnversTiers.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategiePretEnversTiers());

            StrategiesFactory.revenuConjointFactory.container.put(IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAllocationImpotent());

            StrategiesFactory.revenuConjointFactory.container.put(IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieTaxeJournaliere());

            StrategiesFactory.revenuConjointFactory.container.put(IPCLoyer.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieLoyer());

            StrategiesFactory.revenuConjointFactory.container.put(IPCSubsideAssuranceMaladie.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieSubsideAssuranceMaladie());
        }
        return StrategiesFactory.revenuConjointFactory;
    }

    public static final StrategiesFactory getRevenuEnfantFacotry() {
        if (StrategiesFactory.revenuEnfantFactory == null) {
            StrategiesFactory.revenuEnfantFactory = new StrategiesFactory();
            // ajout des strategies spécifique conjoint
            StrategiesFactory.revenuEnfantFactory.container.put(IPCRevenuActiviteDependante.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuActiviteDependanteEnfant());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCRevenuActiviteIndependante.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuActiviteIndependanteEnfant());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCRevenuHypothetique.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuHypothetiqueEnfant());

            // ajout des strategies communes
            StrategiesFactory.revenuEnfantFactory.container.put(IPCAutresAPI.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresAPI());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCAutresRentes.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresRentes());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCAutresRevenus.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresRevenus());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCAssuranceRenteViagere.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAssuranceRenteViagere());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCAllocationsFamiliales.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAllocationsFamiliales());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCRenteAvsAi.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRenteAVSAI());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCNumeraire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCalculNumeraire());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCBienImmoPrincipal.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoPrincipal());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCBienImmoAnnexe.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoAnnexe());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCBienImmoNonHabitable.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoNonHabitable());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCCapitalLPP.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCapitalLPP());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCCompteBancaireCPP.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCompteBancaireCPP());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCContratRenteViager.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieContratRenteViager());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCDessaisissementRevenu.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieDessaisissementRevenu());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCIJAI.CS_TYPE_DONNEE_FINANCIERE, new StrategieIJAI());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCIJAPG.CS_TYPE_DONNEE_FINANCIERE, new StrategieIJAPG());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCPensionAlimentaire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategiePensionAlimentaire());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCTitre.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieTitre());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCPretEnversTiers.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategiePretEnversTiers());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAllocationImpotent());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieTaxeJournaliere());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCLoyer.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieLoyer());

            StrategiesFactory.revenuEnfantFactory.container.put(IPCSubsideAssuranceMaladie.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieSubsideAssuranceMaladie());
        }
        return StrategiesFactory.revenuEnfantFactory;
    }

    public static final StrategiesFactory getRevenuFactory() {
        if (StrategiesFactory.revenuFactory == null) {
            StrategiesFactory.revenuFactory = new StrategiesFactory();

            // ajout des stratégies
            StrategiesFactory.revenuFactory.container.put(IPCAutresAPI.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresAPI());

            StrategiesFactory.revenuFactory.container.put(IPCAutresRentes.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresRentes());

            StrategiesFactory.revenuFactory.container.put(IPCAutresRevenus.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAutresRevenus());

            StrategiesFactory.revenuFactory.container.put(IPCAssuranceRenteViagere.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAssuranceRenteViagere());

            StrategiesFactory.revenuFactory.container.put(IPCAllocationsFamiliales.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieAllocationsFamiliales());

            StrategiesFactory.revenuFactory.container.put(IPCRenteAvsAi.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRenteAVSAI());

            StrategiesFactory.revenuFactory.container.put(IPCNumeraire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCalculNumeraire());

            StrategiesFactory.revenuFactory.container.put(IPCBienImmoPrincipal.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoPrincipal());

            StrategiesFactory.revenuFactory.container.put(IPCBienImmoAnnexe.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoAnnexe());

            StrategiesFactory.revenuFactory.container.put(IPCBienImmoNonHabitable.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieBienImmoNonHabitable());

            StrategiesFactory.revenuFactory.container.put(IPCCapitalLPP.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCapitalLPP());

            StrategiesFactory.revenuFactory.container.put(IPCCompteBancaireCPP.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCompteBancaireCPP());

            StrategiesFactory.revenuFactory.container.put(IPCContratRenteViager.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieContratRenteViager());

            StrategiesFactory.revenuFactory.container.put(IPCDessaisissementRevenu.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieDessaisissementRevenu());

            StrategiesFactory.revenuFactory.container.put(IPCIJAI.CS_TYPE_DONNEE_FINANCIERE, new StrategieIJAI());

            StrategiesFactory.revenuFactory.container.put(IPCIJAPG.CS_TYPE_DONNEE_FINANCIERE, new StrategieIJAPG());

            StrategiesFactory.revenuFactory.container.put(IPCPensionAlimentaire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategiePensionAlimentaire());

            StrategiesFactory.revenuFactory.container.put(IPCRevenuActiviteDependante.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuActiviteDependante());

            StrategiesFactory.revenuFactory.container.put(IPCRevenuActiviteIndependante.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuActiviteIndependante());

            StrategiesFactory.revenuFactory.container.put(IPCRevenuHypothetique.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieRevenuHypothetique());

            StrategiesFactory.revenuFactory.container.put(IPCTitre.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieTitre());

            StrategiesFactory.revenuFactory.container.put(IPCPretEnversTiers.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategiePretEnversTiers());

            StrategiesFactory.revenuFactory.container.put(IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAllocationImpotent());

            StrategiesFactory.revenuFactory.container.put(IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieTaxeJournaliere());

            StrategiesFactory.revenuFactory.container.put(IPCLoyer.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieLoyer());

            StrategiesFactory.revenuFactory.container.put(IPCSubsideAssuranceMaladie.CS_TYPE_DONNEE_FINANCIERE,
                    new StrategieSubsideAssuranceMaladie());

        }
        return StrategiesFactory.revenuFactory;
    }

    public static final StrategiesFactory getRevenuMixteFactory() {
        if (StrategiesFactory.revenuMixteFactory == null) {
            StrategiesFactory.revenuMixteFactory = new StrategiesFactory();
        }

        StrategiesFactory.revenuMixteFactory.mixteContainer
                .put(IPCPensionAlimentaire.CS_TYPE_DONNEE_FINANCIERE,
                        new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.mixte.StrategiePensionAlimentaireMixte());

        return StrategiesFactory.revenuMixteFactory;
    }

    public static StrategieCalcul getStrategieDessaisissementFortune() {
        return new StrategieDessaisissementFortune();
    }

    public static StrategieCalcul getStrategieDessaisissementRevenu() {
        return new StrategieDessaisissementRevenu();
    }

    private HashMap<String, Object> container = new HashMap<String, Object>();

    private HashMap<String, Object> mixteContainer = new HashMap<String, Object>();

    /**
	 * 
	 */
    private StrategiesFactory() {
        super();
    }

    public StrategieCalcul getStrategie(String csTypeDonneeFinanciere, String dateValidite) throws CalculException {
        Object obj = container.get(csTypeDonneeFinanciere);
        if (obj == null) {
            return null;
        }
        if (obj instanceof StrategieCalcul) {
            return (StrategieCalcul) obj;
        } else {
            return ((StrategieCalculFactory) obj).getStrategie(csTypeDonneeFinanciere, dateValidite);
        }
    }

    public StrategieCalcul getStrategieMixte(String csTypeDonneeFinanciere, String dateValidite) throws CalculException {
        Object obj = mixteContainer.get(csTypeDonneeFinanciere);
        if (obj == null) {
            return null;
        }
        if (obj instanceof StrategieCalcul) {
            return (StrategieCalcul) obj;
        } else {
            return ((StrategieCalculFactory) obj).getStrategie(csTypeDonneeFinanciere, dateValidite);
        }
    }

}
