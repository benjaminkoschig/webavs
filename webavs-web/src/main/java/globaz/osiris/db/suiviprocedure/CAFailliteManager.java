package globaz.osiris.db.suiviprocedure;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CAFailliteManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateFaillite;
    private String forIdCompteAnnexe;

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table).
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return CAFaillite.FIELD_DATE_FAILLITE + " DESC ";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "";

        if (!JadeStringUtil.isBlank(getForIdCompteAnnexe())) {
            if (where.length() != 0) {
                where += " AND ";
            }

            where += CAFaillite.FIELD_ID_COMPTEANNEXE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }

        if (!JadeStringUtil.isBlank(getForDateFaillite())) {
            if (where.length() != 0) {
                where += " AND ";
            }

            where += CAFaillite.FIELD_DATE_FAILLITE + " = "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFaillite());
        }

        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAFaillite();
    }

    public String getForDateFaillite() {
        return forDateFaillite;
    }

    /**
     * @return the forIdCompteAnnexe
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public void setForDateFaillite(String forDateFaillite) {
        this.forDateFaillite = forDateFaillite;
    }

    /**
     * @param forIdCompteAnnexe
     *            the forIdCompteAnnexe to set
     */
    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }
}
