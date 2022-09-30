package globaz.corvus.db.annonces;

import globaz.corvus.api.arc.downloader.REAnnoncesDateAugmentation5153;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import lombok.Getter;
import lombok.Setter;

public class REAnnonce61Manager extends PRAbstractManager {

    private static final long serialVersionUID = 1L;
    private static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";

    @Getter
    @Setter
    private String forCodeEnregistrement = "";
    @Getter
    @Setter
    private String forDateAnnonce = "";
    @Getter
    @Setter
    private String forNss = "";

    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement);
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forDateAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REAnnonce61.FIELDNAME_DATE_ANNONCE + " = "
                    + _dbWriteString(statement.getTransaction(), forDateAnnonce);
        }

        if (!JadeStringUtil.isBlankOrZero(forNss)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REAnnonce61.FIELDNAME_NSS_ANNONCE_61 + " = "
                    + _dbWriteString(statement.getTransaction(), forNss);
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
        return new REAnnonce61();
    }

    @Override
    public String getOrderByDefaut() {
        return null;
    }
}
