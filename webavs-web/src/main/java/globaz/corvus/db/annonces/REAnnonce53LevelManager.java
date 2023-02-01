package globaz.corvus.db.annonces;

import globaz.corvus.api.arc.downloader.REAnnoncesDateAugmentation5153;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import lombok.Getter;
import lombok.Setter;

public class REAnnonce53LevelManager extends PRAbstractManager {

    private static final long serialVersionUID = 1L;
    private static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";
    private static final String INNER_JOIN = " INNER JOIN ";

    @Getter
    @Setter
    private String forCodeEnregistrement = "";
    @Getter
    @Setter
    private String forMoisAdaptation = "";
    @Getter
    @Setter
    private String forDateTraitement = "";
    @Getter
    @Setter
    private String forDateAugmentationDateTraitement = "";

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder(_getCollection()).append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER).append(" AS HEA");

        sql.append(INNER_JOIN).append(_getCollection());
        sql.append(REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A);
        sql.append(" ON HEA.").append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        sql.append("=").append(REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A);

        sql.append(INNER_JOIN).append(_getCollection());
        sql.append(REAnnoncesAbstractLevel2A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_2A);
        sql.append(" ON HEA.").append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        sql.append("=").append(REAnnoncesAbstractLevel2A.FIELDNAME_ID_ANNONCE_ABS_LEV_2A);

        sql.append(INNER_JOIN).append(_getCollection());
        sql.append(REAnnoncesAbstractLevel3A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3A);
        sql.append(" ON HEA.").append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        sql.append("=").append(REAnnoncesAbstractLevel3A.FIELDNAME_ID_ANNONCE_ABS_LEV_3A);

        sql.append(INNER_JOIN).append(_getCollection());
        sql.append(REAnnonce53.TABLE_NAME_ANNONCE_53);
        sql.append(" ON HEA.").append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        sql.append("=").append(REAnnonce53.FIELDNAME_ID_ANNONCE_53);

        sql.append(LEFT_OUTER_JOIN).append(_getCollection());
        sql.append(REFicheAugmentation.TABLE_NAME_FICHE_AUGMENTATION).append(" AS CHA");
        sql.append(" ON CHA.WJIANH = YXIDAN");

        sql.append(LEFT_OUTER_JOIN).append(_getCollection());
        sql.append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER).append(" AS HEA2");
        sql.append(" ON HEA2.ZAILIE = YXIDAN");

        sql.append(LEFT_OUTER_JOIN).append(_getCollection());
        sql.append(REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER).append(" AS HEA1");
        sql.append(" ON HEA1.ZAILIE = HEA2.ZAIANH");

        sql.append(LEFT_OUTER_JOIN).append(_getCollection());
        sql.append(REFicheAugmentation.TABLE_NAME_FICHE_AUGMENTATION).append(" AS CHA2");
        sql.append(" ON CHA2.WJIANH = HEA2.ZAIANH");

        sql.append(LEFT_OUTER_JOIN).append(_getCollection());
        sql.append(REFicheAugmentation.TABLE_NAME_FICHE_AUGMENTATION).append(" AS CHA1");
        sql.append(" ON CHA1.WJIANH = HEA1.ZAIANH");

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if(!JadeStringUtil.isEmpty(forDateAugmentationDateTraitement)) {
            forMoisAdaptation = REAnnoncesDateAugmentation5153.retrieveDateAugmentationFromValue(forDateAugmentationDateTraitement);
            forDateTraitement = REAnnoncesDateAugmentation5153.retrieveDateTraitementFromValue(forDateAugmentationDateTraitement);
        }

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

        if (!JadeStringUtil.isEmpty(forDateTraitement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(CHA." + REFicheAugmentation.FIELDNAME_DATE_TRAITEMENT + " = "
                    + _dbWriteString(statement.getTransaction(), forDateTraitement);
            sqlWhere += " OR CHA1." + REFicheAugmentation.FIELDNAME_DATE_TRAITEMENT + " = "
                    + _dbWriteString(statement.getTransaction(), forDateTraitement);
            sqlWhere += " OR CHA2." + REFicheAugmentation.FIELDNAME_DATE_TRAITEMENT + " = "
                    + _dbWriteString(statement.getTransaction(), forDateTraitement)+ ")";
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


    @Override
    public String getOrderByDefaut() {
        return null;
    }
}
