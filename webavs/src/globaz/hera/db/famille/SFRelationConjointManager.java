/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * DOCUMENT ME!
 * 
 * @author jpa
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFRelationConjointManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String[] forIdConjoints = new String[2];
    private String forIdDesConjoints = "";
    private String forTypeRelation = "";
    private String fromDateDebut = "";
    private boolean orderByDateDebutDsc = false;
    private String untilDateDebut = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFRelationConjointManager.
     */
    public SFRelationConjointManager() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (orderByDateDebutDsc) {
            return SFRelationConjoint.FIELD_DATEDEBUT + " DESC ";
        } else {
            return SFRelationConjoint.FIELD_DATEDEBUT + " ASC ";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = new String();
        if ((!JadeStringUtil.isEmpty(forIdConjoints[0])) && (!JadeStringUtil.isEmpty(forIdConjoints[1]))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // Etant donnée que le conjoint peut être soit au champ idCOnjoint1
            // ou idConjoint2,
            // On recherche sur les deux champs
            sqlWhere += "(" + SFConjoint.FIELD_IDCONJOINT1 + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdConjoints[0]) + " AND "
                    + SFConjoint.FIELD_IDCONJOINT2 + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdConjoints[1]) + ")" + " OR " + "("
                    + SFConjoint.FIELD_IDCONJOINT1 + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdConjoints[1]) + " AND "
                    + SFConjoint.FIELD_IDCONJOINT2 + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdConjoints[0]) + ")";
        }

        if (!JadeStringUtil.isEmpty(forIdDesConjoints)) {
            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFRelationConjoint.FIELD_IDCONJOINTS + " = "
                    + _dbWriteNumeric(statement.getTransaction(), forIdDesConjoints);
        }
        if (!JAUtil.isDateEmpty(untilDateDebut)) {
            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFRelationConjoint.FIELD_DATEDEBUT + " <= "
                    + _dbWriteDateAMJ(statement.getTransaction(), untilDateDebut) + " AND "
                    + SFRelationConjoint.FIELD_DATEDEBUT + "  > 0 ";
        }
        if (!JAUtil.isDateEmpty(fromDateDebut)) {
            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFRelationConjoint.FIELD_DATEDEBUT + " >= "
                    + _dbWriteDateAMJ(statement.getTransaction(), fromDateDebut) + " AND "
                    + SFRelationConjoint.FIELD_DATEDEBUT + "  > 0 ";
        }
        if (!JadeStringUtil.isEmpty(forTypeRelation)) {
            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFRelationConjoint.FIELD_TYPERELATION + " = "
                    + _dbWriteNumeric(statement.getTransaction(), forTypeRelation);
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
        return new SFRelationConjoint();
    }

    /**
     * @return
     */
    public String getForIdDesConjoints() {
        return forIdDesConjoints;
    }

    /**
     * @return
     */
    public String getForTypeRelation() {
        return forTypeRelation;
    }

    /**
     * @return
     */
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * @return
     */
    public String getUntilDateDebut() {
        return untilDateDebut;
    }

    /**
     * @return
     */
    public boolean isOrderByDateDebutDsc() {
        return orderByDateDebutDsc;
    }

    /**
     * @param string
     */
    public void setForIdDesConjoints(String string) {
        forIdDesConjoints = string;
    }

    /**
     * @param string
     */
    public void setForTypeRelation(String string) {
        forTypeRelation = string;
    }

    /**
     * Recherche toutes les relations qui commencent à partir de la date donnée (a >= b)
     * 
     * @param string
     */
    public void setFromDateDebut(String string) {
        fromDateDebut = string;
    }

    /**
     * @param b
     */
    public void setOrderByDateDebutDsc(boolean b) {
        orderByDateDebutDsc = b;
    }

    /**
     * Recherche toutes les relations qui commencent à partir de la date donnée (a <= b)
     * 
     * @param string
     *            une date au format "jj.mm.aaaa"
     */
    public void setUntilDateDebut(String string) {
        untilDateDebut = string;
    }

}
