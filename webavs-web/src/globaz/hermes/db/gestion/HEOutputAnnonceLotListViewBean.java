package globaz.hermes.db.gestion;

import globaz.globall.db.BStatement;

public class HEOutputAnnonceLotListViewBean extends HEOutputAnnonceListViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forTypeLot = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.db.gestion.HEAnnoncesListViewBean#_getFrom(globaz.globall .db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = super._getFrom(statement);
        from += (" INNER JOIN " + _getCollection() + "HELOTSP AS HELOTSP ON HELOTSP.RMILOT=HEANNOP.RMILOT");
        return from;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.db.gestion.HEOutputAnnonceListViewBean#_getWhere(globaz .globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if (forTypeLot.length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RMTTYP=" + _dbWriteNumeric(statement.getTransaction(), forTypeLot);
        }
        return sqlWhere;
    }

    /**
     * @return Returns the forTypeLot.
     */
    public String getForTypeLot() {
        return forTypeLot;
    }

    /**
     * @param forTypeLot
     *            The forTypeLot to set.
     */
    public void setForTypeLot(String forTypeLot) {
        this.forTypeLot = forTypeLot;
    }

}
