package ch.globaz.perseus.business.statsmensuelles;

/**
 * @author RCO
 *         Pour que les statistiques puissent �tre compt�s, cette interface doit imp�rativement faire partie de
 *         l'h�ritage.
 *         V�rifier, avant d'impl�menter cette interface qu'il n'extiste pas de classe abstraite correspondant � vos
 *         besoins.
 *         Un diagramme de classe est disponible pour vous permettre de visualiser la hi�rarchie de l'h�ritage.
 */
public interface StatistiquesMensuellesDataMonitoringInterface {
    /**
     * Fonction de comptage des statistiques
     * 
     * @param statDemPcfDec
     */
    public void compter(StatistiquesMensuellesComptageMontantInterface statDemPcfDec);
}
