/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bsc
 * 
 */

public class RERenteAccordeeManager extends REPrestationAccordeeManager implements BIGenericManager<RERenteAccordee> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String REREACC_FIELDS = " YLDAMR, YLLKEY, YLBTMA, YLIBCA, "
            + " YLMRAN, YLDDAN, YLLCSI, YLDFDE, YLLSVE, YLLPAP, YLTRRE, " + " YLLCMU, YLNDAJ, YLMSAJ, YLDRAJ, YLLAAN, "
            + " YLMROR, YLNRFG, YLIPTC, YLIDTC, YLTECI, " + " YLLCAU, ZTLCPR, YLLFRR, YLGAPI ";

    private Boolean forDateDebutAvantDateFin = null;
    private String forIdBaseCalcul = "";
    private String forIdTiersBaseCalcul = "";

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer();

        if (wantLevelField) {
            // les champs de la tables des rentes accordees
            sqlFields.append(RERenteAccordeeManager.REREACC_FIELDS);
        }
        sqlFields.append(super._getFields(statement));
        return sqlFields.toString();

    }

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersBaseCalcul)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RERenteAccordee.FIELDNAME_ID_TIERS_BASE_CALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdTiersBaseCalcul);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdBaseCalcul)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RERenteAccordee.FIELDNAME_ID_BASE_CALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdBaseCalcul);
        }

        if ((forDateDebutAvantDateFin != null) && forDateDebutAvantDateFin.booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "((" + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + "<="
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + ")" + " OR " + "("
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + "=0))";
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteAccordee();
    }

    @Override
    public List<RERenteAccordee> getContainerAsList() {
        List<RERenteAccordee> list = new ArrayList<RERenteAccordee>();

        for (int i = 0; i < size(); i++) {
            list.add((RERenteAccordee) get(i));
        }

        return list;
    }

    public Boolean getForDateDebutAvantDateFin() {
        return forDateDebutAvantDateFin;
    }

    /**
     * @return the forIdBaseCalcul
     */
    public String getForIdBaseCalcul() {
        return forIdBaseCalcul;
    }

    public String getForIdTiersBaseCalcul() {
        return forIdTiersBaseCalcul;
    }

    public void setForDateDebutAvantDateFin(Boolean forDateDebutAvantDateFin) {
        this.forDateDebutAvantDateFin = forDateDebutAvantDateFin;
    }

    /**
     * @param forIdBaseCalcul
     *            the forIdBaseCalcul to set
     */
    public void setForIdBaseCalcul(String forIdBaseCalcul) {
        this.forIdBaseCalcul = forIdBaseCalcul;
    }

    public void setForIdTiersBaseCalcul(String forIdTiersBaseCalcul) {
        this.forIdTiersBaseCalcul = forIdTiersBaseCalcul;
    }

    /**
     * @param b
     */
    @Override
    public void setWantLevelField(boolean b) {
        wantLevelField = b;
    }

    /**
     * @return
     */
    @Override
    public boolean wantLevelField() {
        return wantLevelField;
    }

}
