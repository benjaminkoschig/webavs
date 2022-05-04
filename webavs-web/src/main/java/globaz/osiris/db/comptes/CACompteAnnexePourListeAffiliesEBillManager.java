package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

import java.io.Serializable;

public class CACompteAnnexePourListeAffiliesEBillManager extends BManager implements Serializable {

    private static final String T_COMPTE_ANNEXE = "CACPTAP";
    private static final String T_TIERS_AVS = "TIPAVSP";
    private static final String T_AFFILIATION = "AFAFFIP";
    private static final String C_COMPTE_ANNEXE_TIER_ID = ".IDTIERS";
    private static final String SQL_ON = " ON (";
    private static final String SQL_END_ON = "   )";
    private static final String SQL_INNER_JOIN = " INNER JOIN ";
    private static final String SQL_LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACompteAnnexePourListeAffiliesEBill();
    }

    @Override
    public String _getFrom(BStatement statement){
        return _getCollection() + T_COMPTE_ANNEXE + " "
                + SQL_INNER_JOIN + _getCollection() + T_TIERS_AVS
                + SQL_ON + _getCollection() + T_COMPTE_ANNEXE + C_COMPTE_ANNEXE_TIER_ID + "="
                + _getCollection() + T_TIERS_AVS + ".HTITIE" + SQL_END_ON
                + SQL_LEFT_OUTER_JOIN + _getCollection() + T_AFFILIATION
                + SQL_ON + _getCollection() + T_COMPTE_ANNEXE + C_COMPTE_ANNEXE_TIER_ID + "="
                + _getCollection() + T_AFFILIATION + ".HTITIE" + SQL_END_ON;
    }

    @Override
    public String _getWhere(BStatement statement){
        return "(IDROLE = 517039 OR IDROLE = 517040 OR IDROLE = 517002)" +
                "  AND (MADFIN = 0 OR MADFIN >= to_char(current timestamp, 'YYYYMMDD'))";
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return " MALNAF ";
    }

}
