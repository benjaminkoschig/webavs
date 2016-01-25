package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (08.05.2003 14:09:03)
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
     * Renvoie la valeur de la propri�t� where
     * 
     * @return la valeur de la propri�t� where
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
     * Cr�e une nouvelle entit�
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception
     *                si la cr�ation a �chou�e
     */
    @Override
    public BEntity _newEntity() {
        HEAttenteRetourCIViewBean n = new HEAttenteRetourCIViewBean();
        n.setArchivage(isArchivage());
        return n;
    }
}
