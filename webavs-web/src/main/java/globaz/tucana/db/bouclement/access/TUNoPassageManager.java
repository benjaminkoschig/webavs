package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri May 05 15:07:59 CEST 2006
 */
public class TUNoPassageManager extends BManager {
    /** Table : TUBPNP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Pour csApplication - cs application tu_appli est = à ... (CSAPPL) */
    private String forCsApplication = new String();

    /**
     * Pour idBouclement - clé primaire du fichier bouclement est = à ... (BBOUID)
     */
    private String forIdBouclement = new String();

    /**
     * Pour idNoPassage - représentera la clé primaire du fichier une fois générée est = à ... (BPNPID)
     */
    private String forIdNoPassage = new String();

    /** Pour noPassage - numéro de passage est = à ... (BPNPNP) */
    private String forNoPassage = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "TUBPNP" (Model : TUNoPassage)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(ITUNoPassageDefTable.TABLE_NAME).toString();
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
        return "";
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
            sqlWhere.append(ITUNoPassageDefTable.ID_BOUCLEMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdBouclement()));
        }
        // traitement du positionnement
        if (getForIdNoPassage().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUNoPassageDefTable.ID_NO_PASSAGE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdNoPassage()));
        }
        // traitement du positionnement
        if (getForNoPassage().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUNoPassageDefTable.NO_PASSAGE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNoPassage()));
        }
        // traitement du positionnement
        if (getForCsApplication().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUNoPassageDefTable.CS_APPLICATION).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsApplication()));
        }

        return sqlWhere.toString();
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new TUNoPassage();
    }

    /**
     * Récupère le requête sql exécutée
     * 
     * @return
     */
    public String getCurrentSqlQuery() {
        return _getCurrentSqlQuery();
    }

    /**
     * Renvoie forCsApplication;
     * 
     * @return String csApplication - cs application tu_appli;
     */
    public String getForCsApplication() {
        return forCsApplication;
    }

    /**
     * Renvoie forIdBouclement;
     * 
     * @return String idBouclement - clé primaire du fichier bouclement;
     */
    public String getForIdBouclement() {
        return forIdBouclement;
    }

    /**
     * Renvoie forIdNoPassage;
     * 
     * @return String idNoPassage - représentera la clé primaire du fichier une fois générée;
     */
    public String getForIdNoPassage() {
        return forIdNoPassage;
    }

    /**
     * Renvoie forNoPassage;
     * 
     * @return String noPassage - numéro de passage;
     */
    public String getForNoPassage() {
        return forNoPassage;
    }

    /**
     * Sélection par forCsApplication
     * 
     * @param newForCsApplication
     *            String - cs application tu_appli
     */
    public void setForCsApplication(String newForCsApplication) {
        forCsApplication = newForCsApplication;
    }

    /**
     * Sélection par forIdBouclement
     * 
     * @param newForIdBouclement
     *            String - clé primaire du fichier bouclement
     */
    public void setForIdBouclement(String newForIdBouclement) {
        forIdBouclement = newForIdBouclement;
    }

    /**
     * Sélection par forIdNoPassage
     * 
     * @param newForIdNoPassage
     *            String - représentera la clé primaire du fichier une fois générée
     */
    public void setForIdNoPassage(String newForIdNoPassage) {
        forIdNoPassage = newForIdNoPassage;
    }

    /**
     * Sélection par forNoPassage
     * 
     * @param newForNoPassage
     *            String - numéro de passage
     */
    public void setForNoPassage(String newForNoPassage) {
        forNoPassage = newForNoPassage;
    }

}
