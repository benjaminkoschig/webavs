package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

public class REAnnonce53LevelManager extends PRAbstractManager {

    private static final long serialVersionUID = 1L;
    private static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";

    private String forCodeEnregistrement = "";
    private String forMoisAdaptation = "";

    @Override
    protected String _getFrom(BStatement statement) {
        String sql = super._getFrom(statement);


        sql += LEFT_OUTER_JOIN +_getCollection() + REFicheAugmentation.TABLE_NAME_FICHE_AUGMENTATION + " AS CHA" +
                " ON CHA.WJIANH = YXIDAN";
        sql += LEFT_OUTER_JOIN +_getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER + " AS HEA2" +
                " ON HEA2.ZAILIE = YXIDAN ";
        sql += LEFT_OUTER_JOIN +_getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER + " AS HEA1" +
                " ON HEA1.ZAILIE = HEA2.ZAIANH";
        sql += LEFT_OUTER_JOIN +_getCollection() + REFicheAugmentation.TABLE_NAME_FICHE_AUGMENTATION + " AS CHA2" +
                " ON CHA2.WJIANH = HEA2.ZAIANH";
        sql += LEFT_OUTER_JOIN +_getCollection() + REFicheAugmentation.TABLE_NAME_FICHE_AUGMENTATION + " AS CHA1" +
                " ON CHA1.WJIANH = HEA1.ZAIANH";
        return sql;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forMoisAdaptation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(CHA." + REFicheAugmentation.FIELDNAME_DATE_AUGMENTATION + " = "
                    + _dbWriteString(statement.getTransaction(), forMoisAdaptation);
            sqlWhere += " OR CHA1." + REFicheAugmentation.FIELDNAME_DATE_AUGMENTATION + " = "
                    + _dbWriteString(statement.getTransaction(), forMoisAdaptation);
            sqlWhere += " OR CHA2." + REFicheAugmentation.FIELDNAME_DATE_AUGMENTATION + " = "
                    + _dbWriteString(statement.getTransaction(), forMoisAdaptation)+ ")";
        }

        if (!JadeStringUtil.isBlankOrZero(forCodeEnregistrement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER + "." + REAnnonceHeader.FIELDNAME_CODE_ENREGISTREMENT_01 + " = "
                    + _dbWriteString(statement.getTransaction(), forCodeEnregistrement);
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnonce53();
    }

    public String getForCodeEnregistrement() {
        return forCodeEnregistrement;
    }

    public String getForMoisAdaptation() {
        return forMoisAdaptation;
    }

    public void setForCodeEnregistrement(String forCodeEnregistrement) {
        this.forCodeEnregistrement = forCodeEnregistrement;
    }

    public void setForMoisAdaptation(String forMoisAdaptation) {
        this.forMoisAdaptation = forMoisAdaptation;
    }

    @Override
    public String getOrderByDefaut() {
        return null;
    }
}
