package globaz.tucana.statistiques.config;

/**
 * Classe représentant le noeud catégorie rubrique
 * 
 * @author fgo date de création : 16 août 06
 * @version : version 1.0
 * 
 */
public class TUCategorieRubriqueStatistiqueConfig extends TUCommonStatistiqueConfig {
    private boolean affiche = true;
    private String group = null;
    private String nom = null;
    private String order = null;
    private String signe = null;

    /**
     * Constructeur
     * 
     * @param _id
     * @param _csLabel
     * @param _nom
     * @param _signe
     * @param _affiche
     * @param _order
     * @param _group
     */
    protected TUCategorieRubriqueStatistiqueConfig(String _id, boolean _csLabel, String _nom, String _signe,
            boolean _affiche, String _order, String _group) {
        super(_id, _csLabel);
        nom = _nom;
        signe = _signe;
        affiche = _affiche;
        order = _order;
        group = _group;

    }

    /**
     * Récuère le groupe
     * 
     * @return
     */
    public String getGroup() {
        return group;
    }

    /**
     * Récupère le nom
     * 
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * Récupère le numéro d'ordre
     * 
     * @return
     */
    public String getOrder() {
        return order;
    }

    /**
     * Récupère le signe
     * 
     * @return
     */
    public String getSigne() {
        return signe;
    }

    /**
     * Récupère l'affichage
     * 
     * @return
     */
    public boolean isAffiche() {
        return affiche;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.statistiques.config.TUCommonStatistiqueConfig#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer(super.toString());
        str.append("\n nom : ").append(nom);
        str.append("\n signe : ").append(signe);
        str.append("\n affiché : ").append(affiche);
        str.append("\n ordre : ").append(order);
        str.append("\n groupe : ").append(group);

        return str.toString();
    }

}
