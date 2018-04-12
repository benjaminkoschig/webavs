package ch.globaz.orion.ws.service.adresse;

import globaz.globall.db.BStatement;
import globaz.pyxis.db.adressecourrier.ITIAdresseDefTable;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;

public class AdresseLoaderManager extends TIAdresseDataManager {
    private static final long serialVersionUID = 2248421232700671391L;
    public Collection<String> idsTiers = new ArrayList<String>();
    public Boolean derniereAdresse = false;

    @Override
    protected String _getWhere(BStatement statement) {
        String tiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        StringBuffer sql = new StringBuffer(super._getWhere(statement).trim());
        if (!idsTiers.isEmpty()) {
            if (sql.length() > 0) {
                sql = sql.append(" and ");
            }
            sql = sql.append(" ").append(tiers).append(".").append(ITITiersDefTable.ID_TIERS).append(" in ")
                    .append(writeForIn(idsTiers));
        }
        if (derniereAdresse) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append("HAIADU");
            sql.append(" = (");
            sql.append("SELECT MAX(").append(ITIAdresseDefTable.ID_ADRESSE_UNIQUE).append(")");
            sql.append(" FROM ");
            sql.append(_getCollection()).append(ITIAdresseDefTable.TABLE_NAME);
            sql.append(" INNER JOIN ").append(_getCollection()).append("TIAADRP");
            sql.append(" ON ").append(_getCollection()).append("TIAADRP.HAIADR = ").append(_getCollection())
                    .append("TIADREP.HAIADR ");
            sql.append(" WHERE ").append(_getCollection()).append("TIAADRP.HTITIE = ").append(_getCollection())
                    .append("TITIERP.HTITIE");
            sql.append(")");
        }

        return sql.toString();
    }

    public void setForDerniereAdresse(Boolean derniereAdresse) {
        this.derniereAdresse = derniereAdresse;
    }

    private String writeForIn(Collection<String> ids) {
        return "(" + StringUtils.join(ids, ",") + ")";
    }

    public void setForIdsTiers(Collection<String> idsTiers) {
        this.idsTiers = idsTiers;
    }

}
