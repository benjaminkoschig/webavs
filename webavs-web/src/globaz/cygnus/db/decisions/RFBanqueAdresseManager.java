package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.pyxis.db.tiers.TIBanqueAdresse;

/**
 * @author fha
 */
public class RFBanqueAdresseManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String idTiers = "";

    /**
     * Crée une nouvelle instance de la classe LIDossiersJointTiersManager.
     */
    public RFBanqueAdresseManager() {
        super();
        wantCallMethodBeforeFind(false);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("CCVDWEB.TITIERP.HTITIE");
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), idTiers));
        }

        return sqlWhere.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new TIBanqueAdresse();
    }

    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
