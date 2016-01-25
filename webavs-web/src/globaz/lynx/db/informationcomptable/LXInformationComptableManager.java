package globaz.lynx.db.informationcomptable;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;

public class LXInformationComptableManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsCodeTVA;
    private String forIdFournisseur;
    private String forIdSociete;
    private String likeSocieteDebitrice;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXInformationComptable.TABLE_LXINFCP + " INNER JOIN " + _getCollection()
                + LXSocieteDebitrice.TABLE_LXSOCIP + " ON " + _getCollection() + LXInformationComptable.TABLE_LXINFCP
                + "." + LXInformationComptable.FIELD_IDSOCIETE + "=" + _getCollection()
                + LXSocieteDebitrice.TABLE_LXSOCIP + "." + LXSocieteDebitrice.FIELD_IDSOCIETE + " INNER JOIN "
                + _getCollection() + "TITIERP ON " + _getCollection() + LXSocieteDebitrice.TABLE_LXSOCIP + ".IDTIERS="
                + _getCollection() + "TITIERP.HTITIE";
    }

    /**
     * Renvoie la clause de tri
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return _getCollection() + "TITIERP.HTLDU1, " + _getCollection() + "TITIERP.HTLDU2";
    }

    /*
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdFournisseur()) && JadeStringUtil.isDigit(getForIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXInformationComptable.TABLE_LXINFCP + "."
                    + LXInformationComptable.FIELD_IDFOURNISSEUR + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdFournisseur());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXInformationComptable.TABLE_LXINFCP + "."
                    + LXInformationComptable.FIELD_IDSOCIETE + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdSociete());
        }

        if (!JadeStringUtil.isBlank(getLikeSocieteDebitrice())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String nomFormatte = JadeStringUtil.convertSpecialChars(getLikeSocieteDebitrice());
            nomFormatte = JadeStringUtil.toUpperCase(nomFormatte);

            sqlWhere += _getCollection() + "TITIERP.HTLDU1" + " like "
                    + _dbWriteString(statement.getTransaction(), "%" + nomFormatte + "%");
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsCodeTVA())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXOperation.TABLE_LXOPERP + "." + LXOperation.FIELD_CSCODETVA + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForCsCodeTVA());
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXInformationComptable();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsCodeTVA() {
        return forCsCodeTVA;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getLikeSocieteDebitrice() {
        return likeSocieteDebitrice;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsCodeTVA(String forCsCodeTVA) {
        this.forCsCodeTVA = forCsCodeTVA;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setLikeSocieteDebitrice(String likeSocieteDebitrice) {
        this.likeSocieteDebitrice = likeSocieteDebitrice;
    }

}
