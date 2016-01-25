package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * 
 * @author HPE
 * 
 */
public class REAnnonce53AdaptationManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeEnregistrement = "";
    private String forMoisAdaptation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forMoisAdaptation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "CHA." + REFicheAugmentation.FIELDNAME_DATE_AUGMENTATION + " = "
                    + _dbWriteString(statement.getTransaction(), forMoisAdaptation);
        }

        if (!JadeStringUtil.isBlankOrZero(forCodeEnregistrement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "HEA1." + REAnnonceHeader.FIELDNAME_CODE_ENREGISTREMENT_01 + " = "
                    + _dbWriteString(statement.getTransaction(), forCodeEnregistrement);
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnonce53Adaptation();
    }

    public String getForCodeEnregistrement() {
        return forCodeEnregistrement;
    }

    public String getForMoisAdaptation() {
        return forMoisAdaptation;
    }

    @Override
    public String getOrderByDefaut() {
        return "L1A." + REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION;
    }

    public void setForCodeEnregistrement(String forCodeEnregistrement) {
        this.forCodeEnregistrement = forCodeEnregistrement;
    }

    public void setForMoisAdaptation(String forMoisAdaptation) {
        this.forMoisAdaptation = forMoisAdaptation;
    }

}
