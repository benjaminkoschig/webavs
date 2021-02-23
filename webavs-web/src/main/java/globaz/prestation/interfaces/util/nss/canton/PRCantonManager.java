package globaz.prestation.interfaces.util.nss.canton;

import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.pyxis.util.TIToolBox;
import globaz.tucana.constantes.ITUFWTablesDef;

public class PRCantonManager extends BManager {


    String forCsCanton = "";

    @Override
    protected String _getFields(BStatement statement) {
        return  this._getCollection()+"FWCOSP.PCOSID, PCOLUT";
    }
    protected String _getFrom(BStatement statement) {
        StringBuilder from = new StringBuilder();
        from.append(this._getCollection()).append("FWCOSP");
        from.append(" INNER JOIN ");
        from.append(this._getCollection()).append("FWCOUP");
        from.append(" ON (").append(this._getCollection()).append("FWCOUP.").append("PCOSID = ").append(this._getCollection()).append("FWCOSP.").append("PCOSID AND ")
                .append(this._getCollection()).append("FWCOSP.PCOITC <> 0)");
        return from.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();
        String langue = PRUtil.getISOLangueTiers(getSession().getIdLangueISO());
        sql.append("PPTYGR = 'PYCANTON' AND PLAIDE ='").append(langue.substring(0,1)).append("'");
        if (!JadeStringUtil.isBlankOrZero(forCsCanton)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(this._getCollection()+"FWCOSP.");
            sql.append(ITUFWTablesDef.PCOSID);
            sql.append("='");
            sql.append(forCsCanton);
            sql.append("'");

        }
        return sql.toString();
    }
    @Override
    protected String _getOrder(BStatement statement) {
        return " PCOSID";
    }
    @Override
    protected BEntity _newEntity() throws Exception {
        return new PRCanton();
    }
    public String getForCsCanton() {
        return forCsCanton;
    }

    public void setForCsCanton(String forCsCanton) {
        this.forCsCanton = forCsCanton;
    }
}
