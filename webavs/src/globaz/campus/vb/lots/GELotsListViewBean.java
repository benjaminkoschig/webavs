package globaz.campus.vb.lots;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.lots.GELots;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class GELotsListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // idLot
    private static final String FIELD_ID_LOT = GELotsAnnoncesViewBean.FIELDNAME_ID_LOT + " "
            + GELotsAnnoncesViewBean.ALIAS_FIELDNAME_ID_LOT;
    // annee
    private static final String FIELDNAME_ANNEE = GELotsAnnoncesViewBean.FIELDNAME_ANNEE + " "
            + GELotsAnnoncesViewBean.ALIAS_FIELDNAME_ANNEE;
    // date réception
    private static final String FIELDNAME_DATE_RECEPTION = GELotsAnnoncesViewBean.FIELDNAME_DATE_RECEPTION + " "
            + GELotsAnnoncesViewBean.ALIAS_FIELDNAME_DATE_RECEPTION;
    // etat lot
    private static final String FIELDNAME_ETAT_LOT = GELotsAnnoncesViewBean.FIELDNAME_ETAT_LOT + " "
            + GELotsAnnoncesViewBean.ALIAS_FIELDNAME_ETAT_LOT;
    // id tiers école
    private static final String FIELDNAME_ID_TIERS_ECOLE = GELotsAnnoncesViewBean.FIELDNAME_ID_TIERS_ECOLE + " "
            + GELotsAnnoncesViewBean.ALIAS_FIELDNAME_ID_TIERS_ECOLE;
    // libelle traitement
    private static final String FIELDNAME_LIBELLE_TRAITEMENT = GELotsAnnoncesViewBean.FIELDNAME_LIBELLE_TRAITEMENT
            + " " + GELotsAnnoncesViewBean.ALIAS_FIELDNAME_LIBELLE_TRAITEMENT;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forAnnee = null;
    private String forCsEtatLot = null;
    private String forIdLot = null;
    private String forIdTiersEcole = null;
    private String forLibelleTraitementLike = null;
    private String fromDateReceptionLot = null;
    private String orderBy = GELots.FIELDNAME_DATE_RECEPTION + " DESC, " + GELots.FIELDNAME_ID_LOT + " DESC ";

    public GELotsListViewBean() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        String sqlFields = FIELD_ID_LOT + ", " + FIELDNAME_DATE_RECEPTION + ", " + FIELDNAME_LIBELLE_TRAITEMENT + ", "
                + FIELDNAME_ANNEE + ", " + FIELDNAME_ETAT_LOT + ", " + FIELDNAME_ID_TIERS_ECOLE + ", "
                + "(SELECT COUNT(*) FROM " + _getCollection() + GEAnnonces.TABLE_NAME_ANNONCE + " WHERE "
                + GEAnnonces.FIELDNAME_ID_LOT + "=" + GELotsAnnoncesViewBean.FIELDNAME_ID_LOT + " AND "
                + GEAnnonces.FIELDNAME_IS_IMPUTATION + "="
                + _dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + ") " + GELotsAnnoncesViewBean.ALIAS_NB_ANNONCES + ", (SELECT COUNT(*) FROM " + _getCollection()
                + GEAnnonces.TABLE_NAME_ANNONCE + " WHERE " + GEAnnonces.FIELDNAME_ID_LOT + "="
                + GELotsAnnoncesViewBean.FIELDNAME_ID_LOT + " AND " + GEAnnonces.FIELDNAME_IS_IMPUTATION + "="
                + _dbWriteBoolean(statement.getTransaction(), new Boolean(true), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + ") " + GELotsAnnoncesViewBean.ALIAS_NB_IMPUTATIONS + ", (SELECT COUNT(*) FROM " + _getCollection()
                + GEAnnonces.TABLE_NAME_ANNONCE + " WHERE " + GEAnnonces.FIELDNAME_ID_LOT + "="
                + GELotsAnnoncesViewBean.FIELDNAME_ID_LOT + " AND " + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "="
                + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_ERREUR) + ") "
                + GELotsAnnoncesViewBean.ALIAS_NB_ERREURS + ", (SELECT COUNT(*) FROM " + _getCollection()
                + GEAnnonces.TABLE_NAME_ANNONCE + " WHERE " + GEAnnonces.FIELDNAME_ID_LOT + "="
                + GELotsAnnoncesViewBean.FIELDNAME_ID_LOT + " AND " + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "="
                + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_A_TRAITER) + ") "
                + GELotsAnnoncesViewBean.ALIAS_NB_A_TRAITER + ", (SELECT COUNT(*) FROM " + _getCollection()
                + GEAnnonces.TABLE_NAME_ANNONCE + " WHERE " + GEAnnonces.FIELDNAME_ID_LOT + "="
                + GELotsAnnoncesViewBean.FIELDNAME_ID_LOT + " AND " + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "<>"
                + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_COMPTABILISE) + " AND "
                + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "<>"
                + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_ERREUR_ARCHIVEE) + ") "
                + GELotsAnnoncesViewBean.ALIAS_PAS_TERMINE;

        return sqlFields;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = _getCollection() + GELots.TABLE_NAME_LOT + " " + GELotsAnnoncesViewBean.ALIAS_TABLE_NAME_LOT;
        return sqlFrom;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ID_LOT + "=" + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        if (!JadeStringUtil.isBlank(getFromDateReceptionLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_DATE_RECEPTION + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDateReceptionLot());
        }
        if (!JadeStringUtil.isBlank(getForCsEtatLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ETAT_LOT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForCsEtatLot());
        }
        if (!JadeStringUtil.isBlank(getForLibelleTraitementLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + GELots.FIELDNAME_LIBELLE_TRAITEMENT + ") like UPPER ("
                    + _dbWriteString(statement.getTransaction(), "%" + getForLibelleTraitementLike() + "%") + ")";
        }
        if (!JadeStringUtil.isBlank(getForAnnee())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ANNEE + "=" + _dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        if (!JadeStringUtil.isBlank(getForIdTiersEcole())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ID_TIERS_ECOLE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiersEcole());
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GELotsAnnoncesViewBean();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCsEtatLot() {
        return forCsEtatLot;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForIdTiersEcole() {
        return forIdTiersEcole;
    }

    public String getForLibelleTraitementLike() {
        return forLibelleTraitementLike;
    }

    public String getFromDateReceptionLot() {
        return fromDateReceptionLot;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCsEtatLot(String forCsEtatLot) {
        this.forCsEtatLot = forCsEtatLot;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdTiersEcole(String forIdTiersEcole) {
        this.forIdTiersEcole = forIdTiersEcole;
    }

    public void setForLibelleTraitementLike(String forLibelleTraitementLike) {
        this.forLibelleTraitementLike = forLibelleTraitementLike;
    }

    public void setFromDateReceptionLot(String fromDateReceptionLot) {
        this.fromDateReceptionLot = fromDateReceptionLot;
    }
}
