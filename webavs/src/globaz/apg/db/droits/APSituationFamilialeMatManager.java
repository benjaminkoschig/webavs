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
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class APSituationFamilialeMatManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    protected String forIdDroitMaternite = "";

    /** DOCUMENT ME! */
    protected String forType = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APSituationFamilialeMatManager.
     */
    public APSituationFamilialeMatManager() {
    }

    /**
     * Crée une nouvelle instance de la classe APSituationFamilialeMatManager.
     * 
     * @param forType
     *            DOCUMENT ME!
     */
    protected APSituationFamilialeMatManager(String forType) {
        this.forType = forType;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forIdDroitMaternite)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APSituationFamilialeMat.TABLE_NAME + "."
                    + APSituationFamilialeMat.FIELDNAME_IDDROITMATERNITE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroitMaternite);
        }

        if (!JadeStringUtil.isEmpty(forType)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APSituationFamilialeMat.TABLE_NAME + "." + APSituationFamilialeMat.FIELDNAME_TYPE
                    + "=" + _dbWriteNumeric(statement.getTransaction(), forType);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APSituationFamilialeMat();
    }

    /**
     * getter pour l'attribut for id droit maternite
     * 
     * @return la valeur courante de l'attribut for id droit maternite
     */
    public String getForIdDroitMaternite() {
        return forIdDroitMaternite;
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
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APSituationFamilialeMat.FIELDNAME_IDSITFAMMATERNITE;
    }

    /**
     * setter pour l'attribut for id droit maternite
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroitMaternite(String string) {
        forIdDroitMaternite = string;
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
