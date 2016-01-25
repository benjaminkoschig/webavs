package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.pavo.util.CIUtil;

/**
 * Manager de <tt>CIAnnonceSuspens</tt>. Date de création : (06.12.2002 14:17:49)
 * 
 * @author: Administrator
 */
public class CIAnnonceSuspensManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ORDER_DATE_NUMAVS = "1";
    private String apartir;
    /** (RNIANN) */
    private String forIdAnnonce = new String();
    /** (KMIMOT) */
    private String forIdMotifArc = new String();
    /** (KMITTR) */
    private String forIdTypeTraitement = new String();
    /** liste de type d'enregistrement */
    private String[] forIdTypeTraitementList;
    /** (KMBSUS) */
    // private String forIsSuspens = new String();
    /** (KMIDLO) */
    private String forIsError = new String();
    private String forNumeroAvs = new String();
    private String forSelectionTri = new String();
    /** (KMDREC) */
    private String fromDateReception = new String();
    private String fromNumeroAvs = new String();
    private boolean isSuspens = false;
    /** (KMNAVS) */
    private String likeNumeroAvs = new String();
    private String likeNumeroAvsNNSS = new String();

    // recherche
    private java.lang.String order = new String();
    private String tri;

    /**
     * Constructeur. Autorise l'appel de <tt>_beforeFind</tt> Date de création : (09.12.2002 08:42:35)
     */
    public CIAnnonceSuspensManager() {
        wantCallMethodBeforeFind(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.10.2002 08:31:46)
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {

        if (getFromNumeroAvs() != null && getFromNumeroAvs().trim().length() > 0) {
            setOrderBy("KMNAVS");
        } else if (getFromDateReception() != null && getFromDateReception().trim().length() > 0) {
            setOrderBy("KMDREC");
        }
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CISUSPP";
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
        if (getForSelectionTri().equalsIgnoreCase(ORDER_DATE_NUMAVS)) {
            return "KMDREC, KMNAVS";
        } else {
            return order;
        }
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
        // composant de la requete initialises avec les options par defaut
        // String sqlWhere = "KMBSUS = 1 OR KMIDLO != 0";
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNIANN=" + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonce());
        }
        // traitement du positionnement
        if (getForIdMotifArc().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KMIMOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMotifArc());
        }
        // traitement du positionnement
        if (getForIdTypeTraitement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KMITTR=" + _dbWriteNumeric(statement.getTransaction(), getForIdTypeTraitement());
        }
        // traitement liste de type
        if (getForIdTypeTraitementList() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            for (int i = 0; i < getForIdTypeTraitementList().length; i++) {
                if (i != 0) {
                    sqlWhere += " OR ";
                } else {
                    sqlWhere += "( ";
                }
                sqlWhere += "KMITTR=" + _dbWriteNumeric(statement.getTransaction(), getForIdTypeTraitementList()[i]);
            }
            sqlWhere += " ) ";
        }
        // traitement du positionnement
        if (getFromNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KMNAVS>=" + _dbWriteString(statement.getTransaction(), getFromNumeroAvs());
        }

        if (getForNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KMNAVS=" + _dbWriteString(statement.getTransaction(), getForNumeroAvs());
        }
        // traitement du positionnement
        if (getFromDateReception().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KMDREC>=" + _dbWriteDateAMJ(statement.getTransaction(), getFromDateReception());
        }
        if (getLikeNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS)) {
                String like = NSUtil.unFormatAVS(likeNumeroAvs.trim());
                for (int i = like.length(); i < 13; i++) {
                    like += "_";
                }
                sqlWhere = "RTRIM(KMNAVS) like '" + like + "'";
            } else if ("false".equalsIgnoreCase(likeNumeroAvsNNSS)) {
                String like = NSUtil.unFormatAVS(likeNumeroAvs.trim());
                for (int i = like.length(); i < 11; i++) {
                    like += "_";
                }
                sqlWhere = "RTRIM(KMNAVS) like '" + like + "'";
            } else {
                sqlWhere += "KMNAVS like '"
                        + _dbWriteNumeric(statement.getTransaction(), NSUtil.unFormatAVS(getLikeNumeroAvs()));
                sqlWhere += "%'";
            }
        }
        if (isSuspens) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND (KMBSUS = '1' OR KMIDLO > 0) ";
            } else {

                sqlWhere += " KMBSUS = '1' OR KMIDLO > 0 ";
                // sqlWhere += "%";
            }
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
        return new CIAnnonceSuspens();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public String getForIdMotifArc() {
        return forIdMotifArc;
    }

    public String getForIdTypeTraitement() {
        return forIdTypeTraitement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.12.2002 11:36:45)
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getForIdTypeTraitementList() {
        return forIdTypeTraitementList;
    }

    /**
     * @return
     */
    public String getForIsError() {
        return forIsError;
    }

    /**
     * Returns the forNumeroAvs.
     * 
     * @return String
     */
    public String getForNumeroAvs() {
        return forNumeroAvs;
    }

    /**
     * @return
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    public String getFromDateReception() {
        return fromDateReception;
    }

    public String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    /**
     * Returns the likeNumeroAvs.
     * 
     * @return String
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    /**
     * @return
     */
    public String getLikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
    }

    /**
     * @return
     */
    public boolean isSuspens() {
        return isSuspens;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.10.2002 08:27:27)
     * 
     * @param newApartir
     *            java.lang.String
     */
    public void setApartir(java.lang.String newApartir) {
        apartir = newApartir;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setForIdAnnonce(String newForIdAnnonce) {
        forIdAnnonce = newForIdAnnonce;
    }

    public void setForIdMotifArc(String newForIdMotifArc) {
        forIdMotifArc = newForIdMotifArc;
    }

    public void setForIdTypeTraitement(String newForIdTypeTraitement) {
        forIdTypeTraitement = newForIdTypeTraitement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.12.2002 11:36:45)
     * 
     * @param newForIdTypeTraitementList
     *            java.lang.String[]
     */
    public void setForIdTypeTraitementList(java.lang.String[] newForIdTypeTraitementList) {
        forIdTypeTraitementList = newForIdTypeTraitementList;
    }

    /**
     * @param string
     */
    public void setForIsError(String string) {
        forIsError = string;
    }

    /**
     * Sets the forNumeroAvs.
     * 
     * @param forNumeroAvs
     *            The forNumeroAvs to set
     */
    public void setForNumeroAvs(String forNumeroAvs) {
        this.forNumeroAvs = forNumeroAvs;
    }

    /**
     * @param string
     */
    public void setForSelectionTri(String string) {
        forSelectionTri = string;
    }

    /**
     * @return
     */

    public void setFromDateReception(String newFromDateReception) {
        fromDateReception = newFromDateReception;
    }

    public void setFromNumeroAvs(String newFromNumeroAvs) {
        fromNumeroAvs = CIUtil.unFormatAVS(newFromNumeroAvs);
    }

    /**
     * Sets the likeNumeroAvs.
     * 
     * @param likeNumeroAvs
     *            The likeNumeroAvs to set
     */
    public void setLikeNumeroAvs(String likeNumeroAvs) {
        this.likeNumeroAvs = likeNumeroAvs;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvsNNSS(String string) {
        likeNumeroAvsNNSS = string;
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
     * @param b
     */
    public void setSuspens(boolean b) {
        isSuspens = b;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.10.2002 08:27:59)
     * 
     * @param newTri
     *            java.lang.String
     */
    public void setTri(java.lang.String newTri) {
        tri = newTri;
    }

}
