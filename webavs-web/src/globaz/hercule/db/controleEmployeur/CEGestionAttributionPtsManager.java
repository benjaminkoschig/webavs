package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since 7 sept. 2010
 */
public class CEGestionAttributionPtsManager extends BManager {

    private static final long serialVersionUID = -605133417033408446L;
    private boolean forActif = false;
    private String forIdAttributionPts = "";
    private String forIdControle = "";
    private String forLastModification = "";
    private String forLastUser = "";
    private String forNumAffilie = "";
    private String likeNumAffilie = "";
    private String likeUser = "";
    private String orderBy = "";
    private boolean orderByModification = false;

    @Override
    protected String _getFields(final BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        CEUtils.sqlAddField(sqlFields, "ATT.MPAPID");
        CEUtils.sqlAddField(sqlFields, "ATT.MALNAF");
        CEUtils.sqlAddField(sqlFields, "ATT.MPDREV");
        CEUtils.sqlAddField(sqlFields, "ATT.MPDREC");
        CEUtils.sqlAddField(sqlFields, "ATT.MPQURH");
        CEUtils.sqlAddField(sqlFields, "ATT.MPQURC");
        CEUtils.sqlAddField(sqlFields, "ATT.MPCOLL");
        CEUtils.sqlAddField(sqlFields, "ATT.MPCOLC");
        CEUtils.sqlAddField(sqlFields, "ATT.MPCREN");
        CEUtils.sqlAddField(sqlFields, "ATT.MPCREC");
        CEUtils.sqlAddField(sqlFields, "ATT.MPCOMM");
        CEUtils.sqlAddField(sqlFields, "ATT.CEBAAV");
        CEUtils.sqlAddField(sqlFields, "ATT.MPNBPT");
        CEUtils.sqlAddField(sqlFields, "ATT.MPLUSR");
        CEUtils.sqlAddField(sqlFields, "ATT.MPLMOD");
        CEUtils.sqlAddField(sqlFields, "ATT.MPPEDE");
        CEUtils.sqlAddField(sqlFields, "ATT.MPPEFI");
        CEUtils.sqlAddField(sqlFields, "ATT.MPNBEC");
        CEUtils.sqlAddField(sqlFields, "ATT.MPOBSE");
        CEUtils.sqlAddField(sqlFields, "ATT.CEBMUT");
        CEUtils.sqlAddField(sqlFields, "ATT.MPMAVS");
        CEUtils.sqlAddField(sqlFields, "ATT.MPMAAF");
        CEUtils.sqlAddField(sqlFields, "ATT.MPMAAC");
        CEUtils.sqlAddField(sqlFields, "ATT.MPMAA2");
        CEUtils.sqlAddField(sqlFields, "ATT.MPMASA");
        CEUtils.sqlAddField(sqlFields, "ATT.PSPY");
        CEUtils.sqlAddField(sqlFields, "ATT.PSPY AS PSPYORDER");

        // table de tiers TITIERP
        CEUtils.sqlAddField(sqlFields, "TIER.HTLDE1");
        CEUtils.sqlAddField(sqlFields, "TIER.HTLDE2");

        // Table affilitaiton AFAFFIP
        CEUtils.sqlAddField(sqlFields, "AFF.MADDEB");
        CEUtils.sqlAddField(sqlFields, "AFF.MADFIN");
        CEUtils.sqlAddField(sqlFields, "AFF.HTITIE");
        CEUtils.sqlAddField(sqlFields, "AFF.MATBRA"); // Branche économique
        CEUtils.sqlAddField(sqlFields, "AFF.MATCDN"); // Code noga

        // Table controle employeur CECONTP
        CEUtils.sqlAddField(sqlFields, "CONT.MDDEFF");
        CEUtils.sqlAddField(sqlFields, "CONT.MDTGEN");
        CEUtils.sqlAddField(sqlFields, "CONT.MDDCDE");
        CEUtils.sqlAddField(sqlFields, "CONT.MDDCFI");
        CEUtils.sqlAddField(sqlFields, "CONT.MDICON");
        CEUtils.sqlAddField(sqlFields, "CONT.MDLNAF"); // Num affilié externe
        CEUtils.sqlAddField(sqlFields, "CONT.MDNTJO"); // Temps de controle

        // Table des couvertures CECOUVP
        CEUtils.sqlAddField(sqlFields, "COUV.CENANE");

        // Table des reviseurs
        CEUtils.sqlAddField(sqlFields, "REV.MILNOM");
        CEUtils.sqlAddField(sqlFields, "REV.MITTYR");

        CEUtils.sqlAddField(sqlFields, "COUP.PCOUID AS CODESUVA");
        CEUtils.sqlAddField(sqlFields, "COUP.PCOLUT AS LIBELLESUVA");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(final BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + CEAttributionPts.TABLE_CEATTPTS + " ATT ");
        sqlFrom.append("INNER JOIN " + _getCollection() + CEControleEmployeur.TABLE_CECONTP + " CONT ");
        sqlFrom.append("ON (CONT.MALNAF = ATT.MALNAF AND CONT.MDDCDE = ATT.MPPEDE AND CONT.MDDCFI = ATT.MPPEFI AND CONT.MDBFDR = '1') ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "AFAFFIP AFF ON CONT.MAIAFF = AFF.MAIAFF ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "TITIERP TIER ON AFF.HTITIE = TIER.HTITIE ");
        sqlFrom.append("LEFT JOIN " + _getCollection() + "CECOUVP COUV ON (COUV.MAIAFF = AFF.MAIAFF AND CEBCAV = '1')");
        sqlFrom.append("LEFT JOIN " + _getCollection() + "CEREVIP REV ON REV.MIIREV = CONT.MDICTL");
        sqlFrom.append(" LEFT JOIN " + _getCollection()
                + "FWCOUP as coup on (AFF.MATSUV = coup.pcosid AND coup.PLAIDE='" + getSession().getIdLangue() + "') ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(final globaz.globall.db.BStatement statement) {

        if (!JadeStringUtil.isBlank(getOrderBy())) {
            return getOrderBy();
        }

        if (isOrderByModification()) {
            return " ATT.MPAPID DESC ";
        }

        return " AFF.MALNAF DESC ";
    }

    @Override
    protected String _getWhere(final BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (getLikeNumAffilie().length() != 0) {
            CEUtils.sqlAddCondition(sqlWhere, "AFF.MALNAF like '" + getLikeNumAffilie() + "%'");
        }

        if (getForNumAffilie().length() != 0) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "AFF.MALNAF = " + this._dbWriteString(statement.getTransaction(), getForNumAffilie()));
        }

