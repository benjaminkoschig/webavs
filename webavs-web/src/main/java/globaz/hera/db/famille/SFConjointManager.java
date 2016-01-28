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
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author jpa
 */
public class SFConjointManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdConjoint = "";
    // Si on veut retrouver un membre d'après 2 idConjoint
    private String forIdConjoint1 = "";
    private String forIdConjoint2 = "";
    // Si on veut retrouver un membre d'après l'idConjoints
    private String forIdDesConjoints = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFCoinjointManager.
     */
    public SFConjointManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forIdConjoint)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // Etant donnée que le conjoint peut être soit au champ idCOnjoint1
            // ou idConjoint2,
            // On recherche sur les deux champs
            sqlWhere += "(" + SFConjoint.FIELD_IDCONJOINT1 + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdConjoint) + " OR " + "("
                    + SFConjoint.FIELD_IDCONJOINT2 + "=" + _dbWriteNumeric(statement.getTransaction(), forIdConjoint)
                    + "))";
        }
        if (!JadeStringUtil.isEmpty(forIdDesConjoints)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFConjoint.FIELD_IDCONJOINTS + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDesConjoints) + ")";

        }

        if (forIdConjoint1.length() > 0 && forIdConjoint2.length() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // Etant donnée que le conjoint peut être soit au champ idCOnjoint1
            // ou idConjoint2,
            // On recherche sur les deux champs
            sqlWhere += "(" + SFConjoint.FIELD_IDCONJOINT1 + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdConjoint1) + " AND "
                    + SFConjoint.FIELD_IDCONJOINT2 + "=" + _dbWriteNumeric(statement.getTransaction(), forIdConjoint2)
                    + ") OR (" + SFConjoint.FIELD_IDCONJOINT1 + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdConjoint2) + " AND "
                    + SFConjoint.FIELD_IDCONJOINT2 + "=" + _dbWriteNumeric(statement.getTransaction(), forIdConjoint1)
                    + ")";
        }
        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
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
        return new SFConjoint();
    }

    /**
     * Permet de retrouver tous les conjoint d'un membre
     * 
     * @return
     */
    public String getForIdConjoint() {
        return forIdConjoint;
    }

    /**
     * @return
     */
    public String getForIdDesConjoints() {
        return forIdDesConjoints;
    }

    /**
     * Recherche tout les conjoints d'un membre donné
     * 
     * @param string
     */
    public void setForIdConjoint(String conjoint) {
        forIdConjoint = conjoint;
    }

    /**
     * @param string
     */
    public void setForIdDesConjoints(String string) {
        forIdDesConjoints = string;
    }

    /**
     * Fixe l'id membre des deux conjoints, l'ordre des parametres n'est pas important
     * 
     * @param string
     */
    public void setForIdsConjoints(String conjoint1, String conjoint2) {
        forIdConjoint1 = conjoint1;
        forIdConjoint2 = conjoint2;
    }

}
