/*
 * Créé le 9 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author mmu manager faisant la jointure entre la table des conjoints, la relation des conjoints et les membres de la
 *         famille pour avoir les informations sur les conjoints
 */
public class SFApercuRelationConjointManager extends SFRelationConjointManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String exceptIdConjoints = "";
    private String exceptIdRelationConjoint = "";
    private String forIdConjoint = new String();
    private boolean noWantEnfantCommun = false;
    private boolean noWantRelationIndefinie = false;

    @Override
    protected String _getFields(BStatement statement) {
        return SFApercuRelationConjoint.createSelectClause();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return SFApercuRelationConjoint.createFromClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);
        if (!JadeStringUtil.isEmpty(forIdConjoint)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // Etant donnée que le conjoint peut être soit au champ idCOnjoint1
            // ou idConjoint2,
            // On recherche sur les deux champs
            sqlWhere += "(" + SFConjoint.FIELD_IDCONJOINT1 + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdConjoint) + " OR "
                    + SFConjoint.FIELD_IDCONJOINT2 + "=" + _dbWriteNumeric(statement.getTransaction(), forIdConjoint)
                    + ")";
        }

        if (!JadeStringUtil.isEmpty(exceptIdConjoints)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // Exclut le couple de conjoints
            sqlWhere += SFRelationConjoint.FIELD_IDCONJOINTS + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), exceptIdConjoints);
        }

        if (!JadeStringUtil.isEmpty(exceptIdRelationConjoint)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // Exclut le couple de conjoints
            sqlWhere += SFRelationConjoint.FIELD_IDRELATIONCONJOINT + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), exceptIdRelationConjoint);
        }

        // Si on ne veut pas retourner les relation de type Relation indéfine
        if (noWantRelationIndefinie) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFRelationConjoint.FIELD_TYPERELATION + " <> "
                    + _dbWriteNumeric(statement.getTransaction(), ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE);

        }
        // Si on ne veut pas retourner les relation de type enfant commun
        if (noWantEnfantCommun) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFRelationConjoint.FIELD_TYPERELATION + " <> "
                    + _dbWriteNumeric(statement.getTransaction(), ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN);

        }

        return sqlWhere;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFApercuRelationConjoint();
    }

    /**
     * @return
     */
    public String getExceptIdConjoints() {
        return exceptIdConjoints;
    }

    /**
     * @return
     */
    public String getExceptIdRelationConjoint() {
        return exceptIdRelationConjoint;
    }

    /**
     * @return
     */
    public String getForIdConjoint() {
        return forIdConjoint;
    }

    /**
     * @return
     */
    public boolean isNoWantEnfantCommun() {
        return noWantEnfantCommun;
    }

    /**
     * @return
     */
    public boolean isNoWantRelationIndefinie() {
        return noWantRelationIndefinie;
    }

    /**
     * recherche des relations excepté sur les conjoints donnés
     * 
     * @param string
     */
    public void setExceptIdConjoints(String string) {
        exceptIdConjoints = string;
    }

    /**
     * @param string
     */
    public void setExceptIdRelationConjoint(String string) {
        exceptIdRelationConjoint = string;
    }

    /**
     * @param string
     */
    public void setForIdConjoint(String string) {
        forIdConjoint = string;
    }

    /**
     * Si on ne veut pas retourner les relation de type Enfant commun
     * 
     * @param b
     */
    public void setNoWantEnfantCommun(boolean b) {
        noWantEnfantCommun = b;
    }

    /**
     * Si on ne veut pas retourner les relation de type Relation indéfine
     * 
     * @param b
     */
    public void setNoWantRelationIndefinie(boolean b) {
        noWantRelationIndefinie = b;
    }

}
