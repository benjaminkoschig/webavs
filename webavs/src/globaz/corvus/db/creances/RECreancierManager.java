/*
 * Créé le 18 juil. 07
 */

package globaz.corvus.db.creances;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BSC
 * 
 */
public class RECreancierManager extends PRAbstractManager implements BIGenericManager<RECreancier> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private String forCsType = "";
    private String forIdDemandeRente = "";
    private String forIdTiers = "";

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdDemandeRente)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreancier.FIELDNAME_ID_DEMANDE_RENTE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdDemandeRente);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreancier.FIELDNAME_ID_TIERS + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdTiers);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreancier.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtat);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsType)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreancier.FIELDNAME_CS_TYPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsType);
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RECreancier();
    }

    @Override
    public List<RECreancier> getContainerAsList() {
        List<RECreancier> list = new ArrayList<RECreancier>();
        for (int i = 0; i < size(); i++) {
            list.add((RECreancier) get(i));
        }
        return list;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsType() {
        return forCsType;
    }

    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    @Override
    public String getOrderByDefaut() {
        return RECreancier.FIELDNAME_ID_CREANCIER;
    }

    public void setForCsEtat(String string) {
        forCsEtat = string;
    }

    public void setForCsType(String string) {
        forCsType = string;
    }

    public void setForIdDemandeRente(String string) {
        forIdDemandeRente = string;
    }

    public void setForIdTiers(String string) {
        forIdTiers = string;
    }
}
