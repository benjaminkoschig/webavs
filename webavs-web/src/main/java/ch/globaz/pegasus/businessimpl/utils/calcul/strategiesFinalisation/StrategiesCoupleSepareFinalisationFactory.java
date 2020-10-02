package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu.StrategieFinalRevenuAutresCommun;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu.StrategieFinalRevenuAutresSeul;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu.StrategieFinalRevenuImmobiliereCommun;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu.StrategieFinalRevenuImmobiliereSeul;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu.StrategieFinalRevenuSuppressionSeul;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.*;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu.*;

public class StrategiesCoupleSepareFinalisationFactory {

    private static List<StrategieCalculFinalisation> strategiesAvecEnfantsRevenu = new ArrayList<StrategieCalculFinalisation>();
    private static List<StrategieCalculFinalisation> strategiesRevenuCommun = new ArrayList<StrategieCalculFinalisation>();
    private static List<StrategieCalculFinalisation> strategiesSeulDepense = new ArrayList<StrategieCalculFinalisation>();

    static {
        // depense
        StrategiesCoupleSepareFinalisationFactory.strategiesSeulDepense.add(new StrategieFinalDepenseLoyer());
        StrategiesCoupleSepareFinalisationFactory.strategiesSeulDepense.add(new StrategieFinalDepenseHome());
        StrategiesCoupleSepareFinalisationFactory.strategiesSeulDepense.add(new StrategieFinalPrimeAssuranceMaladie());
        StrategiesCoupleSepareFinalisationFactory.strategiesSeulDepense
                .add(new ProxyStrategieFinalDepensesFraisImmobilier());
        StrategiesCoupleSepareFinalisationFactory.strategiesSeulDepense.add(new ProxyFinalDepenseTotalReconnu());

        // revenu commun
        StrategiesCoupleSepareFinalisationFactory.strategiesRevenuCommun.add(new StrategieFinalRevenuMobiliere());
        StrategiesCoupleSepareFinalisationFactory.strategiesRevenuCommun
                .add(new StrategieFinalRevenuImmobiliereCommun());
        StrategiesCoupleSepareFinalisationFactory.strategiesRevenuCommun
                .add(new StrategieFinalRevenuActiviteLucrative());
        StrategiesCoupleSepareFinalisationFactory.strategiesRevenuCommun
                .add(new StrategieFinalRevenuActiviteLucrativeConjoint());
        StrategiesCoupleSepareFinalisationFactory.strategiesRevenuCommun
                .add(new StrategieFinalRevenuActiviteLucrativeEnfant());
        StrategiesCoupleSepareFinalisationFactory.strategiesRevenuCommun.add(new StrategieFinalRevenuAutresRentes());
        StrategiesCoupleSepareFinalisationFactory.strategiesRevenuCommun.add(new StrategieFinalRevenuAutresCommun());

        // revenu propre
        StrategiesCoupleSepareFinalisationFactory.strategiesAvecEnfantsRevenu
                .add(new StrategieFinalRevenuSuppressionSeul());
        StrategiesCoupleSepareFinalisationFactory.strategiesAvecEnfantsRevenu
                .add(new StrategieFinalRevenuImmobiliereSeul());
        StrategiesCoupleSepareFinalisationFactory.strategiesAvecEnfantsRevenu.add(new StrategieFinalRevenuAPI());
        StrategiesCoupleSepareFinalisationFactory.strategiesAvecEnfantsRevenu.add(new StrategieFinalRevenuAutresSeul());
        StrategiesCoupleSepareFinalisationFactory.strategiesAvecEnfantsRevenu.add(new StrategieFinalRevenuSubsideAssuranceMaladie());
        StrategiesCoupleSepareFinalisationFactory.strategiesAvecEnfantsRevenu.add(new StrategieFinalRevenuActiviteLucrativeEnfant());

    }

    public static StrategiesCoupleSepareFinalisationFactory getFactoryAvecEnfantsRevenu() {
        return new StrategiesCoupleSepareFinalisationFactory(
                StrategiesCoupleSepareFinalisationFactory.strategiesAvecEnfantsRevenu);
    }

    public static StrategiesCoupleSepareFinalisationFactory getFactoryRevenuCommun() {
        return new StrategiesCoupleSepareFinalisationFactory(
                StrategiesCoupleSepareFinalisationFactory.strategiesRevenuCommun);
    }

    public static StrategiesCoupleSepareFinalisationFactory getFactorySeulDepense() {
        return new StrategiesCoupleSepareFinalisationFactory(
                StrategiesCoupleSepareFinalisationFactory.strategiesSeulDepense);
    }

    private List<StrategieCalculFinalisation> strategies;

    private StrategiesCoupleSepareFinalisationFactory(List<StrategieCalculFinalisation> strategies) {
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
