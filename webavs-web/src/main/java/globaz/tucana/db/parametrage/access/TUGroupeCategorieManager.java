package globaz.tucana.db.parametrage.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Liste des groupes de cat�gories
 * 
 * @author fgo date de cr�ation : 22 juin 06
 * @version : version 1.0
 * 
 */
public class TUGroupeCategorieManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Pour csCategorie - id code syst�me (cs : tu_categ) est = � ... (CSCATEG) */
    private String forCsCategorie = new String();

    /**
     * Pour csGroupeRubrique - id code syst�me (cs : tu_grrubr est = � ... (CSGRRU)
     */
    private String forCsGroupeRubrique = new String();
    /** Pour csType - code syst�me type (cs : tu_tygrp) est = � ... (CSTYPE) */
    private String forCsType = new String();
    /** Table : TUBPGRC */

    /**
     * Pour idGroupeCategorie - cl� primaire du fichier TUBPGRC est = � ... (BGRCID)
     */
    private String forIdGroupeCategorie = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "TUBPGRC" (Model : TUGroupeCategorie)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(ITUGroupeCategorieDefTable.TABLE_NAME).toString();
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
        if (getForIdGroupeCategorie().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUGroupeCategorieDefTable.ID_GROUPE_CATEGORIE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdGroupeCategorie()));
        }
        // traitement du positionnement
        if (getForCsCategorie().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUGroupeCategorieDefTable.CS_CATEGORIE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsCategorie()));
        }
        // traitement du positionnement
        if (getForCsGroupeRubrique().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUGroupeCategorieDefTable.CS_GROUPE_RUBRIQUE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsGroupeRubrique()));
        }
        // traitement du positionnement
        if (getForCsType().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUGroupeCategorieDefTable.CS_TYPE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsType()));
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
        return new TUGroupeCategorie();
    }

    /**
     * Renvoie forCsCategorie;
     * 
     * @return String csCategorie - id code syst�me (cs : tu_categ);
     */
    public String getForCsCategorie() {
        return forCsCategorie;
    }

    /**
     * Renvoie forCsGroupeRubrique;
     * 
     * @return String csGroupeRubrique - id code syst�me (cs : tu_grrubr;
     */
    public String getForCsGroupeRubrique() {
        return forCsGroupeRubrique;
    }

    /**
     * Renvoie forCsType;
     * 
     * @return String csType - code syst�me type (cs : tu_tygrp);
     */
    public String getForCsType() {
        return forCsType;
    }

    /**
     * Renvoie forIdGroupeCategorie;
     * 
     * @return String idGroupeCategorie - cl� primaire du fichier TUBPGRC;
     */
    public String getForIdGroupeCategorie() {
        return forIdGroupeCategorie;
    }

    /**
     * S�lection par forCsCategorie
     * 
     * @param newForCsCategorie
     *            String - id code syst�me (cs : tu_categ)
     */
    public void setForCsCategorie(String newForCsCategorie) {
        forCsCategorie = newForCsCategorie;
    }

    /**
     * S�lection par forCsGroupeRubrique
     * 
     * @param newForCsGroupeRubrique
     *            String - id code syst�me (cs : tu_grrubr
     */
    public void setForCsGroupeRubrique(String newForCsGroupeRubrique) {
        forCsGroupeRubrique = newForCsGroupeRubrique;
    }

    /**
     * S�lection par forCsType
     * 
     * @param newForCsType
     *            String - code syst�me type (cs : tu_tygrp)
     */
    public void setForCsType(String newForCsType) {
        forCsType = newForCsType;
    }

    /**
     * S�lection par forIdGroupeCategorie
     * 
     * @param newForIdGroupeCategorie
     *            String - cl� primaire du fichier TUBPGRC
     */
    public void setForIdGroupeCategorie(String newForIdGroupeCategorie) {
        forIdGroupeCategorie = newForIdGroupeCategorie;
    }

}
