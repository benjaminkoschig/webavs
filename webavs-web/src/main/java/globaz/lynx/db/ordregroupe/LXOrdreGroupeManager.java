package globaz.lynx.db.ordregroupe;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.organeexecution.LXOrganeExecution;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;

public class LXOrdreGroupeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat;
    private String forDateCreation;
    private String forDateEcheance;
    private String forIdOrdreGroupe;
    private String forIdOrganeExecution;
    private String forIdSociete;
    private String likeLibelle;
    private String likeProprietaire;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP);
        sqlFrom.append(" INNER JOIN ").append(_getCollection()).append(LXOrganeExecution.TABLE_LXOREXP);
        sqlFrom.append(" ON ").append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                .append(LXOrdreGroupe.FIELD_IDORGANEEXECUTION).append("=").append(_getCollection())
                .append(LXOrganeExecution.TABLE_LXOREXP).append(".").append(LXOrganeExecution.FIELD_IDORGANEEXECUTION);
        sqlFrom.append(" INNER JOIN ").append(_getCollection()).append(LXSocieteDebitrice.TABLE_LXSOCIP);
        sqlFrom.append(" ON ").append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                .append(LXSocieteDebitrice.FIELD_IDSOCIETE).append("=").append(_getCollection())
                .append(LXSocieteDebitrice.TABLE_LXSOCIP).append(".").append(LXSocieteDebitrice.FIELD_IDSOCIETE);
        sqlFrom.append(" INNER JOIN ").append(_getCollection()).append("TITIERP");
        sqlFrom.append(" ON ").append(_getCollection()).append(LXSocieteDebitrice.TABLE_LXSOCIP).append(".IDTIERS=")
                .append(_getCollection()).append("TITIERP.HTITIE");

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return LXOrdreGroupe.FIELD_IDORDREGROUPE + " DESC, " + LXOrdreGroupe.FIELD_DATECREATION + " DESC";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdOrdreGroupe()) && JadeStringUtil.isDigit(getForIdOrdreGroupe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                    .append(LXOrdreGroupe.FIELD_IDORDREGROUPE).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdOrdreGroupe()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                    .append(LXOrdreGroupe.FIELD_IDSOCIETE).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdSociete()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOrganeExecution())
                && JadeStringUtil.isDigit(getForIdOrganeExecution())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                    .append(LXOrdreGroupe.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdOrganeExecution()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat()) && JadeStringUtil.isDigit(getForCsEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                    .append(LXOrdreGroupe.FIELD_CSETAT).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        if (!JadeStringUtil.isBlank(getLikeProprietaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                    .append(LXOrdreGroupe.FIELD_PROPRIETAIRE).append(" like ")
                    .append(_dbWriteString(statement.getTransaction(), "%" + getLikeProprietaire() + "%"));
        }

        if (!JadeStringUtil.isBlank(getLikeLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                    .append(LXOrdreGroupe.FIELD_LIBELLE).append(" like ")
                    .append(_dbWriteString(statement.getTransaction(), "%" + getLikeLibelle() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateCreation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                    .append(LXOrdreGroupe.FIELD_DATECREATION).append(" = ")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateCreation()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateEcheance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOrdreGroupe.TABLE_LXORGRP).append(".")
                    .append(LXOrdreGroupe.FIELD_DATEECHEANCE).append(" = ")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateEcheance()));
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXOrdreGroupe();
    }

    // *******************************************************
    // Getter
    // *******************************************************}

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForDateCreation() {
        return forDateCreation;
    }

    public String getForDateEcheance() {
        return forDateEcheance;
    }

    public String getForIdOrdreGroupe() {
        return forIdOrdreGroupe;
    }

    public String getForIdOrganeExecution() {
        return forIdOrganeExecution;
    }

    public String getForIdSociete() {
        return forIdSociete;
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

    public void setForDateCreation(String forDateCreation) {
        this.forDateCreation = forDateCreation;
    }

    public void setForDateEcheance(String forDateEcheance) {
        this.forDateEcheance = forDateEcheance;
    }

    public void setForIdOrdreGroupe(String forIdOrdreGroupe) {
        this.forIdOrdreGroupe = forIdOrdreGroupe;
    }

    public void setForIdOrganeExecution(String forIdOrganeExecution) {
        this.forIdOrganeExecution = forIdOrganeExecution;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setLikeLibelle(String likeLibelle) {
        this.likeLibelle = likeLibelle;
    }

    public void setLikeProprietaire(String likeProprietaire) {
        this.likeProprietaire = likeProprietaire;
    }

}
