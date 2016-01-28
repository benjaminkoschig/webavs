package globaz.naos.db.affiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class AFAffiliationModeFacturationManager extends AFAffiliationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append(_getCollection() + "AFAFFIP" + "." + "MALNAF");
        sqlFields.append(",");
        sqlFields.append(_getCollection() + "AFAFFIP" + "." + "HTITIE");
        sqlFields.append(",");
        sqlFields.append(_getCollection() + "AFAFFIP" + "." + "MATTAF");
        sqlFields.append(",");
        sqlFields.append(_getCollection() + "AFAFFIP" + "." + "MATCFA");
        sqlFields.append(",");
        sqlFields.append(_getCollection() + "AFAFFIP" + "." + "MABREP");
        sqlFields.append(",");
        sqlFields.append(_getCollection() + "AFAFFIP" + "." + "MATDEC");
        sqlFields.append(",");
        sqlFields.append(_getCollection() + "AFAFFIP" + "." + "MABTRA");

        return sqlFields.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliationModeFacturation();
    }

}
