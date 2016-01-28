package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri May 12 07:35:57 CEST 2006
 */
public class TUDetailManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Pour canton - canton est = � ... (BDETCA) */
    private String forCanton = new String();

    /** Pour csAplication - cs application tu_appli est = � ... (CSAPPL) */
    private String forCsApplication = new String();

    /** Pour csRubrique - cs rubrique tu_rubr est = � ... (CSRUBR) */
    private String forCsRubrique = new String();

    /** Pour csTypeRubrique - cs type de rubr tu_tyrubr est = � ... (CSTYRU) */
    private String forCsTypeRubrique = new String();

    /** Pour dateImport - date importation est = � ... (BDETIM) */
    private String forDateImport = new String();
    /** Table : TUBPDET */

    /**
     * Pour idBouclement - cl� primaire du fichier bouclement est = � ... (BBOUID)
     */
    private String forIdBouclement = new String();

    /** Pour idDetail - id d�tail est = � ... (BDETID) */
    private String forIdDetail = new String();

    /** Pour nombreMontant - nombre ou montant est = � ... (BDETNM) */
    private String forNombreMontant = new String();

    /** Pour noPassage - num�ro de passage est = � ... (BDETNP) */
    private String forNoPassage = new String();

    /** Pour nombreMontant - nombre ou montant est >= � ... (BDETNM) */
    private String fromNombreMontant = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "TUBPDET" (Model : TUDetail)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(ITUDetailDefTable.TABLE_NAME).toString();
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
            sqlWhere.append(ITUDetailDefTable.ID_BOUCLEMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdBouclement()));
        }
        // traitement du positionnement
        if (getForCanton().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.CANTON).append("=")
                    .append(_dbWriteString(statement.getTransaction(), getForCanton()));
        }
        // traitement du positionnement
        if (getForIdDetail().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.ID_DETAIL).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdDetail()));
        }
        // traitement du positionnement
        if (getForDateImport().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.DATE_IMPORT).append("=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateImport()));
        }
        // traitement du positionnement
        if (getForNombreMontant().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.NOMBRE_MONTANT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNombreMontant()));
        }
        // traitement du positionnement
        if (getForNoPassage().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.NO_PASSAGE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNoPassage()));
        }
        // traitement du positionnement
        if (getForCsApplication().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.CS_APPLICATION).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsApplication()));
        }
        // traitement du positionnement
        if (getForCsRubrique().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.CS_RUBRIQUE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsRubrique()));
        }
        // traitement du positionnement
        if (getForCsTypeRubrique().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.CS_TYPE_RUBRIQUE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsTypeRubrique()));
        }

        // traitement du positionnement
        if (getFromNombreMontant().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUDetailDefTable.NOMBRE_MONTANT).append(">=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getFromNombreMontant()));
        }

        return sqlWhere.toString();
    }

    /**
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new TUDetail();
    }

    /**
     * R�cup�re la requ�te sql ex�cut�e
     * 
     * @return
     */
    public String getCurrentSqlQuery() {
        return _getCurrentSqlQuery();
    }

    /**
     * Renvoie forCanton;
     * 
     * @return String canton - canton;
     */
    public String getForCanton() {
        return forCanton;
    }

    /**
     * Renvoie forCsApplication;
     * 
     * @return String csAplication - cs application tu_appli;
     */
    public String getForCsApplication() {
        return forCsApplication;
    }

    /**
     * Renvoie forCsRubrique;
     * 
     * @return String csRubrique - cs rubrique tu_rubr;
     */
    public String getForCsRubrique() {
        return forCsRubrique;
    }

    /**
     * Renvoie forCsTypeRubrique;
     * 
     * @return String csTypeRubrique - cs type de rubr tu_tyrubr;
     */
    public String getForCsTypeRubrique() {
        return forCsTypeRubrique;
    }

    /**
     * Renvoie forDateImport;
     * 
     * @return String dateImport - date importation;
     */
    public String getForDateImport() {
        return forDateImport;
    }

    /**
     * Renvoie forIdBouclement;
     * 
     * @return String idBouclement - cl� primaire du fichier bouclement;
     */
    public String getForIdBouclement() {
        return forIdBouclement;
    }

    /**
     * Renvoie forIdDetail;
     * 
     * @return String idDetail - id d�tail;
     */
    public String getForIdDetail() {
        return forIdDetail;
    }

    /**
     * Renvoie forNombreMontant;
     * 
     * @return String nombreMontant - nombre ou montant;
     */
    public String getForNombreMontant() {
        return forNombreMontant;
    }

    /**
     * Renvoie forNoPassage;
     * 
     * @return String noPassage - num�ro de passage;
     */
    public String getForNoPassage() {
        return forNoPassage;
    }

    /**
     * S�lection par fromNombreMontant;
     * 
     * @return String nombreMontant - nombre ou montant;
     */
    public String getFromNombreMontant() {
        return fromNombreMontant;
    }

    /**
     * S�lection par forCanton
     * 
     * @param newForCanton
     *            String - canton
     */
    public void setForCanton(String newForCanton) {
        forCanton = newForCanton;
    }

    /**
     * S�lection par forCsApplication
     * 
     * @param newForCsAplication
     *            String - cs application tu_appli
     */
    public void setForCsApplication(String newForCsAplication) {
        forCsApplication = newForCsAplication;
    }

    /**
     * S�lection par forCsRubrique
     * 
     * @param newForCsRubrique
     *            String - cs rubrique tu_rubr
     */
    public void setForCsRubrique(String newForCsRubrique) {
        forCsRubrique = newForCsRubrique;
    }

    /**
     * S�lection par forCsTypeRubrique
     * 
     * @param newForCsTypeRubrique
     *            String - cs type de rubr tu_tyrubr
     */
    public void setForCsTypeRubrique(String newForCsTypeRubrique) {
        forCsTypeRubrique = newForCsTypeRubrique;
    }

    /**
     * S�lection par forDateImport
     * 
     * @param newForDateImport
     *            String - date importation
     */
    public void setForDateImport(String newForDateImport) {
        forDateImport = newForDateImport;
    }

    /**
     * S�lection par forIdBouclement
     * 
     * @param newForIdBouclement
     *            String - cl� primaire du fichier bouclement
     */
    public void setForIdBouclement(String newForIdBouclement) {
        forIdBouclement = newForIdBouclement;
    }

    /**
     * S�lection par forIdDetail
     * 
     * @param newForIdDetail
     *            String - id d�tail
     */
    public void setForIdDetail(String newForIdDetail) {
        forIdDetail = newForIdDetail;
    }

    /**
     * S�lection par forNombreMontant
     * 
     * @param newForNombreMontant
     *            String - nombre ou montant
     */
    public void setForNombreMontant(String newForNombreMontant) {
        forNombreMontant = newForNombreMontant;
    }

    /**
     * S�lection par forNoPassage
     * 
     * @param newForNoPassage
     *            String - num�ro de passage
     */
    public void setForNoPassage(String newForNoPassage) {
        forNoPassage = newForNoPassage;
    }

    /**
     * S�lection par fromNombreMontant
     * 
     * @param newFromNombreMontant
     *            String - nombre ou montant
     */
    public void setFromNombreMontant(String newFromNombreMontant) {
        fromNombreMontant = newFromNombreMontant;
    }

}
