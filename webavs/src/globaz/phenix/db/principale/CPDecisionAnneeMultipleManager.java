/**
 * class CPDecisionAnneeMultipleManager écrit le 19/01/05 par JPA
 * 
 * class manager
 * 
 * @author JPA
 **/
package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPDecisionAnneeMultipleManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdPassage = "";

    @Override
    protected String _getDbSchema() {
        return super._getDbSchema();
    }

    /**
     * déclaration de la clause select :
     */
    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {
        return _getFieldsForRepriseCotPers();
    }

    protected String _getFieldsForRepriseCotPers() {
        String champ1 = "temp.*";
        String champ2 = "iattde";
        String champ3 = "iaddeb";
        String champ4 = "iadfin";
        String champ5 = "hxnavs";
        String delimiter = " , ";
        StringBuffer sb = new StringBuffer(champ1);
        sb.append(delimiter);
        sb.append(champ2);
        sb.append(delimiter);
        sb.append(champ3);
        sb.append(delimiter);
        sb.append(champ4);
        sb.append(delimiter);
        sb.append(champ5);
        return sb.toString();
    }

    /**
     * déclaration de la clause select :
     * 
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = "";
        sqlFrom += "(" + "select count(iaanne) as compteur," + _getCollection() + "afaffip.htitie," + "ebipas,"
                + "iaanne," + "malnaf ";
        sqlFrom += "from " + _getCollection() + "cpdecip t1 " + "inner join " + _getCollection() + "afaffip on "
                + _getCollection() + "afaffip.maiaff=t1.maiaff ";
        if (getForIdPassage().length() != 0) {
            sqlFrom += "where " + "ebipas=" + getForIdPassage();
        } else {
            sqlFrom += "where " + "ebipas<>0 ";
        }
        sqlFrom += " group by " + _getCollection() + "afaffip.htitie, " + "ebipas, " + "iaanne, " + "malnaf ";
        sqlFrom += "having count(iaanne) > 1) " + "as temp ";
        sqlFrom += "right outer join " + _getCollection() + "cpdecip " + "on " + _getCollection()
                + "cpdecip.htitie=temp.htitie ";
        sqlFrom += "right outer join " + _getCollection() + "TIPAVSP " + "on " + _getCollection()
                + "TIPAVSP.htitie=temp.htitie ";
        return sqlFrom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return " temp.ebipas desc";

    }

    /**
     * déclaration de la clause where :
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        sqlWhere += _getCollection() + "cpdecip.iaanne" + "=temp.iaanne";
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPDecisionAnneeMultiple();
    }

    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    public void setForIdPassage(java.lang.String string) {
        forIdPassage = string;
    }

}
