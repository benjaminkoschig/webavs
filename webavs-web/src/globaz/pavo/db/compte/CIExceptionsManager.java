package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.util.CIUtil;

/**
 * Permet de créer la requête pour la gestion des exceptions
 * 
 * @author sda
 */
public class CIExceptionsManager extends BManager {

    private static final long serialVersionUID = -6598400912821961165L;

    private String forDateEngagement;
    private String forDateLicenciement;
    private String forIdAffilie;
    private String forIdCompteIndividuel;
    private String forNumeroAffilie;
    private String forNumeroAvs;
    // NNSS
    private String forNumeroAvsNNSS = "";
    private String likeNumeroAffilie;
    private String likeNumeroAvs;
    private String likeNumeroAvsNNSS = "";

    private String where = "";

    public CIExceptionsManager() {
        super();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return "MALNAF,KANAVS";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // Recherche par numéro d'affilié
        if (!JAUtil.isStringEmpty(getLikeNumeroAffilie())) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            try {
                where += "MALNAF like '" + CIUtil.formatNumeroAffilie(getSession(), likeNumeroAffilie) + "%' ";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!JAUtil.isStringEmpty(getForNumeroAffilie())) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "MALNAF = " + _dbWriteString(statement.getTransaction(), getForNumeroAffilie());
        }
        // Recherche par numéro Avs
        if (!JAUtil.isStringEmpty(getForNumeroAvs())) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "KANAVS = " + _dbWriteString(statement.getTransaction(), CIUtil.unFormatAVS(getForNumeroAvs()));
        }
        if (!JAUtil.isStringEmpty(getLikeNumeroAvs())) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "KANAVS like '" + CIUtil.unFormatAVS(getLikeNumeroAvs()) + "%' ";
        }
        // Recherche par id du compte individuel
        if (!JAUtil.isStringEmpty(getForIdCompteIndividuel())) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += _getCollection() + "CIEXCP.KAIIND ="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCompteIndividuel());
        }
        // Recherche par date d'engagement
        if (!JAUtil.isStringEmpty(getForDateEngagement())) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "DAENG >=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateEngagement());
        }
        // Recherche par date de licenciement
        if (!JAUtil.isStringEmpty(getForDateLicenciement())) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += "DALIC <=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateLicenciement());
        }
        // Recherche par id d'affiliation
        if (!JAUtil.isStringEmpty(getForIdAffilie())) {
            if (!JAUtil.isStringEmpty(where)) {
                where += " AND ";
            }
            where += _getCollection() + "CIEXCP.MAIAFF IN (" + getForIdAffilie() + ")";
        }
        if (!JadeStringUtil.isBlankOrZero(forNumeroAvs)) {
            if ("true".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (where.length() != 0) {
                    where += " AND ";
                }
                where += "KABNNS ='1'";
            }
            if ("false".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (where.length() != 0) {
                    where += " AND ";
                }
                where += "KABNNS ='2'";
            }

        }
        if (!JadeStringUtil.isBlankOrZero(likeNumeroAvs)) {
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (where.length() != 0) {
                    where += " AND ";
                }
                where += "KABNNS ='1'";
            }
            if ("false".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (where.length() != 0) {
                    where += " AND ";
                }
                where += "KABNNS ='2'";
            }
        }

        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIExceptions();
    }

    /**
     * @return
     */
    public String getForDateEngagement() {
        return forDateEngagement;
    }

    /**
     * @return
     */
    public String getForDateLicenciement() {
        return forDateLicenciement;
    }

    /**
     * @return
     */
    public String getForIdAffilie() {
        return forIdAffilie;
    }

    /**
     * @return
     */
    public String getForIdCompteIndividuel() {
        return forIdCompteIndividuel;
    }

    /**
     * Retourne le numéro d'affilié
     * 
     * @return forNumeroAffilie
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * Renvoie le numéro Avs de l'assuré
     * 
     * @return forNumeroAvs
     */
    public String getForNumeroAvs() {
        return forNumeroAvs;
    }

    /**
     * @return
     */
    public String getForNumeroAvsNNSS() {
        return forNumeroAvsNNSS;
    }

    /**
     * @return
     */
    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    /**
     * @return
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
     * @param string
     */
    public void setForDateEngagement(String string) {
        forDateEngagement = string;
    }

    /**
     * @param string
     */
    public void setForDateLicenciement(String string) {
        forDateLicenciement = string;
    }

    /**
     * @param string
     */
    public void setForIdAffilie(String string) {
        forIdAffilie = string;
    }

    /**
     * @param string
     */
    public void setForIdCompteIndividuel(String string) {
        forIdCompteIndividuel = string;
    }

    /**
     * Sette le numéro de l'affilié
     * 
     * @param string
     */
    public void setForNumeroAffilie(String string) {
        forNumeroAffilie = string;
    }

    /**
     * Sette le numéro Avs de l'assuré
     * 
     * @param string
     */
    public void setForNumeroAvs(String string) {
        forNumeroAvs = string;
    }

    /**
     * @param string
     */
    public void setForNumeroAvsNNSS(String string) {
        forNumeroAvsNNSS = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAffilie(String string) {
        likeNumeroAffilie = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvs(String string) {
        likeNumeroAvs = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvsNNSS(String string) {
        likeNumeroAvsNNSS = string;
    }

}
