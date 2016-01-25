package globaz.osiris.db.comptes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 11:49:35)
 * 
 * @author: Administrator
 */
public class CATypeOperationManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList forIdTypeOperationIn;
    private String fromIdTypeOperation = new String();
    private String likeIdTypeOperation = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CATOPEP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "IDTYPEOPERATION";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un numéro
        if (getFromIdTypeOperation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEOPERATION>=" + this._dbWriteString(statement.getTransaction(), getFromIdTypeOperation());
        }

        if (!JadeStringUtil.isBlank(getLikeIdTypeOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEOPERATION like "
                    + this._dbWriteString(statement.getTransaction(), getLikeIdTypeOperation() + "%");
        }

        if (getForIdTypeOperationIn() != null) {
            String tmp = "";

            Iterator iter = getForIdTypeOperationIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();

                if (tmp.length() != 0) {
                    tmp += ", ";
                }

                tmp += this._dbWriteString(statement.getTransaction(), element);

            }

            if (!JadeStringUtil.isBlank(tmp)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CATOPEP" + "." + CAOperation.FIELD_IDTYPEOPERATION + " IN (" + tmp
                        + ")";
            }
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CATypeOperation();
    }

    public ArrayList getForIdTypeOperationIn() {
        return forIdTypeOperationIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2002 14:19:24)
     * 
     * @return String
     */
    public String getFromIdTypeOperation() {
        return fromIdTypeOperation;
    }

    /**
     * @return
     */
    public String getLikeIdTypeOperation() {
        return likeIdTypeOperation;
    }

    public void setForIdTypeOperationIn(ArrayList forIdTypeOperationIn) {
        this.forIdTypeOperationIn = forIdTypeOperationIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2002 14:19:24)
     * 
     * @param newFromIdTypeSection
     *            String
     */
    public void setFromIdTypeOperation(String newFromIdTypeOperation) {
        fromIdTypeOperation = newFromIdTypeOperation;
    }

    /**
     * @param string
     */
    public void setLikeIdTypeOperation(String s) {
        likeIdTypeOperation = s;
    }
}
