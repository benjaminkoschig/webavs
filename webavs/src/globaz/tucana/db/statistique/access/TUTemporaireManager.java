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

    /** Pour agence - libell� de l'agence est = � ... (STMPAG) */
    private String forAgence = new String();
    /** Pour annee - ann�e de la statistique est = � ... (STMPAN) */
    private String forAnnee = new String();
    /** Pour categorie - libell� de la cat�gorie est = � ... (STMPCT) */
    private String forCategorie = new String();
    /** Pour dateCreation - date de cr�ation est = � ... (STMPDC) */
    private String forDateCreation = new String();
    /** Pour idTemporaire - cl� primaire du fichier tusptmp est = � ... (STMPID) */
    private String forIdTemporaire = new String();
    /** Pour mois - mois de la statistique est = � ... (STMPMO) */
    private String forMois = new String();
    private String forOrder = "";
    /** Pour cantonCourt - code ofs du canton est like � ... (STMPCC) */
    private String likeCantonCourt = new String();
    /** Pour cantonLong - libell� long du canton est like � ... (STMPCL) */
    private String likeCantonLong = new String();
    /** Pour groupe - libell� du groupe cat�gorie est like � ... (STMPGR) */
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
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
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
     * @return String agence - libell� de l'agence;
     */
    public String getForAgence() {
        return forAgence;
    }

    /**
     * Renvoie forAnnee;
     * 
     * @return String annee - ann�e de la statistique;
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * Renvoie forCategorie;
     * 
     * @return String categorie - libell� de la cat�gorie;
     */
    public String getForCategorie() {
        return forCategorie;
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
     * Renvoie forIdTemporaire;
     * 
     * @return String idTemporaire - cl� primaire du fichier tusptmp;
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
     * S�lection par likeCantonCourt;
     * 
     * @return String cantonLong - libell� long du canton;
     */
    public String getLikeCantonCourt() {
        return likeCantonCourt;
    }

    /**
     * S�lection par likeCantonLong;
     * 
     * @return String cantonLong - libell� long du canton;
     */
    public String getLikeCantonLong() {
        return likeCantonLong;
    }

    /**
     * S�lection par likeGroupe;
     * 
     * @return String groupe - libell� du groupe cat�gorie;
     */
    public String getLikeGroupe() {
        return likeGroupe;
    }

    /**
     * S�lection par forAgence
     * 
     * @param newForAgence
     *            String - libell� de l'agence
     */
    public void setForAgence(String newForAgence) {
        forAgence = newForAgence;
    }

    /**
     * S�lection par forAnnee
     * 
     * @param newForAnnee
     *            String - ann�e de la statistique
     */
    public void setForAnnee(String newForAnnee) {
        forAnnee = newForAnnee;
    }

    /**
     * S�lection par forCategorie
     * 
     * @param newForCategorie
     *            String - libell� de la cat�gorie
     */
    public void setForCategorie(String newForCategorie) {
        forCategorie = newForCategorie;
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
     * S�lection par forIdTemporaire
     * 
     * @param newForIdTemporaire
     *            String - cl� primaire du fichier tusptmp
     */
    public void setForIdTemporaire(String newForIdTemporaire) {
        forIdTemporaire = newForIdTemporaire;
    }

    /**
     * S�lection par forMois
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
     * S�lection par likeCantonCourt
     * 
     * @param newLikeCantonCourt
     *            String - code ofs du canton
     */
    public void setLikeCantonCourt(String newLikeCantonCourt) {
        likeCantonCourt = newLikeCantonCourt;
    }

    /**
     * S�lection par likeCantonLong
     * 
     * @param newLikeCantonLong
     *            String - libell� long du canton
     */
    public void setLikeCantonLong(String newLikeCantonLong) {
        likeCantonLong = newLikeCantonLong;
    }

    /**
     * S�lection par likeGroupe
     * 
     * @param newLikeGroupe
     *            String - libell� du groupe cat�gorie
     */
    public void setLikeGroupe(String newLikeGroupe) {
        likeGroupe = newLikeGroupe;
    }

}
