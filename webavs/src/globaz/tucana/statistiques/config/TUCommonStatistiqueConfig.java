package globaz.tucana.statistiques.config;

/**
 * Classe commune pour la configuration des statistiques
 * 
 * @author fgo date de création : 16 août 06
 * @version : version 1.0
 * 
 */
public class TUCommonStatistiqueConfig {
    private boolean csLabel = false;
    private String id = null;

    protected TUCommonStatistiqueConfig() {
        super();
    }

    protected TUCommonStatistiqueConfig(String _id, boolean _csLabel) {
        super();
        id = _id;
        csLabel = _csLabel;
    }

    /**
     * Récupération de l'id
     * 
     * @return
     */
    public boolean getcsLabel() {
        return csLabel;
    }

    /**
     * Récupération du csLabel, permettant l'écriture des labels ou des codes systèmes
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n").append(getClass().getName());
        str.append("\n id : ").append(getId());
        str.append("\n csLabel : ").append(getcsLabel());
        return str.toString();
    }

}