        if (getForIdControle().length() != 0) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "CONT.MDICON = " + this._dbWriteNumeric(statement.getTransaction(), getForIdControle()));
        }

        if (getForIdAttributionPts().length() != 0) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "ATT.MPAPID = " + this._dbWriteNumeric(statement.getTransaction(), getForIdAttributionPts()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForLastModification())) {
            CEUtils.sqlAddCondition(sqlWhere, "ATT.MPLMOD like '%" + getForLastModification() + "%'");
        }

        if (!JadeStringUtil.isBlank(getForLastUser())) {
            CEUtils.sqlAddCondition(sqlWhere, "ATT.MPLUSR like '%" + getForLastUser() + "%'");
        }

        if (isForActif()) {
            CEUtils.sqlAddCondition(sqlWhere, "ATT.CEBAAV = '1'");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEGestionAttributionPts();
    }

    public String getForIdAttributionPts() {
        return forIdAttributionPts;
    }

    public String getForIdControle() {
        return forIdControle;
    }

    public String getForLastModification() {
        return forLastModification;
    }

    public String getForLastUser() {
        return forLastUser;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public String getLikeUser() {
        return likeUser;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isForActif() {
        return forActif;
    }

    public boolean isOrderByModification() {
        return orderByModification;
    }

    public void setForActif(final boolean forActif) {
        this.forActif = forActif;
    }

    public void setForIdAttributionPts(final String forIdAttributionPts) {
        this.forIdAttributionPts = forIdAttributionPts;
    }

    public void setForIdControle(final String forIdControle) {
        this.forIdControle = forIdControle;
    }

    public void setForLastModification(final String forLastModification) {
        this.forLastModification = forLastModification;
    }

    public void setForLastUser(final String forLastUser) {
        this.forLastUser = forLastUser;
    }

    public void setForNumAffilie(final String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setLikeNumAffilie(final String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

    public void setLikeUser(final String likeUser) {
        this.likeUser = likeUser;
    }

    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    public void setOrderByModification(final boolean orderByModification) {
        this.orderByModification = orderByModification;
    }

}
