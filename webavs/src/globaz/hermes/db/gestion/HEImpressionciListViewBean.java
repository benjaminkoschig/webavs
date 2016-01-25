package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEImpressionciListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCaisse = new String();

    /** La date en format AAAAMMJJ (sans les points) */
    private String forDate = "";
    /** (RNMOT) */
    private String forMotif = new String();

    /**
     * Constructor for HEImpressionciListViewBean.
     */
    private String forService = "";
    private final String HEANNOP_ARCHIVE = "HEANNOR";
    private final String HEANNOP_EN_COURS = "HEANNOP";
    private String isArchivage = "false";
    /** (RNAVS) */
    private String likeNumeroAvs = new String();
    private String likeNumeroAvsNNSS = "";
    private final String TABLE_LOT = "HELOTSP";
    private final String TABLE_LOT_ARCHIVE = "HELOTSR";

    /**
     * Constructor for HEImpressionciListViewBean.
     */
    public HEImpressionciListViewBean() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_afterFind(BTransaction)
     */
    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {
        super._afterFind(transaction);
    }

    /**
     * @see globaz.globall.db.BManager#_beforeFind(BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);
    };

    /**
     * @see globaz.globall.db.BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer theFrom = new StringBuffer();

        String tableAnnonces = _getCollection() + HEANNOP_EN_COURS;
        String tableLots = _getCollection() + TABLE_LOT;

        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            tableAnnonces = _getCollection() + HEANNOP_ARCHIVE;
            tableLots = _getCollection() + TABLE_LOT_ARCHIVE;
        }

        theFrom.append(tableAnnonces + " INNER JOIN " + tableLots);
        theFrom.append(" ON ");
        theFrom.append("(" + tableAnnonces + "." + "rmilot" + " = " + tableLots + "." + "rmilot" + " AND " + tableLots
                + "." + "rmttyp = " + HELotViewBean.CS_TYPE_RECEPTION + ")");

        return theFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "RNLUTI,RNAVS,RNDDAN,RNIANN";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // traitement du positionnement
        /*
         * if (getForIdAttenteRetour().length() != 0) { if (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere +=
         * "ROIARA=" + _dbWriteNumeric(statement.getTransaction(), getForIdAttenteRetour()); }
         */
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "RNTSTA=" + this._dbWriteNumeric(statement.getTransaction(), IHEAnnoncesViewBean.CS_TERMINE);
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "(RNMOT=" + this._dbWriteString(statement.getTransaction(), "71");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "73");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "75");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "79");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "81");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "83");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "85");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "92");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "93");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "94");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "95");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "97");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "98");
        sqlWhere += "OR RNMOT=" + this._dbWriteString(statement.getTransaction(), "02");
        sqlWhere += ") AND (SUBSTR(RNLENR,92,1)='1') AND RNLENR LIKE '39001%'";

        if (getForDate().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }

        // traitement du positionnement
        if ((getLikeNumeroAvs() != null) && (getLikeNumeroAvs().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNAVS like " + this._dbWriteString(statement.getTransaction(), getLikeNumeroAvs() + "%");
        }

        // traitement du positionnement
        if (getForMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNMOT=" + this._dbWriteString(statement.getTransaction(), getForMotif());
        }
        if (getForCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNCAIS=" + this._dbWriteString(statement.getTransaction(), getForCaisse());
        }
        // /////////////
        /** ******** */
        if (!JAUtil.isStringEmpty(getForService())) {
            try {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                // start caractere
                sqlWhere += "SUBSTR(RNLENR,11,4) like "
                        + this._dbWriteString(statement.getTransaction(), getForService().toUpperCase() + "%");
            } catch (Exception e) {
                _addError(statement.getTransaction(), e.getMessage());
            }
        }
        /**
         * ***********************************modifNNSS : suffixer le setter() ***************************
         */
        if (!JadeStringUtil.isBlankOrZero(likeNumeroAvs)) {
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "RNBNNS ='1'";
            }
            if ("false".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "RNBNNS ='2'";
            }
        }
        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        HEImpressionciViewBean n = new HEImpressionciViewBean();
        n.setArchivage(Boolean.valueOf(getIsArchivage()).booleanValue());
        return n;
    }

    public String getForCaisse() {
        return forCaisse;
    }

    /**
     * Returns the forDate (AAAAMMJJ).
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
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
     * @return
     */
    public String getForService() {
        return forService;
    }

    public String getIsArchivage() {
        return isArchivage;
    }

    /**
     * Returns the likeNumeroAvs.
     * 
     * @return String
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    public String getLikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
    }

    public void setForCaisse(String forCaisse) {
        this.forCaisse = forCaisse;
    }

    /**
     * Sets the forDate. (AAAAMMJJ)
     * 
     * @param forDate
     *            The forDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
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
     * /**
     * 
     * @param string
     */
    public void setForService(String string) {
        forService = string;
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
     * Sets the likeNumeroAvs.
     * 
     * @param likeNumeroAvs
     *            The likeNumeroAvs to set
     */
    public void setLikeNumeroAvs(String likeNumeroAvs) {
        this.likeNumeroAvs = StringUtils.removeDots(likeNumeroAvs);
    }

    public void setLikeNumeroAvsNNSS(String likeNumeroAvsNNSS) {
        this.likeNumeroAvsNNSS = likeNumeroAvsNNSS;
    }

}
