package globaz.osiris.db.ordres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * CA ordre groupé manager. Date de création : (13.12.2001 10:23:59)
 * 
 * @author: Brand
 */
public class CAOrdreGroupeManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateEcheance = new String();
    private String forIdOrdreGroupe = new String();
    private String forTypeOrdreGroupe = new String();
    private String fromMotif = new String();
    private String likeMotif = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOrdreGroupe.TABLE_CAORGRP;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return CAOrdreGroupe.FIELD_IDORDREGROUPE + " DESC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlank(getForIdOrdreGroupe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreGroupe.FIELD_IDORDREGROUPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOrdreGroupe());
        }

        if (!JadeStringUtil.isBlank(getForDateEcheance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreGroupe.FIELD_DATEECHEANCE + " = "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateEcheance());

        }

        if (!JadeStringUtil.isBlank(getFromMotif())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreGroupe.FIELD_MOTIF + " = "
                    + this._dbWriteString(statement.getTransaction(), getFromMotif());

        }

        if (!JadeStringUtil.isBlank(getLikeMotif())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreGroupe.FIELD_MOTIF + " like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeMotif() + "%");

        }

        if ((!JadeStringUtil.isBlank(getForTypeOrdreGroupe()))
                && (!forTypeOrdreGroupe.equalsIgnoreCase(CAOrdreGroupe.TOUS))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreGroupe.FIELD_TYPEORDREGROUPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForTypeOrdreGroupe());
        }

        return sqlWhere;

    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAOrdreGroupe();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 16:17:07)
     * 
     * @return String
     */
    public String getForDateEcheance() {
        return forDateEcheance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 15:21:51)
     * 
     * @return String
     */
    public String getForIdOrdreGroupe() {
        return forIdOrdreGroupe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.03.2002 13:21:26)
     * 
     * @return int
     */
    public String getForTypeOrdreGroupe() {
        return forTypeOrdreGroupe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 16:17:49)
     * 
     * @return String
     */
    public String getFromMotif() {
        return fromMotif;
    }

    public String getLikeMotif() {
        return likeMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 16:17:07)
     * 
     * @param newFromDateEcheance
     *            String
     */
    public void setForDateEcheance(String newForDateEcheance) {
        forDateEcheance = newForDateEcheance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 15:21:51)
     * 
     * @param newForIdOrdreGroupe
     *            String
     */
    public void setForIdOrdreGroupe(String newForIdOrdreGroupe) {
        forIdOrdreGroupe = newForIdOrdreGroupe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.03.2002 13:21:26)
     * 
     * @param newForTypeOrdreGroupe
     *            int
     */
    public void setForTypeOrdreGroupe(String newForTypeOrdreGroupe) {
        forTypeOrdreGroupe = newForTypeOrdreGroupe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 16:17:49)
     * 
     * @param newFromMotif
     *            String
     */
    public void setFromMotif(String newFromMotif) {
        fromMotif = newFromMotif;
    }

    public void setLikeMotif(String likeMotif) {
        this.likeMotif = likeMotif;
    }
}
