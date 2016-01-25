/*
 * Créé le 27 mai 05
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEnfantAPGManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSituationFamiliale = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdSituationFamiliale())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APEnfantAPG.FIELDNAME_IDSITUATIONFAM + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdSituationFamiliale);
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
        return new APEnfantAPGJointTiers();
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
        return APEnfantAPG.FIELDNAME_IDENFANTAPG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */

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
