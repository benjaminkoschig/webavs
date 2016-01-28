package globaz.osiris.db.comptes;

import globaz.osiris.api.APIRubrique;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 11:52:47)
 * 
 * @author: Administrator
 */
public class CARubriqueManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String ORDER_DESCRIPTION = "1002";
    public final static java.lang.String ORDER_IDEXTERNE = "1001";
    public final static java.lang.String ORDER_IDRUBRIQUE = "1000";
    private java.lang.String beginWithIdExterne = new String();
    private java.lang.String forIdExterne = new String();
    private java.lang.String forIdRubrique = new String();
    private boolean forNatureCotisation = false;
    private java.lang.String forNatureRubrique = new String();
    private List forNatureRubriqueIn = new ArrayList();
    private java.lang.String fromDescription = new String();
    private java.lang.String fromNumero = new String();
    private java.lang.String orderBy = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        if (getFromDescription().length() != 0) {
            return _getCollection() + "CARUBRV1";
        } else {
            if (getOrderBy().equals(CARubriqueManager.ORDER_DESCRIPTION)) {
                return _getCollection() + "CARUBRV1";
            } else {
                return _getCollection() + "CARUBRP";
            }
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getFromDescription().length() != 0) {
            return "LIBELLE";
        } else {
            if (getOrderBy().equals(CARubriqueManager.ORDER_DESCRIPTION)) {
                return "LIBELLE";
            } else if (getOrderBy().equals(CARubriqueManager.ORDER_IDEXTERNE)) {
                return "IDEXTERNE";
            } else if (getOrderBy().equals(CARubriqueManager.ORDER_IDRUBRIQUE)) {
                return "IDRUBRIQUE";
            } else {
                return "";
            }
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDRUBRIQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        // traitement du positionnement
        if (getForIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE=" + this._dbWriteString(statement.getTransaction(), getForIdExterne());
        }

        // traitement du positionnement depuis un numéro
        if (getFromNumero().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE>=" + this._dbWriteString(statement.getTransaction(), getFromNumero());
        }

        // traitement du positionnement depuis la description
        if (getFromDescription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CODEISOLANGUE="
                    + this._dbWriteString(statement.getTransaction(), statement.getTransaction().getSession()
                            .getIdLangueISO());
            sqlWhere += " AND ";
            sqlWhere += "LIBELLE>=" + this._dbWriteString(statement.getTransaction(), getFromDescription());
        }

        // traitement nature rubrique
        if (getForNatureRubrique().length() != 0) {
            if (!getForNatureRubrique().equals("-1")) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "NATURERUBRIQUE="
                        + this._dbWriteNumeric(statement.getTransaction(), getForNatureRubrique());
            }
        }

        // traitement nature rubrique
        if (getForNatureCotisation()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(NATURERUBRIQUE="
                    + this._dbWriteNumeric(statement.getTransaction(), APIRubrique.COTISATION_AVEC_MASSE)
                    + " OR NATURERUBRIQUE="
                    + this._dbWriteNumeric(statement.getTransaction(), APIRubrique.COTISATION_SANS_MASSE) + ")";
        }

        // traitement du positionnement pour IdExterne
        if (getBeginWithIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE LIKE '" + getBeginWithIdExterne() + "%'";
        }

        if ((getForNatureRubriqueIn() != null) && (getForNatureRubriqueIn().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NATURERUBRIQUE IN (";
            Iterator iter = getForNatureRubriqueIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                sqlWhere += element + ",";
            }
            sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 1);
            sqlWhere += ")";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CARubrique();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2003 14:05:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getBeginWithIdExterne() {
        return beginWithIdExterne;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdExterne() {
        return forIdExterne;
    }

    /**
     * @return
     */
    public java.lang.String getForIdRubrique() {
        return forIdRubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.08.2002 16:30:22)
     * 
     * @return boolean
     */
    public boolean getForNatureCotisation() {
        return forNatureCotisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 16:23:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNatureRubrique() {
        return forNatureRubrique;
    }

    /**
     * @return
     */
    public List getForNatureRubriqueIn() {
        return forNatureRubriqueIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 07:30:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDescription() {
        return fromDescription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 07:29:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumero() {
        return fromNumero;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2002 16:38:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2003 14:05:29)
     * 
     * @param newBeginWithIdExterne
     *            java.lang.String
     */
    public void setBeginWithIdExterne(java.lang.String newBeginWithIdExterne) {
        beginWithIdExterne = newBeginWithIdExterne;
    }

    /**
     * Setter
     */
    public void setForIdExterne(java.lang.String newForIdExterne) {
        forIdExterne = newForIdExterne;
    }

    /**
     * @param string
     */
    public void setForIdRubrique(java.lang.String string) {
        forIdRubrique = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.08.2002 16:30:22)
     * 
     * @param newNatureCotisation
     *            boolean
     */
    public void setForNatureCotisation(boolean newNatureCotisation) {
        forNatureCotisation = newNatureCotisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 16:23:41)
     * 
     * @param newForNatureRubrique
     *            java.lang.String
     */
    public void setForNatureRubrique(java.lang.String newForNatureRubrique) {
        forNatureRubrique = newForNatureRubrique;
    }

    /**
     * @param list
     */
    public void setForNatureRubriqueIn(List list) {
        forNatureRubriqueIn = list;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 07:30:03)
     * 
     * @param newFromDescription
     *            java.lang.String
     */
    public void setFromDescription(java.lang.String newFromDescription) {
        fromDescription = newFromDescription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 07:29:26)
     * 
     * @param newFromNumero
     *            java.lang.String
     */
    public void setFromNumero(java.lang.String newFromNumero) {
        fromNumero = newFromNumero;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2002 16:38:56)
     * 
     * @param newOrderBy
     *            java.lang.String
     */
    public void setOrderBy(java.lang.String newOrderBy) {
        orderBy = newOrderBy;
    }

}
