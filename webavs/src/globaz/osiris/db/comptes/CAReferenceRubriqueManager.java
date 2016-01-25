package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author sch date : 6 sept. 05
 * @author sel date : 15.04.2008
 */
public class CAReferenceRubriqueManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String IN = " IN ";
    private String forCodeReference = "";
    private List forCodeReferenceIn = new ArrayList();
    private String forIdRubrique = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CAReferenceRubrique.TABLE_CARERUP;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return CAReferenceRubrique.FIELD_IDCODEREFERENCE;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForCodeReference())) {
            addCondition(
                    sqlWhere,
                    CAReferenceRubrique.FIELD_IDCODEREFERENCE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForCodeReference()));
        }
        if (!JadeStringUtil.isBlank(getForIdRubrique())) {
            addCondition(
                    sqlWhere,
                    CAReferenceRubrique.FIELD_IDRUBRIQUE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique()));
        }
        if ((getForCodeReferenceIn() != null) && (getForCodeReferenceIn().size() > 0)) {
            addCondition(sqlWhere, CAReferenceRubrique.FIELD_IDCODEREFERENCE + CAReferenceRubriqueManager.IN + "(");

            Iterator iter = getForCodeReferenceIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                sqlWhere.append(element + ",");
            }
            sqlWhere.deleteCharAt(sqlWhere.length() - 1);
            sqlWhere.append(")");
        }

        return sqlWhere.toString();
    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAReferenceRubrique();
    }

    /**
     * Permet l'ajout d'une condition dans la clause WHERE. <br>
     * 
     * @param sqlWhere
     * @param condition
     *            à ajouter au where
     */
    protected void addCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(CAReferenceRubriqueManager.AND);
        }
        sqlWhere.append(condition);
    }

    /**
     * Cette méthode récupère le code de référence (code système) de la rubrique
     * 
     * @return String codeReference
     */
    public String getForCodeReference() {
        return forCodeReference;
    }

    /**
     * @return the inCodeReference
     */
    public List getForCodeReferenceIn() {
        return forCodeReferenceIn;
    }

    /**
     * Cette méthode récupère l'id de la rubrique
     * 
     * @return
     */
    public String getForIdRubrique() {
        return forIdRubrique;
    }

    /**
     * Cette méthode permet de setter le code référence (code système) de la rubrique
     * 
     * @param string
     *            codeReference
     */
    public void setForCodeReference(String codeReference) {
        forCodeReference = codeReference;
    }

    /**
     * @param inCodeReference
     *            the inCodeReference to set
     */
    public void setForCodeReferenceIn(List forCodeReferenceIn) {
        this.forCodeReferenceIn = forCodeReferenceIn;
    }

    /**
     * Cette méthode permet de setter l'id de la rubrique pour la recherche
     * 
     * @param forIdRubrique
     */
    public void setForIdRubrique(String forIdRubrique) {
        this.forIdRubrique = forIdRubrique;
    }
}
