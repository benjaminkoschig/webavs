/*
 * Cr�� le 21 sept. 05
 */
package globaz.babel.db;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Manager abstrait qui devrait �tre �tendu par tous les managers afin d'avoir un orderBy par d�faut</H1>
 * 
 * @author dvh
 */
public abstract class CTAbstractManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String orderBy = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected final String _getOrder(BStatement statement) {
        if (JadeStringUtil.isEmpty(orderBy)) {
            return getOrderByDefaut();
        } else {
            return orderBy;
        }
    }

    /**
     * getter pour l'attribut order by
     * 
     * @return la valeur courante de l'attribut order by
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * A impl�menter pour qu'elle retourne le nom de la colonne � utiliser pour l'orderBy par d�faut
     * 
     * @return le nom de la colonne � utiliser par d�faut pour l'orderBy
     */
    public abstract String getOrderByDefaut();

    /**
     * setter pour l'attribut order by
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }
}
