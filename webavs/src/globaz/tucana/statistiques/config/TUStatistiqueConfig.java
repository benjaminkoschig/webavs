package globaz.tucana.statistiques.config;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe représenant le noeud statistique au niveau du fichier xml de configuration statistique
 * 
 * @author fgo date de création : 16 août 06
 * @version : version 1.0
 * 
 */
public class TUStatistiqueConfig extends TUCommonStatistiqueConfig {
    private HashMap categoriesRubriquesStatistiquesContainer = null;

    /**
     * Constructeur
     * 
     * @param _id
     * @param _csLabel
     */
    protected TUStatistiqueConfig(String _id, boolean _csLabel) {
        super(_id, _csLabel);
        categoriesRubriquesStatistiquesContainer = new HashMap();
    }

    /**
     * Récupère une categorie-rubrique en fonction d'un id, pour une statistique
     * 
     * @param idCategorieRubrique
     * @return
     */
    public TUCategorieRubriqueStatistiqueConfig getCategorieRubriqueConfig(String idCategorieRubrique) {
        return (TUCategorieRubriqueStatistiqueConfig) categoriesRubriquesStatistiquesContainer.get(idCategorieRubrique);
    }

    /**
     * Permet d'ajouter une configuration de rubriqu
     * 
     * @param id
     * @param currentRubriqueStatistiqueConfig
     */
    public void registerCategorieRubriqueStatistique(String id,
            TUCategorieRubriqueStatistiqueConfig currentRubriqueStatistiqueConfig) {
        categoriesRubriquesStatistiquesContainer.put(id, currentRubriqueStatistiqueConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.statistiques.config.TUCommonStatistiqueConfig#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer(super.toString());
        for (Iterator iter = categoriesRubriquesStatistiquesContainer.values().iterator(); iter.hasNext();) {
            str.append(iter.next());
        }
        return str.toString();
    }
}
