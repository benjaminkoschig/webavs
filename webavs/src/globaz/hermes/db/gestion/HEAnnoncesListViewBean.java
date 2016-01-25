package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * La classe
 * 
 * @author auteur
 */
public class HEAnnoncesListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * ???
     * 
     * @param args
     *            args
     */
    public static void main(String[] args) {
        BSession session;
        HEAnnoncesListViewBean annonces = new HEAnnoncesListViewBean();
        try {
            //
            session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            annonces.setSession(session);
            // Find
            annonces.find();
            System.out.println("Taille : " + annonces.size());
            // Find for
            annonces.setForIdAnnonce("1");
            annonces.find();
            System.out.println("Element unique trouvé : " + (annonces.size() == 1));
            // get Entity
            HEAnnoncesViewBean line = (HEAnnoncesViewBean) annonces.getEntity(0);
            System.out.println(line.getChampEnregistrement());
        } catch (Exception e) {
            System.err.println(e + annonces.getErrors().toString());
            System.exit(-1);
        }
        System.exit(0);
    }

    /** SUBSTRING RNENR LIKE */
    private String forCodeEnregistrement = "";
    private String forEnregistrement = "";
    /** Fichier HEANNOP */
    /** (RNIANN) */
    private String forIdAnnonce = new String();
    /** (RMILOT) */
    private String forIdLot = new String();
    private String forMotif = "";
    private String forNotMotif = "";
    /** Not statut */
    private String forNotStatut = "";
    private String forNotStatut2 = "";
    private String forNotStatut3 = "";
    private String forNumAVS = "";
    /** RNREFU */
    private String forRefUnique = new String();
    private String forStatut = "";
    private java.lang.String fromIdLot = new String();
    private String groupBy = new String();
    protected final String HEANNOP_ARCHIVE = "HEANNOR";
    protected final String HEANNOP_EN_COURS = "HEANNOP";
    private boolean isArchivage = false;
    private boolean isCodeApplication3839 = false;
    /** RNENR like */
    private String likeEnregistrement = new String();
    /** RNENR like */
    private String likeEnregistrement2 = new String();
    /** RNENR like */
    private String likeEnregistrementOr = new String();
    private String order = "RNDDAN";

    private String untilDate = "";

    /**
     * Constructeur du type HEAnnoncesListViewBean
     */
    public HEAnnoncesListViewBean() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getFields(BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (getGroupBy().length() != 0) {
            return getGroupBy();
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * Renvoie la valeur de la propriété from
     * 
     * @return la valeur de la propriété from
     * @param statement
     *            statement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        // ACU 16.05.2003 OPTIMIZATION
        if (isArchivage()) {
            return _getCollection() + HEANNOP_ARCHIVE + " AS HEANNOP";
        } else {
            return _getCollection() + HEANNOP_EN_COURS + " AS HEANNOP";
        }
    }

    /**
     * Renvoie la valeur de la propriété order
     * 
     * @return la valeur de la propriété order
     * @param statement
     *            statement
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return getOrder();
    }

    /**
     * @see globaz.globall.db.BManager#_getSql(BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        if (getGroupBy().length() == 0) {
            return super._getSql(statement);
        } else {
            return super._getSql(statement) + " GROUP BY " + getGroupBy();
        }
    }

    /**
     * Renvoie la valeur de la propriété where
     * 
     * @return la valeur de la propriété where
     * @param statement
     *            statement
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // ACU 16.05.2003 OPTIMIZATION
        // - composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNIANN=" + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonce());
        }
        // traitement du positionnement
        // ACU 16.05.2003 OPTIMIZATION
        if (getFromIdLot().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RMILOT=" + getFromIdLot();
        } else if (getForIdLot().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RMILOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        // traitement du positionnement
        if (getForRefUnique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNREFU=" + _dbWriteString(statement.getTransaction(), getForRefUnique());
        }
        // traitement du positionnement
        if (getForMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNMOT=" + _dbWriteString(statement.getTransaction(), getForMotif());
        }
        // traitement du positionnement
        if (getForEnregistrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNLENR=" + _dbWriteString(statement.getTransaction(), getForEnregistrement());
        }
        // traitement du positionnement
        if (getForNotStatut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNTSTA<>" + _dbWriteNumeric(statement.getTransaction(), getForNotStatut());
        }
        // traitement du positionnement
        if (getForNotStatut2().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNTSTA<>" + _dbWriteNumeric(statement.getTransaction(), getForNotStatut2());
        }
        // traitement du positionnement
        if (getForNotStatut3().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNTSTA<>" + _dbWriteNumeric(statement.getTransaction(), getForNotStatut3());
        }
        // traitement du positionnement
        if (getForStatut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNTSTA=" + _dbWriteNumeric(statement.getTransaction(), getForStatut());
        }
        if (getLikeEnregistrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getLikeEnregistrementOr().length() != 0) {
                sqlWhere += " (HEANNOP.RNLENR LIKE '" + getLikeEnregistrement() + "%' OR HEANNOP.RNLENR LIKE '"
                        + getLikeEnregistrementOr() + "%') ";
            } else {
                sqlWhere += "HEANNOP.RNLENR LIKE '" + getLikeEnregistrement() + "%'";
            }
        }
        if (getLikeEnregistrement2().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNLENR LIKE '" + getLikeEnregistrement2() + "%'";
        }
        if (getForNumAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNAVS = "
                    + _dbWriteString(statement.getTransaction(), StringUtils.removeDots(getForNumAVS()));
        }
        if (getForCodeEnregistrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (isCodeApplication3839()) {
                sqlWhere += " SUBSTR(HEANNOP.RNLENR,3,3)='"
                        + JadeStringUtil.rightJustify(getForCodeEnregistrement(), 3, '0') + "'";
            } else {
                sqlWhere += " SUBSTR(HEANNOP.RNLENR,3,2)='"
                        + JadeStringUtil.rightJustify(getForCodeEnregistrement(), 2, '0') + "'";
            }

            // /* sqlWhere += "(SUBSTR(RNLENR,3,2)='0" +
            // getForCodeEnregistrement() + "' OR SUBSTR(RNLENR,3,3)='00" +
            // getForCodeEnregistrement() + "')";*/
            // sqlWhere +=
            // " (((SUBSTR(HEANNOP.RNLENR,1,2)='38' OR SUBSTR(HEANNOP.RNLENR,1,2)='39') AND SUBSTR(HEANNOP.RNLENR,3,3)='"
            // +
            // globaz.hermes.utils.StringUtils.padBeforeString(getForCodeEnregistrement(),
            // "0", 3)
            // +
            // "') OR ((SUBSTR(HEANNOP.RNLENR,1,2)<>'38' AND SUBSTR(HEANNOP.RNLENR,1,2)<>'39') AND SUBSTR(HEANNOP.RNLENR,3,2)='"
            // +
            // globaz.hermes.utils.StringUtils.padBeforeString(getForCodeEnregistrement(),
            // "0", 2)
            // + "')) ";
        }
        if (getUntilDate().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNDDAN < " + _dbWriteNumeric(statement.getTransaction(), getUntilDate());
        }
        if (getForNotMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNMOT <>" + _dbWriteString(statement.getTransaction(), getForNotMotif());
        }
        return sqlWhere;
    }

    /**
     * Renvoie ???
     * 
     * @return ???
     * @exception Exception
     *                si ???
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        HEAnnoncesViewBean n = new HEAnnoncesViewBean();
        n.setArchivage(isArchivage());
        return n;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2003 14:13:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForCodeEnregistrement() {
        return forCodeEnregistrement;
    }

    /**
     * Returns the forEnregistrement.
     * 
     * @return String
     */
    public String getForEnregistrement() {
        return forEnregistrement;
    }

    /**
     * Renvoie la valeur de la propriété forIdAnnonce
     * 
     * @return la valeur de la propriété forIdAnnonce
     */
    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * Renvoie la valeur de la propriété forIdLot
     * 
     * @return la valeur de la propriété forIdLot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * Returns the forMotif.
     * 
     * @return String
     */
    public String getForMotif() {
        return forMotif;
    }

    /**
     * Returns the forNotMotif.
     * 
     * @return String
     */
    public String getForNotMotif() {
        return forNotMotif;
    }

    /**
     * Returns the forNotStatut.
     * 
     * @return String
     */
    public String getForNotStatut() {
        return forNotStatut;
    }

    /**
     * Returns the forNotStatut2.
     * 
     * @return String
     */
    public String getForNotStatut2() {
        return forNotStatut2;
    }

    /**
     * Returns the forNotStatut3.
     * 
     * @return String
     */
    public String getForNotStatut3() {
        return forNotStatut3;
    }

    /**
     * Returns the forNumAVS.
     * 
     * @return String
     */
    public String getForNumAVS() {
        return forNumAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.01.2003 13:35:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForRefUnique() {
        return forRefUnique;
    }

    /**
     * Returns the forStatut.
     * 
     * @return String
     */
    public String getForStatut() {
        return forStatut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2003 12:42:55)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdLot() {
        return fromIdLot;
    }

    /**
     * Returns the groupBy.
     * 
     * @return String
     */
    public String getGroupBy() {
        return groupBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 16:49:54)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLikeEnregistrement() {
        return likeEnregistrement;
    }

    /**
     * Returns the likeEnregistrement2.
     * 
     * @return String
     */
    public String getLikeEnregistrement2() {
        return likeEnregistrement2;
    }

    /**
     * @return
     */
    public String getLikeEnregistrementOr() {
        return likeEnregistrementOr;
    }

    /**
     * Returns the order.
     * 
     * @return String
     */
    public String getOrder() {
        return order;
    }

    /**
     * Returns the untilDate.
     * 
     * @return String
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * Returns the isArchivage.
     * 
     * @return boolean
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    public boolean isCodeApplication3839() {
        return isCodeApplication3839;
    }

    public void setCodeApplication3839(boolean isCodeApplication3839) {
        this.isCodeApplication3839 = isCodeApplication3839;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2003 14:13:20)
     * 
     * @param newForCodeEnregistrement
     *            java.lang.String
     */
    public void setForCodeEnregistrement(java.lang.String newForCodeEnregistrement) {
        forCodeEnregistrement = newForCodeEnregistrement;
    }

    /**
     * Sets the forEnregistrement.
     * 
     * @param forEnregistrement
     *            The forEnregistrement to set
     */
    public void setForEnregistrement(String forEnregistrement) {
        this.forEnregistrement = forEnregistrement;
    }

    /**
     * Définit la valeur de la propriété forIdAnnonce
     * 
     * @param newForIdAnnonce
     *            newForIdAnnonce
     */
    public void setForIdAnnonce(String newForIdAnnonce) {
        forIdAnnonce = newForIdAnnonce;
    }

    /**
     * Définit la valeur de la propriété forIdLot
     * 
     * @param newForIdLot
     *            newForIdLot
     */
    public void setForIdLot(String newForIdLot) {
        forIdLot = newForIdLot;
    }

    /**
     * Sets the forMotif.
     * 
     * @param forMotif
     *            The forMotif to set
     */
    public void setForMotif(String forMotif) {
        this.forMotif = forMotif;
    }

    /**
     * Sets the forNotMotif.
     * 
     * @param forNotMotif
     *            The forNotMotif to set
     */
    public void setForNotMotif(String forNotMotif) {
        this.forNotMotif = forNotMotif;
    }

    /**
     * Method setForNotStatut.
     * 
     * @param string
     */
    public void setForNotStatut(String statut) {
        forNotStatut = statut;
    }

    /**
     * Sets the forNotStatut2.
     * 
     * @param forNotStatut2
     *            The forNotStatut2 to set
     */
    public void setForNotStatut2(String forNotStatut2) {
        this.forNotStatut2 = forNotStatut2;
    }

    /**
     * Sets the forNotStatut3.
     * 
     * @param forNotStatut3
     *            The forNotStatut3 to set
     */
    public void setForNotStatut3(String forNotStatut3) {
        this.forNotStatut3 = forNotStatut3;
    }

    /**
     * Sets the forNumAVS.
     * 
     * @param forNumAVS
     *            The forNumAVS to set
     */
    public void setForNumAVS(String forNumAVS) {
        this.forNumAVS = forNumAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.01.2003 13:35:29)
     * 
     * @param newForRefUnique
     *            java.lang.String
     */
    public void setForRefUnique(java.lang.String newForRefUnique) {
        forRefUnique = newForRefUnique;
    }

    /**
     * Method setForStatut.
     * 
     * @param string
     */
    public void setForStatut(String statut) {
        forStatut = statut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2003 12:42:55)
     * 
     * @param newFromIdLot
     *            java.lang.String
     */
    public void setFromIdLot(java.lang.String newFromIdLot) {
        fromIdLot = newFromIdLot;
    }

    /**
     * Sets the groupBy.
     * 
     * @param groupBy
     *            The groupBy to set
     */
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    /**
     * Sets the isArchivage.
     * 
     * @param isArchivage
     *            The isArchivage to set
     */
    public void setIsArchivage(boolean isArchivage) {
        this.isArchivage = isArchivage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 16:49:54)
     * 
     * @param newLikeEnregistrement
     *            java.lang.String
     */
    public void setLikeEnregistrement(java.lang.String newLikeEnregistrement) {
        likeEnregistrement = newLikeEnregistrement;
    }

    /**
     * Sets the likeEnregistrement2.
     * 
     * @param likeEnregistrement2
     *            The likeEnregistrement2 to set
     */
    public void setLikeEnregistrement2(String likeEnregistrement2) {
        this.likeEnregistrement2 = likeEnregistrement2;
    }

    /**
     * @param string
     */
    public void setLikeEnregistrementOr(String string) {
        likeEnregistrementOr = string;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Sets the untilDate.
     * 
     * @param untilDate
     *            The untilDate to set
     */
    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }
}
