package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

/**
 * Manager pour les lots<br>
 * Date de création : (20.11.2002 10:50:49)<br>
 * Fichier HELOTSP<br>
 * 
 * @author: ado
 */
public class HELotListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ORDER_BY_DATE_DESC = " RMDDEN DESC";
    public static final String ORDER_BY_RMI_LOT = " RMILOT ";

    /**
     * ???
     * 
     * @param args
     *            args
     */
    public static void main(String[] args) {
        BSession session;
        HELotListViewBean lots = new HELotListViewBean();
        try {
            //
            session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            lots.setSession(session);
            // Find
            lots.find();
            System.out.println("Taille : " + lots.size());
            // Find for
            lots.setForIdLot("1");
            lots.find();
            System.out.println("Element unique trouvé : " + (lots.size() == 1));
            // get Entity
            HELotViewBean line = (HELotViewBean) lots.getEntity(0);
            System.out.println(line.getHeureTraitement());
        } catch (Exception e) {
            System.err.println(e + lots.getErrors().toString());
            System.exit(-1);
        }
        System.exit(0);
    }

    /** recherche par date envoi (RMDDEN) */
    private String forDateEnvoi = new String();
    /** recherche par etat */
    private String forEtat = new String();
    /** recherche par idLot (RMILOT) */
    private String forIdLot = new String();
    private boolean forLotReception = false;
    /** recherche tous les etats sauf celui-la */
    private String forNotEtat = new String();
    /** recherche par priorité */
    private String forPriorite = new String();
    /** recherche par quittance (RMBQUI) */
    private String forQuittance = new String();
    /** recherche par type (RMTTYP) */
    private String forType = new String();
    /** recherche par utilisateur (RMLUTI) */
    private String forUtilisateur = new String();
    /** recherche à partir d'une date (RMDDEN) */
    private String fromDateEnvoi = new String();
    /** recherche à partir du type (RMILOT) */
    private String fromIdLot = new String();
    /** recherche à partir d'un type (RMTTYP) */
    private String fromType = new String();
    private final String HEANNOP_ARCHIVE = "HEANNOR";
    private final String HEANNOP_EN_COURS = "HEANNOP";
    private final String HELOTSP_ARCHIVE = "HELOTSR";
    private final String HELOTSP_EN_COURS = "HELOTSP";
    private String isArchivage = "false";
    /** recherche à partir d'une date de la copie d'envoi (RMDDEC) */
    private boolean isDateEnvoiCopie = false;
    /** ordre de recherch */
    private String order = "";

    /** recherche jusqu'à une date (RMDDEN) */
    private String untilDateEnvoi = new String();

    /**
     * Constructeur par défaut HELotListViewBean.
     */
    public HELotListViewBean() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        // ACU 16.05.2003 OPTIMIZATION
        String from;
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            from = _getCollection() + HELOTSP_ARCHIVE + " AS HELOTSP ";
            // from += " LEFT OUTER JOIN " + _getCollection() +
            // HEANNOP_ARCHIVE+" AS HEANNOP ON (";
        } else {
            from = _getCollection() + HELOTSP_EN_COURS + " AS HELOTSP ";
            // from += " LEFT OUTER JOIN " + _getCollection() +
            // HEANNOP_EN_COURS+" AS HEANNOP ON (";
        }
        // HEAnnoncesListViewBean liste = new HEAnnoncesListViewBean();
        // liste.setSession(getSession());
        // liste.setFromIdLot("HELOTSP.RMILOT");
        // liste.setForCodeEnregistrement("1");
        // from += liste._getWhere(statement);
        // from += ")";
        return from;
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

        return getOrder();
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
        // ACU 16.05.2003 OPTIMIZATION
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdLot().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMILOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        // traitement du positionnement
        if (getForDateEnvoi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMDDEN=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateEnvoi());
        }
        // traitement du positionnement
        if (getForUtilisateur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMLUTI=" + _dbWriteString(statement.getTransaction(), getForUtilisateur());
        }
        // traitement du positionnement
        if (getForType().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMTTYP=" + _dbWriteNumeric(statement.getTransaction(), getForType());
        }
        // traitement du positionnement
        if (getFromIdLot().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMILOT>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdLot());
        }
        // traitement du positionnement
        if (getFromDateEnvoi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMDDEN>=" + _dbWriteDateAMJ(statement.getTransaction(), getFromDateEnvoi());
        }
        // traitement du positionnement
        if (getFromType().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMTTYP>=" + _dbWriteNumeric(statement.getTransaction(), getFromType());
        }
        // traitement du positionnement
        if (getFromType().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMBQUI=" + _dbWriteNumeric(statement.getTransaction(), getForQuittance());
        }
        // traitement d'un filtre
        if (getForQuittance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMBQUI=" + _dbWriteString(statement.getTransaction(), getForQuittance());
        }
        // traitement du positionnement
        if (getUntilDateEnvoi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMDDEN <=" + _dbWriteDateAMJ(statement.getTransaction(), getUntilDateEnvoi());
        }
        if (getForPriorite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMTPRI=" + _dbWriteNumeric(statement.getTransaction(), getForPriorite());
        }
        if (getForEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMTETA=" + _dbWriteNumeric(statement.getTransaction(), getForEtat());
        }
        if (getForNotEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMTETA<>" + _dbWriteNumeric(statement.getTransaction(), getForNotEtat());
        }
        // traitement
        if (isDateEnvoiCopie()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HELOTSP.RMDDEC =" + _dbWriteNumeric(statement.getTransaction(), "");
        }
        if (isForLotReception()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(";
            for (int i = 0; i < HELotViewBean.getLotReception().length; i++) {
                if (i > 0) {
                    sqlWhere += " OR ";
                }
                sqlWhere += "HELOTSP.RMTTYP = ";
                sqlWhere += HELotViewBean.getLotReception()[i];
            }
            sqlWhere += ")";
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
        HELotViewBean n = new HELotViewBean();
        n.setArchivage(Boolean.valueOf(getIsArchivage()).booleanValue());
        return n;
    }

    // protected java.lang.String _getSql(BStatement statement) {
    // try {
    // StringBuffer sqlBuffer = new StringBuffer("SELECT ");
    // String sqlFields = _getFields(statement);
    // if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
    // sqlBuffer.append(sqlFields);
    // } else {
    // sqlBuffer.append("*");
    // }
    // sqlBuffer.append(" FROM ");
    // sqlBuffer.append(_getFrom(statement));
    // //
    // String sqlWhere = _getWhere(statement);
    // if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
    // sqlBuffer.append(" WHERE ");
    // sqlBuffer.append(sqlWhere);
    // }
    // String sqlGroup = _getGroup(statement);
    // if ((sqlGroup != null) && (sqlGroup.trim().length() != 0)) {
    // sqlBuffer.append(" GROUP BY ");
    // sqlBuffer.append(sqlGroup);
    // }
    // String sqlOrder = _getOrder(statement);
    // if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
    // sqlBuffer.append(" ORDER BY ");
    // sqlBuffer.append(sqlOrder);
    // }
    // sqlBuffer.append(" FOR FETCH ONLY OPTIMIZE FOR 20 ROWS");
    // return sqlBuffer.toString();
    // } catch (Exception e) {
    // e.printStackTrace();
    // return "";
    // }
    // }
    /**
     * Renvoie la date d'envoi
     * 
     * @return String la date AMJ
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    public String getForEtat() {
        return forEtat;
    }

    /**
     * Renvoie la pk recherchée
     * 
     * @return String la clef primaires
     */
    public String getForIdLot() {
        return forIdLot;
    }

    public String getForNotEtat() {
        return forNotEtat;
    }

    public String getForPriorite() {
        return forPriorite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 15:59:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForQuittance() {
        return forQuittance;
    }

    /**
     * Renvoie la date AMJ
     * 
     * @return String le type recherché
     */
    public String getForType() {
        return forType;
    }

    /**
     * Renvoie l'utilisateur recherche
     * 
     * @return String l'utilisateur
     */
    public String getForUtilisateur() {
        return forUtilisateur;
    }

    /**
     * Renvoie la date à partir de laquelle on chercher
     * 
     * @return String la date
     */
    public String getFromDateEnvoi() {
        return fromDateEnvoi;
    }

    /**
     * Renvoie le lot à partir duquel on cherche
     * 
     * @return String l'id du lot
     */
    public String getFromIdLot() {
        return fromIdLot;
    }

    /**
     * Renvoie le type à partir duquel on cherche
     * 
     * @return String le type
     */
    public String getFromType() {
        return fromType;
    }

    /**
     * Returns the isArchivage.
     * 
     * @return String
     */
    public String getIsArchivage() {
        return isArchivage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 13:09:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2003 13:58:59)
     * 
     * @return java.lang.String
     * @param statement
     *            globaz.globall.db.BStatement
     */
    // protected String _getFields(BStatement statement) {
    // return
    // "HELOTSP.RMILOT, HELOTSP.RMDDEN, HELOTSP.RMDDTR ,HELOTSP.RMDHEU, HELOTSP.RMLUTI, HELOTSP.RMTTYP, HELOTSP.RMTPRI , HELOTSP.RMTETA , HELOTSP.RMBQUI, HELOTSP.PSPY";
    // }
    // /**
    // * Insérez la description de la méthode ici.
    // * Date de création : (16.05.2003 13:56:31)
    // * @return java.lang.String
    // * @param param globaz.globall.db.BStatement
    // */
    // protected String _getGroup(BStatement param) {
    // return "";
    // // return
    // "HELOTSP.RMILOT, HELOTSP.RMDDEN, HELOTSP.RMDDTR ,HELOTSP.RMDHEU, HELOTSP.RMLUTI, HELOTSP.RMTTYP, HELOTSP.RMTPRI , HELOTSP.RMTETA , HELOTSP.RMBQUI, HELOTSP.PSPY";
    // }
    /**
     * Returns the untilDateEnvoi.
     * 
     * @return String
     */
    public String getUntilDateEnvoi() {
        return untilDateEnvoi;
    }

    /**
     * @return
     */
    public boolean isDateEnvoiCopie() {
        return isDateEnvoiCopie;
    }

    /**
     * @return
     */
    public boolean isForLotReception() {
        return forLotReception;
    }

    /**
     * @param b
     */
    public void setDateEnvoiCopie(boolean b) {
        isDateEnvoiCopie = b;
    }

    /**
     * Fixe la date d'envoie recherchée
     * 
     * @return String la nouvelle date d'envoie
     */
    public void setForDateEnvoi(String newForDateEnvoi) {
        forDateEnvoi = newForDateEnvoi;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * Fixe le lot recherché
     * 
     * @param String
     *            l'id du lot
     */
    public void setForIdLot(String newForIdLot) {
        forIdLot = newForIdLot;
    }

    /**
     * @param b
     */
    public void setForLotReception(boolean b) {
        forLotReception = b;
    }

    public void setForNotEtat(String forNotEtat) {
        this.forNotEtat = forNotEtat;
    }

    public void setForPriorite(String forPriorite) {
        this.forPriorite = forPriorite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 15:59:02)
     * 
     * @param newForQuittance
     *            java.lang.String
     */
    public void setForQuittance(java.lang.String newForQuittance) {
        forQuittance = newForQuittance;
    }

    /**
     * Fixe le type du lot recherché
     * 
     * @param String
     *            le nouveau type
     */
    public void setForType(String newForType) {
        forType = newForType;
    }

    /**
     * Fixe l'utilisateur recherché
     * 
     * @param String
     *            l'utilisateur recherché
     */
    public void setForUtilisateur(String newForUtilisateur) {
        forUtilisateur = newForUtilisateur;
    }

    /**
     * Fixe la date à partir de laquelle on cherche
     * 
     * @param String
     *            la date AML
     */
    public void setFromDateEnvoi(String newFromDateEnvoi) {
        fromDateEnvoi = newFromDateEnvoi;
    }

    /**
     * Fixe l'id du lot à partir duquel on cherche
     * 
     * @param String
     *            l'id du lot (PK)
     */
    public void setFromIdLot(String newFromIdLot) {
        fromIdLot = newFromIdLot;
    }

    /**
     * Fixe le type à partir duquel on cherche
     * 
     * @param String
     *            le type
     */
    public void setFromType(String newFromType) {
        fromType = newFromType;
    }

    /**
     * Sets the isArchivage.
     * 
     * @param isArchivage
     *            The isArchivage to set
     */
    public void setIsArchivage(String isArchivage) {
        this.isArchivage = isArchivage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 13:09:11)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    /**
     * Sets the untilDateEnvoi.
     * 
     * @param untilDateEnvoi
     *            The untilDateEnvoi to set
     */
    public void setUntilDateEnvoi(String untilDateEnvoi) {
        this.untilDateEnvoi = untilDateEnvoi;
    }

}
