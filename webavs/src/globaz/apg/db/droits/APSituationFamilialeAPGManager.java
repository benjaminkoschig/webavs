/*
 * Créé le 27 mai 05
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * DOCUMENT ME!
 * 
 * @author dvh
 */
public class APSituationFamilialeAPGManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdidSitFamAPG = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forIdidSitFamAPG)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APSituationFamilialeMat.TABLE_NAME + "."
                    + APSituationFamilialeMat.FIELDNAME_IDDROITMATERNITE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdidSitFamAPG);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APSituationFamilialeAPG();
    }

    /**
     * getter pour l'attribut for idid sit fam APG
     * 
     * @return la valeur courante de l'attribut for idid sit fam APG
     */
    public String getForIdidSitFamAPG() {
        return forIdidSitFamAPG;
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
        return APSituationFamilialeAPG.FIELDNAME_IDSITUATIONFAMAPG;
    }

    /**
     * setter pour l'attribut for idid sit fam APG
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdidSitFamAPG(String string) {
        forIdidSitFamAPG = string;
    }
}
