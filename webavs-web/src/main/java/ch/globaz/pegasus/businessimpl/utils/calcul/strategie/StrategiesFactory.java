package ch.globaz.pegasus.businessimpl.utils.calcul.strategie;

import java.util.HashMap;
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
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCContratRenteViager;
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
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieAssuranceRenteViagere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieAutreFortuneMobiliere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieAutresDettesProuvees;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieCapitalLPP;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieCompteBancaireCPP;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieDessaisissementFortune;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieMarchandiseStock;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategiePretEnversTiers;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune.StrategieVehicule;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAllocationsFamiliales;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAutresAPI;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAutresRentes;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAutresRevenus;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieContratRenteViager;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieDessaisissementRevenu;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieIJAI;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieIJAPG;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieRenteAVSAI;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieRevenuActiviteDependante;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieRevenuActiviteIndependante;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieRevenuHypothetique;

public class StrategiesFactory {
    private static StrategiesFactory depenseFactory;
    private static StrategiesFactory fortuneFactory = null;
    private static StrategiesFactory revenuFactory;
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

            StrategiesFactory.depenseFactory.container.put(IPCPensionAlimentaire.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategiePensionAlimentaire());

            StrategiesFactory.depenseFactory.container.put(IPCLoyer.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieLoyer());

            StrategiesFactory.depenseFactory.container.put(IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE,
                    new ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieTaxeJournaliere());
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
