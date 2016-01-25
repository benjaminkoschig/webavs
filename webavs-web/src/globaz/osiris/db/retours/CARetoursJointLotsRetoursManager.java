/*
 * Créé le 20 jan. 09
 */
package globaz.osiris.db.retours;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author bsc
 * 
 */
public class CARetoursJointLotsRetoursManager extends CARetoursManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_NAME_FOR_CORRELATION = " RETOURS ";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forCsEtatLot = null;
    private String forCsEtatRetourIn = null;
    private Boolean montantRetourEqualSommeMontantsLignes = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";
        String as = " AS ";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(CARetours.TABLE_NAME_RETOURS);

        // utilisation d'une sous requete correlee
        if (getMontantRetourEqualSommeMontantsLignes().booleanValue()) {
            fromClauseBuffer.append(as);
            fromClauseBuffer.append(CARetoursJointLotsRetoursManager.TABLE_NAME_FOR_CORRELATION);
        }

        // jointure entre table des retours et table des lots de retours
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(CALotsRetours.TABLE_NAME_LOTS_RETOURS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(CALotsRetours.TABLE_NAME_LOTS_RETOURS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(CARetours.FIELDNAME_ID_LOT);
        fromClauseBuffer.append(egal);

        // utilisation d'une sous requete correlee
        if (getMontantRetourEqualSommeMontantsLignes().booleanValue()) {
            fromClauseBuffer.append(CARetoursJointLotsRetoursManager.TABLE_NAME_FOR_CORRELATION);
        } else {
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(CARetours.TABLE_NAME_RETOURS);
        }

        fromClauseBuffer.append(point);
        fromClauseBuffer.append(CALotsRetours.FIELDNAME_ID_LOT);

        return fromClauseBuffer.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isBlank(getForCsEtatLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CALotsRetours.FIELDNAME_ETAT_LOT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForCsEtatLot());
        }

        if (!JadeStringUtil.isBlank(getForCsEtatRetourIn())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CARetours.FIELDNAME_ETAT_RETOUR + " IN (" + getForCsEtatRetourIn() + ")";
        }

        if (getMontantRetourEqualSommeMontantsLignes().booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CARetours.FIELDNAME_MONTANT_RETOUR + "=" + creatSumLignesRetours();
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CARetoursViewBean();
    }

    /**
     * 
     * @return
     */
    private String creatSumLignesRetours() {
        StringBuffer sumBuffer = new StringBuffer();
        String select = " SELECT ";
        String sum = " SUM ";
        String point = ".";
        String egal = "=";
        String from = " FROM ";
        String where = " WHERE ";

        sumBuffer.append("(");
        sumBuffer.append(select);
        sumBuffer.append(sum + "(" + CALignesRetours.FIELDNAME_MONTANT + ")");
        sumBuffer.append(from);
        sumBuffer.append(_getCollection());
        sumBuffer.append(CALignesRetours.TABLE_NAME_LIGNES_RETOURS);
        sumBuffer.append(where);
        sumBuffer.append(_getCollection());
        sumBuffer.append(CALignesRetours.TABLE_NAME_LIGNES_RETOURS);
        sumBuffer.append(point);
        sumBuffer.append(CALignesRetours.FIELDNAME_ID_RETOUR);
        sumBuffer.append(egal);
        sumBuffer.append(CARetoursJointLotsRetoursManager.TABLE_NAME_FOR_CORRELATION);
        sumBuffer.append(point);
        sumBuffer.append(CARetours.FIELDNAME_ID_RETOUR);
        sumBuffer.append(")");

        return sumBuffer.toString();
    }

    public String getForCsEtatLot() {
        return forCsEtatLot;
    }

    public String getForCsEtatRetourIn() {
        return forCsEtatRetourIn;
    }

    public Boolean getMontantRetourEqualSommeMontantsLignes() {
        return montantRetourEqualSommeMontantsLignes;
    }

    public void setForCsEtatLot(String forCsEtatLot) {
        this.forCsEtatLot = forCsEtatLot;
    }

    public void setForCsEtatRetourIn(String forCsEtatLotIn) {
        forCsEtatRetourIn = forCsEtatLotIn;
    }

    public void setMontantRetourEqualSommeMontantsLignes(Boolean montantRetourEqualSommeMontantsLignes) {
        this.montantRetourEqualSommeMontantsLignes = montantRetourEqualSommeMontantsLignes;
    }
}
