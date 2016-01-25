package ch.globaz.perseus.business.statsmensuelles;

/**
 * @author RCO
 *         Pour que les statistiques puissent être comptés, cette interface doit impérativement faire partie de
 *         l'héritage.
 *         Vérifier, avant d'implémenter cette interface qu'il n'extiste pas de classe abstraite correspondant à vos
 *         besoins.
 *         Un diagramme de classe est disponible pour vous permettre de visualiser la hiérarchie de l'héritage.
 */
public interface StatistiquesMensuellesDataMonitoringInterface {
    /**
     * Fonction de comptage des statistiques
     * 
     * @param statDemPcfDec
     */
    public void compter(StatistiquesMensuellesComptageMontantInterface statDemPcfDec);
}
