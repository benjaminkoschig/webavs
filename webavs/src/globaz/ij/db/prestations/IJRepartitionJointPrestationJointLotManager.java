package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.ij.db.lots.IJLot;
import globaz.jade.client.util.JadeStringUtil;

public class IJRepartitionJointPrestationJointLotManager extends IJRepartitionJointPrestationManager {

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
        return IJRepartitionJointPrestationJointLot.createFromClause(_getCollection());
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

            sqlWhere += schema + IJLot.TABLE_NAME + "." + IJLot.FIELDNAME_CS_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsEtatLot);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebutComptaLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJLot.TABLE_NAME + "." + IJLot.FIELDNAME_DATECOMPTABLE + ">="
                    + _dbWriteNumeric(statement.getTransaction(), forDateDebutComptaLot);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateFinComptaLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + IJLot.TABLE_NAME + "." + IJLot.FIELDNAME_DATECOMPTABLE + "<="
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
        return new IJRepartitionJointPrestationJointLot();
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
        return IJRepartitionPaiements.FIELDNAME_IDREPARTITION_PAIEMENT;
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
