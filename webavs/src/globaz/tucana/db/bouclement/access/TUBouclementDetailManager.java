package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

/**
 * TUBouclementManager permet de lister les bouclements
 * 
 * @author fgo date de cr�ation : 11 mai 06
 * @version : version 1.0
 */
public class TUBouclementDetailManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String TRI_ANNEE_MOIS = ITUBouclementDefTable.ANNEE_COMPTABLE
            + " DESC,".concat(ITUBouclementDefTable.MOIS_COMPTABLE + " DESC");

    /** Table : TUBPBOU */
    /** Pour anneeComptable - ann�e comptable est = � ... (BBOUAN) */
    private String forAnneeComptable = new String();
    /** Pour csAgence - code syst�me agence est = � ... (CSAGEN) */
    private String forCsAgence = new String();
    /** Pour csEtat - code syst�me etat est = � ... (CSETAT) */
    private String forCsEtat = new String();
    /** Pour csRubriqueList - code syst�me rubrique IN ... (CSAGEN) */
    private ArrayList forCsRubriqueList = new ArrayList();
    /** Pour dateCreation - date de cr�ation est = � ... (BBOUCR) */
    private String forDateCreation = new String();
    /** Pour dateEtat - date �tat est = � ... (BBOUET) */
    private String forDateEtat = new String();
    /**
     * Pour idBouclement - cl� primaire du fichier bouclement est = � ... (BBOUID)
     */
    private String forIdBouclement = new String();
    /** Pour idImportation - id importation cl� �trang�re est = � ... (BBOUIM) */
    private String forIdImportation = new String();
    /** Pour moisComptable - mois comptable est = � ... (BBOUMO) */
    private String forMoisComptable = new String();
    /** Instruction sql ORDER BY */
    private String forOrderBy = new String();
    /** Pour soldeBouclement - solde bouclement est = � ... (BBOUSO) */
    private String forSoldeBouclement = new String();

    /**
     * Constructeur
     */
    public TUBouclementDetailManager() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (getForOrderBy().length() != 0) {
            return getForOrderBy();
        } else {
            return super._getOrder(statement);
        }
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
        if (getForAnneeComptable().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.ANNEE_COMPTABLE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForAnneeComptable()));
        }
        // traitement du positionnement
        if (getForDateCreation().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.DATE_CREATION).append("=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateCreation()));
        }
        // traitement du positionnement
        if (getForDateEtat().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.DATE_ETAT).append("=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateEtat()));
        }
        // traitement du positionnement
        if (getForIdBouclement().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdBouclement()));
        }
        // traitement du positionnement
        if (getForIdImportation().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.ID_IMPORTATION).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdImportation()));
        }
        // traitement du positionnement
        if (getForMoisComptable().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.MOIS_COMPTABLE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForMoisComptable()));
        }
        // traitement du positionnement
        if (getForSoldeBouclement().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.SOLDE_BOUCLEMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForSoldeBouclement()));
        }
        // traitement du positionnement
        if (getForCsAgence().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.CS_AGENCE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsAgence()));
        }
        // traitement du positionnement
        if (getForCsRubriqueList().size() > 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            String vlist = getForCsRubriqueList().toString();
            vlist = JadeStringUtil.removeChar(vlist, '[');
            vlist = JadeStringUtil.removeChar(vlist, ']');
            sqlWhere.append(ITUDetailDefTable.CS_RUBRIQUE).append(" IN (").append(vlist).append(")");
        }
        // traitement du positionnement
        if (getForCsEtat().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.CS_ETAT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
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
        return new TUBouclementDetail();
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
     * Renvoie forAnneeComptable;
     * 
     * @return String anneeComptable - ann�e comptable;
     */
    public String getForAnneeComptable() {
        return forAnneeComptable;
    }

    /**
     * Renvoie forCsAgence;
     * 
     * @return String csAgence - code syst�me agence;
     */
    public String getForCsAgence() {
        return forCsAgence;
    }

    /**
     * Renvoie forCsEtat;
     * 
     * @return String csEtat - code syst�me etat;
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * R�cup�ration de la liste des codes syst�me rubrique
     * 
     * @return
     */
    public ArrayList getForCsRubriqueList() {
        return forCsRubriqueList;
    }

    /**
     * Renvoie forDateCreation;
     * 
     * @return String dateCreation - date de cr�ation;
     */
    public String getForDateCreation() {
        return forDateCreation;
    }

    /**
     * Renvoie forDateEtat;
     * 
     * @return String dateEtat - date �tat;
     */
    public String getForDateEtat() {
        return forDateEtat;
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
     * Renvoie forIdImportation;
     * 
     * @return String idImportation - id importation cl� �trang�re;
     */
    public String getForIdImportation() {
        return forIdImportation;
    }

    /**
     * Renvoie forMoisComptable;
     * 
     * @return String moisComptable - mois comptable;
     */
    public String getForMoisComptable() {
        return forMoisComptable;
    }

    /**
     * R�cup�ration de l'instruction ORDER BY
     * 
     * @return
     */
    public String getForOrderBy() {
        return forOrderBy;
    }

    /**
     * Renvoie forSoldeBouclement;
     * 
     * @return String soldeBouclement - solde bouclement;
     */
    public String getForSoldeBouclement() {
        return forSoldeBouclement;
    }

    /**
     * S�lection par forAnneeComptable
     * 
     * @param newForAnneeComptable
     *            String - ann�e comptable
     */
    public void setForAnneeComptable(String newForAnneeComptable) {
        forAnneeComptable = newForAnneeComptable;
    }

    /**
     * S�lection par forCsAgence
     * 
     * @param newForCsAgence
     *            String - code syst�me agence
     */
    public void setForCsAgence(String newForCsAgence) {
        forCsAgence = newForCsAgence;
    }

    /**
     * S�lection par forCsEtat
     * 
     * @param newForCsEtat
     *            String - code syst�me etat
     */
    public void setForCsEtat(String newForCsEtat) {
        forCsEtat = newForCsEtat;
    }

    /**
     * Modification de la s�lection des id code syst�me rubrique
     * 
     * @param newForCsRubriqueList
     */
    public void setForCsRubriqueList(ArrayList newForCsRubriqueList) {
        forCsRubriqueList = newForCsRubriqueList;
    }

    /**
     * S�lection par forDateCreation
     * 
     * @param newForDateCreation
     *            String - date de cr�ation
     */
    public void setForDateCreation(String newForDateCreation) {
        forDateCreation = newForDateCreation;
    }

    /**
     * S�lection par forDateEtat
     * 
     * @param newForDateEtat
     *            String - date �tat
     */
    public void setForDateEtat(String newForDateEtat) {
        forDateEtat = newForDateEtat;
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
     * S�lection par forIdImportation
     * 
     * @param newForIdImportation
     *            String - id importation cl� �trang�re
     */
    public void setForIdImportation(String newForIdImportation) {
        forIdImportation = newForIdImportation;
    }

    /**
     * S�lection par forMoisComptable
     * 
     * @param newForMoisComptable
     *            String - mois comptable
     */
    public void setForMoisComptable(String newForMoisComptable) {
        forMoisComptable = newForMoisComptable;
    }

    /**
     * Modification de l'instruction ORDER BY
     * 
     * @param newForOrderBy
     */
    public void setForOrderBy(String newForOrderBy) {
        forOrderBy = newForOrderBy;
    }

    /**
     * S�lection par forSoldeBouclement
     * 
     * @param newForSoldeBouclement
     *            String - solde bouclement
     */
    public void setForSoldeBouclement(String newForSoldeBouclement) {
        forSoldeBouclement = newForSoldeBouclement;
    }

}
