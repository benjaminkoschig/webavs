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
 * DOCUMENT ME!
 * 
 * @author jpa
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFEnfantManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdConjoint = new String();
    private String forIdConjoints = new String();
    private String forIdConjointsIn = new String();
    private String forIdMembreFamille = new String();

    private String idConjoints = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFEnfantManager.
     */
    public SFEnfantManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forIdConjoint)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFEnfant.FIELD_IDCONJOINT + "=" + _dbWriteNumeric(statement.getTransaction(), forIdConjoint);
        }
        if (!JadeStringUtil.isEmpty(forIdMembreFamille)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFEnfant.FIELD_IDMEMBREFAMILLE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdMembreFamille);
        }
        if (!JadeStringUtil.isEmpty(forIdConjoints)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFEnfant.FIELD_IDCONJOINT + "=" + _dbWriteNumeric(statement.getTransaction(), forIdConjoints);
        }

        if (!JadeStringUtil.isEmpty(forIdConjointsIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFEnfant.FIELD_IDCONJOINT + " IN (" + forIdConjointsIn + ") ";
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
        return new SFEnfant();
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
    public String getForIdConjoints() {
        return forIdConjoints;
    }

    public String getForIdConjointsIn() {
        return forIdConjointsIn;
    }

    /**
     * @return
     */
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * @return
     */
    public String getIdConjoints() {
        return idConjoints;
    }

    /**
     * @param string
     */
    public void setForIdConjoint(String string) {
        forIdConjoint = string;
    }

    /**
     * @param string
     */
    public void setForIdConjoints(String string) {
        forIdConjoints = string;
    }

    public void setForIdConjointsIn(String forIdConjointsIn) {
        this.forIdConjointsIn = forIdConjointsIn;
    }

    /**
     * @param string
     */
    public void setForIdMembreFamille(String string) {
        forIdMembreFamille = string;
    }

    /**
     * @param string
     */
    public void setIdConjoints(String string) {
        idConjoints = string;
    }

}
