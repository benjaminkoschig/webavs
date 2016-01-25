package ch.globaz.perseus.business.statsmensuelles;

/**
 * Cette classe abstraite implémente le comptage des statistiques qui font la séparation entre RI (qui vient des
 * services sociaux) et Hors RI (qui ne vient pas des services sociaux)
 * 
 * @author RCO
 * 
 */
public abstract class StatistiquesMensuellesRiHriAbstract extends StatistiquesMensuellesTotalAbstract {
    public String nomStat;
    private int RI;
    private int HRI;

    @Override
    public void compter(StatistiquesMensuellesComptageMontantInterface statDemPcfDec) {
        super.compter(statDemPcfDec);
        if (statDemPcfDec.getDemande().getFromRI()) {
            incrementeRI();
        } else {
            incrementeHorsRI();
        }
    }

    /**
     * @return
     */
    public int getRI() {
        return RI;
    }

    /**
     * @return
     */
    public int getHorsRI() {
        return HRI;
    }

    /**
	 * 
	 */
    public void incrementeRI() {
        RI++;
    }

    /**
	 * 
	 */
    public void incrementeHorsRI() {
        HRI++;
    }
}
