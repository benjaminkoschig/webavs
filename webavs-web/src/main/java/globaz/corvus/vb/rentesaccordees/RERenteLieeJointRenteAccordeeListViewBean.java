/*
 * Créé le 5 juil. 07
 */

package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteLieeJointPrestationAccordeeManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author HPE
 * 
 */

public class RERenteLieeJointRenteAccordeeListViewBean extends RERenteLieeJointPrestationAccordeeManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_COUNT_POSTIT = "CNTPOST";

    private static final String FIELDS_QUERY = " YLIRAC, YLIBAC, YLLCAU, YLTECI, YLLREF, YLIPTC, YLIDTC, YLMROR, YLLCS1,"
            + " YLLCS2, YLLCS3, YLLCS4, YLLCS5, YLNRFG, YLLCMU, YLNDAJ, YLMSAJ, YLDRAJ,"
            + " YLLAAN, YLMRAN, YLDDAN, YLLCSI, YLDFDE, YLLSVE, YLLPAP, YLTRRE, YLBTMA,"
            + " YLDAMR, YLLKEY, YLITBC, YLMTRA, YLLREM, ZTIPRA, ZTTGEN, YNICOA, "
            + " ZTITBE, ZTIICT, ZTMPRE, ZTLRFP, ZTBRET, ZTBPRB, ZTBERR, ZTDDDR, ZTDFDR,"
            + " ZTTETA, ZTDECH, ZTLFRR, ZTIDPA, ZTICIM, ZTLCPR, ZTIEBK, ZTBAMB, ZTBAMR";

    private boolean hasPostitField = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer();

        sqlFields.append(FIELDS_QUERY);

        sqlFields.append("," + _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE + "." + "CSPY");
        sqlFields.append("," + _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE + "." + "PSPY");

        sqlFields.append(", CASE WHEN ");
        sqlFields.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sqlFields.append(" = 0 THEN 999999 WHEN ");
        sqlFields.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sqlFields.append(" > 0 THEN ");
        sqlFields.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        sqlFields.append(" END AS DATE2");

        if (hasPostitField) {
            sqlFields
                    .append(", (" + createSelectCountPostit(_getCollection()) + ") AS " + FIELDNAME_COUNT_POSTIT + " ");
        }

        return sqlFields.toString();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteLieeJointRenteAccordeeViewBean();
    }

    /**
     * creation du count pour les postit de l'entity
     * 
     * @return
     */
    private String createSelectCountPostit(String schema) {

        StringBuffer query = new StringBuffer();
        query.append("SELECT COUNT(*) FROM ");
        query.append(schema);
        query.append(FWNoteP.TABLE_NAME);
        query.append(" WHERE ");
        query.append("NPSRCID");
        query.append(" = ");
        query.append(schema);
        query.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        query.append(".");
        query.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        query.append(" AND ");
        query.append("NPTBLSRC");
        query.append(" = '");
        query.append(REApplication.KEY_POSTIT_RENTES);
        query.append("'");

        return query.toString();
    }

    @Override
    public String getOrderByDefaut() {
        return "DATE2 DESC, ZTTETA," + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + ",  "
                + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " DESC ";
    }

    public boolean hasPostitField() {
        return hasPostitField;
    }

    public void setHasPostitField(boolean hasPostitField) {
        this.hasPostitField = hasPostitField;
    }

    public String getDateDernierPaiement() {
        return REPmtMensuel.getDateDernierPmt(getSession());
    }

}
