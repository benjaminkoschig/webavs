package globaz.lynx.db.section;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Manager sur la table LXSECP représentant un object LXSection
 * 
 * @author SCO
 */
public class LXSectionManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeSection;
    private String forIdExterne;
    private String forIdFournisseur;
    private String forIdJournal;
    private String forIdSection;
    private String forIdSociete;
    private String forNotIdSection;
    private String likeIdExterne;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXSection.TABLE_LXSECTP;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer tmp = new StringBuffer();

        tmp.append(_getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDEXTERNE + " DESC ");

        return tmp.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdJournal()) && JadeStringUtil.isDigit(getForIdJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDJOURNAL + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDSOCIETE + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdSociete());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdFournisseur()) && JadeStringUtil.isDigit(getForIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDFOURNISSEUR + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdFournisseur());
        }

        if (!JadeStringUtil.isBlank(getForIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDEXTERNE + " = "
                    + _dbWriteString(statement.getTransaction(), getForIdExterne());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsTypeSection()) && JadeStringUtil.isDigit(getForCsTypeSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_CSTYPESECTION + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForCsTypeSection());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSection()) && JadeStringUtil.isDigit(getForIdSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDSECTION + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDEXTERNE + " like "
                    + _dbWriteString(statement.getTransaction(), "%" + getLikeIdExterne() + "%");
        }

        if (!JadeStringUtil.isIntegerEmpty(getForNotIdSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDSECTION + " <> "
                    + _dbWriteNumeric(statement.getTransaction(), getForNotIdSection());
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXSection();
    }

    // *******************************************************
    // Getter
    // *******************************************************}

    public String getForCsTypeSection() {
        return forCsTypeSection;
    }

    public String getForIdExterne() {
        return forIdExterne;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getForNotIdSection() {
        return forNotIdSection;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    // *******************************************************
    // Setter
    // *******************************************************}

    public void setForCsTypeSection(String forCsTypeSection) {
        this.forCsTypeSection = forCsTypeSection;
    }

    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setForNotIdSection(String forNotIdSection) {
        this.forNotIdSection = forNotIdSection;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

}
