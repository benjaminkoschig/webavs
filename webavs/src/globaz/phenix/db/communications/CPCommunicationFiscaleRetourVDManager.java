package globaz.phenix.db.communications;

import globaz.globall.db.BStatement;
import globaz.phenix.interfaces.ICommunicationrRetourManager;

public class CPCommunicationFiscaleRetourVDManager extends CPCommunicationFiscaleRetourManager implements
        ICommunicationrRetourManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forVdNumAffilie = "";
    private String forVdNumAvs = "";
    private String forVdNumContribuable = "";
    private String orderBy = "";
    private String tri = "";

    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement)
                + " ,IKIMDE, IKNDEM, IKGAVD, IKAFVD, IKNAVD, IKCOVD, IKLNOM, IKDPRD, IKFPRD, IKADCH, IKADRU, IKDATN"
                + " , IKACTB, IKPAOB, IKDHAB, IKTTAX, IKDASS, IKFASS, IKCINV, IKDATC, IKRAIN, IKGIPR, IKNLOC"
                + " ,IKEXLI, IKSVCJ, IKNACO, IKSACO, IKFOIM, IKDDFO, IKMORE, IKMALU, IKCORE, IKRENE, IKCOMM, IKRALU";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "CPCRVDP";
        return from + " LEFT OUTER JOIN " + _getCollection() + table1 + " " + table1 + " ON (" + table1 + ".IKIRET="
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
        if (getForVdNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKAFVD = " + _dbWriteString(statement.getTransaction(), forVdNumAffilie);
        }
        if (getForVdNumAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKNAVD = " + _dbWriteString(statement.getTransaction(), forVdNumAvs);
        }
        if (getForVdNumContribuable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKCOVD = " + _dbWriteString(statement.getTransaction(), forVdNumContribuable);
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscaleRetourVDViewBean();
    }

    public String getForVdNumAffilie() {
        return forVdNumAffilie;
    }

    public String getForVdNumAvs() {
        return forVdNumAvs;
    }

    public String getForVdNumContribuable() {
        return forVdNumContribuable;
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

    public void setForVdNumAffilie(String forVdNumAffilie) {
        this.forVdNumAffilie = forVdNumAffilie;
    }

    public void setForVdNumAvs(String forVdNumAvs) {
        this.forVdNumAvs = forVdNumAvs;
    }

    public void setForVdNumContribuable(String forVdNumContribuable) {
        this.forVdNumContribuable = forVdNumContribuable;
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
