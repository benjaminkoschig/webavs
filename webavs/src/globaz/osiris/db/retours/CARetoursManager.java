package globaz.osiris.db.retours;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;

/**
 * <H1>Description</H1>
 * 
 * @author acr
 */

public class CARetoursManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatRetour = null;
    private String forCsMotifRetour = null;
    private String forDateRetour = null;
    private String forIdCompteAnnexe = null;
    private String forIdLot = null;
    private String forMontantRetour = null;
    private String likeLibelleRetour = new String();
    private String likeNumNom = new String();
    private String orderBy = CARetours.FIELDNAME_ID_LOT + " DESC, " + CARetours.FIELDNAME_DATE_RETOUR + " DESC ";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(CARetours.TABLE_NAME_RETOURS);

        // jointure entre table des retours et table des comptes annexes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(CACompteAnnexe.TABLE_CACPTAP);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(CARetours.TABLE_NAME_RETOURS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(CARetours.FIELDNAME_ID_COMPTE_ANNEXE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(CACompteAnnexe.TABLE_CACPTAP);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);

        return fromClauseBuffer.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    /**
     * pour le delete pas de jointure sur la table des comptes annexes
     */
    @Override
    protected String _getSqlDelete(BStatement statement) throws Exception {
        StringBuffer sqlBuffer = new StringBuffer("DELETE FROM ");
        sqlBuffer.append(super._getFrom(statement));
        String sqlWhere = _getWhere(statement);
        if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
            sqlBuffer.append(" WHERE ");
            sqlBuffer.append(sqlWhere);
            return sqlBuffer.toString();
        } else {
            throw new Exception("Not allowed to empty all the contents of the table: " + _getFrom(statement));
        }
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // Id Lot
        if (!JadeStringUtil.isBlank(getForIdLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CARetours.TABLE_NAME_RETOURS + "." + CARetours.FIELDNAME_ID_LOT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        // CsEtatRetour
        if (!JadeStringUtil.isBlank(getForCsEtatRetour())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }

            if (CARetours.CLE_ETAT_RETOUR_NON_LIQUIDE.equals(getForCsEtatRetour())) {

                sqlWhere += "(" + CARetours.FIELDNAME_ETAT_RETOUR + "="
                        + this._dbWriteNumeric(statement.getTransaction(), CARetours.CS_ETAT_RETOUR_OUVERT) + " OR "
                        + CARetours.FIELDNAME_ETAT_RETOUR + "="
                        + this._dbWriteNumeric(statement.getTransaction(), CARetours.CS_ETAT_RETOUR_SUSPENS) + " OR "
                        + CARetours.FIELDNAME_ETAT_RETOUR + "="
                        + this._dbWriteNumeric(statement.getTransaction(), CARetours.CS_ETAT_RETOUR_TRAITE) + ")";
            } else {
                sqlWhere += CARetours.FIELDNAME_ETAT_RETOUR + "="
                        + this._dbWriteNumeric(statement.getTransaction(), getForCsEtatRetour());
            }
        }
        // Id Compte Annexe
        if (!JadeStringUtil.isBlank(getForIdCompteAnnexe())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CARetours.FIELDNAME_ID_COMPTE_ANNEXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }
        // date retour
        if (!JadeStringUtil.isBlank(getForDateRetour())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CARetours.FIELDNAME_DATE_RETOUR + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateRetour());
        }
        // Libellé retour
        if (!JadeStringUtil.isBlank(getLikeLibelleRetour())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + CARetours.FIELDNAME_LIBELLE_RETOUR + ") like UPPER ("
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeLibelleRetour() + "%") + ")";
        }
        // montant retour
        if (!JadeStringUtil.isBlank(getForMontantRetour())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CARetours.FIELDNAME_MONTANT_RETOUR + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForMontantRetour());
        }
        // Traitement de la sélection à partir d'un numéro ou d'un nom
        if (!JadeStringUtil.isBlank(getLikeNumNom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String choix = JadeStringUtil.change(getLikeNumNom(), ".", "");
            choix = JadeStringUtil.change(choix, "-", "");
            if (JadeStringUtil.isDigit(choix)) {
                sqlWhere += "IDEXTERNEROLE like "
                        + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumNom() + "%");
            } else {
                sqlWhere += CACompteAnnexe.FIELD_DESCUPCASE
                        + " like "
                        + this._dbWriteString(statement.getTransaction(),
                                "%" + JadeStringUtil.convertSpecialChars(getLikeNumNom()).toUpperCase() + "%");
            }
        }
        // CsMotifRetour
        if (!JadeStringUtil.isBlank(getForCsMotifRetour())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CARetours.FIELDNAME_MOTIF_RETOUR + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForCsMotifRetour());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CARetours();
    }

    public String getForCsEtatRetour() {
        return forCsEtatRetour;
    }

    public String getForCsMotifRetour() {
        return forCsMotifRetour;
    }

    public String getForDateRetour() {
        return forDateRetour;
    }

    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForMontantRetour() {
        return forMontantRetour;
    }

    public String getLikeLibelleRetour() {
        return likeLibelleRetour;
    }

    public String getLikeNumNom() {
        return likeNumNom;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setForCsEtatRetour(String forCsEtatRetour) {
        this.forCsEtatRetour = forCsEtatRetour;
    }

    public void setForCsMotifRetour(String forCsMotifRetour) {
        this.forCsMotifRetour = forCsMotifRetour;
    }

    public void setForDateRetour(String forDateRetour) {
        this.forDateRetour = forDateRetour;
    }

    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForMontantRetour(String forMontantRetour) {
        this.forMontantRetour = forMontantRetour;
    }

    public void setLikeLibelleRetour(String likeLibelleRetour) {
        this.likeLibelleRetour = likeLibelleRetour;
    }

    public void setLikeNumNom(String likeNumNom) {
        this.likeNumNom = likeNumNom;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
