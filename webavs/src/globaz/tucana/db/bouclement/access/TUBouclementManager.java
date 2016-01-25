package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * TUBouclementManager permet de lister les bouclements
 * 
 * @author fgo date de création : 11 mai 06
 * @version : version 1.0
 */
public class TUBouclementManager extends BManager {

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
    /** Pour soldeBouclement - solde bouclement est = à ... (BBOUSO) */
    private String forSoldeBouclement = new String();
    private String fromMoisComptable = new String();
    /** Tri souhaité */
    private String order = new String();

    private String untilMoisComptable = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "TUBPBOU" (Model : TUBouclement)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(ITUBouclementDefTable.TABLE_NAME).toString();
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
        // String orderBy = "";
        // if (!JadeStringUtil.isEmpty(order)) {
        // try {
        // switch (Integer.parseInt(order)) {
        // case 1 :
        // orderBy = TRI_ANNEE_MOIS;
        // break;
        // default :
        // orderBy = order;
        // break;
        // }
        //
        // } catch (NumberFormatException nfe) {
        // JadeCodingUtil.catchException(this, "_getOrder", nfe);
        // }
        // }
        return getOrder();
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
        if (getFromMoisComptable().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.MOIS_COMPTABLE).append(">=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getFromMoisComptable()));
        }
        if (getUntilMoisComptable().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.MOIS_COMPTABLE).append("<=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getUntilMoisComptable()));
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
        return new TUBouclement();
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
     * Renvoie forSoldeBouclement;
     * 
     * @return String soldeBouclement - solde bouclement;
     */
    public String getForSoldeBouclement() {
        return forSoldeBouclement;
    }

    public String getFromMoisComptable() {
        return fromMoisComptable;
    }

    /**
     * Récupère la valeur ORDER By de l'instrction SQL
     * 
     * @return la valeur ORDER BY
     */
    public String getOrder() {
        return order;
    }

    public String getUntilMoisComptable() {
        return untilMoisComptable;
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
     * Sélection par forSoldeBouclement
     * 
     * @param newForSoldeBouclement
     *            String - solde bouclement
     */
    public void setForSoldeBouclement(String newForSoldeBouclement) {
        forSoldeBouclement = newForSoldeBouclement;
    }

    public void setFromMoisComptable(String newFromMoisComptable) {
        fromMoisComptable = newFromMoisComptable;
    }

    /**
     * Modifie la valeur ORDER BY de l'instruction SQL
     * 
     * @param newOrder
     */
    public void setOrder(String newOrder) {
        order = newOrder;
    }

    public void setUntilMoisComptable(String newUntilMoisComptable) {
        untilMoisComptable = newUntilMoisComptable;
    }

}
