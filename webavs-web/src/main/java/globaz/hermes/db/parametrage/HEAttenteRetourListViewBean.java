package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (19.12.2002 11:33:22)
 * 
 * @author: Administrator
 */
public class HEAttenteRetourListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** END FOR Reference unique */
    private String forEndReferenceUnique = "";
    /** (RNIANN) */
    private String forIdAnnonce = new String();
    /** (HEA_RNIANN) */
    private String forIdAnnonceRetour = new String();
    /** (ROTATT) */
    private String forIdAnnonceRetourAttendue = new String();
    /** Fichier HEAREAP */
    /** (ROIARA) */
    private String forIdAttenteRetour = new String();
    /** motif (ROMOT) */
    private String forMotif = "";
    /** NOT (HEA_RNIANN) */
    private String forNotIdAnnonceRetour = new String();
    /** NOT motif (ROMOT) */
    private String forNotMotif = "";
    /** numéro AVS (ROAVS) */
    private String forNumeroAVS = "";
    /** caisse (ROCAIS) */
    private String forNumeroCaisse = "";
    /** (ROLRUN) */
    private String forReferenceUnique = new String();
    /** forStatut pour l'annonce */
    private String forStatut = "";
    /** LIKE NUM AVS */
    private String likeNumeroAVS = "";

    /**
     * Commentaire relatif au constructeur HEAttenteRetourListViewBean.
     */
    public HEAttenteRetourListViewBean() {
        super();
    }

    /**
     * Constructor HEAttenteRetourListViewBean.
     * 
     * @param bSession
     */
    public HEAttenteRetourListViewBean(BSession bSession) {
        setSession(bSession);
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HEAREAP";
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
        return "ROIARA";
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
        String sqlWhere = "";
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
        if (getForIdAnnonceRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEA_RNIANN=" + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonceRetour());
        }
        // traitement du positionnement
        if (getForNotIdAnnonceRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEA_RNIANN<>" + _dbWriteNumeric(statement.getTransaction(), getForNotIdAnnonceRetour());
        }
        // traitement du positionnement
        if (getForIdAnnonceRetourAttendue().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROTATT=" + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonceRetourAttendue());
        }
        // traitement du positionnement
        if (getForReferenceUnique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROLRUN=" + _dbWriteString(statement.getTransaction(), getForReferenceUnique());
        }
        if (!JadeStringUtil.isEmpty(getForEndReferenceUnique())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROLRUN LIKE"
                    + _dbWriteString(statement.getTransaction(), "%" + getForEndReferenceUnique() + "%");
        }
        // traitement du positionnement
        if (getForNumeroAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROAVS=" + _dbWriteString(statement.getTransaction(), getForNumeroAVS());
        }
        // traitement du positionnement
        if (getForMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROMOT=" + _dbWriteString(statement.getTransaction(), getForMotif());
        }
        // traitement du positionnement
        if (getForNotMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROMOT<>" + _dbWriteString(statement.getTransaction(), getForNotMotif());
        }
        // traitement du positionnement
        if (getForNumeroCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROCAIS=" + _dbWriteString(statement.getTransaction(), getForNumeroCaisse());
        }
        // traitement du positionnement
        if (getLikeNumeroAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROAVS LIKE " + _dbWriteString(statement.getTransaction(), getLikeNumeroAVS() + "%");
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
        HEAttenteRetourViewBean vBean = new HEAttenteRetourViewBean();
        return vBean;
    }

    /**
     * Method clearNumAVS.
     */
    public void clearNumAVS() {
        forNumeroAVS = new String();
    }

    public String getForEndReferenceUnique() {
        return forEndReferenceUnique;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public String getForIdAnnonceRetour() {
        return forIdAnnonceRetour;
    }

    public String getForIdAnnonceRetourAttendue() {
        return forIdAnnonceRetourAttendue;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdAttenteRetour() {
        return forIdAttenteRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 13:06:22)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForMotif() {
        return forMotif;
    }

    /**
     * Returns the forNotIdAnnonceRetour. <>HEA_RNIANN
     * 
     * @return String
     */
    public String getForNotIdAnnonceRetour() {
        return forNotIdAnnonceRetour;
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
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 13:06:22)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNumeroAVS() {
        return forNumeroAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 13:06:22)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNumeroCaisse() {
        return forNumeroCaisse;
    }

    public String getForReferenceUnique() {
        return forReferenceUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.01.2003 15:46:42)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForStatut() {
        return forStatut;
    }

    /**
     * Returns the likeNumeroAVS.
     * 
     * @return String
     */
    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public void setForEndReferenceUnique(String forEndReferenceUnique) {
        this.forEndReferenceUnique = forEndReferenceUnique;
    }

    /**
     * Method setForIdAnnonce. RNIANN
     * 
     * @param newForIdAnnonce
     */
    public void setForIdAnnonce(String newForIdAnnonce) {
        forIdAnnonce = newForIdAnnonce;
    }

    /**
     * Method setForIdAnnonceRetour.
     * 
     * @param newForIdAnnonceRetour
     */
    public void setForIdAnnonceRetour(String newForIdAnnonceRetour) {
        forIdAnnonceRetour = newForIdAnnonceRetour;
    }

    /**
     * Method setForIdAnnonceRetourAttendue. HEA_RNIANN
     * 
     * @param newForIdAnnonceRetourAttendue
     */
    public void setForIdAnnonceRetourAttendue(String newForIdAnnonceRetourAttendue) {
        forIdAnnonceRetourAttendue = newForIdAnnonceRetourAttendue;
    }

    /**
     * Insérez la description de la méthode ici. le param ROTATT Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdAttenteRetour(String newForIdAttenteRetour) {
        forIdAttenteRetour = newForIdAttenteRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 13:06:22)
     * 
     * @param newForMotif
     *            java.lang.String
     */
    public void setForMotif(java.lang.String newForMotif) {
        forMotif = newForMotif;
    }

    /**
     * Sets the forNotIdAnnonceRetour. <>HEA_RNIANN
     * 
     * @param forNotIdAnnonceRetour
     *            The forNotIdAnnonceRetour to set
     */
    public void setForNotIdAnnonceRetour(String forNotIdAnnonceRetour) {
        this.forNotIdAnnonceRetour = forNotIdAnnonceRetour;
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
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 13:06:22)
     * 
     * @param newForNumeroAVS
     *            java.lang.String
     */
    public void setForNumeroAVS(java.lang.String newForNumeroAVS) {
        forNumeroAVS = StringUtils.padAfterString(newForNumeroAVS.trim(), "0", 11);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 13:06:22)
     * 
     * @param newForNumeroCaisse
     *            java.lang.String
     */
    public void setForNumeroCaisse(java.lang.String newForNumeroCaisse) {
        forNumeroCaisse = newForNumeroCaisse;
    }

    public void setForReferenceUnique(String newForReferenceUnique) {
        forReferenceUnique = newForReferenceUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.01.2003 15:46:42)
     * 
     * @param newForStatut
     *            java.lang.String
     */
    public void setForStatut(java.lang.String newForStatut) {
        forStatut = newForStatut;
    }

    /**
     * Sets the likeNumeroAVS.
     * 
     * @param likeNumeroAVS
     *            The likeNumeroAVS to set
     */
    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }
}
