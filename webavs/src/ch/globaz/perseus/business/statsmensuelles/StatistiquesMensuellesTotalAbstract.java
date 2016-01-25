package ch.globaz.perseus.business.statsmensuelles;

/**
 * Cette classe abstraite implémente le comptage des totaux pour les statistiques
 * 
 * @author RCO
 * 
 */
public abstract class StatistiquesMensuellesTotalAbstract implements StatistiquesMensuellesDataMonitoringInterface {

    private int total = 0;

    @Override
    public void compter(StatistiquesMensuellesComptageMontantInterface statDemPcfDec) {
        incrementeTotal();
    }

    /**
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
	 * 
	 */
    public void incrementeTotal() {
        total++;
    }
}
