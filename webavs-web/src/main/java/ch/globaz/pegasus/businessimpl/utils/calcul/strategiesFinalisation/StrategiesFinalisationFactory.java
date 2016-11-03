package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.ProxyFinalDepenseTotalReconnu;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.ProxyStrategieFinalDepensesFraisImmobilier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.StrategieFinalDepenseHome;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.StrategieFinalDepenseLoyer;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.ProxyFinalFortuneImmobiliere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.StrategieFinalDessaisissementFortune;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.StrategieFinalFortuneMobiliere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.StrategieFinalFortuneNette;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu.StrategieFinalRevenuAPI;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu.StrategieFinalRevenuActiviteLucrative;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu.StrategieFinalRevenuAutres;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu.StrategieFinalRevenuAutresRentes;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu.StrategieFinalRevenuImmobiliere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu.StrategieFinalRevenuMobiliere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu.StrategieFinalRevenuTotalDeterminant;

public class StrategiesFinalisationFactory {

    private static List<StrategieCalculFinalisation> strategiesCleaning = new ArrayList<StrategieCalculFinalisation>();
    private static List<StrategieCalculFinalisation> strategiesDepense = new ArrayList<StrategieCalculFinalisation>();
    private static List<StrategieCalculFinalisation> strategiesFortune = new ArrayList<StrategieCalculFinalisation>();
    private static List<StrategieCalculFinalisation> strategiesRevenu = new ArrayList<StrategieCalculFinalisation>();
    private static List<StrategieCalculFinalisation> strategiesTotal = new ArrayList<StrategieCalculFinalisation>();
    static {
        // fortune
        StrategiesFinalisationFactory.strategiesFortune.add(new StrategieFinalFortuneMobiliere());
        StrategiesFinalisationFactory.strategiesFortune.add(new ProxyFinalFortuneImmobiliere());
        StrategiesFinalisationFactory.strategiesFortune.add(new StrategieFinalDessaisissementFortune());
        StrategiesFinalisationFactory.strategiesFortune.add(new StrategieFinalFortuneNette());

        // revenu
        StrategiesFinalisationFactory.strategiesRevenu.add(new StrategieFinalRevenuMobiliere());
        StrategiesFinalisationFactory.strategiesRevenu.add(new StrategieFinalRevenuImmobiliere());
        StrategiesFinalisationFactory.strategiesRevenu.add(new StrategieFinalRevenuActiviteLucrative());
        StrategiesFinalisationFactory.strategiesRevenu.add(new StrategieFinalRevenuAutresRentes());
        StrategiesFinalisationFactory.strategiesRevenu.add(new StrategieFinalRevenuAPI());
        StrategiesFinalisationFactory.strategiesRevenu.add(new StrategieFinalRevenuAutres());
        StrategiesFinalisationFactory.strategiesRevenu.add(new StrategieFinalRevenuTotalDeterminant());
        // depense
        StrategiesFinalisationFactory.strategiesDepense.add(new StrategieFinalDepenseLoyer());
        StrategiesFinalisationFactory.strategiesDepense.add(new StrategieFinalDepenseHome());
        StrategiesFinalisationFactory.strategiesDepense.add(new ProxyStrategieFinalDepensesFraisImmobilier());

        // StrategiesFinalisationFactory.strategiesDepense.add(new StrategieFinalDepenseTotalReconnu());
        StrategiesFinalisationFactory.strategiesDepense.add(new ProxyFinalDepenseTotalReconnu());

        // total
        StrategiesFinalisationFactory.strategiesTotal.add(new StrategieFinalTotal());
        // Nettoyage final des clés
        StrategiesFinalisationFactory.strategiesCleaning.add(new StrategieFinalCleaning());
    }

    public static StrategiesFinalisationFactory getFactoryCleaning() {
        return new StrategiesFinalisationFactory(StrategiesFinalisationFactory.strategiesCleaning);
    }

    public static StrategiesFinalisationFactory getFactoryDepense() {
        return new StrategiesFinalisationFactory(StrategiesFinalisationFactory.strategiesDepense);
    }

    public static StrategiesFinalisationFactory getFactoryFortune() {
        return new StrategiesFinalisationFactory(StrategiesFinalisationFactory.strategiesFortune);
    }

    public static StrategiesFinalisationFactory getFactoryRevenu() {
        return new StrategiesFinalisationFactory(StrategiesFinalisationFactory.strategiesRevenu);
    }

    public static StrategiesFinalisationFactory getFactoryTotal() {
        return new StrategiesFinalisationFactory(StrategiesFinalisationFactory.strategiesTotal);
    }

    private List<StrategieCalculFinalisation> strategies;

    private StrategiesFinalisationFactory(List<StrategieCalculFinalisation> strategies) {
        super();
        this.strategies = strategies;
    }

    /**
     * @return the strategies
     */
    public List<StrategieCalculFinalisation> getStrategies() {
        return strategies;
    }

}
