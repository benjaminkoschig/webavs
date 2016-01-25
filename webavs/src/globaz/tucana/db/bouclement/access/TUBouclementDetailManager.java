package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

/**
 * TUBouclementManager permet de lister les bouclements
 * 
 * @author fgo date de création : 11 mai 06
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
    /** Pour anneeComptable - année comptable est = à ... (BBOUAN) */
    private String forAnneeComptable = new String();
    /** Pour csAgence - code système agence est = à ... (CSAGEN) */
    private String forCsAgence = new String();
    /** Pour csEtat - code système etat est = à ... (CSETAT) */
    private String forCsEtat = new String();
    /** Pour csRubriqueList - code système rubrique IN ... (CSAGEN) */
    private ArrayList forCsRubriqueList = new ArrayList();
    /** Pour dateCreation - date de création est = à ... (BBOUCR) */
    private String forDateCreation = new String();
    /** Pour dateEtat - date état est = à ... (BBOUET) */
    private String forDateEtat = new String();
    /**
     * Pour idBouclement - clé primaire du fichier bouclement est = à ... (BBOUID)
     */
    private String forIdBouclement = new String();
    /** Pour idImportation - id importation clé étrangère est = à ... (BBOUIM) */
    private String forIdImportation = new String();
    /** Pour moisComptable - mois comptable est = à ... (BBOUMO) */
    private String forMoisComptable = new String();
    /** Instruction sql ORDER BY */
    private String forOrderBy = new String();
    /** Pour soldeBouclement - solde bouclement est = à ... (BBOUSO) */
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
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new TUBouclementDetail();
    }

    /**
     * Récupère la requête sql exécutée
     * 
     * @return
     */
    public String getCurrentSqlQuery() {
        return _getCurrentSqlQuery();
    }

    /**
     * Renvoie forAnneeComptable;
     * 
     * @return String anneeComptable - année comptable;
     */
    public String getForAnneeComptable() {
        return forAnneeComptable;
    }

    /**
     * Renvoie forCsAgence;
     * 
     * @return String csAgence - code système agence;
     */
    public String getForCsAgence() {
        return forCsAgence;
    }

    /**
     * Renvoie forCsEtat;
     * 
     * @return String csEtat - code système etat;
     */
    public String getForCsEtat() {
        return forCsEtat;
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
     * Renvoie forDateCreation;
     * 
     * @return String dateCreation - date de création;
     */
    public String getForDateCreation() {
        return forDateCreation;
    }

    /**
     * Renvoie forDateEtat;
     * 
     * @return String dateEtat - date état;
     */
    public String getForDateEtat() {
        return forDateEtat;
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
     * Renvoie forIdImportation;
     * 
     * @return String idImportation - id importation clé étrangère;
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
     * Récupération de l'instruction ORDER BY
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
     * Sélection par forAnneeComptable
     * 
     * @param newForAnneeComptable
     *            String - année comptable
     */
    public void setForAnneeComptable(String newForAnneeComptable) {
        forAnneeComptable = newForAnneeComptable;
    }

    /**
     * Sélection par forCsAgence
     * 
     * @param newForCsAgence
     *            String - code système agence
     */
    public void setForCsAgence(String newForCsAgence) {
        forCsAgence = newForCsAgence;
    }

    /**
     * Sélection par forCsEtat
     * 
     * @param newForCsEtat
     *            String - code système etat
     */
    public void setForCsEtat(String newForCsEtat) {
        forCsEtat = newForCsEtat;
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
     * Sélection par forDateCreation
     * 
     * @param newForDateCreation
     *            String - date de création
     */
    public void setForDateCreation(String newForDateCreation) {
        forDateCreation = newForDateCreation;
    }

    /**
     * Sélection par forDateEtat
     * 
     * @param newForDateEtat
     *            String - date état
     */
    public void setForDateEtat(String newForDateEtat) {
        forDateEtat = newForDateEtat;
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
     * Sélection par forIdImportation
     * 
     * @param newForIdImportation
     *            String - id importation clé étrangère
     */
    public void setForIdImportation(String newForIdImportation) {
        forIdImportation = newForIdImportation;
    }

    /**
     * Sélection par forMoisComptable
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
     * Sélection par forSoldeBouclement
     * 
     * @param newForSoldeBouclement
     *            String - solde bouclement
     */
    public void setForSoldeBouclement(String newForSoldeBouclement) {
        forSoldeBouclement = newForSoldeBouclement;
    }

}
