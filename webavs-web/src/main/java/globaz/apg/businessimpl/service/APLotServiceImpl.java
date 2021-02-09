package globaz.apg.businessimpl.service;

import ch.globaz.common.domaine.Montant;
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
        return getTotauxOPAE1(session, idLot).substract(getTotauxOPAE2(session, idLot)).add(getTotauxOPAE3(session, idLot)).toStringFormat();
    }

    private Montant getTotauxOPAE1(BSession session, String idLot) throws Exception {

        String sql = "SELECT sum(vnmmot) FROM :schema.apcompp " +
                " where vnilot = :idLot and vnmmot > 0";

        return executeRequete(session, idLot, sql);
    }

    private Montant getTotauxOPAE2(BSession session, String idLot) throws Exception {

        String sql = "SELECT sum(vommon) FROM :schema.apcompp "
                +" inner join :schema.apfaacp on vnicom = voicom "
                +" where vnilot = :idLot and (vobcom = '1') and (vommon <> 0) ";

        return executeRequete(session, idLot, sql);
    }

    private Montant getTotauxOPAE3(BSession session, String idLot) throws Exception {

        String sql = "SELECT sum(vimmov) FROM :schema.appresp "
                +" inner join :schema.aprepap on vhiprs = viipra "
                +" where vhilot = :idLot and viipar <> 0 ";

        return executeRequete(session, idLot, sql);
    }

    private Montant executeRequete(BSession session, String idLot, String sql) throws Exception {
        sql = sql.replace(":schema", Jade.getInstance().getDefaultJdbcSchema());
        sql = sql.replace(":idLot", idLot);

        BStatement statement = new BStatement(session.getCurrentThreadTransaction());
        statement.createStatement();
        ResultSet res = statement.executeQuery(sql);
        res.next();
        if (res.getObject(1) == null) {
            return Montant.ZERO;
        }
        return Montant.valueOf((res.getObject(1).toString()));
    }

}
