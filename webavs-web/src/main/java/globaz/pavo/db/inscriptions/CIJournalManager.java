package globaz.pavo.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;

/**
 * Liste des journeaux. Date de création : (07.11.2002 15:03:14)
 * 
 * @author: ema
 */
public class CIJournalManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String apartir;
    /** (KCANCO) */
    private String forAnneeCotisation = new String();
    /** (KCDATE) */
    private String forDate = new String();
    private String fordateInscription = new String();
    /** (KCIAFF) */
    private String forIdAffiliation = new String();
    private String forIdAffiliationReel = new String();
    /** (KCIETA) */
    private String forIdEtat = new String();
    /** (KCID) */
    private String forIdJournal = new String();

    /** (KCITIN) */
    private String forIdTypeInscription = new String();
    /** (KCREFEX) */
    private String forReferenceExterneFacturation = new String();
    /** (KCDATE) */
    private String fromDate = new String();
    /** (KCIAFF) */
    private String fromIdAffiliation = new String();
    /** (KCID) */
    private String fromIdJournal = new String();
    private String fromUser = new String();
    private String likeIdAffiliation = new String();
    private String order = new String();
    private String tri;

    /**
     * Commentaire relatif au constructeur CIJournalManager.
     */
    public CIJournalManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.10.2002 08:31:46)
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {

        if (getFromDate() != null && getFromDate().trim().length() > 0) {
            setOrderBy("KCDATE DESC");
        } else if (getFromIdJournal() != null && getFromIdJournal().trim().length() > 0) {
            setOrderBy(_getCollection() + "CIJOURP.KCID");
        } else if (getFromIdAffiliation() != null && getFromIdAffiliation().trim().length() > 0) {
            setOrderBy("MALNAF");
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {

        CIApplication ciApp = null;
        try {

            ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCID="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        // traitement du positionnement
        if (getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCDATE="
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }
        // traitement du positionnement
        if (getForAnneeCotisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCANCO="
                    + _dbWriteNumeric(statement.getTransaction(), getForAnneeCotisation());
        }
        // traitement du positionnement
        if (getForIdTypeInscription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCITIN="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTypeInscription());
        }
        // traitement de la référence externe de facturation
        if (getForReferenceExterneFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCREFEX="
                    + _dbWriteString(statement.getTransaction(), getForReferenceExterneFacturation());
        }
        // traitement du positionnement
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF="
                    + _dbWriteString(statement.getTransaction(), getForIdAffiliation());
        }
        // traitement du positionnement
        if (getForIdEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCIETA="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdEtat());
        }
        // traitement du positionnement
        if (getFromIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCID>="
                    + _dbWriteNumeric(statement.getTransaction(), getFromIdJournal());
        }
        // traitement du positionnement
        if (getFromDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCDATE>="
                    + _dbWriteNumeric(statement.getTransaction(), CIUtil.formatDateAMJ(getFromDate()));
        }
        // traitement du positionnement
        if (getFromIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF>="
                    + _dbWriteString(statement.getTransaction(), getFromIdAffiliation());
        }

        if (getLikeIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String numAff = getLikeIdAffiliation();
            if (numAff != null && numAff.indexOf('.') == -1) {
                try {
                    IFormatData affilieFormater = ciApp.getAffileFormater();
                    if (affilieFormater != null) {
                        numAff = affilieFormater.format(numAff);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF like "
                    + _dbWriteString(statement.getTransaction(), numAff + "%");

        }
        if (getForIdAffiliationReel().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCIAFF="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdAffiliationReel());
        }
        if (getFordateInscription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIJOURP.KCDINS="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFordateInscription());
        }

        // filtre par utilisateur
        if (getFromUser().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "CIJOURP.KCUSER LIKE "
                    + _dbWriteString(statement.getTransaction(), getFromUser() + "%");
        }

        return sqlWhere;
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
        return new CIJournal();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2003 07:28:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getApartir() {
        return apartir;
    }

    public String getForAnneeCotisation() {
        return forAnneeCotisation;
    }

    public String getForDate() {
        return forDate;
    }

    /**
     * @return
     */
    public String getFordateInscription() {
        return fordateInscription;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    /**
     * @return
     */
    public String getForIdAffiliationReel() {
        return forIdAffiliationReel;
    }

    public String getForIdEtat() {
        return forIdEtat;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdTypeInscription() {
        return forIdTypeInscription;
    }

    /**
     * Returns the forReferenceExterneFacturation.
     * 
     * @return String
     */
    public String getForReferenceExterneFacturation() {
        return forReferenceExterneFacturation;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getFromIdAffiliation() {
        return fromIdAffiliation;
    }

    public String getFromIdJournal() {
        return fromIdJournal;
    }

    /**
     * @return
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * Returns the likeIdAffiliation.
     * 
     * @return String
     */
    public String getLikeIdAffiliation() {
        return likeIdAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2003 07:28:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTri() {
        return tri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2003 07:28:30)
     * 
     * @param newApartir
     *            java.lang.String
     */
    public void setApartir(java.lang.String newApartir) {
        apartir = newApartir;
    }

    public void setForAnneeCotisation(String newForAnneeCotisation) {
        forAnneeCotisation = newForAnneeCotisation;
    }

    public void setForDate(String newForDate) {
        if (getSession() != null) {
            try {
                BSessionUtil.checkDateGregorian(getSession(), newForDate);
            } catch (Exception inEx) {
            }
        }
        forDate = newForDate;
    }

    /**
     * @param string
     */
    public void setFordateInscription(String string) {
        fordateInscription = string;
    }

    public void setForIdAffiliation(String newForIdAffiliation) {
        forIdAffiliation = newForIdAffiliation;
    }

    /**
     * @param string
     */
    public void setForIdAffiliationReel(String string) {
        forIdAffiliationReel = string;
    }

    public void setForIdEtat(String newForIdEtat) {
        forIdEtat = newForIdEtat;
    }

    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    public void setForIdTypeInscription(String newForIdTypeInscription) {
        forIdTypeInscription = newForIdTypeInscription;
    }

    /**
     * Sets the forReferenceExterneFacturation.
     * 
     * @param forReferenceExterneFacturation
     *            The forReferenceExterneFacturation to set
     */
    public void setForReferenceExterneFacturation(String forReferenceExterneFacturation) {
        this.forReferenceExterneFacturation = forReferenceExterneFacturation;
    }

    public void setFromDate(String newFromDate) {
        fromDate = newFromDate;
    }

    public void setFromIdAffiliation(String newFromIdAffiliation) {
        fromIdAffiliation = newFromIdAffiliation;
    }

    public void setFromIdJournal(String newFromIdJournal) {
        fromIdJournal = newFromIdJournal;
    }

    /**
     * @param string
     */
    public void setFromUser(String string) {
        fromUser = string;
    }

    /**
     * Sets the likeIdAffiliation.
     * 
     * @param likeIdAffiliation
     *            The likeIdAffiliation to set
     */
    public void setLikeIdAffiliation(String likeIdAffiliation) {
        this.likeIdAffiliation = likeIdAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.10.2002 10:03:23)
     * 
     * @param order
     *            java.lang.String
     */
    public void setOrderBy(String order) {
        this.order = order;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2003 07:28:30)
     * 
     * @param newTri
     *            java.lang.String
     */
    public void setTri(java.lang.String newTri) {
        tri = newTri;
    }

}
