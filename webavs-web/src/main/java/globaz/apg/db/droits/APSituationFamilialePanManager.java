/*
 * Créé le 13 mai 05
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
 *
 */
public class APSituationFamilialePanManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    protected String forIdDroit = "";

    /** DOCUMENT ME! */
    protected String forType = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APSituationFamilialeMatManager.
     */
    public APSituationFamilialePanManager() {
    }

    /**
     * Crée une nouvelle instance de la classe APSituationFamilialePanManager.
     *
     * @param forType
     *            DOCUMENT ME!
     */
    protected APSituationFamilialePanManager(String forType) {
        this.forType = forType;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forIdDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APSituationFamilialePan.TABLE_NAME + "."
                    + APSituationFamilialePan.FIELDNAME_IDDROIT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroit);
        }

        if (!JadeStringUtil.isEmpty(forType)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APSituationFamilialePan.TABLE_NAME + "." + APSituationFamilialePan.FIELDNAME_TYPE
                    + "=" + _dbWriteNumeric(statement.getTransaction(), forType);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APSituationFamilialePan();
    }

    /**
     * getter pour l'attribut for id droit
     *
     * @return la valeur courante de l'attribut for id droit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * getter pour l'attribut for type
     *
     * @return la valeur courante de l'attribut for type
     */
    public String getForType() {
        return forType;
    }

    /**
     * (non-Javadoc)
     *
     * @see PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APSituationFamilialePan.FIELDNAME_IDSITFAM;
    }

    /**
     * setter pour l'attribut for id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String string) {
        forIdDroit = string;
    }

    /**
     * setter pour l'attribut for type
     * 
     * @param forType
     *            une nouvelle valeur pour cet attribut
     */
    public void setForType(String forType) {
        this.forType = forType;
    }
}
