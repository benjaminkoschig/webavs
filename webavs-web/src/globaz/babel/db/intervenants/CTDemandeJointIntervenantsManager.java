/*
 * Créé le 26 sept. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.intervenants;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTDemandeJointIntervenantsManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeIntervenant = "";
    private String forIdDemande = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        if (!JadeStringUtil.isBlank(forIdDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + CTDemandeJointIntervenants.TABLE_DEMANDE + "."
                    + CTDemandeJointIntervenants.FIELDNAME_ID_DEMANDE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDemande));
        }

        if (!JadeStringUtil.isBlank(forCsTypeIntervenant)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + CTDemandeJointIntervenants.TABLE_INTRVENANTS + "."
                    + CTDemandeJointIntervenants.FIELDNAME_CS_DESCRIPTION + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsTypeIntervenant));
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTDemandeJointIntervenants();
    }

    /**
     * @return
     */
    public String getForCsTypeIntervenant() {
        return forCsTypeIntervenant;
    }

    /**
     * @return
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    public String getOrderByDefaut() {
        return CTDemandeJointIntervenants.FIELDNAME_ID_META_DOSSIER;
    }

    /**
     * @param string
     */
    public void setForCsTypeIntervenant(String string) {
        forCsTypeIntervenant = string;
    }

    /**
     * @param string
     */
    public void setForIdDemande(String string) {
        forIdDemande = string;
    }

}
