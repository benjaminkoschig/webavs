/*
 * Créé le 15 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APDroitAPGManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDroitParent = "";
    private String forIdSituationFamiliale = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdDroitParent)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroitParent);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdSituationFamiliale)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APDroitAPG.FIELDNAME_IDSITUATIONFAM + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdSituationFamiliale);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APDroitAPG();
    }

    /**
     * getter pour l'attribut for id droit parent
     * 
     * @return la valeur courante de l'attribut for id droit parent
     */
    public String getForIdDroitParent() {
        return forIdDroitParent;
    }

    /**
     * getter pour l'attribut for id situation familiale
     * 
     * @return la valeur courante de l'attribut for id situation familiale
     */
    public String getForIdSituationFamiliale() {
        return forIdSituationFamiliale;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APDroitAPG.FIELDNAME_IDDROIT_APG;
    }

    /**
     * setter pour l'attribut for id droit parent
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroitParent(String string) {
        forIdDroitParent = string;
    }

    /**
     * setter pour l'attribut for id situation familiale
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdSituationFamiliale(String string) {
        forIdSituationFamiliale = string;
    }
}
