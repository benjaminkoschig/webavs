/*
 * Créé le Dec 12, 2005, dda
 */
package globaz.helios.db.mapping;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author dda
 * 
 */
public class CGMappingComptabiliserManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String AUCUN_CENTRE_CHARGE = "0";
    private String forIdCentreChargeSource;
    private String forIdCompteSource;

    private String forIdMandatSource;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CGMappingComptabiliser.TABLE_CGMAPCP;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdCompteSource())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMappingComptabiliser.FIELD_IDCOMPTESRC + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCompteSource());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdMandatSource())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMappingComptabiliser.FIELD_IDMANDATSRC + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandatSource());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdCentreChargeSource())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMappingComptabiliser.FIELD_IDCENTRECHARGESRC + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCentreChargeSource());
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGMappingComptabiliser();
    }

    public String getForIdCentreChargeSource() {
        return forIdCentreChargeSource;
    }

    public String getForIdCompteSource() {
        return forIdCompteSource;
    }

    public String getForIdMandatSource() {
        return forIdMandatSource;
    }

    public void setForIdCentreChargeSource(String forIdCentreChargeSource) {
        this.forIdCentreChargeSource = forIdCentreChargeSource;
    }

    public void setForIdCompteSource(String forIdCompteSource) {
        this.forIdCompteSource = forIdCompteSource;
    }

    public void setForIdMandatSource(String forIdMandatSource) {
        this.forIdMandatSource = forIdMandatSource;
    }

}
