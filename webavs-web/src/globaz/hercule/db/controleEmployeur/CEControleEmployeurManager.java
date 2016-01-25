package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CEControleEmployeurManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String field = "";
    private boolean forActif = false;
    private String forAffiliationId;
    private String forAnnee;
    private String forAnneeEffectuee = "";
    private String forAnneeFin = "";
    private String forControleEmployeurId;
    private String forDateDebutControle = "";
    private boolean forDateEffective = false;
    private String forDateFinControle = "";
    private String forGenreControle;
    private String forIdReviseur;
    private boolean forLastControlEffectif;
    private String forNotAnneePrevue;
    private boolean forNotDateEffective = false;
    private String forNotId;
    private String forNumAffilie;
    private boolean forNumRapportNonVide;
    private String likeNouveauNumRapport;
    private String likeNumAffilie;
    private String likeVisaReviseur;
    private String orderBy = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (getField().length() != 0) {
            return getField();
        }
        return "*";
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + CEControleEmployeur.TABLE_CECONTP);
        sqlFrom.append(" left join " + _getCollection() + CEReviseur.TABLE_CEREVIP + " on (mdictl=miirev) ");

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
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
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer();

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie()));
        }
        if (!JadeStringUtil.isEmpty(getLikeNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere, "MALNAF LIKE '%" + getLikeNumAffilie() + "%'");
        }
        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "MDDPRE >= " + this._dbWriteNumeric(statement.getTransaction(), getForAnnee() + "0101")
                            + " AND MDDPRE <="
                            + this._dbWriteNumeric(statement.getTransaction(), getForAnnee() + "1231"));
        }
        if (!JadeStringUtil.isEmpty(getForAnneeFin())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "MDDCFI >= " + this._dbWriteNumeric(statement.getTransaction(), getForAnnee() + "0101")
                            + " AND MDDCFI <="
                            + this._dbWriteNumeric(statement.getTransaction(), getForAnnee() + "1231"));
        }
        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MAIAFF = " + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId()));
        }
        if (!JadeStringUtil.isEmpty(getForControleEmployeurId())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MDICON = " + this._dbWriteNumeric(statement.getTransaction(), getForControleEmployeurId()));
        }
        if (getForLastControlEffectif() && !JadeStringUtil.isEmpty(getForAffiliationId())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "MDDEFF = " + "(SELECT MAX(MDDEFF) FROM " + _getCollection() + CEControleEmployeur.TABLE_CECONTP
                            + " WHERE MAIAFF="
                            + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId()) + ")");
        }
        if (!JadeStringUtil.isEmpty(getLikeNouveauNumRapport())) {
            CEUtils.sqlAddCondition(sqlWhere, "MDLNRA LIKE '" + getLikeNouveauNumRapport() + "%'");
        }
        if (!JadeStringUtil.isEmpty(getForNotId())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MDICON <> " + this._dbWriteNumeric(statement.getTransaction(), getForNotId()));
        }
        if (getForNumRapportNonVide()) {
            CEUtils.sqlAddCondition(sqlWhere, "(MDLNRA IS NULL OR MDLNRA='0')");
        }
        if (!JadeStringUtil.isEmpty(getForDateDebutControle())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MDDCDE = " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebutControle()));
        }
        if (!JadeStringUtil.isEmpty(getForDateFinControle())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MDDCFI = " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFinControle()));
        }
        if (forNotDateEffective) {
            CEUtils.sqlAddCondition(sqlWhere, "MDDEFF = 0");
        }
        if (forDateEffective) {
            CEUtils.sqlAddCondition(sqlWhere, "MDDEFF <> 0");
        }
        if (forActif) {
            CEUtils.sqlAddCondition(sqlWhere, "mdbfdr = '1'");
        }
        if (!JadeStringUtil.isEmpty(getLikeVisaReviseur())) {
            CEUtils.sqlAddCondition(sqlWhere, "MILVIS LIKE '%" + getLikeVisaReviseur() + "%'");
        }
        if (!JadeStringUtil.isBlankOrZero(getForIdReviseur())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MIIREV = " + this._dbWriteNumeric(statement.getTransaction(), getForIdReviseur()));
        }
        if (!JadeStringUtil.isBlankOrZero(getForGenreControle())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MDTGEN = " + this._dbWriteNumeric(statement.getTransaction(), getForGenreControle()));
        }
        if (!JadeStringUtil.isBlankOrZero(getForNotAnneePrevue())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "( MDDPRE < " + this._dbWriteNumeric(statement.getTransaction(), getForNotAnneePrevue() + "0101")
                            + " OR MDDPRE > "
                            + this._dbWriteNumeric(statement.getTransaction(), getForNotAnneePrevue() + "1231") + ")");
        }
        if (!JadeStringUtil.isBlankOrZero(getForAnneeEffectuee())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "( MDDEFF >= " + this._dbWriteNumeric(statement.getTransaction(), getForAnneeEffectuee() + "0101")
                            + " AND MDDEFF <= "
                            + this._dbWriteNumeric(statement.getTransaction(), getForAnneeEffectuee() + "1231") + ")");
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEControleEmployeur();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getField() {
        return field;
    }

    public String getForAffiliationId() {
        return forAffiliationId;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForAnneeEffectuee() {
        return forAnneeEffectuee;
    }

    public String getForAnneeFin() {
        return forAnneeFin;
    }

    public String getForControleEmployeurId() {
        return forControleEmployeurId;
    }

    public String getForDateDebutControle() {
        return forDateDebutControle;
    }

    public String getForDateFinControle() {
        return forDateFinControle;
    }

    public String getForGenreControle() {
        return forGenreControle;
    }

    public String getForIdReviseur() {
        return forIdReviseur;
    }

    public boolean getForLastControlEffectif() {
        return forLastControlEffectif;
    }

    public String getForNotAnneePrevue() {
        return forNotAnneePrevue;
    }

    public String getForNotId() {
        return forNotId;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public boolean getForNumRapportNonVide() {
        return forNumRapportNonVide;
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

    // *******************************************************
    // Setter
    // *******************************************************

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isForActif() {
        return forActif;
    }

    public boolean isForDateEffective() {
        return forDateEffective;
    }

    public boolean isForNotDateEffective() {
        return forNotDateEffective;
    }

    public void setField(String string) {
        field = string;
    }

    public void setForActif(boolean forActif) {
        this.forActif = forActif;
    }

    public void setForAffiliationId(String string) {
        forAffiliationId = string;
    }

    public void setForAnnee(String string) {
        forAnnee = string;
    }

    public void setForAnneeEffectuee(String forAnneeEffectuee) {
        this.forAnneeEffectuee = forAnneeEffectuee;
    }

    public void setForAnneeFin(String forAnneeFin) {
        this.forAnneeFin = forAnneeFin;
    }

    public void setForControleEmployeurId(String string) {
        forControleEmployeurId = string;
    }

    public void setForDateDebutControle(String forDateDebutControle) {
        this.forDateDebutControle = forDateDebutControle;
    }

    public void setForDateEffective(boolean forDateEffective) {
        this.forDateEffective = forDateEffective;
    }

    public void setForDateFinControle(String forDateFinControle) {
        this.forDateFinControle = forDateFinControle;
    }

    public void setForGenreControle(String forGenreControle) {
        this.forGenreControle = forGenreControle;
    }

    public void setForIdReviseur(String forIdReviseur) {
        this.forIdReviseur = forIdReviseur;
    }

    public void setForLastControlEffectif(boolean forLastControlEffectif) {
        this.forLastControlEffectif = forLastControlEffectif;
    }

    public void setForNotAnneePrevue(String forNotAnneePrevue) {
        this.forNotAnneePrevue = forNotAnneePrevue;
    }

    public void setForNotDateEffective(boolean forNotDateEffective) {
        this.forNotDateEffective = forNotDateEffective;
    }

    public void setForNotId(String forNotId) {
        this.forNotId = forNotId;
    }

    public void setForNumAffilie(String string) {
        forNumAffilie = string;
    }

    public void setForNumRapportNonVide(boolean forNumRapportNonVide) {
        this.forNumRapportNonVide = forNumRapportNonVide;
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

    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

}