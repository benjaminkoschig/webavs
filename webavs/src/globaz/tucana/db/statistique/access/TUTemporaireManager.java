package globaz.tucana.db.statistique.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Wed Jun 28 11:01:21 CEST 2006
 */
public class TUTemporaireManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Table : TUSPTMP */
    public static final String ORDER_AGENCE_ANNUELLE = "STMPAG, STMPAN, STMPMO, STMPGR, STMPCT";

    /** Pour agence - libellé de l'agence est = à ... (STMPAG) */
    private String forAgence = new String();
    /** Pour annee - année de la statistique est = à ... (STMPAN) */
    private String forAnnee = new String();
    /** Pour categorie - libellé de la catégorie est = à ... (STMPCT) */
    private String forCategorie = new String();
    /** Pour dateCreation - date de création est = à ... (STMPDC) */
    private String forDateCreation = new String();
    /** Pour idTemporaire - clé primaire du fichier tusptmp est = à ... (STMPID) */
    private String forIdTemporaire = new String();
    /** Pour mois - mois de la statistique est = à ... (STMPMO) */
    private String forMois = new String();
    private String forOrder = "";
    /** Pour cantonCourt - code ofs du canton est like à ... (STMPCC) */
    private String likeCantonCourt = new String();
    /** Pour cantonLong - libellé long du canton est like à ... (STMPCL) */
    private String likeCantonLong = new String();
    /** Pour groupe - libellé du groupe catégorie est like à ... (STMPGR) */
    private String likeGroupe = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "TUSPTMP" (Model : TUTemporaire)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(ITUTemporaireDefTable.TABLE_NAME).toString();
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
        String sqlOrder = "";
        if (!JadeStringUtil.isEmpty(getForOrder())) {
            sqlOrder = getForOrder();
        }
        return sqlOrder;
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
        if (getForAgence().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.AGENCE).append("=")
                    .append(_dbWriteString(statement.getTransaction(), getForAgence()));
        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.ANNEE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForAnnee()));
        }
        // traitement du positionnement
        if (getForCategorie().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.CATEGORIE).append("=")
                    .append(_dbWriteString(statement.getTransaction(), getForCategorie()));
        }
        // traitement du positionnement
        if (getForDateCreation().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.DATE_CREATION).append("=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateCreation()));
        }
        // traitement du positionnement
        if (getForIdTemporaire().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.ID_TEMPORAIRE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdTemporaire()));
        }
        // traitement du positionnement
        if (getForMois().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.MOIS_NUME).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForMois()));
        }

        // traitement du positionnement
        if (getLikeCantonCourt().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.CANTON_COURT).append(" LIKE ")
                    .append(_dbWriteString(statement.getTransaction(), getLikeCantonCourt() + "%"));
        }
        // traitement du positionnement
        if (getLikeCantonLong().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.CANTON_LONG).append(" LIKE ")
                    .append(_dbWriteString(statement.getTransaction(), getLikeCantonLong() + "%"));
        }
        // traitement du positionnement
        if (getLikeGroupe().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUTemporaireDefTable.GROUPE).append(" LIKE ")
                    .append(_dbWriteString(statement.getTransaction(), getLikeGroupe() + "%"));
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
        return new TUTemporaire();
    }

    /**
     * Retourne l'instruction SQL
     * 
     * @return
     */
    public String getCurrentSqlQuery() {
        return _getCurrentSqlQuery();
    }

    /**
     * Renvoie forAgence;
     * 
     * @return String agence - libellé de l'agence;
     */
    public String getForAgence() {
        return forAgence;
    }

    /**
     * Renvoie forAnnee;
     * 
     * @return String annee - année de la statistique;
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * Renvoie forCategorie;
     * 
     * @return String categorie - libellé de la catégorie;
     */
    public String getForCategorie() {
        return forCategorie;
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
     * Renvoie forIdTemporaire;
     * 
     * @return String idTemporaire - clé primaire du fichier tusptmp;
     */
    public String getForIdTemporaire() {
        return forIdTemporaire;
    }

    /**
     * Renvoie forMois;
     * 
     * @return String mois - mois de la statistique;
     */
    public String getForMois() {
        return forMois;
    }

    /**
     * Renvoir l'instruction Order By
     * 
     * @return
     */
    public String getForOrder() {
        return forOrder;
    }

    /**
     * Sélection par likeCantonCourt;
     * 
     * @return String cantonLong - libellé long du canton;
     */
    public String getLikeCantonCourt() {
        return likeCantonCourt;
    }

    /**
     * Sélection par likeCantonLong;
     * 
     * @return String cantonLong - libellé long du canton;
     */
    public String getLikeCantonLong() {
        return likeCantonLong;
    }

    /**
     * Sélection par likeGroupe;
     * 
     * @return String groupe - libellé du groupe catégorie;
     */
    public String getLikeGroupe() {
        return likeGroupe;
    }

    /**
     * Sélection par forAgence
     * 
     * @param newForAgence
     *            String - libellé de l'agence
     */
    public void setForAgence(String newForAgence) {
        forAgence = newForAgence;
    }

    /**
     * Sélection par forAnnee
     * 
     * @param newForAnnee
     *            String - année de la statistique
     */
    public void setForAnnee(String newForAnnee) {
        forAnnee = newForAnnee;
    }

    /**
     * Sélection par forCategorie
     * 
     * @param newForCategorie
     *            String - libellé de la catégorie
     */
    public void setForCategorie(String newForCategorie) {
        forCategorie = newForCategorie;
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
     * Sélection par forIdTemporaire
     * 
     * @param newForIdTemporaire
     *            String - clé primaire du fichier tusptmp
     */
    public void setForIdTemporaire(String newForIdTemporaire) {
        forIdTemporaire = newForIdTemporaire;
    }

    /**
     * Sélection par forMois
     * 
     * @param newForMois
     *            String - mois de la statistique
     */
    public void setForMois(String newForMois) {
        forMois = newForMois;
    }

    /**
     * Modifie l'instruction SQL ORDER BY
     * 
     * @param string
     */
    public void setForOrder(String string) {
        forOrder = string;
    }

    /**
     * Sélection par likeCantonCourt
     * 
     * @param newLikeCantonCourt
     *            String - code ofs du canton
     */
    public void setLikeCantonCourt(String newLikeCantonCourt) {
        likeCantonCourt = newLikeCantonCourt;
    }

    /**
     * Sélection par likeCantonLong
     * 
     * @param newLikeCantonLong
     *            String - libellé long du canton
     */
    public void setLikeCantonLong(String newLikeCantonLong) {
        likeCantonLong = newLikeCantonLong;
    }

    /**
     * Sélection par likeGroupe
     * 
     * @param newLikeGroupe
     *            String - libellé du groupe catégorie
     */
    public void setLikeGroupe(String newLikeGroupe) {
        likeGroupe = newLikeGroupe;
    }

}
