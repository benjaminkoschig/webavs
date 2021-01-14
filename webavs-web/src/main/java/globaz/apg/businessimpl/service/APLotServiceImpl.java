package globaz.apg.businessimpl.service;

import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.business.service.APLotService;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.patterns.JadeAbstractService;

import java.sql.ResultSet;

public class APLotServiceImpl  extends JadeAbstractService implements APLotService {

    @Override
    public String getTotauxOPAE(BSession session, String idLot) throws Exception {
        String sql = "SELECT (sum(vnmmot) - sum(vommon)) as totOPAE FROM :schema.apcompp"
                +" left outer join :schema.apfaacp on vnicom = voicom"
                +" where vnilot = :idLot and vnmmot > 0 and (vobcom = '1' or vobcom is null) and (vommon <> 0 or vommon is null)";

        sql = sql.replace(":schema", Jade.getInstance().getDefaultJdbcSchema());
        sql = sql.replace(":idLot", idLot);
        BStatement statement = new BStatement(session.getCurrentThreadTransaction());
        statement.createStatement();
        ResultSet res = statement.executeQuery(sql);
        res.next();
        if(res.getObject(1) == null) {
            return null;
        }
        return res.getObject(1).toString();

    }

}
