package globaz.hermes.db.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HERassemblementManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAnnonceDepart = "";
    private String forIdAnnonceRetour = "";
    private String forMotif = "";
    private String forNumAvsRCI = "";
    private String forNumCaisse = "";
    private String forNumeroAvsNNSS = "";
    private String forReferenceUnique = "";
    private String forTypeAnnonce = "";
    private String fromNumAVS = "";
    protected final String HEANNOP_ARCHIVE = "HEANNOR";
    protected final String HEANNOP_EN_COURS = "HEANNOP";
    protected final String HEAREAP_ARCHIVE = "HEAREAR";
    protected final String HEAREAP_EN_COURS = "HEAREAP";
    private String isArchivage = "false";
    private String likeNumAVS = "";
    private String likeNumeroAvsNNSS = "";
    private String order = "";
    private boolean searchOnlyRassemblement = false;

    /**
     * Constructor for HERassemblementManager.
     */
    public HERassemblementManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        if (isSearchOnlyRassemblement()) {
            return super._getSql(statement);
        } else {
            try {
                StringBuffer sqlBuffer = new StringBuffer();
                if ("95".equals(getForMotif())) {
                    // si le rassemblement est un motif 95,
                    // on inclus également les Cis non attendus dans la
                    // recherche
                    sqlBuffer.append(" SELECT ");
                    sqlBuffer.append(getFields1());
                    sqlBuffer.append(" FROM ");
                    sqlBuffer.append(getFrom1());
                    sqlBuffer.append(" WHERE ");
                    sqlBuffer.append(getWhere1(statement));
                    //
                    sqlBuffer.append(" UNION ");
                }
                sqlBuffer.append(" SELECT ");
                sqlBuffer.append(getFields2());
                sqlBuffer.append(" FROM ");
                sqlBuffer.append(getFrom2());
                sqlBuffer.append(" WHERE ");
                sqlBuffer.append(getWhere2(statement));

                String sqlOrder = _getOrder(statement);
                if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                    sqlBuffer.append(" ORDER BY ");
                    sqlBuffer.append(sqlOrder);
                }
                return sqlBuffer.toString();
            } catch (Exception e) {
                JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
                return "";
            }
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "";
        // tri en rapport avec un numéro d'avs
        if (!JadeStringUtil.isEmpty(getLikeNumAVS())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "ROAVS LIKE "
                    + _dbWriteString(statement.getTransaction(), JadeStringUtil.change(getLikeNumAVS(), ".", "") + "%");
        }
        // tri en rapport avec un numéro d'avs
        if (!JadeStringUtil.isEmpty(getFromNumAVS())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "ROAVS>="
                    + _dbWriteString(statement.getTransaction(), JadeStringUtil.change(getFromNumAVS(), ".", ""));
        }
        // tri en rapport avec un numéro d'avs
        // forNumAvsRCI
        if (!JadeStringUtil.isEmpty(getForNumAvsRCI())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "rolrun in (select rolrun from " + _getCollection() + "heareap where roavs="
                    + _dbWriteString(statement.getTransaction(), getForNumAvsRCI(), "num avs rci") + ")";
        }
        /** ************ */
        // tri en rapport avec le motif
        if (!JadeStringUtil.isEmpty(getForMotif())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "ROMOT = " + _dbWriteString(statement.getTransaction(), getForMotif());
        } // **************
          // tri en rapport avec la caisse
        if (!JadeStringUtil.isEmpty(getForNumCaisse())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "ROCAIS = " + _dbWriteString(statement.getTransaction(), getForNumCaisse());
        } // **************
          // tri en rapport avec le type d'annonce
        if (!JadeStringUtil.isEmpty(getForTypeAnnonce())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "ROTATT = " + _dbWriteNumeric(statement.getTransaction(), getForTypeAnnonce());
        } // **************
          // tri en rapport avec la référence unique
        if (!JadeStringUtil.isEmpty(getForReferenceUnique())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "ROLRUN = " + _dbWriteString(statement.getTransaction(), getForReferenceUnique());
        } // **************
          // tri en rapport avec le numéro de l'annonce de départ
        if (!JadeStringUtil.isEmpty(getForIdAnnonceDepart())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "RNIANN = " + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonceDepart());
        } // **************
          // tri en rapport avec le numéro de l'annnonce de retour
        if (!JadeStringUtil.isEmpty(getForIdAnnonceRetour())) {
            if (!JadeStringUtil.isEmpty(where)) {
                where += " AND ";
            }
            where += "HEA_RNIANN = " + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonceRetour());
        } // **************
        /**
         * ***********************************modifNNSS : suffixer le setter() ***************************
         */
        if (!JadeStringUtil.isBlankOrZero(forNumAvsRCI)) {
            if ("true".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (where.length() != 0) {
                    where += " AND ";
                }
                where += "RNBNNS ='1'";
            }
            if ("false".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (where.length() != 0) {
                    where += " AND ";
                }
                where += "RNBNNS ='2'";
            }

        }
        if (!JadeStringUtil.isBlankOrZero(likeNumAVS)) {
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (where.length() != 0) {
                    where += " AND ";
                }
                where += "RNBNNS ='1'";
            }
            if ("false".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (where.length() != 0) {
                    where += " AND ";
                }
                where += "RNBNNS ='2'";
            }
        }
        return where;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        HERassemblement n = new HERassemblement();
        n.setArchivage(Boolean.valueOf(getIsArchivage()).booleanValue());
        return n;
    }

    /**
     * @param statement
     * @return
     */
    private String getFields1() {
        StringBuffer sqlBuffer = new StringBuffer(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEANNOP_ARCHIVE);
        } else {
            sqlBuffer.append(HEANNOP_EN_COURS);
        }
        sqlBuffer.append(".RNIANN,");
        sqlBuffer.append("ROIARA,");
        sqlBuffer.append("RNAVS AS ROAVS,");
        sqlBuffer.append("RNCAIS AS ROCAIS,");
        sqlBuffer.append("RNDDAN,");
        sqlBuffer.append("RODRAP,");
        sqlBuffer.append("HEA_RNIANN,");
        sqlBuffer.append("RNREFU AS ROLRUN, ");
        sqlBuffer.append("ROBNNS");
        return sqlBuffer.toString();
    }

    /**
     * @param statement
     * @return
     */
    private String getFields2() {
        StringBuffer sqlBuffer = new StringBuffer(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEANNOP_ARCHIVE);
        } else {
            sqlBuffer.append(HEANNOP_EN_COURS);
        }
        sqlBuffer.append(".RNIANN,");
        sqlBuffer.append("ROIARA,");
        sqlBuffer.append("ROAVS,");
        sqlBuffer.append("ROCAIS AS ROCAIS,");
        sqlBuffer.append("RNDDAN,");
        sqlBuffer.append("RODRAP,");
        sqlBuffer.append("HEA_RNIANN,");
        sqlBuffer.append("ROLRUN, ");
        sqlBuffer.append("ROBNNS");
        return sqlBuffer.toString();
    }

    /**
     * Returns the forIdAnnonceDepart.
     * 
     * @return String
     */
    public String getForIdAnnonceDepart() {
        return forIdAnnonceDepart;
    }

    /**
     * Returns the forIdAnnonceRetour.
     * 
     * @return String
     */
    public String getForIdAnnonceRetour() {
        return forIdAnnonceRetour;
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
     * Returns the forNumAvsRCI.
     * 
     * @return String
     */
    public String getForNumAvsRCI() {
        return forNumAvsRCI;
    }

    /**
     * @return
     */
    public String getForNumCaisse() {
        return forNumCaisse;
    }

    public String getForNumeroAvsNNSS() {
        return forNumeroAvsNNSS;
    }

    /**
     * Returns the forReferenceUnique.
     * 
     * @return String
     */
    public String getForReferenceUnique() {
        return forReferenceUnique;
    }

    /**
     * Returns the forTypeAnnonce.
     * 
     * @return String
     */
    public String getForTypeAnnonce() {
        return forTypeAnnonce;
    }

    /**
     * @param statement
     * @return
     */
    private String getFrom1() {
        StringBuffer sqlBuffer = new StringBuffer(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEANNOP_ARCHIVE);
        } else {
            sqlBuffer.append(HEANNOP_EN_COURS);
        }
        sqlBuffer.append(" LEFT OUTER JOIN ");
        sqlBuffer.append(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEAREAP_ARCHIVE);
        } else {
            sqlBuffer.append(HEAREAP_EN_COURS);
        }
        sqlBuffer.append(" ON ");
        sqlBuffer.append(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEANNOP_ARCHIVE);
        } else {
            sqlBuffer.append(HEANNOP_EN_COURS);
        }
        sqlBuffer.append(".RNIANN=");
        sqlBuffer.append(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEAREAP_ARCHIVE);
        } else {
            sqlBuffer.append(HEAREAP_EN_COURS);
        }
        sqlBuffer.append(".HEA_RNIANN");
        return sqlBuffer.toString();
    }

    /**
     * @param statement
     * @return
     */
    private String getFrom2() {
        StringBuffer sqlBuffer = new StringBuffer(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEAREAP_ARCHIVE);
        } else {
            sqlBuffer.append(HEAREAP_EN_COURS);
        }
        sqlBuffer.append(" LEFT OUTER JOIN ");
        sqlBuffer.append(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEANNOP_ARCHIVE);
        } else {
            sqlBuffer.append(HEANNOP_EN_COURS);
        }
        sqlBuffer.append(" ON ");
        sqlBuffer.append(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEAREAP_ARCHIVE);
        } else {
            sqlBuffer.append(HEAREAP_EN_COURS);
        }
        sqlBuffer.append(".HEA_RNIANN=");
        sqlBuffer.append(_getCollection());
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            sqlBuffer.append(HEANNOP_ARCHIVE);
        } else {
            sqlBuffer.append(HEANNOP_EN_COURS);
        }
        sqlBuffer.append(".RNIANN");
        return sqlBuffer.toString();
    }

    /**
     * Returns the fromNumAVS.
     * 
     * @return String
     */
    public String getFromNumAVS() {
        return fromNumAVS;
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
     * Returns the likeNumAVS.
     * 
     * @return String
     */
    public String getLikeNumAVS() {
        return likeNumAVS;
    }

    public String getLikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
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
     * @param statement
     * @return
     */
    private String getWhere1(BStatement statement) {
        StringBuffer sqlBuffer = new StringBuffer(" RNREFU=");
        sqlBuffer.append(_dbWriteString(statement.getTransaction(), getForReferenceUnique()));
        sqlBuffer.append(" AND RNLENR LIKE ");
        sqlBuffer.append(_dbWriteString(statement.getTransaction(), "39001%"));
        return sqlBuffer.toString();
    }

    /**
     * @param statement
     * @return
     */
    private String getWhere2(BStatement statement) {
        StringBuffer sqlBuffer = new StringBuffer(" ROLRUN=");
        sqlBuffer.append(_dbWriteString(statement.getTransaction(), getForReferenceUnique()));
        sqlBuffer.append(" AND ROTATT = ");
        sqlBuffer.append(_dbWriteNumeric(statement.getTransaction(), "9"));
        return sqlBuffer.toString();
    }

    /**
     * @return
     */
    public boolean isSearchOnlyRassemblement() {
        return searchOnlyRassemblement;
    }

    /**
     * Sets the forIdAnnonceDepart.
     * 
     * @param forIdAnnonceDepart
     *            The forIdAnnonceDepart to set
     */
    public void setForIdAnnonceDepart(String forIdAnnonceDepart) {
        this.forIdAnnonceDepart = forIdAnnonceDepart;
    }

    /**
     * Sets the forIdAnnonceRetour.
     * 
     * @param forIdAnnonceRetour
     *            The forIdAnnonceRetour to set
     */
    public void setForIdAnnonceRetour(String forIdAnnonceRetour) {
        this.forIdAnnonceRetour = forIdAnnonceRetour;
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
     * Sets the forNumAvsRCI.
     * 
     * @param forNumAvsRCI
     *            The forNumAvsRCI to set
     */
    public void setForNumAvsRCI(String forNumAvsRCI) {
        this.forNumAvsRCI = forNumAvsRCI;
    }

    /**
     * @param string
     */
    public void setForNumCaisse(String string) {
        forNumCaisse = string;
    }

    public void setForNumeroAvsNNSS(String forNumeroAvsNNSS) {
        this.forNumeroAvsNNSS = forNumeroAvsNNSS;
    }

    /**
     * Sets the forReferenceUnique.
     * 
     * @param forReferenceUnique
     *            The forReferenceUnique to set
     */
    public void setForReferenceUnique(String forReferenceUnique) {
        this.forReferenceUnique = forReferenceUnique;
    }

    /**
     * Sets the forTypeAnnonce.
     * 
     * @param forTypeAnnonce
     *            The forTypeAnnonce to set
     */
    public void setForTypeAnnonce(String forTypeAnnonce) {
        this.forTypeAnnonce = forTypeAnnonce;
    }

    /**
     * Sets the fromNumAVS.
     * 
     * @param fromNumAVS
     *            The fromNumAVS to set
     */
    public void setFromNumAVS(String fromNumAVS) {
        this.fromNumAVS = fromNumAVS;
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
     * Sets the likeNumAVS.
     * 
     * @param likeNumAVS
     *            The likeNumAVS to set
     */
    public void setLikeNumAVS(String likeNumAVS) {
        this.likeNumAVS = likeNumAVS;
    }

    public void setLikeNumeroAvsNNSS(String likeNumeroAvsNNSS) {
        this.likeNumeroAvsNNSS = likeNumeroAvsNNSS;
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
     * @param b
     */
    public void setSearchOnlyRassemblement(boolean b) {
        searchOnlyRassemblement = b;
    }

}
