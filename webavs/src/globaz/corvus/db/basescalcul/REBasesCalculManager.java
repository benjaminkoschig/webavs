package globaz.corvus.db.basescalcul;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager de la classe REBasesCalcul
 */
public class REBasesCalculManager extends PRAbstractManager implements BIGenericManager<REBasesCalcul> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private String forIdRenteCalculee = "";
    private String forIdTiersBC = "";

    public REBasesCalculManager() {
        super();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlank(forIdRenteCalculee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdRenteCalculee);
        }

        if (!JadeStringUtil.isBlank(forIdTiersBC)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdTiersBC);
        }

        if (!JadeStringUtil.isBlank(forCsEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += REBasesCalcul.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtat);
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REBasesCalcul();
    }

    @Override
    public List<REBasesCalcul> getContainerAsList() {
        List<REBasesCalcul> list = new ArrayList<REBasesCalcul>();

        for (int i = 0; i < size(); i++) {
            list.add((REBasesCalcul) get(i));
        }

        return list;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForIdRenteCalculee() {
        return forIdRenteCalculee;
    }

    public String getForIdTiersBC() {
        return forIdTiersBC;
    }

    @Override
    public String getOrderByDefaut() {
        return REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForIdRenteCalculee(String forIdRenteCalculee) {
        this.forIdRenteCalculee = forIdRenteCalculee;
    }

    public void setForIdTiersBC(String forIdTiersBC) {
        this.forIdTiersBC = forIdTiersBC;
    }
}
