package globaz.tucana.db.journal.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.db.bouclement.access.ITUDetailDefTable;
import globaz.tucana.db.parametrage.access.ITUGroupeCategorieDefTable;
import java.util.ArrayList;

/**
 * Classe représentant une liste de détail, catégorie-rubrique et groupe-catégorie
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 */
public class TUDetailGroupeCategorieRubriqueManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Pour csRubriqueList - code système rubrique IN ... (CSAGEN) */
    private ArrayList forCsRubriqueList = new ArrayList();
    private String forCsType = new String();
    private String forIdBouclement = new String();
    private ArrayList inIdBouclement = new ArrayList();
    private String order = new String();

    /**
     * Constructeur
     */
    public TUDetailGroupeCategorieRubriqueManager() {
        super();
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param statement
     *            de type BStatement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Retourne la clause WHERE de la requete SQL
     * 
     * @param statement
     *            BStatement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        /* composant de la requete initialises avec les options par defaut */
        StringBuffer sqlWhere = new StringBuffer();
        // traitement du positionnement
        if (getForIdBouclement().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("det.").append(ITUDetailDefTable.ID_BOUCLEMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdBouclement()));
        }
        // traitement du positionnement
        if (getForCsRubriqueList().size() > 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            String vlist = getForCsRubriqueList().toString();
            vlist = JadeStringUtil.removeChar(vlist, '[');
            vlist = JadeStringUtil.removeChar(vlist, ']');
            if (!JadeStringUtil.isEmpty(vlist)) {
                sqlWhere.append("det.").append(ITUDetailDefTable.CS_RUBRIQUE).append(" IN (").append(vlist).append(")");
            }
        }
        // traitement du positionnement par liste IN
        if (getInIdBouclement().size() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(det.").append(ITUDetailDefTable.ID_BOUCLEMENT).append(" IS NULL OR ");
            sqlWhere.append("det.").append(ITUDetailDefTable.ID_BOUCLEMENT).append(" IN (");

            for (int i = 0; i < getInIdBouclement().size(); i++) {
                sqlWhere.append((String) getInIdBouclement().get(i));
                if (i < getInIdBouclement().size() - 1) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append("))");

        }

        // traitement du positionnement sur le csType
        if (getForCsType().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("grp.").append(ITUGroupeCategorieDefTable.CS_TYPE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsType()));
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new TUDetailGroupeCategorieRubrique();
    }

    /**
     * Récupère l'instruction SQL exécutée
     */
    public String getCurrentSqlQuery() {
        return _getCurrentSqlQuery();

    }

    /**
     * Récupération de la liste des codes système rubrique
     * 
     * @return
     */
    public ArrayList getForCsRubriqueList() {
        return forCsRubriqueList;
    }

    /**
     * Récupère le csType
     * 
     * @return
     */
    public String getForCsType() {
        return forCsType;
    }

    /**
     * Récupère l'id bouclement de la clause WHERE
     * 
     * @return
     */
    public String getForIdBouclement() {
        return forIdBouclement;
    }

    /**
     * Récupère les id bouclement de la clause WHERE
     * 
     * @return
     */
    public ArrayList getInIdBouclement() {
        return inIdBouclement;
    }

    /**
     * Récupère la clause ORDER BY
     * 
     * @return
     */
    public String getOrder() {
        return order;
    }

    /**
     * Modification de la sélection des id code système rubrique
     * 
     * @param newForCsRubriqueList
     */
    public void setForCsRubriqueList(ArrayList newForCsRubriqueList) {
        forCsRubriqueList = newForCsRubriqueList;
    }

    /**
     * Modifie la sélection sur le csType
     * 
     * @param string
     */
    public void setForCsType(String string) {
        forCsType = string;
    }

    /**
     * Modifie la clause idBouclement de la clause WHERE
     * 
     * @param string
     */
    public void setForIdBouclement(String string) {
        forIdBouclement = string;
    }

    /**
     * Modifie la clause idBouclement de la clause WHERE
     * 
     * @param newInIdBouclement
     */
    public void setInIdBouclement(ArrayList newInIdBouclement) {
        inIdBouclement = newInIdBouclement;
    }

    /**
     * Modifie la clause order de l'instruction ORDER BY
     * 
     * @param string
     */
    public void setOrder(String string) {
        order = string;
    }
}
