package globaz.helios.db.consolidation;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CGSuccursaleManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forNumeroSuccursale;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CGSuccursale.TABLE_NAME;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return CGSuccursale.FIELD_NUMEROSUCCURSALE + " ASC";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlank(getForNumeroSuccursale())) {
            sqlWhere += CGSuccursale.FIELD_NUMEROSUCCURSALE + "="
                    + _dbWriteString(statement.getTransaction(), getForNumeroSuccursale());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGSuccursale();
    }

    public String getForNumeroSuccursale() {
        return forNumeroSuccursale;
    }

    public void setForNumeroSuccursale(String forNumeroSuccursale) {
        this.forNumeroSuccursale = forNumeroSuccursale;
    }
}
