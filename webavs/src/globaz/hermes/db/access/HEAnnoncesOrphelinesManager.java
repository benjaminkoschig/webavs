package globaz.hermes.db.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesOrphelinesViewBean;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ald To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEAnnoncesOrphelinesManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCaisse = "";
    private String forCodeAppl = "";
    private String forCodeEnregistrement = "";
    private String forDateAnnonce = "";
    private String forEnr = "";
    private String forMotif = "";
    private String forNotCodeEnr = "";
    private String forNotRefUnique = "";
    private String forService = "";
    private String forStatut = "";
    private String forUtilisateur = "";
    private String likeNumAVS = "";
    private String likeNumeroAvsNNSS = "";
    private String order = "RNDDAN DESC,RNIANN,RNAVS";

    /**
     * Constructor for HEAnnoncesOrphelinesManager.
     */
    public HEAnnoncesOrphelinesManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_beforeFind(BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        setForStatut(IHEAnnoncesViewBean.CS_ORPHELIN);
        setForCodeEnregistrement("1");
    }

    /**
     * Renvoie la composante de tri de la requête SQL (sans le mot-clé ORDER BY)
     * 
     * @return la composante ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return getOrder();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (getLikeNumAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " RNAVS LIKE "
                    + _dbWriteString(statement.getTransaction(), StringUtils.removeDots(getLikeNumAVS()) + "%");
        }
        if (getForNotRefUnique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " RNREFU <> " + _dbWriteString(statement.getTransaction(), getForNotRefUnique());
        }
        if (getForEnr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " SUBSTR(RNLENR,1,4) = " + _dbWriteString(statement.getTransaction(), getForEnr());
        }
        if (getForCodeAppl().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " SUBSTR(RNLENR,1,2) = " + _dbWriteString(statement.getTransaction(), getForCodeAppl());
        }
        if (getForNotCodeEnr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "  SUBSTR(RNLENR,3,2) <> " + _dbWriteString(statement.getTransaction(), getForNotCodeEnr());
        }

        if (getForUtilisateur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNLUTI="
                    + _dbWriteString(statement.getTransaction(), getForUtilisateur().toUpperCase(), "utilisateur");
        }
        //
        if (getForMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNMOT=" + _dbWriteString(statement.getTransaction(), getForMotif(), "motif");
        }
        //
        if (getForStatut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNTSTA=" + _dbWriteNumeric(statement.getTransaction(), getForStatut(), "statut");
        }
        //
        if (getForDateAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateAnnonce(), "dateannonce");
        }
        if (getForCodeEnregistrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // sqlWhere +=
            // " (((SUBSTR(RNLENR,1,2)='38' OR SUBSTR(RNLENR,1,2)='39') AND SUBSTR(RNLENR,3,3)='"
            sqlWhere += " (((SUBSTR(RNLENR,1,2)='39') AND SUBSTR(RNLENR,3,3)='"
                    + globaz.hermes.utils.StringUtils.padBeforeString(getForCodeEnregistrement(), "0", 3)
                    + "') OR ((SUBSTR(RNLENR,1,2)<>'38' AND SUBSTR(RNLENR,1,2)<>'39') AND SUBSTR(RNLENR,3,2)='"
                    + globaz.hermes.utils.StringUtils.padBeforeString(getForCodeEnregistrement(), "0", 2) + "')) ";
        }
        /**
         * ***********************************modifNNSS : suffixer le setter() ***************************
         */
        if (!JadeStringUtil.isBlankOrZero(likeNumAVS)) {
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
        if (!JadeStringUtil.isBlank((getForService()))) {
            sqlWhere = getForServiceWhere(statement, sqlWhere);
        }
        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new HEAnnoncesOrphelinesViewBean();
    }

    /**
     * Returns the forCodeAppl.
     * 
     * @return String
     */
    public String getForCodeAppl() {
        return forCodeAppl;
    }

    /**
     * Returns the forCodeEnregistrement.
     * 
     * @return String
     */
    public String getForCodeEnregistrement() {
        return forCodeEnregistrement;
    }

    /**
     * Returns the forDateAnnonce.
     * 
     * @return String
     */
    public String getForDateAnnonce() {
        return forDateAnnonce;
    }

    /**
     * Returns the forEnr.
     * 
     * @return String
     */
    public String getForEnr() {
        return forEnr;
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
     * Returns the forNotCodeEnr.
     * 
     * @return String
     */
    public String getForNotCodeEnr() {
        return forNotCodeEnr;
    }

    /**
     * Returns the forNotRefUnique.
     * 
     * @return String
     */
    public String getForNotRefUnique() {
        return forNotRefUnique;
    }

    public String getForService() {
        return forService;
    }

    protected String getForServiceWhere(BStatement statement, String sqlWhere) {
        try {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // start caractere
            sqlWhere += " (((SUBSTR(RNLENR,1,2)='11') AND SUBSTR(RNLENR,11,4) like '" + getForService().toUpperCase()
                    + "%') OR";
            sqlWhere += " ((SUBSTR(RNLENR,1,2)='20') AND SUBSTR(RNLENR,11,4) like '" + getForService().toUpperCase()
                    + "%') OR";
            sqlWhere += " ((SUBSTR(RNLENR,1,2)='21') AND SUBSTR(RNLENR,80,4) like '" + getForService().toUpperCase()
                    + "%') OR";
            sqlWhere += " ((SUBSTR(RNLENR,1,2)='22') AND SUBSTR(RNLENR,85,4) like '" + getForService().toUpperCase()
                    + "%') OR";
            sqlWhere += " ((SUBSTR(RNLENR,1,2)='25') AND SUBSTR(RNLENR,73,4) like '" + getForService().toUpperCase()
                    + "%') OR";
            sqlWhere += " ((SUBSTR(RNLENR,1,2)='39') AND SUBSTR(RNLENR,12,4) like '" + getForService().toUpperCase()
                    + "%') OR";
            sqlWhere += " ((SUBSTR(RNLENR,1,2)='38') AND SUBSTR(RNLENR,12,4) like '" + getForService().toUpperCase()
                    + "%'))";
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }
        return sqlWhere;

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
     * Returns the forUtilisateur.
     * 
     * @return String
     */
    public String getForUtilisateur() {
        return forUtilisateur;
    }

    /**
     * Returns the likeNumAVS.
     * 
     * @return String
     */
    public String getLikeNumAVS() {
        return likeNumAVS;
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
     * Sets the forCodeAppl.
     * 
     * @param forCodeAppl
     *            The forCodeAppl to set
     */
    public void setForCodeAppl(String forCodeAppl) {
        this.forCodeAppl = forCodeAppl;
    }

    /**
     * Sets the forCodeEnregistrement.
     * 
     * @param forCodeEnregistrement
     *            The forCodeEnregistrement to set
     */
    public void setForCodeEnregistrement(String forCodeEnregistrement) {
        this.forCodeEnregistrement = forCodeEnregistrement;
    }

    /**
     * Sets the forDateAnnonce.
     * 
     * @param forDateAnnonce
     *            The forDateAnnonce to set
     */
    public void setForDateAnnonce(String forDateAnnonce) {
        this.forDateAnnonce = forDateAnnonce;
    }

    /**
     * Sets the forEnr.
     * 
     * @param forEnr
     *            The forEnr to set
     */
    public void setForEnr(String forEnr) {
        this.forEnr = forEnr;
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
     * Sets the forNotCodeEnr.
     * 
     * @param forNotCodeEnr
     *            The forNotCodeEnr to set
     */
    public void setForNotCodeEnr(String forNotCodeEnr) {
        this.forNotCodeEnr = forNotCodeEnr;
    }

    /**
     * Sets the forNotRefUnique.
     * 
     * @param forNotRefUnique
     *            The forNotRefUnique to set
     */
    public void setForNotRefUnique(String forNotRefUnique) {
        this.forNotRefUnique = forNotRefUnique;
    }

    public void setForService(String forService) {
        this.forService = forService;
    }

    /**
     * Sets the forStatut.
     * 
     * @param forStatut
     *            The forStatut to set
     */
    public void setForStatut(String forStatut) {
        this.forStatut = forStatut;
    }

    /**
     * Sets the forUtilisateur.
     * 
     * @param forUtilisateur
     *            The forUtilisateur to set
     */
    public void setForUtilisateur(String forUtilisateur) {
        this.forUtilisateur = forUtilisateur;
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

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

}
