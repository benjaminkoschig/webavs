package globaz.osiris.db.comptes;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (12.12.2001 10:13:16)
 * 
 * @author: Administrator
 */
public class CAComptabiliteAuxiliaireCompteCourantManager extends globaz.globall.db.BManager implements
        java.io.Serializable {

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
        return _getCollection() + "CACXCCP";
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
        return new CAComptabiliteAuxiliaireCompteCourant();
    }
}
