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
 * @author mmu
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFRelationFamilialeRequerantManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdMembreFamille = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isBlankOrZero(getForIdMembreFamille())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(SFRelationFamilialeRequerant.FIELD_IDMEMBREFAMILLE);
            whereClause.append(" = ");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdMembreFamille()));

        }
        return whereClause.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFRelationFamilialeRequerant();
    }

    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

}
