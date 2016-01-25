package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class LXCanevasSectionManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeSection;
    private String forIdExterne;
    private String forIdFournisseur;
    private String forIdSectionCanevas;
    private String forIdSociete;
    private String likeIdExterne;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXCanevasSection.TABLE_LXCANSP;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer tmp = new StringBuffer();

        tmp.append(_getCollection() + LXCanevasSection.TABLE_LXCANSP + "." + LXCanevasSection.FIELD_IDEXTERNE
                + " DESC ");

        return tmp.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXCanevasSection.TABLE_LXCANSP + "." + LXCanevasSection.FIELD_IDSOCIETE
                    + " = " + this._dbWriteNumeric(statement.getTransaction(), getForIdSociete());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdFournisseur()) && JadeStringUtil.isDigit(getForIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXCanevasSection.TABLE_LXCANSP + "." + LXCanevasSection.FIELD_IDFOURNISSEUR
                    + " = " + this._dbWriteNumeric(statement.getTransaction(), getForIdFournisseur());
        }

        if (!JadeStringUtil.isBlank(getForIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXCanevasSection.TABLE_LXCANSP + "." + LXCanevasSection.FIELD_IDEXTERNE
                    + " = " + this._dbWriteString(statement.getTransaction(), getForIdExterne());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSectionCanevas())
                && JadeStringUtil.isDigit(getForIdSectionCanevas())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXCanevasSection.TABLE_LXCANSP + "."
                    + LXCanevasSection.FIELD_IDSECTIONCANEVAS + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSectionCanevas());
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXCanevasSection.TABLE_LXCANSP + "." + LXCanevasSection.FIELD_IDEXTERNE
                    + " like " + this._dbWriteString(statement.getTransaction(), "%" + getLikeIdExterne() + "%");
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXCanevasSection();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsTypeSection() {
        return forCsTypeSection;
    }

    public String getForIdExterne() {
        return forIdExterne;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdSectionCanevas() {
        return forIdSectionCanevas;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsTypeSection(String forCsTypeSection) {
        this.forCsTypeSection = forCsTypeSection;
    }

    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdSectionCanevas(String forIdSectionCanevas) {
        this.forIdSectionCanevas = forIdSectionCanevas;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

}
