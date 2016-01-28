/*
 * Créé le 17 nov. 06
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;

/**
 * @author hpe
 * 
 */
public class IJNoPassageInscriptionCIManager extends BManager {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getGroupBy(BStatement statement) {
        return " GROUP BY " + IJInscriptionCI.FIELDNAME_NOPASSAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        JADate today = JACalendar.today();

        StringBuffer sqlWhere = new StringBuffer("");
        sqlWhere.append(IJInscriptionCI.FIELDNAME_NOPASSAGE);
        sqlWhere.append(">");
        sqlWhere.append(today.getYear() - 2);
        sqlWhere.append(today.getMonth());
        sqlWhere.append(today.getDay());

        return sqlWhere.toString() + _getGroupBy(statement) + " " + getOrderByDefaut();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJNoPassageInscriptionCI();
    }

    /**
     * @return la valeur courante de l'attribut order by defaut
     **/
    public String getOrderByDefaut() {
        return "ORDER BY " + IJInscriptionCI.FIELDNAME_NOPASSAGE + " DESC";
    }

}
