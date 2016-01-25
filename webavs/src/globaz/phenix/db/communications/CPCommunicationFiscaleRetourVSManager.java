package globaz.phenix.db.communications;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.interfaces.ICommunicationrRetourManager;

public class CPCommunicationFiscaleRetourVSManager extends CPCommunicationFiscaleRetourManager implements
        ICommunicationrRetourManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String orderBy = "";
    private String tri = "";

    @Override
    protected String _getFields(BStatement statement) {
        return "*";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "CPCRVSP";
        return from + " INNER JOIN " + _getCollection() + table1 + " " + table1 + " ON (" + table1 + ".IKIRET="
                + "CPCRETP.IKIRET)";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrderBy();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // appel du parent
        String sqlWhere = super._getWhere(statement);
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscaleRetourVSViewBean();
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    @Override
    public String getTri() {
        return tri;
    }

    @Override
    public void orderByNumAffilie() {
        if (JadeStringUtil.isEmpty(orderBy)) {
            orderBy = "IKLTRI";
        } else {
            orderBy += ", IKLTRI";
        }
    }

    @Override
    public void orderByNumAvs() {
        if (JadeStringUtil.isEmpty(orderBy)) {
            orderBy = "IKNAVS, IKNACO";
        } else {
            orderBy += ", IKNAVS, IKNACO";
        }
    }

    @Override
    public void orderByNumContribuable() {
        if (JadeStringUtil.isEmpty(orderBy)) {
            orderBy = "IKNUMC";
        } else {
            orderBy += ", IKNUMC";
        }
    }

    @Override
    public void orderByNumIFD() {
        if (JadeStringUtil.isEmpty(orderBy)) {
            orderBy = "IKANNE";
        } else {
            orderBy += ", IKANNE";
        }
    }

    /**
     * @param string
     */
    @Override
    public void setOrderBy(java.lang.String string) {
        orderBy = string;
    }

    @Override
    public void setTri(String tri) {
        this.tri = tri;
    }

}
