package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.db.couverture.CECouverture;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * @author SCO
 * @since SCO 9 juin 2010
 */
public class CEAffilieControleManager extends BManager {

    private static final long serialVersionUID = -7410843134308200391L;

    public static final String ORDERBY_NUM_AFFILIE_CROISSANT = "A.MALNAF ASC";

    private String forDateCouverture = "";
    private String forDateFinAffiliation = "";
    private String forIdAttributionPts = "";
    private String forIdControle = "";
    private String forRattrapageDate = "";
    private String fromNumAffilie = "";
    private String orderBy;
    private String toNumAffilie = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer();

        // Fields sur la table affiliation
        CEUtils.sqlAddField(sqlFields, "A.HTITIE");
        CEUtils.sqlAddField(sqlFields, "A.MALNAF");
        CEUtils.sqlAddField(sqlFields, "A.MAIAFF");
        CEUtils.sqlAddField(sqlFields, "A." + AFAffiliation.FIELDNAME_AFF_DDEBUT);
        CEUtils.sqlAddField(sqlFields, "A." + AFAffiliation.FIELDNAME_AFF_DFIN);

        // Jointure sur la table des notes
        CEUtils.sqlAddField(sqlFields, "B.MPAPID"); // idAttributionPts
        CEUtils.sqlAddField(sqlFields, "B.MPDREV"); // derniereRevision
        CEUtils.sqlAddField(sqlFields, "B.MPQURH"); // qualiteRH
        CEUtils.sqlAddField(sqlFields, "B.MPCOLL"); // collaboration
        CEUtils.sqlAddField(sqlFields, "B.MPCREN"); // criteresEntreprise
        CEUtils.sqlAddField(sqlFields, "B." + CEAttributionPts.FIELD_MODIFICATION_UTILISATEUR); // isModificationUtilisateur
        CEUtils.sqlAddField(sqlFields, "B.MPNBPT"); // nbrePoints

        // Jointure sur table des controles
        CEUtils.sqlAddField(sqlFields, "C.MDDCFI");

        // Jointure sur table des couvertures
        CEUtils.sqlAddField(sqlFields, " D." + CECouverture.FIELD_ANNEE); // Annee
        // de
        // couverture
        CEUtils.sqlAddField(sqlFields, "D." + CECouverture.FIELD_IDCOUVERTURE); // Annee
        // de
        // couverture

        // Jointure sur la table des particularites d'une affiliation
        CEUtils.sqlAddField(sqlFields, "E.MFMNUM"); // Nombre année de pour
        // dérogation de la
        // particularité d'un
        // affilié

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + AFAffiliation.TABLE_NAME + " A");

        sqlFrom.append(" LEFT JOIN " + _getCollection() + CEControleEmployeur.TABLE_CECONTP + " C ");
        sqlFrom.append(" ON (C.MAIAFF  = A.MAIAFF");
        sqlFrom.append(" AND C.MDDEFF <> 0");
        sqlFrom.append(" AND C.MDBFDR = '1' )");

        sqlFrom.append(" LEFT JOIN " + _getCollection() + CEAttributionPts.TABLE_CEATTPTS + " B ");
        sqlFrom.append(" ON (B.MALNAF = A.MALNAF AND C.MDDCDE = B.MPPEDE AND C.MDDCFI = B.MPPEFI");
        sqlFrom.append(" AND B." + CEAttributionPts.FIELD_ATTRIBUTIONACTIVE + " = '1')");

        sqlFrom.append(" LEFT JOIN " + _getCollection() + CECouverture.TABLE_CECOUVP + " D ");
        sqlFrom.append(" ON (D." + CECouverture.FIELD_IDAFFILIE + " = A.MAIAFF");
        sqlFrom.append(" AND D." + CECouverture.FIELD_COUVERTUREACTIVE + " = '1')");

        // récupération particularité d'un affilié
        sqlFrom.append(" LEFT JOIN " + _getCollection() + "AFPARTP E ");
        sqlFrom.append(" ON (E.MAIAFF = A.MAIAFF");
        sqlFrom.append(" AND E.MFTPAR = " + CodeSystem.PARTIC_PERIO_CONTR_EMPLOYEUR + ")");

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (!JadeStringUtil.isBlank(getOrderBy())) {
            return getOrderBy();
        }

        return "";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        // Tous les affiliés de type : employeur, independant employeur,
        // employeur D.F
        CEUtils.sqlAddCondition(sqlWhere, AFAffiliation.FIELDNAME_AFFILIATION_TYPE + " IN ("
                + CodeSystem.TYPE_AFFILI_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", "
                + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ")");

        CEUtils.sqlAddCondition(sqlWhere, "(C.MDDCFI = (SELECT MAX(X.MDDCFI) FROM " + _getCollection()
                + CEControleEmployeur.TABLE_CECONTP
                + " X WHERE  X.MAIAFF = A.MAIAFF AND X.MDDEFF <> 0 AND X.MDBFDR = '1') OR C.MDDCFI IS NULL)");

        // Date de radiation vide ou suprérieur a la date donnée
        if (!JadeStringUtil.isEmpty(getForRattrapageDate())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "( MADFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getForRattrapageDate())
                            + " OR MADFIN = 0 )");
        }

        // Date de debut d'affiliation inférieur a la date donnée
        if (!JadeStringUtil.isEmpty(getForRattrapageDate())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MADDEB < " + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + getForRattrapageDate()));
        }

        // Date de radiation vide ou suprérieur a la date donnée
        if (!JadeStringUtil.isEmpty(getForDateFinAffiliation())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "( MADFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFinAffiliation())
                            + " OR MADFIN = 0 )");
        }

        if (!JadeStringUtil.isEmpty(getForDateCouverture())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "D." + CECouverture.FIELD_ANNEE + " = "
                            + this._dbWriteNumeric(statement.getTransaction(), getForDateCouverture()));
        }

        if (!JadeStringUtil.isEmpty(getForIdAttributionPts())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "B.MPAPID = " + this._dbWriteNumeric(statement.getTransaction(), getForIdAttributionPts()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdControle())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "C.MDICON = " + this._dbWriteNumeric(statement.getTransaction(), getForIdControle()));
        }

        if (!JadeStringUtil.isBlank(getFromNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "A.MALNAF >= " + this._dbWriteString(statement.getTransaction(), getFromNumAffilie()));
        }

        if (!JadeStringUtil.isBlank(getToNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "A.MALNAF <= " + this._dbWriteString(statement.getTransaction(), getToNumAffilie()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEAffilieControle();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForDateCouverture() {
        return forDateCouverture;
    }

    public String getForDateFinAffiliation() {
        return forDateFinAffiliation;
    }

    public String getForIdAttributionPts() {
        return forIdAttributionPts;
    }

    public String getForIdControle() {
        return forIdControle;
    }

    public String getForRattrapageDate() {
        return forRattrapageDate;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getOrderBy() {
        return orderBy;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getToNumAffilie() {
        return toNumAffilie;
    }

    public void setForDateCouverture(String forDateCouverture) {
        this.forDateCouverture = forDateCouverture;
    }

    public void setForDateFinAffiliation(String forDateFinAffiliation) {
        this.forDateFinAffiliation = forDateFinAffiliation;
    }

    public void setForIdAttributionPts(String forIdAttributionPts) {
        this.forIdAttributionPts = forIdAttributionPts;
    }

    public void setForIdControle(String forIdControle) {
        this.forIdControle = forIdControle;
    }

    public void setForRattrapageDate(String forRattrapageDate) {
        this.forRattrapageDate = forRattrapageDate;
    }

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setToNumAffilie(String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }

}
