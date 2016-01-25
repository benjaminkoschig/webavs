package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.tucana.db.bouclement.access.ITUBouclementDefTable;
import globaz.tucana.db.bouclement.access.ITUNoPassageDefTable;
import globaz.tucana.db.bouclement.access.TUNoPassageManager;

/**
 * @author fgo
 * 
 * @version 1.0 Created on Fri May 05 15:07:59 CEST 2006
 */
public class TUApplicationBouclementListViewBean extends TUNoPassageManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Pour anneeComptable - année comptable est = à ... (BBOUAN) */
    private String forAnneeComptable = new String();
    /** Pour csAgence - code système agence est = à ... (CSAGEN) */
    private String forCsAgence = new String();
    /** Pour csApplication - cs application tu_appli est = à ... (CSAPPL) */
    private String forCsApplication = new String();
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
    /**
     * Pour idNoPassage - représentera la clé primaire du fichier une fois générée est = à ... (BPNPID)
     */
    private String forIdNoPassage = new String();
    /** Pour moisComptable - mois comptable est = à ... (BBOUMO) */
    private String forMoisComptable = new String();
    /** Pour noPassage - numéro de passage est = à ... (BPNPNP) */
    private String forNoPassage = new String();
    /** Pour soldeBouclement - solde bouclement est = à ... (BBOUSO) */
    private String forSoldeBouclement = new String();
    /** Tri souhaité */
    private String order = new String();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(ITUNoPassageDefTable.TABLE_NAME).append(" AS pca");

        // jointure sur bouclement
        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection()).append(ITUBouclementDefTable.TABLE_NAME).append(" AS bou");
        sqlFrom.append(" ON bou.").append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=pca.")
                .append(ITUNoPassageDefTable.ID_BOUCLEMENT);

        return sqlFrom.toString();
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
        if (getForCsEtat().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUBouclementDefTable.CS_ETAT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
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
    public BEntity _newEntity() throws Exception {
        return new TUApplicationBouclementViewBean();
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
     * Renvoie forCsApplication;
     * 
     * @return String csApplication - cs application tu_appli;
     */
    @Override
    public String getForCsApplication() {
        return forCsApplication;
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
    @Override
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
     * Renvoie forIdNoPassage;
     * 
     * @return String idNoPassage - représentera la clé primaire du fichier une fois générée;
     */
    @Override
    public String getForIdNoPassage() {
        return forIdNoPassage;
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
     * Renvoie forNoPassage;
     * 
     * @return String noPassage - numéro de passage;
     */
    @Override
    public String getForNoPassage() {
        return forNoPassage;
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
     * Récupère la valeur ORDER By de l'instrction SQL
     * 
     * @return la valeur ORDER BY
     */
    public String getOrder() {
        return order;
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
     * Sélection par forCsApplication
     * 
     * @param newForCsApplication
     *            String - cs application tu_appli
     */
    @Override
    public void setForCsApplication(String newForCsApplication) {
        forCsApplication = newForCsApplication;
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
    @Override
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
     * Sélection par forIdNoPassage
     * 
     * @param newForIdNoPassage
     *            String - représentera la clé primaire du fichier une fois générée
     */
    @Override
    public void setForIdNoPassage(String newForIdNoPassage) {
        forIdNoPassage = newForIdNoPassage;
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
     * Sélection par forNoPassage
     * 
     * @param newForNoPassage
     *            String - numéro de passage
     */
    @Override
    public void setForNoPassage(String newForNoPassage) {
        forNoPassage = newForNoPassage;
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

    /**
     * Modifie la valeur ORDER BY de l'instruction SQL
     * 
     * @param newOrder
     */
    public void setOrder(String newOrder) {
        order = newOrder;
    }
}
