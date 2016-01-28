package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (08.05.2003 14:09:03)
 * 
 * @author: ado
 */
public class HEAttenteRetourCIListViewBean extends globaz.hermes.db.gestion.HEOutputAnnonceListViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEAttenteRetourCIListViewBean.
     */
    public HEAttenteRetourCIListViewBean() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "RNIANN";
    }

    /**
     * Renvoie la valeur de la propriété where
     * 
     * @return la valeur de la propriété where
     * @param statement
     *            statement
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);
        if (sqlWhere.trim().length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += " RNLENR LIKE " + _dbWriteString(statement.getTransaction(), "3%");
        // + " OR RNLENR LIKE " + _dbWriteString(statement.getTransaction(),
        // "39001%") ;
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    public BEntity _newEntity() {
        HEAttenteRetourCIViewBean n = new HEAttenteRetourCIViewBean();
        n.setArchivage(isArchivage());
        return n;
    }
}
