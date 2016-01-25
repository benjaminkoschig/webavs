package globaz.phenix.db.communications;

import globaz.globall.db.BStatement;
import globaz.phenix.interfaces.ICommunicationrRetourManager;

public class CPCommunicationFiscaleRetourNEManager extends CPCommunicationFiscaleRetourManager implements
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
        /*
         * return super._getFields(statement) +
         * " ,IKNANE, IKGANE, IKCONE, CPCRNEP.IKNCOM, IKNBDP, CPCRNEP.IKNCLI, IKGTNE" ;
         */
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "CPCRNEP";
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
        return new CPCommunicationFiscaleRetourNEViewBean();
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
