package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 13:21:46)
 * 
 * @author: Administrator
 */
public class CASecteurManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * Getter
     */

    /**
     * Setter
     */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CASECOP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CASecteur();
    }
}
