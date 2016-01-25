package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

public class HERappelListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** */
    /** (RNDDAN) */
    private String forAfterDate = new String();
    /** (RNDDAN) */
    private String forBeforeDate = new String();
    /** (RNDDAN) */
    private String forDate = new String();
    /** (RNIANN) */
    private String forIdAnnonce = new String();
    /** (ROIARA) */
    private String forIdAttenteRetour = new String();
    /** (ROMOT) */
    private String forMotif = new String();
    /** (ROAVS) */
    private String forNumeroAvs = new String();
    private String forNumeroAvsNNSS = "";
    /** (ROCAIS) */
    private String forNumeroCaisse = new String();
    /** (ROLRUN) */
    private String forReferenceUnique = new String();
    /** Fichier HEAREAP */
    /** */
    private String forService = "";
    /** (ROAVS) */
    private String fromNumeroAvs = new String();
    /** */
    private boolean isGroupByCaisse = false;
    /** (ROAVS) */
    private String likeNumeroAvs = new String();
    private String likeNumeroAvsNNSS = "";

    public HERappelListViewBean() {
        super();
    }

    public HERappelListViewBean(BSession session) {
        super();
        setSession(session);
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HEAREAP," + _getCollection() + "HEANNOP";
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
        return _getCollection() + "HEAREAP.RNIANN,ROAVS,ROCAIS";
    }

    /**
     * @see globaz.globall.db.BManager#_getSql(BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        if (isGroupByCaisse()) {
            try {
                StringBuffer sqlBuffer = new StringBuffer("SELECT ROCAIS ");
                sqlBuffer.append(" FROM ");
                sqlBuffer.append(_getFrom(statement));
                //
                String sqlWhere = _getWhere(statement);
                if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                    sqlBuffer.append(" WHERE ");
                    sqlBuffer.append(sqlWhere);
                }
                sqlBuffer.append(" GROUP BY ");
                sqlBuffer.append(" ROCAIS ");

                return sqlBuffer.toString();
            } catch (Exception e) {
                JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
                return "";
            }
        } else {
            return super._getSql(statement);
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
        String sqlWhere = _getCollection() + "HEAREAP.RNIANN=" + _getCollection() + "HEANNOP.RNIANN";
        // traitement du positionnement
        if (getForIdAttenteRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROIARA=" + _dbWriteNumeric(statement.getTransaction(), getForIdAttenteRetour());
        }
        // traitement du positionnement
        if (getForIdAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNIANN=" + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonce());
        }
        // traitement du positionnement
        if (getForReferenceUnique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROLRUN=" + _dbWriteString(statement.getTransaction(), getForReferenceUnique());
        }
        // traitement du positionnement
        if (getForNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROAVS=" + _dbWriteString(statement.getTransaction(), getForNumeroAvs());
        }
        // traitement du positionnement
        if (getForMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROMOT=" + _dbWriteString(statement.getTransaction(), getForMotif());
        }
        // traitement du positionnement
        if (getForNumeroCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROCAIS=" + _dbWriteString(statement.getTransaction(), getForNumeroCaisse());
        }
        // traitement du positionnement
        if (getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "((RNDDAN<=" + _dbWriteDateAMJ(statement.getTransaction(), getForDate());
            sqlWhere += " AND RODRAP=0)";
            sqlWhere += " OR (RODRAP>0 AND RODRAP<=" + _dbWriteDateAMJ(statement.getTransaction(), getForDate());
            sqlWhere += "))";
        }
        // traitement du positionnement
        if (getForBeforeDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN<=" + _dbWriteNumeric(statement.getTransaction(), getForBeforeDate());
        }
        // traitement du positionnement
        if (getForAfterDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN>" + _dbWriteNumeric(statement.getTransaction(), getForAfterDate());
        }
        // traitement du positionnement
        if (getFromNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROAVS>=" + _dbWriteString(statement.getTransaction(), getFromNumeroAvs());
        }
        // traitement du positionnement
        if (getLikeNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROAVS like " + _dbWriteString(statement.getTransaction(), getLikeNumeroAvs() + "%");
        }
        // traitement du positionnement
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND HEA_RNIANN=0 AND ROTATT=9 ";
        } else {
            sqlWhere += "HEA_RNIANN=0 AND ROTATT=9 ";
        }
        /** ******** */
        if (!JAUtil.isStringEmpty(getForService())) {
            try {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                // start caractere
                sqlWhere += "SUBSTR(RNLENR,11,4) like "
                        + _dbWriteString(statement.getTransaction(), getForService().toUpperCase() + "%");
            } catch (Exception e) {
                _addError(statement.getTransaction(), e.getMessage());
            }
        }
        /**
         * ***********************************modifNNSS : suffixer le setter() ***************************
         */
        if (!JadeStringUtil.isBlankOrZero(forNumeroAvs)) {
            if ("true".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "RNBNNS ='1'";
            }
            if ("false".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "RNBNNS ='2'";
            }

        }
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
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new HERappelViewBean();
    }

    /**
     * Returns the forAfterDate.
     * 
     * @return String
     */
    public String getForAfterDate() {
        return forAfterDate;
    }

    /**
     * Returns the forBeforeDate.
     * 
     * @return String
     */
    public String getForBeforeDate() {
        return forBeforeDate;
    }

    /**
     * Returns the forDate.
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdAttenteRetour() {
        return forIdAttenteRetour;
    }

    public String getForMotif() {
        return forMotif;
    }

    public String getForNumeroAvs() {
        return forNumeroAvs;
    }

    public String getForNumeroAvsNNSS() {
        return forNumeroAvsNNSS;
    }

    public String getForNumeroCaisse() {
        return forNumeroCaisse;
    }

    public String getForReferenceUnique() {
        return forReferenceUnique;
    }

    /**
     * @return
     */
    public String getForService() {
        return forService;
    }

    public String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:49:52)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    public String getLikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
    }

    /**
     * Returns the isGroupBy.
     * 
     * @return boolean
     */
    public boolean isGroupByCaisse() {
        return isGroupByCaisse;
    }

    /**
     * Sets the forAfterDate.
     * 
     * @param forAfterDate
     *            The forAfterDate to set
     */
    public void setForAfterDate(String forAfterDate) {
        this.forAfterDate = forAfterDate;
    }

    /**
     * Sets the forBeforeDate.
     * 
     * @param forBeforeDate
     *            The forBeforeDate to set
     */
    public void setForBeforeDate(String forBeforeDate) {
        this.forBeforeDate = forBeforeDate;
    }

    /**
     * Sets the forDate.
     * 
     * @param forDate
     *            The forDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForIdAnnonce(String newForIdAnnonce) {
        forIdAnnonce = newForIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdAttenteRetour(String newForIdAttenteRetour) {
        forIdAttenteRetour = newForIdAttenteRetour;
    }

    public void setForMotif(String newForMotif) {
        forMotif = newForMotif;
    }

    public void setForNumeroAvs(String newForNumeroAvs) {
        forNumeroAvs = StringUtils.padAfterString(StringUtils.removeDots(newForNumeroAvs).trim(), "0", 11);
    }

    public void setForNumeroAvsNNSS(String forNumeroAvsNNSS) {
        this.forNumeroAvsNNSS = forNumeroAvsNNSS;
    }

    public void setForNumeroCaisse(String newForNumeroCaisse) {
        forNumeroCaisse = newForNumeroCaisse;
    }

    public void setForReferenceUnique(String newForReferenceUnique) {
        forReferenceUnique = newForReferenceUnique;
    }

    /**
     * @param string
     */
    public void setForService(String string) {
        forService = string;
    }

    public void setFromNumeroAvs(String newFromNumeroAvs) {
        fromNumeroAvs = StringUtils.removeDots(newFromNumeroAvs);
    }

    /**
     * Sets the isGroupBy.
     * 
     * @param isGroupBy
     *            The isGroupBy to set
     */
    public void setIsGroupByCaisse(boolean isGroupByCaisse) {
        this.isGroupByCaisse = isGroupByCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 13:49:52)
     * 
     * @param newLikeNumeroAvs
     *            java.lang.String
     */
    public void setLikeNumeroAvs(java.lang.String newLikeNumeroAvs) {
        likeNumeroAvs = StringUtils.removeDots(newLikeNumeroAvs);
    }

    public void setLikeNumeroAvsNNSS(String likeNumeroAvsNNSS) {
        this.likeNumeroAvsNNSS = likeNumeroAvsNNSS;
    }
}
