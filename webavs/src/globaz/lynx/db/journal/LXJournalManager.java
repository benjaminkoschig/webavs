package globaz.lynx.db.journal;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;

public class LXJournalManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat;
    private String forCsTypeJournal;
    private String forDateCreation;
    private String forDateValeurCG;
    private String forIdJournal;
    private String forIdSociete;
    private String forProprietaire;
    private String likeLibelle;
    private String likeProprietaire;

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(LXJournal.TABLE_LXJOURP);
        sqlFrom.append(" INNER JOIN ").append(_getCollection()).append(LXSocieteDebitrice.TABLE_LXSOCIP);
        sqlFrom.append(" ON " + _getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                .append(LXJournal.FIELD_IDSOCIETE).append("=").append(_getCollection())
                .append(LXSocieteDebitrice.TABLE_LXSOCIP).append(".").append(LXSocieteDebitrice.FIELD_IDSOCIETE);
        sqlFrom.append(" INNER JOIN ").append(_getCollection()).append("TITIERP");
        sqlFrom.append(" ON ").append(_getCollection()).append(LXSocieteDebitrice.TABLE_LXSOCIP).append(".IDTIERS=")
                .append(_getCollection()).append("TITIERP.HTITIE");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return LXJournal.FIELD_IDJOURNAL + " DESC, " + LXJournal.FIELD_DATECREATION + " DESC";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdJournal()) && JadeStringUtil.isDigit(getForIdJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_IDJOURNAL).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdJournal()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_IDSOCIETE).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdSociete()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat()) && JadeStringUtil.isDigit(getForCsEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_CSETAT).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        if (!JadeStringUtil.isBlank(getLikeLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_LIBELLE).append(" like ")
                    .append(_dbWriteString(statement.getTransaction(), "%" + getLikeLibelle() + "%"));
        }

        if (!JadeStringUtil.isBlank(getLikeProprietaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_PROPRIETAIRE).append(" like ")
                    .append(_dbWriteString(statement.getTransaction(), "%" + getLikeProprietaire() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateCreation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_DATECREATION).append(" = ")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateCreation()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateValeurCG())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_DATEVALEURCG).append(" = ")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateValeurCG()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsTypeJournal()) && JadeStringUtil.isDigit(getForCsTypeJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_CSTYPEJOURNAL).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsTypeJournal()));
        }

        if (!JadeStringUtil.isBlank(getForProprietaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXJournal.TABLE_LXJOURP).append(".")
                    .append(LXJournal.FIELD_PROPRIETAIRE).append(" = ")
                    .append(_dbWriteString(statement.getTransaction(), getForProprietaire()));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXJournal();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsTypeJournal() {
        return forCsTypeJournal;
    }

    public String getForDateCreation() {
        return forDateCreation;
    }

    public String getForDateValeurCG() {
        return forDateValeurCG;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getForProprietaire() {
        return forProprietaire;
    }

    public String getLikeLibelle() {
        return likeLibelle;
    }

    public String getLikeProprietaire() {
        return likeProprietaire;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsTypeJournal(String forCsTypeJournal) {
        this.forCsTypeJournal = forCsTypeJournal;
    }

    public void setForDateCreation(String forDateCreation) {
        this.forDateCreation = forDateCreation;
    }

    public void setForDateValeurCG(String forDateValeurCG) {
        this.forDateValeurCG = forDateValeurCG;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setForProprietaire(String forProprietaire) {
        this.forProprietaire = forProprietaire;
    }

    public void setLikeLibelle(String likeLibelle) {
        this.likeLibelle = likeLibelle;
    }

    public void setLikeProprietaire(String likeProprietaire) {
        this.likeProprietaire = likeProprietaire;
    }

}
