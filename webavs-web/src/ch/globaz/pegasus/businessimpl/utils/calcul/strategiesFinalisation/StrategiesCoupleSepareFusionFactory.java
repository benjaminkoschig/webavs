package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion.StrategieFusionDepenses;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion.StrategieFusionRevenuTotalDeterminant;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion.StrategieFusionRevenus;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion.StrategieFusionTotal;

public class StrategiesCoupleSepareFusionFactory {

    private static List<StrategieCalculFusion> strategiesFinalFusion = new ArrayList<StrategieCalculFusion>();

    static {

        // revenu propre
        StrategiesCoupleSepareFusionFactory.strategiesFinalFusion.add(new StrategieFusionRevenus());
        StrategiesCoupleSepareFusionFactory.strategiesFinalFusion.add(new StrategieFusionDepenses());

        StrategiesCoupleSepareFusionFactory.strategiesFinalFusion.add(new StrategieFusionRevenuTotalDeterminant());
        StrategiesCoupleSepareFusionFactory.strategiesFinalFusion.add(new StrategieFusionTotal());
    }

    public static StrategiesCoupleSepareFusionFactory getFactoryFusion() {
        return new StrategiesCoupleSepareFusionFactory(StrategiesCoupleSepareFusionFactory.strategiesFinalFusion);
    }

    private List<StrategieCalculFusion> strategies;

    private StrategiesCoupleSepareFusionFactory(List<StrategieCalculFusion> strategies) {
        super();
        this.strategies = strategies;
    }

    /**
     * @return the strategies
     */
    public List<StrategieCalculFusion> getStrategies() {
        return strategies;
    }

}
