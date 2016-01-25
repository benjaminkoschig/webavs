package globaz.apg.db.prestation;

import globaz.apg.db.lots.APLot;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class APRepartitionJointPrestationJointLotManager extends APRepartitionJointPrestationManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatLot = "";
    private String forDateDebutComptaLot = "";
    private String forDateFinComptaLot = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return APRepartitionJointPrestationJointLot.createFromClause(_getCollection());
    }

    /**
     * (non-Javadoc)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);
        String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(forCsEtatLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APLot.TABLE_NAME + "." + APLot.FIELDNAME_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsEtatLot);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebutComptaLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APLot.TABLE_NAME + "." + APLot.FIELDNAME_DATECOMPTABLE + ">="
                    + _dbWriteNumeric(statement.getTransaction(), forDateDebutComptaLot);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateFinComptaLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APLot.TABLE_NAME + "." + APLot.FIELDNAME_DATECOMPTABLE + "<="
                    + _dbWriteNumeric(statement.getTransaction(), forDateFinComptaLot);
        }

        return sqlWhere;
    }

    /**
     * (non-Javadoc)
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APRepartitionJointPrestationJointLot();
    }

    public String getForCsEtatLot() {
        return forCsEtatLot;
    }

    public String getForDateDebutComptaLot() {
        return forDateDebutComptaLot;
    }

    public String getForDateFinComptaLot() {
        return forDateFinComptaLot;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT;
    }

    public void setForCsEtatLot(String forCsEtatLot) {
        this.forCsEtatLot = forCsEtatLot;
    }

    public void setForDateDebutComptaLot(String forDateDebutComptaLot) {
        this.forDateDebutComptaLot = forDateDebutComptaLot;
    }

    public void setForDateFinComptaLot(String forDateFinComptaLot) {
        this.forDateFinComptaLot = forDateFinComptaLot;
    }

}
