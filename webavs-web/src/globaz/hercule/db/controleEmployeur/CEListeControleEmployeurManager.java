package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.db.groupement.CEGroupe;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;

/**
 * @author SCO
 * @since 18 oct. 2010
 */
public class CEListeControleEmployeurManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean forActif = false;
    private String forAnnee;
    private String forGenreControle;
    private boolean forSansAttributionPts = false;
    private String likeLibelleGroupe;
    private String likeNouveauNumRapport;
    private String likeNumAffilie;
    private String likeVisaReviseur;
    private String orderBy;

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        CEUtils.sqlAddField(sqlFields, "AFF.MAIAFF"); // Id affiliation
        CEUtils.sqlAddField(sqlFields, "AFF.MALNAF"); // Numéro d'affilié
        CEUtils.sqlAddField(sqlFields, "AFF.MADDEB"); // Debut d'affiliation
        CEUtils.sqlAddField(sqlFields, "AFF.MADFIN"); // Fin d'affiliation
        CEUtils.sqlAddField(sqlFields, "TIERS.HTITIE"); // Id tiers
        CEUtils.sqlAddField(sqlFields, "TIERS.HTLDE1"); // Description 1 du
        // tiers
        CEUtils.sqlAddField(sqlFields, "TIERS.HTLDE2"); // Description 2 du
        // tiers
        CEUtils.sqlAddField(sqlFields, "CONT.MDICON"); // id du controle
        CEUtils.sqlAddField(sqlFields, "CONT.MDDEFF"); // DAte effective
        CEUtils.sqlAddField(sqlFields, "CONT.MDDCDE"); // Date debut controle
        CEUtils.sqlAddField(sqlFields, "CONT.MDDCFI"); // Date fin de controle
        CEUtils.sqlAddField(sqlFields, "CONT.MDDPRE"); // Date prévu
        CEUtils.sqlAddField(sqlFields, "CONT.MDLNRA"); // Nouveau num rapport
        CEUtils.sqlAddField(sqlFields, "CONT.MDTGEN"); // Genre controle
        CEUtils.sqlAddField(sqlFields, "CONT.MDBFDR"); // Controle actif
        CEUtils.sqlAddField(sqlFields, "REV.MILVIS"); // Nom réviseur
        CEUtils.sqlAddField(sqlFields, "aff2.MAIAFF AS CHANGETYPE");

        if (isForSansAttributionPts()) {
            CEUtils.sqlAddField(sqlFields, "ATT.MPAPID");
        }

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(CEControleEmployeur.TABLE_CECONTP).append(" CONT");
        sqlFrom.append(" LEFT JOIN ").append(_getCollection()).append("AFAFFIP AFF ON CONT.MAIAFF = AFF.MAIAFF ");
        sqlFrom.append(" LEFT JOIN ").append(_getCollection()).append("TITIERP TIERS ON AFF.HTITIE = TIERS.HTITIE");
        sqlFrom.append(" LEFT JOIN ").append(_getCollection()).append(CEReviseur.TABLE_CEREVIP)
                .append(" REV ON CONT.MDICTL = REV.MIIREV ");

        sqlFrom.append(" LEFT JOIN ")
                .append(_getCollection())
                .append("AFAFFIP AFF2 ON CONT.MAIAFF = AFF2.MAIAFF AND AFF2.MATTAF IN ("
                        + CodeSystem.TYPE_AFFILI_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", "
                        + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ")");
        if (!JadeStringUtil.isEmpty(getLikeLibelleGroupe())) {
            sqlFrom.append(" LEFT JOIN " + _getCollection() + "cememp as membre on (membre.maiaff = aff.maiaff)");
            sqlFrom.append(" LEFT JOIN " + _getCollection() + "CEGRPP as GROUPE on (GROUPE.ceidgr = membre.ceidgr)");
        }

        if (isForSansAttributionPts()) {
            sqlFrom.append(" LEFT JOIN "
                    + _getCollection()
                    + "CEATTPTS ATT on ATT.MPPEFI = CONT.MDDCFI AND ATT.MPPEDE = CONT.MDDCDE AND ATT.CEBAAV = '1' AND ATT.MALNAF = CONT.MALNAF");
        }

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (getOrderBy().length() != 0) {
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

        if (!JadeStringUtil.isEmpty(getLikeNouveauNumRapport())) {
            CEUtils.sqlAddCondition(sqlWhere, "CONT.MDLNRA LIKE '" + getLikeNouveauNumRapport() + "%'");
        }

        if (!JadeStringUtil.isEmpty(getLikeVisaReviseur())) {
            CEUtils.sqlAddCondition(sqlWhere, "REV.MILVIS LIKE '%" + getLikeVisaReviseur() + "%'");
        }

        if (!JadeStringUtil.isBlankOrZero(getForGenreControle())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "CONT.MDTGEN = " + this._dbWriteNumeric(statement.getTransaction(), getForGenreControle()));
        }

        if (!JadeStringUtil.isEmpty(getLikeNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere, "CONT.MALNAF LIKE '%" + getLikeNumAffilie() + "%'");
        }

        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "CONT.MDDPRE >= " + this._dbWriteNumeric(statement.getTransaction(), getForAnnee() + "0101")
                            + " AND MDDPRE <="
                            + this._dbWriteNumeric(statement.getTransaction(), getForAnnee() + "1231"));
        }

        if (!JadeStringUtil.isEmpty(getLikeLibelleGroupe())) {
            CEUtils.sqlAddCondition(sqlWhere, "GROUPE." + CEGroupe.FIELD_LIBELLE + " LIKE '" + getLikeLibelleGroupe()
                    + "%'");
        }

        if (isForActif()) {
            CEUtils.sqlAddCondition(sqlWhere, "CONT.MDBFDR = '1'");
        }

        if (isForSansAttributionPts()) {
            CEUtils.sqlAddCondition(sqlWhere, "ATT.MPAPID IS NULL");
            CEUtils.sqlAddCondition(sqlWhere, "CONT.MDDEFF <> 0");
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEListeControleEmployeur();
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForGenreControle() {
        return forGenreControle;
    }

    public String getLikeLibelleGroupe() {
        return likeLibelleGroupe;
    }

    public String getLikeNouveauNumRapport() {
        return likeNouveauNumRapport;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public String getLikeVisaReviseur() {
        return likeVisaReviseur;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isForActif() {
        return forActif;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public boolean isForSansAttributionPts() {
        return forSansAttributionPts;
    }

    public void setForActif(boolean forActif) {
        this.forActif = forActif;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForGenreControle(String forGenreControle) {
        this.forGenreControle = forGenreControle;
    }

    public void setForSansAttributionPts(boolean forSansAttributionPts) {
        this.forSansAttributionPts = forSansAttributionPts;
    }

    public void setLikeLibelleGroupe(String likeLibelleGroupe) {
        this.likeLibelleGroupe = likeLibelleGroupe;
    }

    public void setLikeNouveauNumRapport(String likeNouveauNumRapport) {
        this.likeNouveauNumRapport = likeNouveauNumRapport;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

    public void setLikeVisaReviseur(String likeVisaReviseur) {
        this.likeVisaReviseur = likeVisaReviseur;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
