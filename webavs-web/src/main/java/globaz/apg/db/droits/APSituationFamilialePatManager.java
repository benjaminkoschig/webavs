/*
 * Cr�� le 13 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
public class APSituationFamilialePatManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    protected String forIdDroitPaternite = "";

    /** DOCUMENT ME! */
    protected String forType = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe APSituationFamilialePatManager.
     */
    public APSituationFamilialePatManager() {
    }

    /**
     * Cr�e une nouvelle instance de la classe APSituationFamilialePatManager.
     *
     * @param forType
     *            DOCUMENT ME!
     */
    protected APSituationFamilialePatManager(String forType) {
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

        if (!JadeStringUtil.isEmpty(forIdDroitPaternite)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APSituationFamilialePat.TABLE_NAME + "."
                    + APSituationFamilialePat.FIELDNAME_IDDROITPATERNITE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroitPaternite);
        }

        if (!JadeStringUtil.isEmpty(forType)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APSituationFamilialePat.TABLE_NAME + "." + APSituationFamilialePat.FIELDNAME_TYPE
                    + "=" + _dbWriteNumeric(statement.getTransaction(), forType);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APSituationFamilialePat();
    }

    /**
     * getter pour l'attribut for id droit paternite
     *
     * @return la valeur courante de l'attribut for id droit paternite
     */
    public String getForIdDroitPaternite() {
        return forIdDroitPaternite;
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
        return APSituationFamilialePat.FIELDNAME_IDSITFAMPATERNITE;
    }

    /**
     * setter pour l'attribut for id droit paternite
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroitPaternite(String string) {
        forIdDroitPaternite = string;
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
